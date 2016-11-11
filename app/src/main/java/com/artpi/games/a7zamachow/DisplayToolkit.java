package com.artpi.games.a7zamachow;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by kortm on 7/15/2016.
 */

public class DisplayToolkit {
    private DisplayMetrics dMetrics;
    private float density;
    private int width;
    private int height;

    public DisplayToolkit(Activity activity) {

        this.dMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        this.density = dMetrics.density;
        this.width = Math.round(dMetrics.widthPixels / density);
        this.height = Math.round(dMetrics.heightPixels / density);
    }

    public float getDensity() {
        return density;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}