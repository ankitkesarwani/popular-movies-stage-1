package com.henriquenfaria.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeValue(mPosterUri);
    }


    public Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mPosterUri = (Uri) in.readValue(Movie.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
