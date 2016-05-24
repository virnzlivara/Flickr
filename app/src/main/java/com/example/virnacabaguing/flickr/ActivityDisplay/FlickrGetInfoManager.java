package com.example.virnacabaguing.flickr.ActivityDisplay;

import android.content.Context;
import android.os.Message;
import com.example.virnacabaguing.flickr.ActivityDisplay.DisplayImageInfo.UIHandlerDisplayActivity;
import com.example.virnacabaguing.flickr.URLConnector;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


/**
 * Created by virnacabaguing on 5/23/16.
 */
public class FlickrGetInfoManager {
    // String to create Flickr API urls
    private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
    private static final String FLICKR_GET_INFO_STRING = "flickr.photos.getInfo";
    private static final int FLICKR_GET_INFO_ID = 3;

    //You can set here your API_KEY
    private static final String APIKEY_SEARCH_STRING = "&api_key=722ca392ec53e930396f24afd29a10d6";
    private static final String PHOTO_ID_STRING = "&photo_id=";
    private static final String FORMAT_STRING = "&format=json";

    public static UIHandlerDisplayActivity uihandler;

    private static String createURL(int methodId, String parameter) {
        String method_type = "";
        String url = null;
        switch (methodId) {
            case FLICKR_GET_INFO_ID:
                method_type = FLICKR_GET_INFO_STRING;
                url = FLICKR_BASE_URL + method_type + PHOTO_ID_STRING + parameter + APIKEY_SEARCH_STRING + FORMAT_STRING;
                break;
        }
        return url;
    }


    public static ImageInfo searchImageInfo(UIHandlerDisplayActivity uih, Context ctx, String photoSetId){
        uihandler = uih;
        String url = createURL(FLICKR_GET_INFO_ID, photoSetId);
        ImageInfo imageInfo = new ImageInfo();
        String jsonString = null;
        try {
            if (URLConnector.isOnline(ctx)) {
                ByteArrayOutputStream baos = URLConnector.readBytes(url);
                jsonString = baos.toString();
            }
            try {
                JSONObject root = new JSONObject(jsonString.replace("jsonFlickrApi(", "").replace(")", ""));
                JSONObject photo = root.getJSONObject("photo");
                JSONObject owner = photo.getJSONObject("owner");
                JSONObject title = photo.getJSONObject("title");
                JSONObject description  = photo.getJSONObject("description");
                JSONObject date  = photo.getJSONObject("dates");

                String photoURL = createLargePhotoURL(photo.getString("id"), photo.getString("owner"), photo.getString("secret"), photo.getString("server"), photo.getString("farm"));

                imageInfo = new ImageInfo(photoURL, owner.getString("username"), title.getString("_content"), description.getString("_content"), date.getString("taken"));
                Message msg = Message.obtain(uih, UIHandlerDisplayActivity.ID_SHOW_IMAGE);
                msg.obj = imageInfo;
                uih.sendMessage(msg);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (NullPointerException nue) {
            nue.printStackTrace();
        }

        return imageInfo;
    }

    private static String createLargePhotoURL(String id, String owner, String secret, String server, String farm) {
        return "http://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + "_z.jpg";

    }





}
