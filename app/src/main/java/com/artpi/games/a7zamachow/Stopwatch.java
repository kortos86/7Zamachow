package com.artpi.games.a7zamachow;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Stopwatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    final int REFRESH_RATE = 100;
    TextView stopWatchTextView;
    String format = "%02d:%02d:%03d";

    Handler stopWatchHandler = new Handler()
    {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    startTimer(); //start timer
                    stopWatchHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    stopWatchTextView.setText(getTime(format));
                    stopWatchHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every refresh rate interval,
                    break; //though the timer is still running

                case MSG_STOP_TIMER:
                    stopWatchHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    stopTimer();//stop timer
                    stopWatchTextView.setText(getTime (format));
                    break;

                default:
                    break;
            }
        }
    };


    public void startTimer() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }


    public void stopTimer() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }


    // elaspsed time in milliseconds
    public long getElapsedTime() {
        if (running) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }


    // elaspsed time in seconds
    public long getElapsedTimeSecs() {
        if (running) {
            return ((System.currentTimeMillis() - startTime) / 1000);
        }
        return ((stopTime - startTime) / 1000);
    }

    TextView getStopWatch (Context context) {

        stopWatchTextView = new TextView(context);
        String timeValue = "";
        timeValue = String.format("Your time: " + format,
                0,
                0,
                0
        );
        stopWatchTextView.setText(timeValue);

        return stopWatchTextView;
    }

    void start () {
        stopWatchHandler.sendEmptyMessage(MSG_START_TIMER);
    }

    void stop () {
        stopWatchHandler.sendEmptyMessage(MSG_STOP_TIMER);
    }

    String getTime (String format)
    {
        long longTime = getElapsedTime();
        String timeValue = "";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(longTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(longTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(longTime));
        long miliseconds = longTime - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(longTime)) - TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(longTime));
        timeValue = String.format("Your time: " + format,
                minutes,
                seconds,
                miliseconds
        );
        return timeValue;
    }

}
