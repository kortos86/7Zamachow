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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

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
        cards[0] = new CardObject(context,140,false, CardObject.Pos.LEFT );
        cards[0].setFront(BitmapFactory.decodeResource
                (context.getResources(), R.drawable.ace_of_diamonds));
        regs[0] = new Region(140, 150, 284, 350);
        cards[1]= new CardObject(context,460, true, CardObject.Pos.CENTER);
        cards[1].setFront(BitmapFactory.decodeResource
                (context.getResources(), R.drawable.qc));
        regs[1] = new Region(460, 150, 604, 350);
        cards[2] = new CardObject(context,780, false, CardObject.Pos.RIGHT);
        cards[2].setFront(BitmapFactory.decodeResource
                (context.getResources(), R.drawable.qh));
        regs[2] = new Region(780, 150, 924, 350);



    }

    @Override
    public void run() {
      //  prepareCards();
            while(playing)
                switch (stage){

                    case 0:
                        draw();
                        dowWait(2000);
                        cards[0].flip(getContext());
                        draw();
                        dowWait(500);
                        cards[1].flip(getContext());
                        draw();
                        dowWait(500);
                        cards[2].flip(getContext());
                        draw();
                        dowWait(500);
                        draw();
                        dowWait(500);
                        cards[0].flip(getContext());
                        draw();
                        dowWait(500);
                        draw();
                        cards[1].flip(getContext());
                        draw();
                        dowWait(500);
                        draw();
                        cards[2].flip(getContext());
                        draw();
                       //prepareCards();
                        stage++;
                      //  break;

                    case 1:
                        draw();
                        update(30);
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
                card.increaseAnimDistance(v);
            } else {
                card.stop();
            }

        }
        anyMoving = (cards[0].isMoving()|| cards[1].isMoving() || cards[2].isMoving());
        if(!anyMoving)
        prepareCards();

    }

    private void initCards(){
        cards[0].setAnimData(Shuffler.init_to_left());
        cards[1].setAnimData(Shuffler.init_to_center());
        cards[2].setAnimData(Shuffler.init_to_right());
    }

    private void prepareCards(){
       // action++;
       // action = action%3;
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
               Shuffler.switchLeftRight(left, right);
           } else if (action == 0) {
               Shuffler.switchCenterLeft(center, left);
           } else {
               Shuffler.switchCenterRight(center, right);
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
                        Log.i("APP", "Tap Listener: you've tapped card");
                    }
                }
                    break;

            }




        }
        return true;
    }





}
