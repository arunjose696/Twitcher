package edu.ovgu.twitcher;

import android.graphics.Bitmap;
import java.util.Date;


public class Bird {
    private String id;
    public static final String COLLECTION = "birds";
    public static final String FIELD_birdId = "birdId";
    public static final String FIELD_name = "name";
    public static final String FIELD_notes = "notes";
    public static final String FIELD_date = "date";
    public static final String FIELD_wikiLink = "wikiLink";
    public static final String FIELD_time = "time";

    private int imageView;
    private String birdName;
    private Date date;
    private Date time;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    public Bird(int imageView, String birdName, Date date, Date time, String wikiLink, String category, String notes) {
        this.imageView = imageView;
        this.birdName = birdName;
        this.date = date;
        this.time = time;
        this.wikiLink = wikiLink;
        this.category = category;
        this.notes = notes;
    }
    public Bird(){}

    private String wikiLink,category,notes;

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public void setWikiLink(String wikiLink) {
        this.wikiLink = wikiLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    Bird(int imageView, String birdName ) {
        this.imageView = imageView;
        this.birdName = birdName;
    }
    public int getImageView() {
        return imageView;
    }

    public String getBirdName(){
        return birdName;
    }

    @Override
    public String toString() {
        return this.birdName;
    }




}
