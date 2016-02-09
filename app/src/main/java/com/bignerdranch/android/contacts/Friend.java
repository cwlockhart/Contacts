package com.bignerdranch.android.contacts;

import android.graphics.Bitmap;

/**
 * Created by Curt on 2/3/2016.
 * model class containing data for each friend
 */
public class Friend {

    // member variables
    private int mId;
    private String mImageURL;
    private Bitmap mImage;
    private String mFirstName;
    private String mLastName;
    private String mStatus;
    private boolean mAvailable;

    // getters and setters for member variables
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public boolean isAvailable() {
        return mAvailable;
    }

    public void setAvailable(boolean available) {
        mAvailable = available;
    }

    public void setImage(Bitmap bmp) {
        mImage = bmp;
    }

    public Bitmap getImage() {
        return mImage;
    }
}
