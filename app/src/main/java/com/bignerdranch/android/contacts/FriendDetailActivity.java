package com.bignerdranch.android.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Created by Curt on 2/3/2016.
 * activity that hosts the fragment layout of the friends detail page
 */
public class FriendDetailActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Friend> mFriends;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // viewpager allows for swiping between detail pages
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mFriends = FriendList.get(this).getFriends();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Friend friend = mFriends.get(position);
                return FriendDetailFragment.newInstance(friend.getId());
            }

            @Override
            public int getCount() {
                return mFriends.size();
            }
        });

        // sets the viewpaer to the selected friend in the list
        int friendId = (int)getIntent().getSerializableExtra(FriendDetailFragment.EXTRA_FRIEND_ID);
        for (int i = 0; i < mFriends.size(); i++) {
            if (mFriends.get(i).getId() == friendId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
