package com.artpi.games.a7zamachow;

import java.util.Random;



public class SandPoint {
    private int x, y;
    private int speed;
    // Detect dust leaving the screen
    private int maxX;
    private int maxY;

    // Constructor
    public SandPoint(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;

// Set a speed between 0 and 9
        Random generator = new Random();
        speed = 5;
        // Set the starting coordinates
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }
    public void update(int playerSpeed){
// Speed up when the player does
        y += playerSpeed;
        y += speed;
//respawn space dust
        if( y > maxY){
            Random generator = new Random();
            y = generator.nextInt(20)-40;
            x = generator.nextInt(maxX);
            speed = 5;
        }
    }
    // Getters and Setters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}