package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;



/**
 * Created by gruca on 11/19/2016.
 */

public class RoadLines {
    private Bitmap bitmap;
    private int x, y;
    private int speed;
    // Detect dust leaving the screen
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    int cosdziwnego;
    // Constructor
    public RoadLines(Context context, int screenX, int screenY, int linenumber, int lineheight){
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        speed = 20;

        cosdziwnego = maxY / 4;

        // Set the starting coordinates
        x = maxX/5 *linenumber +100;
        y = cosdziwnego *lineheight -cosdziwnego;


        Log.println(Log.INFO,"tag","x = "+x + " y= "+y);
    }
    public void update(){
        // Speed up when the player does

        y = y + speed;
        //respawn space dust
        if(y > maxY){
            y = 0 - cosdziwnego;
        }
    }
    // Getters and Setters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getCosDziwnego(){
        return cosdziwnego;
    }


}
