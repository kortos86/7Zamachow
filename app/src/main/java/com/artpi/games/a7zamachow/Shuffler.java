package com.artpi.games.a7zamachow;

import android.graphics.Path;

/**
 * Created by kortm on 6/23/2016.
 */

public class Shuffler {
    static Path left_to_right(){
       Path animPath = new Path();
        animPath.moveTo(140, 150);
        animPath.lineTo(460, 100);
        animPath.lineTo(780, 150);
        return animPath;
    }

    static Path right_to_left(){
        Path animPath = new Path();

        animPath.moveTo(780, 150);
        animPath.lineTo(140, 150);
        return animPath;
    }

    static Path right_to_center(){
        Path animPath = new Path();
        animPath.moveTo(780, 150);
        animPath.lineTo(620, 100);
        animPath.lineTo(460, 150);
        return animPath;
    }

    static Path left_to_center(){
        Path animPath = new Path();
        animPath.moveTo(140, 150);
        animPath.lineTo(300, 100);
        animPath.lineTo(460, 150);
        return animPath;
    }

    static Path center_to_left(){
        Path animPath = new Path();
        animPath.moveTo(460, 150);
        animPath.lineTo(300, 100);
        animPath.lineTo(140, 150);
        return animPath;
    }

    static Path center_to_right(){
        Path animPath = new Path();
        animPath.moveTo(460, 150);
        animPath.lineTo(620, 100);
        animPath.lineTo(780, 150);
        return animPath;
    }
    static void switchLeftRight(CardObject left, CardObject right){
        right.setAnimDistance(0);
        left.setAnimDistance(0);
        left.setAnimData(Shuffler.left_to_right());
        right.setAnimData(Shuffler.right_to_left());

        left.setPos(CardObject.Pos.RIGHT);
        left.setTargetPos(left.posSet.get(left.pos.RIGHT));
        right.setPos(CardObject.Pos.LEFT);
        right.setTargetPos(right.posSet.get(right.pos.LEFT));
    }

    static void switchCenterRight(CardObject center, CardObject right){
        right.setAnimDistance(0);
        center.setAnimDistance(0);
        center.setAnimData(Shuffler.center_to_right());
        right.setAnimData(Shuffler.right_to_center());

        center.setPos(CardObject.Pos.RIGHT);
        center.setTargetPos(center.posSet.get(center.pos.RIGHT));
        right.setPos(CardObject.Pos.CENTER);
        right.setTargetPos(right.posSet.get(right.pos.CENTER));
    }

    static void switchCenterLeft(CardObject center, CardObject left){
        left.setAnimDistance(0);
        center.setAnimDistance(0);
        center.setAnimData(Shuffler.center_to_left());
        left.setAnimData(Shuffler.left_to_center());

        center.setPos(CardObject.Pos.LEFT);
        center.setTargetPos(center.posSet.get(center.pos.LEFT));
        left.setPos(CardObject.Pos.CENTER);
        left.setTargetPos(left.posSet.get(left.pos.CENTER));
    }
}
