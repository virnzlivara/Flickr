package com.example.virnacabaguing.flickr.ActivityDisplay;


import com.example.virnacabaguing.flickr.ActivityMain.FlickrSearchManager;

/**
 * Created by virnacabaguing on 5/23/16.
 */
public class ImageInfo {
    private String owner;
    private String title;
    private String description;
    private String date; //// TODO: 5/23/16 changed this to date
    private String url;

    public ImageInfo(){
        super();

    }
    public ImageInfo(String url, String owner, String title, String description, String date){
        super();
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.date = date;
        this.url = url;

    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}
