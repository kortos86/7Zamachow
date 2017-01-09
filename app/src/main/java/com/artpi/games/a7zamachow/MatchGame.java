package com.artpi.games.a7zamachow;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Map;


public class MatchGame extends AppCompatActivity {

    private int firstTouch = -1;
    private int currTouch = -1;
    private int lastTouch = -1;

    private ArrayDeque<Integer> touchList = new ArrayDeque<>();
    private GridView gridview;
    final IBoardManager boardManager = BoardManager.Create();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_game);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, boardManager));

        gridview.setOnTouchListener(new AdapterView.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.v("MainActivity", "Action down, position: " + getCurrentPosition(motionEvent, gridview));
                        if (isTouchInsideBoard(motionEvent, gridview))
                        {
                            firstTouch = getCurrentPosition(motionEvent, gridview);
                            touchList.addFirst(firstTouch);
                            boardManager.setSelected(firstTouch);
                            gridview.invalidateViews();
                            currTouch = firstTouch;
                        }
                        //Toast.makeText(MainActivity.this, "row: " + firstTouch / 8 + " col: " + firstTouch % 8, Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isTouchInsideBoard(motionEvent, gridview) && hasFirstTouchValid())
                        {
                            currTouch = getCurrentPosition(motionEvent, gridview);
                            if(currTouch != touchList.getLast() && boardManager.arePointsVerticallyOrHorizontallyAligned(currTouch, touchList.getLast()))
                            {
                                if(boardManager.isImageTheSame(currTouch, firstTouch))
                                {
                                    touchList.addLast(currTouch);
                                    boardManager.setSelected(currTouch);
                                    gridview.invalidateViews();
                                    Log.v("Main Activity", "Adding to list " + currTouch );
                                }
                                else
                                {
                                    clearSelection(false);
                                }
                            }
                            Log.v("MainActivity", "Action Move, position: " + getCurrentPosition(motionEvent, gridview));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                       // Log.v("MainActivity", "Action up, position: " + gridview.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY()));
                        if (isTouchInsideBoard(motionEvent, gridview) && touchList.size() > 1 && hasFirstTouchValid())
                        {
                            lastTouch = getCurrentPosition(motionEvent, gridview);
                            if(lastTouch == touchList.getLast()) {
                                Log.v("Main Activity", "Last touch " + lastTouch);

                                boardManager.removeAndRefillFields(touchList);
                                gridview.invalidateViews();

                                if (boardManager.isFinished()){
                                    finishGame(view);
                                }
                                clearSelection(true);
                            }
                        } else {
                            clearSelection(false);
                        }
                        break;
                    default:
                        clearSelection(false);
                        break;
                }

                return true;
            }
        });
    }

    private void clearSelection(boolean finishedSelection){
        if(!finishedSelection){
            boardManager.setUnselected(touchList);
            gridview.invalidateViews();
        }
        firstTouch = -1;
        currTouch = -1;
        lastTouch = -1;
        touchList.clear();
    }

    private void finishGame(View view) {

        final View currentView = view;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(currentView.getContext());
                dlgAlert.setMessage("Congratulations! You have completed the Memory Quest! ");
                dlgAlert.setTitle("Great Job!");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                                try {
                                    getActivity().finish();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

            }
        }, 1000);
    }

    private int getCurrentPosition(MotionEvent motionEvent, GridView gridview) {
        return gridview.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
    }

    private boolean hasFirstTouchValid() {
        return firstTouch != -1;
    }

    private boolean isTouchInsideBoard(MotionEvent motionEvent, GridView gridview) {
        return (getCurrentPosition(motionEvent, gridview)) != -1;
    }

    public static Activity getActivity() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);

        Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
        if(activities == null)
            return null;

        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
