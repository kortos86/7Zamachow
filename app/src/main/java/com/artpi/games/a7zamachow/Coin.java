package com.artpi.games.a7zamachow;

/**
 * Created by gruca on 11/22/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;


public class Coin {

    private Bitmap bitmap;
    Random generator = new Random();
    private int x, y;
    private int speed = generator.nextInt(6);
    // Detect  coin leaving the screen
    private int maxX;
    private int minX;
    // Spawn coin within screen bounds
    private int maxY;
    private int minY;
    private Rect hitBox;
    int rotuj = 1;
    int delay =0;



    // Constructor
    public Coin(Context context, int screenX, int screenY){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin1);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        speed = generator.nextInt(6);
        x = generator.nextInt(maxX)- bitmap.getWidth();
        y = 0 - bitmap.getHeight();
        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(Context context, int playerSpeed){
        y += playerSpeed;
        y += speed;

        if (rotuj==1) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin1);
            delay++;
            if (delay == 5) {
                rotuj++;
                delay = 0;
            }
        }else if(rotuj==2){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin2);
            delay++;
            if (delay == 5) {
                rotuj++;
                delay = 0;
            }
        }else if(rotuj==3){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin3);
            delay++;
            if (delay == 5) {
                rotuj++;
                delay = 0;
            }
        }else if(rotuj==4){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin4);
            delay++;
            if (delay == 5) {
                rotuj++;
                delay = 0;
            }
        }else if(rotuj==5){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin5);
            delay++;
            if (delay == 5) {
                rotuj++;
                delay = 0;
            }
        }else if(rotuj==6){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin6);
            delay++;
            if (delay == 5) {
                rotuj=1;
                delay = 0;
            }
        }

        if (y> maxY+bitmap.getHeight()){
            x = generator.nextInt(maxX)- bitmap.getWidth();
            if (x<0){
                x=0;
            }
            y = 0 - bitmap.getHeight();
        }

        // Refresh hit box location
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

    }


    //Getters and Setters
    public Bitmap getBitmap(){
        return bitmap;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Rect getHitbox(){
        return hitBox;
    }

    // This is used by the TDView update() method to
    // Make an enemy out of bounds and force a re-spawn
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y += y;
    }

}
