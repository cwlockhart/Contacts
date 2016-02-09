package com.bignerdranch.android.contacts;

import android.graphics.Bitmap;

/**
 * Created by Curt on 2/4/2016.
 * model class containing the details page information for a friend
 */
public class FriendDetail {

    // member variables
    private int mId;
    private String mImageURL;
    private Bitmap mImage;
    private String mFirstName;
    private String mLastName;
    private String mPhone;
    private String mAddress;
    private String mBio;
    private String[] mPhotoURLs;
    private Bitmap[] mPhotos;
    private String[] mStatuses;
    private boolean mAvailable;

    // getters and setters for member variables
    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address_1, String city, String state, String zipcode) {
        mAddress = address_1 + "\n" + city + ", " + state + " " + zipcode;
    }

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        mBio = bio;
    }

    public Bitmap[] getPhotos() {
        return mPhotos;
    }

    public void setPhotos(Bitmap[] photos) {
        mPhotos = photos;
    }

    public String[] getStatuses() {
        return mStatuses;
    }

    public void setStatuses(String[] statuses) {
        mStatuses = statuses;
    }

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

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
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

    public String[] getPhotoURLs() {
        return mPhotoURLs;
    }

    public void setPhotoURLs(String[] photoURLs) {
        mPhotoURLs = photoURLs;
    }

    public boolean isAvailable() {
        return mAvailable;
    }

    public void setAvailable(boolean available) {
        mAvailable = available;
    }
}
