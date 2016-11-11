package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.Log;

import java.util.EnumMap;

/**
 * Created by kortm on 6/22/2016.
 */

public class CardObject {
    private boolean moving;
    private boolean reverse = true;
    private AnimationData anim;
    private Bitmap bitmap;
    private Point cornerCord;
    private Bitmap front;
    private int speed = 0;
    private Region reg;
    public enum Pos  {
        LEFT, CENTER, RIGHT
    }

    public EnumMap<Pos, Point> posSet = new EnumMap<Pos, Point>(Pos.class);
    Pos pos;
    private final boolean winning;

    private boolean left;

    public CardObject(Context context, int x, boolean winning, Pos pos) {

        posSet.put(Pos.LEFT, new Point(140,150));
        posSet.put(Pos.CENTER, new Point(460,150));
        posSet.put(Pos.RIGHT, new Point(780,150));

        this.cornerCord = new Point(x,150);
        speed = 5;
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.card_reverse);
        this.pos = pos;
        Path path= new Path();
        path.moveTo(cornerCord.getX(), cornerCord.getY());
        path.lineTo(cornerCord.getX()+1, cornerCord.getY()+1);
        this.anim = new AnimationData(path);
        this.winning = winning;
    }

    public void update() {

    }
    public void drawSelf(Canvas canvas, Paint paint){
        if(moving) {
            canvas.drawBitmap(
                    this.getBitmap(),
                    this.getAnimMatrix(),
                    paint);
        }
        else{
            canvas.drawBitmap(
                    this.getBitmap(),
                    this.cornerCord.getX(),
                    this.cornerCord.getY(),
                    paint);
        }
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void flip(Context context){
        if(reverse) {
            this.bitmap = this.front;
                    //BitmapFactory.decodeResource
                    //(context.getResources(), R.drawable.ace_of_diamonds);
            reverse=false;
        }
        else{
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.card_reverse);
            reverse= true;
        }
    }
    public int getSpeed() {
        return speed;
    }
    public int getX() {
        return cornerCord.getX();
    }
    public int getY() {
        return cornerCord.getY();
    }
    public Region getRegion(){
        return this.reg;
    }
    public void setRegion(Region region){
        this.reg = region;
    }
    public Matrix getAnimMatrix(){
        return this.anim.getMatrix();
    }
    public void increaseAnimDistance(float value){
         this.anim.increaseDistance(value);
    }
    public void setAnimDistance(float value){
        Log.i("APP", Float.toString(value));
        this.anim.setDistance(value);
    }
    public void setAnimData(Path path){
        this.anim = new AnimationData(path);
        this.moving = true;
    }
    public void setPos(Pos p){
        this.pos = p;
    }
    public void setFront(Bitmap b){
        front =b;
    }

    public void setTargetPos(Point p){
        cornerCord.setX(p.getX());
        cornerCord.setY(p.getY());
    }

    public boolean animCompleted(){
        return (this.anim.completed() && moving);
    }
    public boolean isMoving(){
        return this.moving;
    }
    public void stop(){
        this.moving = false;
    }
    public Path getAnimPath(){
        return this.anim.getPath();
    }

}