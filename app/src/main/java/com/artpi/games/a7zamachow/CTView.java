package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by kortm on 6/21/2016.
 */

public class CTView extends SurfaceView implements Runnable {
    volatile boolean playing;
    Thread gameThread = null;

    private CardObject left;
    private CardObject center;
    private CardObject right;
    private CardObject[] cards;
    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private Path animPath;
    private PathMeasure pathMeasure;
    private float pathLength;
    float[] pos;
    float[] tan;
    Matrix matrix;
    float step;   //distance each step
    float distance;  //distance moved
   // float delay = 100;
    int action = 0;

    public CTView(Context context) {
        super(context);

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        cards = new CardObject[3];

        cards[0] = new CardObject(context,140,false, CardObject.Pos.LEFT );
        cards[1]= new CardObject(context,460, true, CardObject.Pos.CENTER);
        cards[2] = new CardObject(context,780, false, CardObject.Pos.RIGHT);

    }

    @Override
    public void run() {
        prepareCards();
        while (playing) {
            draw();
            update();
            control();
        }
    }
    private void update(){
        boolean anyMoving = false;
        for(CardObject card : cards) {
            if (!card.animCompleted()) {
                card.increaseAnimDistance(40);
            } else {
                card.stop();
            }

        }
        anyMoving = (cards[0].isMoving()|| cards[1].isMoving() || cards[2].isMoving());
        if(!anyMoving)
        prepareCards();

        // else card1.setAnimDistance(0);

  //      if(!card2.animCompleted()){

 //           card2.increaseAnimDistance(20);
 //       }
        // else card2.setAnimDistance(0);

 //       if(!card3.animCompleted()){
          //  card3.increaseAnimDistance(20);
 //       }
        // else card3.setAnimDistance(0);

    }

    private void prepareCards(){
        action++;
        action = action%3;
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

       if (action == 1) {
            Shuffler.switchLeftRight(left, right);
        }

        else if (action == 0) {
           Shuffler.switchCenterLeft(center, left);
       }
        else {
           Shuffler.switchCenterRight(center, right);
       }

    }

    private void draw(){
        if (ourHolder.getSurface().isValid()) {

            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            // Rub out the last frame
            canvas.drawColor(Color.argb(255, 255, 255, 255));

        // Draw the cards in current positions
        /*    canvas.drawBitmap(
                    cards[0].getBitmap(),
                    cards[0].getAnimMatrix(),
                    paint);
            canvas.drawBitmap(
                    cards[1].getBitmap(),
                    cards[1].getAnimMatrix(),
                    paint);
            canvas.drawBitmap(
                    cards[2].getBitmap(),
                    cards[2].getAnimMatrix(),
                    paint);
        */
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





}
