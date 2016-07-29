package com.henriquenfaria.popularmovies;

public class Movie {

    //TODO: Use double for the id?
    private String mId;
    private String mTitle;


    public Movie(String id, String title) {
        mId = id;
        mTitle = title;
    }


    public String getName() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}
