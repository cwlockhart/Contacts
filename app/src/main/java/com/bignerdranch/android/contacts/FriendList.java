package com.bignerdranch.android.contacts;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Curt on 2/3/2016.
 * singleton class used for holding the data for all the friends
 */
public class FriendList {

    // member variables
    private ArrayList<Friend> mFriends;
    private ArrayList<FriendDetail> mFriendDetails;

    private static FriendList sFriendList;
    private Context mAppContext;

    // constructor
    private FriendList(Context appContext) {
        mAppContext = appContext;
        mFriends = new ArrayList<Friend>();
        mFriendDetails = new ArrayList<FriendDetail>();
    }

    // returns instance of the singleton if it has already been created
    public static FriendList get(Context c) {
        if (sFriendList == null) {
            sFriendList = new FriendList(c.getApplicationContext());
        }
        return sFriendList;
    }

    // getters and setters
    public ArrayList<Friend> getFriends() {
        return mFriends;
    }

    public Friend getFriend(int friendId) {
        for (Friend f : mFriends) {
            if (f.getId() == friendId) {
                return f;
            }
        }
        return null;
    }

    public ArrayList<FriendDetail> getFriendDetails() {
        return mFriendDetails;
    }

    public FriendDetail getFriendDetail(int friendId) {
        for (FriendDetail fd : mFriendDetails) {
            if (fd.getId() == friendId) {
                return fd;
            }
        }
        return null;
    }

    public void addFriend(Friend f) {
        mFriends.add(f);
    }

    public void addFriendDetail(FriendDetail fd) {
        mFriendDetails.add(fd);
    }
}
