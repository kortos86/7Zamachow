package com.artpi.games.a7zamachow;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private IBoardManager _boardManager;

    public ImageAdapter(Context c, IBoardManager boardManager) {
        mContext = c;
        _boardManager = boardManager;
    }

    public int getCount() {
        return _boardManager.getLength();
    }

    public Object getItem(int position) {
        return _boardManager.getImageAt(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("ImageAdapter", "inside image adapter");
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        imageView.setImageResource(_boardManager.getImageAt(position));
        return imageView;
    }
}