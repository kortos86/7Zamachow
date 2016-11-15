package com.artpi.games.a7zamachow;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PuzzleLayout extends RelativeLayout implements View.OnClickListener {

    private int mColumn=4;
    private int mPadding; // the edge distance
    private int mMargin=0;// the distance between pieces
    private ImageView[] mItems;
    private int mItemWidth;
    private Bitmap mBitmap; //the picture for the game
    private List<ImagePiece> mItemsBitmaps;
    private boolean once;
    private int mWidth;//the width of the container
    private int mHeight;//the width of the container
    Button shuffleButton;
    Stopwatch stopwatch = new Stopwatch();


    public PuzzleLayout(Context context) {
        this(context,null);
    }

    public PuzzleLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PuzzleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public void init(){
        mMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3,
                getResources().getDisplayMetrics());
        mPadding=min(getPaddingLeft(),getPaddingRight(),getPaddingBottom(),getPaddingTop());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth= Math.min(getMeasuredHeight(),getMeasuredWidth());
        mHeight= Math.max(getMeasuredHeight(),getMeasuredWidth());
        if (!once){
            initBitmap();//cut the image,order it
            initItem();
            once=true;
        }
        setMeasuredDimension(mWidth,mHeight);
    }
    private void initBitmap(){
        if (mBitmap==null){
            mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bomb);

        }
        mItemsBitmaps= ImageSpliter.splitimage(mBitmap,mColumn);

        //use sort to implement out of order
        Collections.sort(mItemsBitmaps,new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                return Math.random()>0.5?1:-1;
            }
        });

    }
    private void initItem(){
        mItemWidth=(mWidth-mPadding*2-mMargin*(mColumn-1))/mColumn;
        mItems= new ImageView[mColumn*mColumn];

        Log.w("PuzzleLayout", "Image array length: " + mItems.length);
        //implement item,and set rule
        for (int i=0;i<mItems.length;i++){
            ImageView item= new ImageView(getContext());
            item.setOnClickListener(this);

            item.setImageBitmap(mItemsBitmaps.get(i).getBitmap());
            mItems[i]=item;
            item.setId(i+1);
            //save the index in the tag
            item.setTag(i+"_"+ mItemsBitmaps.get(i).getIndex());
            //Log.w("PuzzleLayout", "Image item tag: " + i+"_"+ mItemsBitmaps.get(i).getIndex());
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(mItemWidth,mItemWidth);

            //not the last column, set rightmagin
            if ((i+1)%mColumn!=0){
                lp.rightMargin=mMargin;
            }
            //not the first column，leftmagin
            if (i%mColumn!=0){
                lp.addRule(RelativeLayout.RIGHT_OF,mItems[i-1].getId());
            }
            //if not the first row
            if ((i+1)>mColumn){
                lp.topMargin=mMargin;
                lp.addRule(RelativeLayout.BELOW,mItems[i-mColumn].getId());
            }

            addView(item,lp);


        }

        RelativeLayout.LayoutParams rl1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rl1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(stopwatch.getStopWatch(getContext()),rl1);

        stopwatch.start();

    }

    private int min(int... params){
        int min=params[0];
        for (int param:params){
            if(param<min){
                min=param;
            }
        }
        return 0;
    }

    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View view) {
       //click the same image piece at the same time

            if (mFirst == view) {
                Log.w("PuzzleLayout", "Image onClick");
                mFirst.setColorFilter(null);
                mFirst = null;
                return;
            }

            if (mFirst == null) {
                mFirst = (ImageView) view;
                mFirst.setColorFilter(Color.parseColor("#55FF0000"));
            } else {
                mSecond = (ImageView) view;
                exchangeView();
            }

    }

    private void exchangeView() {
        mFirst.setColorFilter(null);
        String firstTag=(String)mFirst.getTag();
        String secondTag=(String)mSecond.getTag();
        Log.w("Puzzle Layout", "FirstTag: " + firstTag + "SecondTag: " + secondTag );

        String[] firstParams=firstTag.split("_");
        String[] secondParams=secondTag.split("_");

        firstTag = secondParams[0] + "_" + firstParams[1];
        secondTag = firstParams[0] + "_" + secondParams[1];

        Bitmap firstBitmap=mItemsBitmaps.get(Integer.parseInt(firstParams[0])).getBitmap();
        //mSecond.setImageBitmap(firstBitmap);
        Bitmap secondBitmap=mItemsBitmaps.get(Integer.parseInt(secondParams[0])).getBitmap();
        //mFirst.setImageBitmap(secondBitmap);

        animateExchange(mFirst, mSecond);

        mFirst.setTag(firstTag);
        mSecond.setTag(secondTag);

        mFirst=mSecond=null;

        //checkWin(9);
        checkWin(16);
    }

    private void checkWin( int items) {

        int win = 0;

        for (int i = 0; i < items ; i++) {
            String[] itemParams = ((String) mItems[i].getTag()).split("_");
            Log.w("PuzzleLayout", "Element tags: " + itemParams[0] + " ," + itemParams[1]);
            if (itemParams[0].equals(itemParams[1])) {
                Log.w("PuzzleLayout", "Win value: " + win);
                win++;
            }
        }

            //Log.w("PuzzleLayout", "Win value: " + win);
            if (win == 16)
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
                stopwatch.stop();
                dlgAlert.setMessage("Congratulations! You have completed the Puzzle Quest! " + stopwatch.getTime("%02d:%02d:%03d"));
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

    }

    private void animateExchange(final ImageView image1, final ImageView image2) {
        final float firstX = image1.getX();
        final float firstY = image1.getY();
        final float secondX = image2.getX();
        final float secondY = image2.getY();
        float moveX = 0.0f;
        float moveY = 0.0f;
        int moves = 2;

        ObjectAnimator animation1 = ObjectAnimator.ofFloat(image1, "x", firstX);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(image2, "x", secondX);
        ObjectAnimator animation3 = ObjectAnimator.ofFloat(image1, "y", firstY);
        ObjectAnimator animation4 = ObjectAnimator.ofFloat(image2, "y", secondY);

        if (firstX < secondX && firstY == secondY) {
            moveX = secondX - firstX;
            animation1 = ObjectAnimator.ofFloat(image1, "x", firstX + moveX);
            animation2 = ObjectAnimator.ofFloat(image2, "x", secondX - moveX);
        }
        else if (firstX > secondX && firstY == secondY) {
            moveX = firstX - secondX;
            animation1 = ObjectAnimator.ofFloat(image1, "x", firstX - moveX);
            animation2 = ObjectAnimator.ofFloat(image2, "x", secondX + moveX);
        }
        else if (firstX == secondX && firstY < secondY) {
            moveY = secondY - firstY;
            animation1 = ObjectAnimator.ofFloat(image1, "y", firstY + moveY);
            animation2 = ObjectAnimator.ofFloat(image2, "y", secondY - moveY);
        }
        else if (firstX == secondX && firstY > secondY) {
            moveY = firstY - secondY;
            animation1 = ObjectAnimator.ofFloat(image1, "y", firstY - moveY);
            animation2 = ObjectAnimator.ofFloat(image2, "y", secondY + moveY);
        }
        else if (firstX < secondX && firstY < secondY) {
            moves = 4;
            moveX = secondX - firstX;
            moveY = secondY - firstY;
            animation1 = ObjectAnimator.ofFloat(image1, "x", firstX + moveX);
            animation2 = ObjectAnimator.ofFloat(image2, "x", secondX - moveX);
            animation3 = ObjectAnimator.ofFloat(image1, "y", firstY + moveY);
            animation4 = ObjectAnimator.ofFloat(image2, "y", secondY - moveY);
        }
        else if (firstX < secondX && firstY > secondY) {
            moves = 4;
            moveX = secondX - firstX;
            moveY = firstY - secondY;
            animation1 = ObjectAnimator.ofFloat(image1, "x", firstX + moveX);
            animation2 = ObjectAnimator.ofFloat(image2, "x", secondX - moveX);
            animation3 = ObjectAnimator.ofFloat(image1, "y", firstY - moveY);
            animation4 = ObjectAnimator.ofFloat(image2, "y", secondY + moveY);
        }
        else if (firstX > secondX && firstY > secondY) {
            moves = 4;
            moveX = firstX - secondX;
            moveY = firstY - secondY;
            animation1 = ObjectAnimator.ofFloat(image1, "x", firstX - moveX);
            animation2 = ObjectAnimator.ofFloat(image2, "x", secondX + moveX);
            animation3 = ObjectAnimator.ofFloat(image1, "y", firstY - moveY);
            animation4 = ObjectAnimator.ofFloat(image2, "y", secondY + moveY);
        }
        else if (firstX > secondX && firstY < secondY) {
            moves = 4;
            moveX = firstX - secondX;
            moveY = secondY - firstY;
            animation1 = ObjectAnimator.ofFloat(image1, "x", firstX - moveX);
            animation2 = ObjectAnimator.ofFloat(image2, "x", secondX + moveX);
            animation3 = ObjectAnimator.ofFloat(image1, "y", firstY + moveY);
            animation4 = ObjectAnimator.ofFloat(image2, "y", secondY - moveY);
        }

        if (moves == 2) {
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(animation1, animation2);
            animSetXY.setDuration(600);
            animSetXY.start();
        }
        else {
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(animation1, animation2, animation3, animation4);
            animSetXY.setDuration(600);
            animSetXY.start();
        }
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

}