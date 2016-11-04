package com.artpi.games.a7zamachow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PuzzleLayout extends RelativeLayout implements View.OnClickListener {

    private int mColumn=3;
    private int mPadding; // the edge distance
    private int mMargin=3;// the distance between pieces
    private ImageView[] mItems;
    private int mItemWidth;
    private Bitmap mBitmap; //the picture for the game
    private List<ImagePiece> mItemsBitmaps;
    private boolean once;
    private int mWidth;//the width of the container

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
        if (!once){
            initBitmap();//cut the image,order it
            initItem();
            once=true;
        }
        setMeasuredDimension(mWidth,mWidth);
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
        mItems=new ImageView[mColumn*mColumn];

        //implement item,and set rule
        for (int i=0;i<mItems.length;i++){
            ImageView item=new ImageView(getContext());
            item.setOnClickListener(this);

            item.setImageBitmap(mItemsBitmaps.get(i).getBitmap());
            mItems[i]=item;
            item.setId(i+1);
            //save the index in the tag
            item.setTag(i+"_"+mItemsBitmaps.get(i).getIndex());
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
        if (mFirst==view){
            mFirst.setColorFilter(null);
            mFirst=null;
            return;
        }

        if(mFirst==null){
            mFirst=(ImageView) view;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
        }
        else {
            mSecond=(ImageView) view;
            exchangeView();
        }

    }

    private void exchangeView() {
        mFirst.setColorFilter(null);
        String firstTag=(String)mFirst.getTag();
        String secondTag=(String)mSecond.getTag();
        String[] firstParams=firstTag.split("_");
        String[] secondParams=secondTag.split("_");

        Bitmap firstBitmap=mItemsBitmaps.get(Integer.parseInt(firstParams[0])).getBitmap();
        mSecond.setImageBitmap(firstBitmap);
        Bitmap secondBitmap=mItemsBitmaps.get(Integer.parseInt(secondParams[0])).getBitmap();
        mFirst.setImageBitmap(secondBitmap);

        mFirst.setTag(secondTag);
        mSecond.setTag(firstTag);

        mFirst=mSecond=null;


    }
}
