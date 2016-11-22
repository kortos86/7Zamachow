package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;


public class EnemyCar {
    //Image of the car presented to user
    private Bitmap bitmap;
    //Holds position on image
    private int x, y;
    //car default speed
    private int speed;
    // Detect enemies leaving the screen
    private int maxX;
    // Spawn enemies within screen bounds
    private int maxY;
    private Rect hitBox;
    int sideOfScreen;
    Random generator = new Random();


    // Constructor
    public EnemyCar(Context context, int screenX, int screenY, int side){
        //generates and Set color for the Enemy car 1 2 or 3
        setColor(context);
        maxX = screenX;
        maxY = screenY;
        //defines if car should be located on line 1.2 or 3.4
        sideOfScreen = side;
        //Sets Enemy car speed
        speed = generator.nextInt(6);
        //Sets car in proper position !!!!!!!!!!!!!!!!!!!!!!! TO BE VALIDATED
        setCarPosition(sideOfScreen);
        y = 0 - bitmap.getHeight();
        // Initialize the hit box
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }
    public void update(Context context, int playerSpeed){
        //Move enemy car down, taking into account player speed and coin speed
        y += playerSpeed;
        y += speed;
        //If enemy car left the screen respawn it on top with different color.
        if (y> maxY+bitmap.getHeight()){
            speed = generator.nextInt(6);
            setCarPosition(sideOfScreen);
            y = 0 - bitmap.getHeight();
            setColor(context);
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
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void setColor( Context context){
        int color = generator.nextInt(4 -1) +1;
        if(color == 1){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        }else if(color == 2){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
        } else if(color == 3){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
        }
    }

    public void setCarPosition(int side){
        if(side == 1){
            x = generator.nextInt(maxX/2-bitmap.getWidth());
            if (x<0){
                x=0;
            }
        }else if (side==2){
            x= generator.nextInt((maxX-bitmap.getWidth()) - maxX/2) + maxX/2 +1;
            if (x>maxX){
                x=maxX-bitmap.getWidth();
            }
        }

    }
}
