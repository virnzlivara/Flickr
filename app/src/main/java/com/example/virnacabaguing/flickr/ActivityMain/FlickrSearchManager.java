package com.example.virnacabaguing.flickr.ActivityMain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.example.virnacabaguing.flickr.ActivityMain.MainActivity.UIHandlerMainActivity;
import com.example.virnacabaguing.flickr.ActivityDisplay.ImageInfo;
import com.example.virnacabaguing.flickr.URLConnector;

/**
 * Created by virnacabaguing on 5/23/16.
 */
public class FlickrSearchManager {
    // String to create Flickr API urls
    private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
    private static final String FLICKR_PHOTOS_SEARCH_STRING = "flickr.photos.search";
    private static final int FLICKR_PHOTOS_SEARCH_ID = 1;
    private static final int NUMBER_OF_PHOTOS = 20;

    //You can set here your API_KEY
    private static final String APIKEY_SEARCH_STRING = "&api_key=722ca392ec53e930396f24afd29a10d6";
    private static final String TAGS_STRING = "&tags=";
    private static final String FORMAT_STRING = "&format=json";
    public static final int PHOTO_THUMB = 111;
    public static final int PHOTO_LARGE = 222;

    public static UIHandlerMainActivity uihandler;

    private static String createURL(int methodId, String parameter) {
        String method_type = "";
        String url = null;
        switch (methodId) {
            case FLICKR_PHOTOS_SEARCH_ID:
                method_type = FLICKR_PHOTOS_SEARCH_STRING;
                url = FLICKR_BASE_URL + method_type + APIKEY_SEARCH_STRING + TAGS_STRING + parameter + FORMAT_STRING + "&per_page="+NUMBER_OF_PHOTOS+"&media=photos";
                break;
        }
        return url;
    }

    public static ArrayList<ImageItem> searchImagesByTag(UIHandlerMainActivity uih, Context ctx, String tag) {
        uihandler = uih;
        String url = createURL(FLICKR_PHOTOS_SEARCH_ID, tag);
        ArrayList<ImageItem> tmp = new ArrayList<ImageItem>();
        String jsonString = null;
        try {
            if (URLConnector.isOnline(ctx)) {
                ByteArrayOutputStream baos = URLConnector.readBytes(url);
                jsonString = baos.toString();
            }
            try {
                JSONObject root = new JSONObject(jsonString.replace("jsonFlickrApi(", "").replace(")", ""));
                JSONObject photos = root.getJSONObject("photos");
                JSONArray imageJSONArray = photos.getJSONArray("photo");
                for (int i = 0; i < imageJSONArray.length(); i++) {
                    JSONObject item = imageJSONArray.getJSONObject(i);
                    ImageItem imgCon = new ImageItem(item.getString("id"), item.getString("owner"), item.getString("secret"), item.getString("server"),
                            item.getString("farm"));
                    imgCon.setPosition(i);
                    tmp.add(imgCon);
                }
                Message msg = Message.obtain(uih, UIHandlerMainActivity.ID_METADATA_DOWNLOADED);
                msg.obj = tmp;
                uih.sendMessage(msg);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (NullPointerException nue) {
            nue.printStackTrace();
        }

        return tmp;
    }



    public static class GetThumbnailsThread extends Thread {
        UIHandlerMainActivity uih;
        ImageItem imgContener;

        public GetThumbnailsThread(UIHandlerMainActivity uih, ImageItem imgCon) {
            this.uih = uih;
            this.imgContener = imgCon;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            imgContener.setThumb(getThumbnail(imgContener));
            if (imgContener.getThumb() != null) {
                Message msg = Message.obtain(uih, UIHandlerMainActivity.ID_UPDATE_ADAPTER);
                uih.sendMessage(msg);

            }
        }

    }

    public static Bitmap getThumbnail(ImageItem imgCon) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(imgCon.getThumbURL());
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
            Log.e("FlickrSearchManager", e.getMessage());
        }
        return bm;
    }

}
