package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

/**
 * Created by gruca on 11/19/2016.
 */

public class EnemyCar {

    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;
    // Detect enemies leaving the screen
    private int maxX;
    private int minX;
    // Spawn enemies within screen bounds
    private int maxY;
    private int minY;
    int position;
    private Rect hitBox;


    // Constructor
    public EnemyCar(Context context, int screenX, int screenY, int color){

        if(color == 1){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        }else if(color == 2){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
        } else if(color == 3){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
        }

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = generator.nextInt(6)+25;

        position = generator.nextInt(4);
        Log.println(Log.INFO,"1","xxxxxxx position" + position);

        x = maxX/5 *position+140;
        y = 0 - bitmap.getHeight();
        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(){
        y=y+speed;

        if (y> maxY+bitmap.getHeight()){
            Random generator = new Random();
            speed = generator.nextInt(6)+25;
            //x = generator.nextInt(maxX) - bitmap.getWidth();
            position = generator.nextInt(4);
            x = maxX/5 *position+140;
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
