package com.artpi.games.a7zamachow;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MemoryLayout extends RelativeLayout implements View.OnClickListener {

    //private int mColumn=3;
    private int mColumn=4;
    private int mPadding; // the edge distance
    private int mMargin=3;// the distance between pieces
    private int win = 0;
    private ImageView[] mItems;
    private ImageView[] visibleItems;
    private int mItemWidth;
    private Bitmap mBitmap; //the picture for the game
    private Bitmap cardBitmap;
    private List<ImagePiece> mItemsBitmaps;
    private ImagePiece[] visibleBitmaps;
    private boolean once;
    private int mWidth;//the width of the container
    private int mHeight;//the width of the container
    private ImageView mFirst;
    private ImageView mSecond;
    private int mFirstIndex = 0;
    private int mSecondIndex = 1;
    private boolean blocked = false;


    public MemoryLayout(Context context) {
        this(context,null);
    }

    public MemoryLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MemoryLayout(Context context, AttributeSet attrs, int defStyle) {
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

    public int getDrawable(String name) {
        Context context = getContext();
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(name, "drawable",
                context.getPackageName());

        return resourceId;
    }

    private void initBitmap(){
        if (mBitmap==null){
            mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bomb);

        }

        cardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mem_reverse);
        mItemsBitmaps = ImageSpliter.splitimage(mBitmap,mColumn);
        visibleBitmaps= new ImagePiece[mColumn*mColumn];

        int j = 0;

        for (int i=0; i<((mColumn*mColumn))/2; i++){

           // Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), getDrawable("bomb.jpg"));

            Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), getDrawable("mem" + i));

            visibleBitmaps[j] = new ImagePiece();
            visibleBitmaps[j].setBitmap(tempBitmap);
            visibleBitmaps[j].setIndex(getDrawable("mem" + i));

            visibleBitmaps[j+1] = new ImagePiece();
            visibleBitmaps[j+1].setBitmap(tempBitmap);
            visibleBitmaps[j+1].setIndex(getDrawable("mem" + i));

            j = j+2;
        }

        //use sort to implement out of order
        Collections.sort(mItemsBitmaps,new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                return Math.random()>0.5?1:-1;
            }
        });

        Arrays.sort(visibleBitmaps,new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                return Math.random()>0.5?1:-1;
            }
        });

    }

    private void initItem(){
        mItemWidth=(mWidth-mPadding*2-mMargin*(mColumn-1))/mColumn;
        mItems= new ImageView[mColumn*mColumn];

        Log.w("MemoryLayout", "Image array length: " + mItems.length);
        //implement item,and set rule
        for (int i=0;i<mItems.length;i++){
            ImageView item= new ImageView(getContext());
            item.setOnClickListener(this);

            item.setImageBitmap(cardBitmap);
            mItems[i]=item;
            item.setId(i+1);
            //save the index in the tag
            item.setTag(i);
            LayoutParams lp=new LayoutParams(mItemWidth,mItemWidth);

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

    @Override
    public void onClick(View view) {
       //click the same image piece at the same time
            int tag;
            tag = (int) view.getTag();
            if (blocked || tag == 111){
                Log.w("MemoryLayout", "onClick locked");
            }
            else if (mFirst == null) {
                mFirst = (ImageView) view;
                tag = (int) mFirst.getTag();
                mFirst.setImageBitmap(visibleBitmaps[tag].getBitmap());
                mFirstIndex = visibleBitmaps[tag].getIndex();

            } else {
                mSecond = (ImageView) view;

                if (mFirst.getId() != mSecond.getId()) {
                    blocked = true;
                    Log.w("MemoryLayout", "Different Images: ");

                    tag = (int) mSecond.getTag();
                    mSecond.setImageBitmap(visibleBitmaps[tag].getBitmap());
                    mSecondIndex = visibleBitmaps[tag].getIndex();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                if (checkMatch()) {
                                    mFirst.setTag(111);
                                    mSecond.setTag(111);
                                    win++;
                                    checkWin(mColumn * mColumn);
                                } else {
                                    mFirst.setImageBitmap(cardBitmap);
                                    mSecond.setImageBitmap(cardBitmap);
                                }

                                    mFirst = null;
                                    mSecond = null;
                                    blocked = false;
                        }
                    }, 500);
                }
            }
    }

    private boolean checkMatch() {
        boolean check = false;

        if (mSecondIndex  == mFirstIndex){
            check = true;
        }

        return check;
    }

    private void checkWin( int items) {

            Log.w("PuzzleLayout", "Win value: " + win);
            if (win == items/2)
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getContext());
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