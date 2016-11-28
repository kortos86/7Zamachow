package com.artpi.games.a7zamachow;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;

/**
 * Created by kortm on 6/23/2016.
 */

public class AnimationData {
    private Matrix matrix;
    private Path path;
    private PathMeasure pathMeasure;
    private float pathLength;
    private float[] pos;
    private float[] tan;
    private float distance;


 public AnimationData(){
     matrix = new Matrix();
     path = new Path();
     pathMeasure = new PathMeasure(path, false);
     pathLength = pathMeasure.getLength();
     distance=0;
 }

  public  AnimationData(Path path){
      this.path = path;
      pathMeasure = new PathMeasure(path, false);
      pathLength = pathMeasure.getLength();
      matrix = new Matrix();

      distance = 0;

  }
    public Matrix getMatrix(){

        pos = new float[2];
        tan = new float[2];
        pathMeasure.getPosTan(distance, pos, tan);
        matrix.reset();
        matrix.postTranslate(pos[0], pos[1]);

        return matrix;
    }

    public float getDistance() {
        return distance;
    }
    public void increaseDistance(float value){
        distance += value;
    }
    public void setDistance(float value){
        distance = value;
    }
    public boolean completed (){
        return distance >=pathLength;
    }
    public Path getPath(){
        return this.path;
    }
}
