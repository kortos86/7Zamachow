package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

import static com.artpi.games.a7zamachow.Measurments.dpToPixels;
import static com.artpi.games.a7zamachow.Measurments.getScreenHeight;
import static com.artpi.games.a7zamachow.Measurments.getScreenWidth;
import static com.artpi.games.a7zamachow.Measurments.pixelsToDp;

/**
 * Created by kortm on 6/21/2016.
 */

public class CTView extends SurfaceView implements Runnable {
    volatile boolean playing;
    volatile int stage = 0;
    Thread gameThread = null;

    private CardObject left;
    private CardObject center;
    private CardObject right;
    private CardObject[] cards;
    private Region[] regs;
    private CanvasPoint central;
    private Shuffler shuffler;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private Path animPath;
    private PathMeasure pathMeasure;
    private float pathLength;
    float[] pos;
    float[] tan;
    private int counter = 21;
    boolean shuffled = false;
    Matrix matrix;
    float step;   //distance each step
    float distance;  //distance moved
   // float delay = 100;
    int action = 0;
    Random generator = new Random();

    public CTView(Context context) {
        super(context);



        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        cards = new CardObject[3];
        regs = new Region[3];




        cards[0] = new CardObject(context,false, CardObject.Pos.LEFT );
        cards[0].setFront(BitmapFactory.decodeResource
                (context.getResources(), R.drawable.ace_of_diamonds));
        cards[0].setB_reverse(BitmapFactory.decodeResource(context.getResources(), R.drawable.card_reverse2));


        cards[1]= new CardObject(context, true, CardObject.Pos.CENTER);
        cards[1].setFront(BitmapFactory.decodeResource
                (context.getResources(), R.drawable.qc));
        cards[1].setB_reverse(BitmapFactory.decodeResource(context.getResources(), R.drawable.card_reverse2));


        cards[2] = new CardObject(context, false, CardObject.Pos.RIGHT);
        cards[2].setFront(BitmapFactory.decodeResource
                (context.getResources(), R.drawable.qh));
        cards[2].setB_reverse(BitmapFactory.decodeResource(context.getResources(), R.drawable.card_reverse2));


        int card_width = cards[0].getBitmap().getWidth();
        int card_height = cards[0].getBitmap().getHeight();

        central = calculateCenterCoordinates(cards[1]);
        CanvasPoint left = new CanvasPoint(central.getX() - card_width - 50 ,central.getY());
        CanvasPoint right = new CanvasPoint(central.getX() + card_width + 50 ,central.getY());

        shuffler = new Shuffler(central.getX(), left.getX(), right.getX(), central.getY());

        cards[1].setTargetPos(central);
        cards[0].setTargetPos(left);
        cards[2].setTargetPos(right);

        for(CardObject card : cards)
        {
            card.setPosSet(left, central, right);
        }

        regs[0] = new Region(left.getX(), 150, left.getX() + dpToPixels(138), left.getY() + dpToPixels(200));
        regs[1] = new Region(central.getX(), 150, central.getX() + dpToPixels(138), central.getY() + dpToPixels(200));
        regs[2] = new Region(right.getX(), 150, right.getX() + dpToPixels(138), left.getY() + dpToPixels(200));
    }

    public CanvasPoint calculateCenterCoordinates(CardObject card){
        int card_width = card.getBitmap().getWidth();
        int card_height = card.getBitmap().getHeight();
        int canvas_width = Measurments.getScreenWidth(getContext());
        int canvas_height = Measurments.getScreenHeight(getContext());

        int x = canvas_width - card_width >> 1;
        int y = canvas_height - card_height >> 1;

        return new CanvasPoint(x, y);


    }

    @Override
    public void run() {
      //  prepareCards();
            while(playing)
                switch (stage){

                    case 0:
                        initCards();
                        cards[0].flip(getContext());
                        draw();
                        dowWait(1500);
                        cards[1].flip(getContext());
                        draw();
                        dowWait(1500);
                        cards[2].flip(getContext());
                        draw();
                        dowWait(1500);
                        draw();
                        dowWait(1500);
                        cards[0].flip(getContext());
                        draw();
                        dowWait(1500);
                        draw();
                        cards[1].flip(getContext());
                        draw();
                        dowWait(1500);
                        draw();
                        cards[2].flip(getContext());
                        draw();
                       //prepareCards();
                        stage++;
                      //  break;

                    case 1:
                        draw();
                        update(15);
                        control();
                        break;

                    case 2:
                        draw();
                        control();
                        break;

        }
    }
    // For cards that
    private void update(int v){
        boolean anyMoving = false;
        for(CardObject card : cards) {
            if (!card.animCompleted()) {
                card.increaseAnimDistance(dpToPixels(v));
            } else {
                card.stop();
            }

        }
        anyMoving = (cards[0].isMoving()|| cards[1].isMoving() || cards[2].isMoving());
        if(!anyMoving)
        prepareCards();

    }

    private void paintInstructions(){

    }

    private void initCards(){
        dowWait(100);
        draw();
        dowWait(3500);
    }

    private void prepareCards(){

        action = generator.nextInt(3);

        Log.i("APP", "Action::"+ Integer.toString(action));
        for(CardObject card : cards) {
            if (card.pos == CardObject.Pos.LEFT) {
                left = card;
            }
            else if (card.pos == CardObject.Pos.CENTER) {
                center = card;
            }
            else {
                right = card;
            }
                }
        counter--;
       if (counter >0) {
           if (action == 1) {
               shuffler.switchLeftRight(left, right);
           } else if (action == 0) {
               shuffler.switchCenterLeft(center, left);
           } else {
               shuffler.switchCenterRight(center, right);
           }
       }
        else{
            //playing = false;
           left.setRegion(regs[0]);
           center.setRegion(regs[1]);
           right.setRegion(regs[2]);

           shuffled = true;
           stage = 2;
           return;
        }

    }

    private void draw(){
        if (ourHolder.getSurface().isValid()) {

            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            // Rub out the last frame
            canvas.drawColor(Color.argb(255, 255, 255, 255));

            //Draw each card in current position
            for(CardObject card : cards){
                card.drawSelf(canvas, paint);
            }

        // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }

    }


    private void control(){
        try {
            gameThread.sleep(17);

        } catch (InterruptedException e) {
        }

    }

    private void dowWait(long time){
        try {
            gameThread.sleep(time);

        } catch (InterruptedException e) {
        }
    }
    public void pause(){
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }

    }
    public void resume(){
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();

    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!shuffled)
        return false;
        else{
            int action = event.getAction();
            int _x = (int) event.getX();
            int _y = (int) event.getY();
            switch(action){
                case MotionEvent.ACTION_DOWN:
                for(CardObject card : cards) {
                    if (card.getRegion().contains(_x, _y)) {
                        card.flip(getContext());
                    }
                }
                    break;

            }




        }
        return true;
    }






}
