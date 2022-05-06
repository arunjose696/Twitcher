package edu.ovgu.twitcher;

public class ListBirdsRecycler {
    private int imageView;
    private String birdName;

    ListBirdsRecycler(int imageView, String birdName ) {
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
