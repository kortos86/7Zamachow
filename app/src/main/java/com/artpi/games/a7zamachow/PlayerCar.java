package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerCar {
    //Image of the car presented to user
    private Bitmap bitmap;
    //Holds position on image
    private int x, y;
    //Player Car Speed
    private float speed = 25;
    private float newspeed = 25;
    //Holds info if car is turning left or right
    private boolean turningLeft = false;
    private boolean turningRight = false;
    // Stop ship leaving the screen
    private int maxX;
    private Rect hitBox;
    private int shieldStrength;

    // Constructor
    public PlayerCar(Context context, int screenX, int screenY){
        //Set image for players car
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.car2);
        //Place player car in the middle of the screen, bottom
        x = screenX/2 - bitmap.getWidth()/2;
        y = screenY - bitmap.getHeight() - 50;

        //Limit car from leaving the screen
        maxX = screenX - bitmap.getWidth();
        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        //How many shields.
        shieldStrength = 5;
    }

    public void update() {
        // Are we turning?
        int turningSpeed = 15;
        if (turningLeft) {
            // turnLeft
            x = x - turningSpeed;
        } else if (turningRight){
            // turn right
            x = x + turningSpeed;
        }
        // Do not Leave the screen
        if (x > maxX) {
            x = maxX;
        }
        if (x < 0) {
            x = 0;
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

        if((speed!=newspeed)&&(newspeed<speed)){
            speed-=0.5;
        }else if((speed!=newspeed)&&(newspeed>speed)){
            speed+=0.5;
        }
    }

    //Getters
    public Bitmap getBitmap() {
        return bitmap;
    }
    public float getSpeed() {
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

    public void slowCarDown(int finalSpeed){
        newspeed = finalSpeed;
    }

    public void speedCarUp(int finalSpeed){
        newspeed = finalSpeed;
    }
}
