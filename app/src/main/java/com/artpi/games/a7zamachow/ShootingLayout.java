package com.artpi.games.a7zamachow;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShootingLayout extends RelativeLayout implements View.OnClickListener {

    private int mColumn=5;
    private int mPadding; // the edge distance
    private int mMargin=3;// the distance between pieces
    private ImageView[] mItems;
    private int mItemWidth;
    private int win = 0;
    private int[] targetsArray;
    private Bitmap mBitmap; //the picture for the game
    private Bitmap blankBitmap;
    private Bitmap targetBitmap;
    private List<ImagePiece> mItemsBitmaps;
    private ImagePiece[] visibleBitmaps;
    private boolean once;
    private int mWidth;//the width of the container
    private int mHeight;//the width of the container
    Button shuffleButton;
    private boolean blocked = false;
    Random random = new Random();


    Stopwatch stopwatch = new Stopwatch();


    public ShootingLayout(Context context) {
        this(context,null);
    }

    public ShootingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShootingLayout(Context context, AttributeSet attrs, int defStyle) {
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

        blankBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notarget);
        targetBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.target);
        mItemsBitmaps = ImageSpliter.splitimage(mBitmap,mColumn);
        visibleBitmaps= new ImagePiece[mColumn*mColumn];
        targetsArray= new int[mColumn];
        for (int i=0; i < mColumn ; i++)
        {
            targetsArray[i] = 0;
        }

    }


    private void initItem(){
        mItemWidth=(mWidth-mPadding*2-mMargin*(mColumn-1))/mColumn;
        mItems= new ImageView[mColumn*mColumn];

        Log.w("MemoryLayout", "Image array length: " + mItems.length);
        //implement item,and set rule
        for (int i=0;i<mItems.length;i++){
            ImageView item= new ImageView(getContext());
            item.setOnClickListener(this);

            item.setImageBitmap(blankBitmap);
            mItems[i]=item;
            item.setId(i+1);
            //save the index in the tag
            item.setTag(i);
            LayoutParams lp=new LayoutParams(mItemWidth,mItemWidth);

            //not the last column, set rightmagin
            if ((i+1)%mColumn!=0){
                //lp.rightMargin=mMargin;
            }
            //not the first column，leftmagin
            if (i%mColumn!=0){
                //lp.addRule(RelativeLayout.RIGHT_OF,mItems[i-1].getId());
            }
            //if not the first row
            if ((i+1)>mColumn){
                lp.topMargin=mMargin;
                lp.addRule(RelativeLayout.BELOW,mItems[i-mColumn].getId());
                addView(item,lp);
            }

            if(i==0) {
                addView(item, lp);
            }


        }


        shuffleButton = new Button(getContext());
        shuffleButton.setOnClickListener(this);
        LayoutParams rl = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //rl.addRule(RelativeLayout.BELOW,mItems[6].getId());
        rl.addRule(RelativeLayout.BELOW,mItems[20].getId());
        rl.topMargin = 50;
        shuffleButton.setLayoutParams(rl);

        shuffleButton.setText("Start Shooting");
        addView(shuffleButton,rl);

        LayoutParams rl1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
        Log.w("PuzzleLayout", "onClick Called");

        if (view instanceof Button) {
            if (shuffleButton == (Button) view) {
                Log.w("PuzzleLayout", "Shuffle button onClick");
                placeTargets(mColumn);
            }
        }
        else {

            mFirst = (ImageView) view;

            int tag;
            tag = (int) view.getTag();
            Log.w("PuzzleLayout", "View onClick. Tag: " + tag);

            if (tag == 111) {
                Log.w("MemoryLayout", "onClick locked");
                //mFirst.setImageBitmap(blankBitmap);

                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setDuration(500);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation anim)
                    {
                    };
                    public void onAnimationRepeat(Animation anim)
                    {
                    };
                    public void onAnimationEnd(Animation anim)
                    {
                        mFirst.setImageBitmap(blankBitmap);
                    };
                });

                mFirst.startAnimation(fadeOut);
                mFirst.setTag(222);
                win++;
                checkWin();
            }
        }
    }

    private void placeTargets( int targets ) {

        for (int i = 0; i < targets; i++) {
            mItems[targetsArray[i]].setImageBitmap(blankBitmap);
        }

        int target = 0;

        for (int i = 0; i < targets; i++) {
            targetsArray[i] = target;
            mItems[target].setImageBitmap(targetBitmap);
            mItems[target].setTag(111);

            int duration;
            int delay;
            ObjectAnimator animation;
            AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);


            if(i%2 == 0)
            {
                mItems[target].setX(0 - mItems[target].getWidth());
                animation = ObjectAnimator.ofFloat(mItems[target], "x", mItems[target].getX() + getWidth() +  mItems[target].getWidth());
                duration = random.nextInt(2500 - 1800) + 1800;
                delay = random.nextInt(200 - 0) + 0;
            }
            else {
                mItems[target].setX(getWidth() + mItems[target].getWidth());
                animation = ObjectAnimator.ofFloat(mItems[target], "x", mItems[target].getX() - getWidth() -  mItems[target].getWidth());
                duration = random.nextInt(1800 - 1000) + 1000;
                delay = random.nextInt(200 - 0) + 0;
            }

            Log.w("ShootingLayout", "Duration: " + duration + " Delay: " + delay);

            animation.setDuration(duration);
            animation.setStartDelay(delay);
            animation.setRepeatCount(animation.INFINITE);
            animation.setRepeatMode(animation.REVERSE);

            mItems[target].startAnimation(alphaAnimation);
            animation.start();

            target = target +5;

            Log.w("ShootingLayout", "Random target position: " + target);
        }

    }


    private void checkWin() {

        if (win == 5)
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
            dlgAlert.setMessage("Congratulations! You have completed the ShootingGame! ");
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