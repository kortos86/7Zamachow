package com.artpi.games.a7zamachow;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class RacingGame extends Activity {
    // Our object to handle the View
    private RGView gameView;


    // This is where the "Play" button from HomeActivity sends us
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create an instance of our Tappy Defender View (TDView)
        // Also passing in "this" which is the Context of our app
        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
// Load the resolution into a Point object
       android.graphics.Point size = new android.graphics.Point();
       display.getSize(size);
       gameView = new RGView(this, size.x, size.y);
        // Make our gameView the view for the Activity
       setContentView(gameView);

    }
    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}