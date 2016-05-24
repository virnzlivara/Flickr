package com.example.virnacabaguing.flickr.ActivityDisplay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virnacabaguing.flickr.R;

import java.net.URL;


public class DisplayImageInfo extends AppCompatActivity {


    private ImageInfo imageInfo;
    public UIHandlerDisplayActivity uihandler;
    private String photoId;
    private TextView txtOwner;
    private TextView txtDateTaken;
    private TextView txtTitle;
    private TextView txtDescription;
    private ImageView imageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uihandler = new UIHandlerDisplayActivity();
        Intent intent = getIntent();
        photoId = intent.getExtras().getString("photosetId");

        new Thread(searchImageInfo).start();
    }

    Runnable searchImageInfo = new Runnable() {
        @Override
        public void run() {
            if (photoId != null)
                FlickrGetInfoManager.searchImageInfo(uihandler, getApplicationContext(), photoId);
        }
    };

    private void displayImageInfo(ImageInfo imageInfo) {

        txtOwner = (TextView) findViewById(R.id.txtOwner);
        txtDateTaken = (TextView) findViewById(R.id.txtTaken);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        txtOwner.setText("Owner : " + imageInfo.getOwner());
        txtDateTaken.setText("Date Taken :" + imageInfo.getDate());
        txtTitle.setText("Title : " + imageInfo.getTitle());
        txtDescription.setText("Descriptions : "  + imageInfo.getDescription());
    }

    private void displayImage(String str) {

        imageDisplay = (ImageView) findViewById(R.id.imageDisplay);

//        URL newurl = new URL(str);
//        Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
//        imageDisplay.setImageBitmap(mIcon_val);
    }

    class UIHandlerDisplayActivity extends Handler {
        public static final int ID_SHOW_IMAGE = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ID_SHOW_IMAGE:
                    imageInfo = (ImageInfo) msg.obj;
                    setContentView(R.layout.activity_display_image_info);
                    displayImage(imageInfo.getUrl());

                    displayImageInfo(imageInfo);
                    break;
//                case ID_UPDATE_ADAPTER:
                    // Update adapter with thumnails
//                    imgAdapter.notifyDataSetChanged();
//                    break;
            }
            super.handleMessage(msg);
        }
    }

}
