package com.example.virnacabaguing.flickr.ActivityMain;

import android.graphics.Bitmap;

import com.example.virnacabaguing.flickr.ActivityMain.MainActivity.UIHandlerMainActivity;
import com.example.virnacabaguing.flickr.ActivityMain.FlickrSearchManager.GetThumbnailsThread;
/**
 * Created by virnacabaguing on 5/23/16.
 */
public class ImageItem {
    private String id;
    private int position;
    private String thumbURL;
    private Bitmap thumb;
    private Bitmap photo;
    private String owner;
    private String secret;
    private String server;
    private String farm;



    public ImageItem(String id, String thumbURL, String largeURL, String owner, String secret, String server, String farm) {
        super();
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
    }

    public ImageItem(String id, String owner, String secret, String server, String farm) {
        super();
        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
//        this.setThumbURL(createPhotoURL(FlickrSearchManager.PHOTO_THUMB, this));
        setThumbURL(createPhotoURL(FlickrSearchManager.PHOTO_THUMB, this));
//        setLargeURL(createPhotoURL(FlickrSearchManager.PHOTO_LARGE, this));
    }



    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
        onSaveThumbURL(FlickrSearchManager.uihandler, this);
    }

    public void onSaveThumbURL(UIHandlerMainActivity uih, ImageItem ic) {
        // TODO Auto-generated method stub
        new GetThumbnailsThread(uih, ic).start();
    }

    private String createPhotoURL(int photoType, ImageItem imgCon) {
        String tmp = null;
        tmp = "http://farm" + imgCon.farm + ".staticflickr.com/" + imgCon.server + "/" + imgCon.id + "_" + imgCon.secret;// +".jpg";
        switch (photoType) {
            case FlickrSearchManager.PHOTO_THUMB:
                tmp += "_t";
                break;
            case FlickrSearchManager.PHOTO_LARGE:
                tmp += "_z";
                break;

        }
        tmp += ".jpg";
        return tmp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Bitmap getThumb() {
        return thumb;
    }



    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getThumbURL() {
        return thumbURL;
    }
}