package com.artpi.games.a7zamachow;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


public class RGView extends SurfaceView implements  Runnable{
    private SharedPreferences.Editor editor;
    private Context context;
    //Indicates if we should update and draw and the screen.
    volatile boolean playing;
    Thread gameThread = null;
    //Game objects
    private PlayerCar player;
    public EnemyCar enemy1;
    public EnemyCar enemy2;
    public Coin coin;
    // Make some random sand
    public ArrayList<SandPoint> sandPoints = new ArrayList<>(150);
    // For drawing
    private Paint paint;
    private SurfaceHolder ourHolder;
    //Position of click
    float fingerX,fingerY;
    //For screen size
    int maxX;
    int maxY;
    //Indicates that there was a crash
    boolean enemyBoom  = false;
    //Indicates that shield should be removed
    boolean removeShield = false;
    //Indicates that coin was collected
    boolean collectedCoin = false;
    //Stats
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;
    private boolean gameEnded;

    public RGView(Context context, int x, int y) {
        super(context);
        this.context = context;
        // Get a reference to a file called HiScores.
        // If id doesn't exist one is created
        SharedPreferences prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);
        // Initialize the editor ready
        editor = prefs.edit();
        // Load fastest time from a entry in the file
        // labeled "fastestTime"
        // if not available highscore = 1000000
        fastestTime = prefs.getLong("fastestTime", 1000000);
        //Sets screen size
        maxX=x;
        maxY=y;
        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        startGame();
    }

    //Handler to speed up car after crash
    Handler handler = new Handler(Looper.getMainLooper());
    final Runnable r2 = new Runnable() {
        public void run() {
            enemyBoom = false;
        }
    };
    final Runnable r3 = new Runnable() {
        public void run() {
            enemyBoom = false;
            player.speedCarUp(25);
        }
    };
    //To slow down Car after coin collected
    final Runnable r4 = new Runnable() {
        public void run() {
            collectedCoin = false;
            player.slowCarDown(25);
        }
    };


    private void startGame(){
        //Initialize game objects
        player = new PlayerCar(context, maxX, maxY);
        //enemies on different lines
        enemy1 = new EnemyCar(context, maxX, maxY, 1);
        enemy2 = new EnemyCar(context, maxX, maxY, 2);
        coin = new Coin(context, maxX, maxY);
        //Number of Sand dots
        int howMuchSand = 150;
        sandPoints.clear();
        for (int j = 0; j < howMuchSand; j++) {
                SandPoint spec = new SandPoint( maxX, maxY);
                sandPoints.add(spec);
        }
        // Reset time and distance
        distanceRemaining = 30000;// 10 km
        timeTaken = 0;
        // Get start time
        timeStarted = System.currentTimeMillis();
        gameEnded= false;
    }


    @Override
    public void run() {
        while (playing) {
            if (!gameEnded) {
                update();
                draw();
                control();
            }
        }
    }


    private void update(){
        // Collision detection on new positions
        // Before move because we are testing last frames
        // position which has just been drawn
        if(Rect.intersects
                (player.getHitbox(), enemy1.getHitbox())){
            enemy1.setY(enemy1.getY()+1000);
            enemyBoom  = true;
            removeShield  = true;
            handler.removeCallbacks(r2);
            handler.removeCallbacks(r3);
        }
        if(Rect.intersects
                (player.getHitbox(), enemy2.getHitbox())){
            enemy2.setY(enemy2.getY()+1000);
            enemyBoom  = true;
            removeShield  = true;
            handler.removeCallbacks(r2);
            handler.removeCallbacks(r3);
        }
        // Coin Collection detection
        if(Rect.intersects
                (player.getHitbox(), coin.getHitbox())){
            coin.setY(coin.getY()+1000);
            collectedCoin  = true;
            handler.removeCallbacks(r4);
        }
        // If coin collected speed up for 1 second
        if (collectedCoin) {
            player.speedCarUp(45);
            handler.postDelayed(r4, 3000);
            collectedCoin = false;
        }

        // If crash with enemy Car, slow down for 2 seconds
        // and sidplay boom image for 0.5 sec
        if (enemyBoom) {
            player.setSpeed(5);
            handler.postDelayed(r2, 500);
            handler.postDelayed(r3, 2000);
        }
        //If shield should be removed, do it or end game if no more shields
        if (removeShield){
            player.reduceShieldStrength();
            if (player.getShieldStrength() < 1) {
                gameEnded = true;
            }
            removeShield=false;
        }
        // Update the player, enemies, sand and coin
        player.update();
        enemy1.update(getContext(), player.getSpeed());
        enemy2.update(getContext(), player.getSpeed());
        coin.update(getContext(),player.getSpeed());
        for (SandPoint sd : sandPoints) {
            sd.update(player.getSpeed());
        }
        //If game continues
        if(!gameEnded) {
            //subtract distance to home planet based on current speed
            distanceRemaining -= player.getSpeed();
            //How long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }
        //Completed the game!
        if(distanceRemaining < 0){
        //check for new fastest time
            if(timeTaken < fastestTime) {
                editor.putLong("fastestTime", timeTaken);
                editor.commit();
                fastestTime = timeTaken;
            }
            // avoid ugly negative numbers
            // in the HUD
            distanceRemaining = 0;
            // Now end the game
            gameEnded = true;
        }
    }


    private void draw(){
        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            Canvas canvas = ourHolder.lockCanvas();
            // Rub out the last frame with sand background color
            canvas.drawColor(Color.rgb(197,178,128));
            // Sand dots color
            paint.setColor(Color.rgb(114,85,55));
            // Draw sand
            for (SandPoint sd : sandPoints) {
                canvas.drawRect(sd.getX(),sd.getY(),sd.getX()+5,sd.getY()+5,paint);
            }
            // Draw the player, enemies and coin
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);
            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY(), paint);
            canvas.drawBitmap(enemy2.getBitmap(), enemy2.getX(), enemy2.getY(), paint);
            canvas.drawBitmap(coin.getBitmap(), coin.getX(), coin.getY(), paint);
            //If there was a crash draw boom
            if(enemyBoom){
                Bitmap boom = BitmapFactory.decodeResource(this.getResources(), R.drawable.boom);
                canvas.drawBitmap( boom, player.getX(), player.getY()-50, paint);
            }
            //If game continues
            if(!gameEnded){
                // Draw the hud
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 0, 0, 0));
                paint.setTextSize(40);
                canvas.drawText("Fastest:"+ fastestTime + "s", 10, 40, paint);
                canvas.drawText("Time:" + timeTaken + "s", 10, 80,paint);
                canvas.drawText("Distance:" + distanceRemaining  +" M", 10, 120, paint);
                canvas.drawText("Shield:" , 10, 170, paint);
                    paint.setColor(Color.argb(255, 255, 0, 0));
                    for (int i = 0; i < player.getShieldStrength(); i++) {
                        Bitmap shield = BitmapFactory.decodeResource(this.getResources(), R.drawable.shield);
                        canvas.drawBitmap(shield, 140 + i*40,130,paint);
                       // canvas.drawRect( 180 + i*40,140,180+ i*40+20,160,paint);
                    }
                    paint.setColor(Color.argb(255, 0, 0, 0));
                canvas.drawText("Speed:" + player.getSpeed() * 60 + " MPS", 10, 220, paint);
            }else{//game has ended
                // Show pause screen
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 0, 0, 0));
                canvas.drawText("Game Over", maxX/2, 100, paint);
                paint.setTextSize(25);
                canvas.drawText("Fastest:"+fastestTime + "s", maxX/2, 160, paint);
                canvas.drawText("Time:" + timeTaken + "s", maxX / 2, 200, paint);
                canvas.drawText("Distance remaining:" + distanceRemaining/1000 + " KM",maxX/2, 240, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", maxX/2, 350, paint);
            }
            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void control(){

        try {
            gameThread.sleep(2);
        } catch (InterruptedException ignored) {
        }
    }
    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException ignored) {
        }
    }
    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Has the player lifted their finger up?
            case MotionEvent.ACTION_UP:
                player.stopTurning();
                break;
            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN:
                // Do something here
                fingerX = motionEvent.getX();
                fingerY = motionEvent.getY();
                if (fingerX<(getWidth()/2)){
                    player.turnLeft();
                }else{
                    player.turnRight();
                }
                // If we are currently on the pause screen, start a new game
                if(gameEnded){
                    startGame();
                }
                break;
        }
        return true;
    }
}


