package edu.ovgu.twitcher;

import android.view.View;

import java.util.ArrayList;
import java.util.Date;

public class Bird {
    private int imageView;
    private String birdName;
    private String date;
    private String time;
    private String wikiLink;
    private String category;
    private String notes;

    public Bird(int imageView, String birdName, String date, String time, String wikiLink, String category, String notes) {
        this.imageView = imageView;
        this.birdName = birdName;
        this.date = date;
        this.time = time;
        this.wikiLink = wikiLink;
        this.category = category;
        this.notes = notes;
    }

    private View.OnClickListener requestBtnClickListener;

    public int getImageView() { return imageView; }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getBirdName(){
        return birdName;
    }

    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public static ArrayList<Bird> getBirds() {
        ArrayList<Bird> items = new ArrayList<>();
        items.add(new Bird(R.drawable.twitcher, "Bird 1", "11.05.2022", "05:10 PM", "abc", "small", "NA"));
        items.add(new Bird(R.drawable.twitcher, "Bird 2", "12.05.2022", "05:11 PM", "bcd", "medium", "NA"));
        items.add(new Bird(R.drawable.twitcher, "Bird 3", "13.05.2022", "05:12 PM", "cde", "big", "NA"));
        items.add(new Bird(R.drawable.twitcher, "Bird 4", "14.05.2022", "05:13 PM", "def", "small", "NA"));
        return items;

    }




}
