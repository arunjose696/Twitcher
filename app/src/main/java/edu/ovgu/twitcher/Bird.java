package edu.ovgu.twitcher;

import java.util.Date;

public class Bird {
    private int imageView;
    private String birdName;
    private Date date;
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


}
