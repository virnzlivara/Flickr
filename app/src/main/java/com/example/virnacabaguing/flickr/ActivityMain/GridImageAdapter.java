package com.example.virnacabaguing.flickr.ActivityMain;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.virnacabaguing.flickr.R;

import java.util.ArrayList;

/**
 * Created by virnacabaguing on 5/23/16.
 */
public class GridImageAdapter extends BaseAdapter {
    private Context mContext;

    private int defaultItemBackground;
    public ArrayList<ImageItem> imageItem;

    public ArrayList<ImageItem> getImageItem() {
        return imageItem;
    }

    public GridImageAdapter(Context c) {
        mContext = c;
    }

    public GridImageAdapter(Context c, ArrayList<ImageItem> imageItem) {
        mContext = c;
        this.imageItem = imageItem;
        TypedArray styleAttrs = c.obtainStyledAttributes(R.styleable.PicGallery);
        styleAttrs.getResourceId(R.styleable.PicGallery_android_galleryItemBackground, 0);
        defaultItemBackground = styleAttrs.getResourceId(R.styleable.PicGallery_android_galleryItemBackground, 0);
        styleAttrs.recycle();
    }

    public int getCount() {
        return imageItem.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        if (imageItem.get(position).getThumb() != null) {
            imageView.setImageBitmap(imageItem.get(position).getThumb());
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setBackgroundResource(defaultItemBackground);
        }
//        } else
//            imageView.setImageDrawable(getResources().getDrawable(android.R.color.black));
        return imageView;
    }
}
