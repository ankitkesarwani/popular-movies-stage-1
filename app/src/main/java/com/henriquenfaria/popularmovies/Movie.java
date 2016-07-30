package com.henriquenfaria.popularmovies;

import android.net.Uri;

public class Movie {

    //TODO: Use double for the id?
    private String mId;
    private String mTitle;
    private Uri mPosterUri;

    public Movie(String id, String title, Uri posterUri) {
        mId = id;
        mTitle = title;
        mPosterUri = posterUri;
    }

    public Movie() {
    }

    public String getTitle() {
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

    public Uri getPosterUri() {
        return mPosterUri;
    }

    public void setPosterUri(Uri posterUri) {
        mPosterUri = posterUri;
    }

}
