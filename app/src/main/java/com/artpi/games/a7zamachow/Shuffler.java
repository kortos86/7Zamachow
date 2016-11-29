package com.artpi.games.a7zamachow;

import android.graphics.Path;


/**
 * Created by kortm on 6/23/2016.
 */

public class Shuffler {

    private int LEFT;
    private int MIDLEFT;
    private int CENTER;
    private int MIDRIGHT;
    private int RIGHT;
    private int HEIGHT;

    public Shuffler (int center, int left, int right, int height) {

        LEFT = left;
        MIDLEFT = left +(center - left)/2;
        CENTER = center;
        MIDRIGHT= center + (right - center)/2;
        RIGHT = right;
        HEIGHT = height;

        }


    static Path init_to_left(){
        Path animPath = new Path();
        animPath.moveTo(0, 150);
        animPath.lineTo(70, 100);
        animPath.lineTo(140, 150);
        return animPath;
    }

    static Path init_to_center(){
        Path animPath = new Path();
        animPath.moveTo(0, 150);
        animPath.lineTo(230, 100);
        animPath.lineTo(460, 150);
        return animPath;
    }

    static Path init_to_right(){
        Path animPath = new Path();
        animPath.moveTo(0, 150);
        animPath.lineTo(460, 100);
        animPath.lineTo(780, 150);
        return animPath;
    }

    public Path left_to_right(){
       Path animPath = new Path();
        animPath.moveTo(LEFT, HEIGHT);
        animPath.lineTo(CENTER, HEIGHT - 100);
        animPath.lineTo(RIGHT, HEIGHT);
        return animPath;
    }

    public Path right_to_left(){
        Path animPath = new Path();

        animPath.moveTo(RIGHT, HEIGHT);
        animPath.lineTo(LEFT, HEIGHT);
        return animPath;
    }

    public Path right_to_center(){
        Path animPath = new Path();
        animPath.moveTo(RIGHT, HEIGHT);
        animPath.lineTo(MIDRIGHT, HEIGHT -100);
        animPath.lineTo(CENTER, HEIGHT);
        return animPath;
    }

    public Path left_to_center(){
        Path animPath = new Path();
        animPath.moveTo(LEFT, HEIGHT);
        animPath.lineTo(MIDLEFT, HEIGHT -100);
        animPath.lineTo(CENTER, HEIGHT);
        return animPath;
    }

    public Path center_to_left(){
        Path animPath = new Path();
        animPath.moveTo(CENTER, HEIGHT);
        animPath.lineTo(MIDLEFT, HEIGHT -100);
        animPath.lineTo(LEFT, HEIGHT);
        return animPath;
    }

    public Path center_to_right(){
        Path animPath = new Path();
        animPath.moveTo(CENTER, HEIGHT);
        animPath.lineTo(MIDRIGHT, HEIGHT -100);
        animPath.lineTo(RIGHT, HEIGHT);
        return animPath;
    }
    public void switchLeftRight(CardObject left, CardObject right){
        right.setAnimDistance(0);
        left.setAnimDistance(0);
        left.setAnimData(this.left_to_right());
        right.setAnimData(this.right_to_left());

        left.setPos(CardObject.Pos.RIGHT);
        left.setTargetPos(left.posSet.get(left.pos.RIGHT));
        right.setPos(CardObject.Pos.LEFT);
        right.setTargetPos(right.posSet.get(right.pos.LEFT));
    }

    public void switchCenterRight(CardObject center, CardObject right){
        right.setAnimDistance(0);
        center.setAnimDistance(0);
        center.setAnimData(this.center_to_right());
        right.setAnimData(this.right_to_center());

        center.setPos(CardObject.Pos.RIGHT);
        center.setTargetPos(center.posSet.get(center.pos.RIGHT));
        right.setPos(CardObject.Pos.CENTER);
        right.setTargetPos(right.posSet.get(right.pos.CENTER));
    }

    public void switchCenterLeft(CardObject center, CardObject left){
        left.setAnimDistance(0);
        center.setAnimDistance(0);
        center.setAnimData(this.center_to_left());
        left.setAnimData(this.left_to_center());

        center.setPos(CardObject.Pos.LEFT);
        center.setTargetPos(center.posSet.get(center.pos.LEFT));
        left.setPos(CardObject.Pos.CENTER);
        left.setTargetPos(left.posSet.get(left.pos.CENTER));
    }
}
