package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;
//Class for spinning coin presented on the screen
//when collected speeds up the player car

public class Coin {
    //Image of the coin presented to user
    private Bitmap bitmap;
    //Random generator
    private Random generator = new Random();
    //Holds position on image
    private int x, y;
    //Coins default speed
    private float speed = generator.nextInt(6);
    // Detect  coin leaving the screen
    private int maxX;
    // Spawn coin within screen bounds
    private int maxY;
    //Hitbox
    private Rect hitBox;
    //Used to change the bitmap of coin to get spinning effect;
    private int rotuj = 1;
    //Slows down the spinning
    private int delay =0;
    private int maxDelay =5;



    // Constructor
    public Coin(Context context, int screenX, int screenY){
        //Set first coin image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin1);
        // maximum screen size.
        maxX = screenX;
        maxY = screenY;
        speed = generator.nextInt(6);
        //Position of the coin at start
        x = generator.nextInt(maxX)- bitmap.getWidth();
        y = 0 - bitmap.getHeight();
        if(y<0){
            y=0;
        }
        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(Context context, float playerSpeed){
        //Move coin down, taking into account player speed and coin speed
        y += playerSpeed;
        y += speed;
        //Change the image of coin to make it spin
        if (rotuj==1) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin1);
            delayCoin();
        }else if(rotuj==2){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin2);
            delayCoin();
        }else if(rotuj==3){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin3);
            delayCoin();
        }else if(rotuj==4){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin4);
            delayCoin();
        }else if(rotuj==5){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin5);
            delayCoin();
        }else if(rotuj==6){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin6);
            delayCoin();
        }
        //if coin is of the screen move it up a bit.
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
    // slows down the spin of the coin
    public void delayCoin(){
        delay++;
        if (delay == maxDelay && rotuj < 6) {
            rotuj++;
            delay = 0;
        }else if (delay == maxDelay && rotuj ==6){
            rotuj=1;
            delay = 0;
        }

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
    public void setX(int x) { this.x = x; }
    public void setY(int y) {
        this.y = y;
    }

}
