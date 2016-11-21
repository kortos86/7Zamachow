package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerCar {
    private Bitmap bitmap;
    private int x, y;
    private int speed = 25;
    private int turningSpeed=15;
    private boolean turningLeft;
    private boolean turningRight;
    // Stop ship leaving the screen
    private int maxX;
    private int minX;
    private Rect hitBox;
    private int shieldStrength;
    //Limit the bounds of the ship's speed


    // Constructor
    public PlayerCar(Context context, int screenX, int screenY){
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.car2);
        turningLeft = false;
        turningRight = false;
        x = screenX/2 - bitmap.getWidth()/2;
        y = screenY - bitmap.getHeight();
        maxX = screenX - bitmap.getWidth();
        minX = 0;
        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        shieldStrength = 5;
    }

    public void update() {
        // Are we boosting?
        if (turningLeft) {
            // Log.println(Log.INFO,"1","xxxxxxx should be left");
            // Speed up
            x = x - turningSpeed;
        } else if (turningRight){
            //Log.println(Log.INFO,"1","xxxxxxx should be right");
            // Slow down
            x = x + turningSpeed;
        }
        // Constrain top speed
        if (x > maxX) {
            x = maxX;
        }
        // Never stop completely
        if (x < 0) {
            x = 0;
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    //Getters
    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed2) {
        speed = speed2;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void turnLeft() {
        turningRight= false;
        turningLeft = true;

    }
    public void turnRight() {
        turningLeft = false;
        turningRight = true;
    }

    public void stopTurning(){
        turningLeft = false;
        turningRight= false;
    }
    public Rect getHitbox(){
        return hitBox;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }

    public void reduceShieldStrength(){
        shieldStrength --;
    }
}
