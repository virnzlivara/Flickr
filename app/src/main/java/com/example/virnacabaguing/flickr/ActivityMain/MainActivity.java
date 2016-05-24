package com.example.virnacabaguing.flickr.ActivityMain;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


import com.example.virnacabaguing.flickr.ActivityDisplay.DisplayImageInfo;
import com.example.virnacabaguing.flickr.ActivityMain.FlickrSearchManager.GetThumbnailsThread;
import com.example.virnacabaguing.flickr.R;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;

    public UIHandlerMainActivity uihandler;
    public GridImageAdapter imgAdapter;
    private ArrayList<ImageItem> imageList;

    private Button searchButton;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uihandler = new UIHandlerMainActivity();
        searchButton = (Button) findViewById(R.id.button1);
        searchField = (EditText) findViewById(R.id.editText1);

        searchButton.setOnClickListener(onSearchButtonClicked);

        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                searchImageInfo(position);
            }
        });
    }

    OnClickListener onSearchButtonClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            hideVirtualKeyBoard();
            if (gridView.getAdapter() != null) {
                imgAdapter.imageItem = new ArrayList<ImageItem>();
                gridView.setAdapter(imgAdapter);
            }
            new Thread(searchImages).start();
        }


    };

    private void hideVirtualKeyBoard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    Runnable searchImages = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            String tag = searchField.getText().toString().trim();
            if (tag != null && tag.length() >= 3)
                FlickrSearchManager.searchImagesByTag(uihandler, getApplicationContext(), encodeTagCharacters(tag));
        }
    };

    private String encodeTagCharacters(String tag){
        //basic encoding
        String newTag = tag;
        newTag = newTag.replace(" ", "+");
        newTag = newTag.replace("/", "%2F");
        return newTag;
    }


    private void searchImageInfo(int position){
        String photosetId = imgAdapter.getImageItem().get(position).getId();
        Intent intent = new Intent(getApplicationContext(), DisplayImageInfo.class);
        intent.putExtra("photosetId", photosetId);
        startActivity(intent);
    }

    class UIHandlerMainActivity extends Handler {
        public static final int ID_METADATA_DOWNLOADED = 0;
        public static final int ID_UPDATE_ADAPTER = 2;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ID_METADATA_DOWNLOADED:
                    if (msg.obj != null) {
                        imageList = (ArrayList<ImageItem>) msg.obj;
                        imgAdapter = new GridImageAdapter(getApplicationContext(), imageList);
                        gridView.setAdapter(imgAdapter);
                        if (imgAdapter.getCount() > 0) {
                            for (int i = 0; i < imgAdapter.getCount(); i++) {
                                new GetThumbnailsThread(uihandler, imgAdapter.getImageItem().get(i)).start();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No record found.",
                                    Toast.LENGTH_SHORT).show();
                        }


                        imgAdapter.notifyDataSetChanged();

                    }
                    break;
                case ID_UPDATE_ADAPTER:
                    imgAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    }



}
