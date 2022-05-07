package edu.ovgu.twitcher;

public class Bird {
    private int imageView;
    private String birdName;

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
