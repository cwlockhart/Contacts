package com.bignerdranch.android.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Curt on 2/3/2016.
 * fragment displaying the layout of the friends detail page
 */
public class FriendDetailFragment extends Fragment {

    // member variables
    public static final String EXTRA_FRIEND_ID = "id";

    private Friend mFriend;
    private FriendDetail mFriendDetail;
    private ImageView mImgImageView;
    private TextView mNameTextView;
    private TextView mPhoneTextView;
    private TextView mAddressTextView;
    private TextView mBioTextView;
    private TextView mPhotoTextView;
    private TextView mStatusesTextView;
    private ImageView mAvailableImageView;

    // allows for putting an extra containing the friendId of the friend selected from the list
    public static FriendDetailFragment newInstance(int friendId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FRIEND_ID, friendId);

        FriendDetailFragment fragment = new FriendDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int friendId = (int)getArguments().getSerializable(EXTRA_FRIEND_ID);

        if (FriendList.get(getActivity()) != null) {
            mFriend = FriendList.get(getActivity()).getFriend(friendId);
            mFriendDetail = FriendList.get(getActivity()).getFriendDetail(6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_frienddetail, parent, false);

        if (mFriendDetail != null) {

            mImgImageView = (ImageView) v.findViewById(R.id.detail_img_imageview);
            mImgImageView.setImageBitmap(mFriendDetail.getImage());

            mNameTextView = (TextView) v.findViewById(R.id.detail_name_textview);
            mNameTextView.setText(mFriendDetail.getFirstName() + " " + mFriendDetail.getLastName());

            mPhoneTextView = (TextView) v.findViewById(R.id.detail_phone_textview);
            mPhoneTextView.setText(mFriendDetail.getPhone());

            mAddressTextView = (TextView) v.findViewById(R.id.detail_address_textview);
            mAddressTextView.setText(mFriendDetail.getAddress());

            mBioTextView = (TextView) v.findViewById(R.id.detail_bio_textview);
            mBioTextView.setText(mFriendDetail.getBio());
            mBioTextView.setMovementMethod(new ScrollingMovementMethod());

            mPhotoTextView = (TextView) v.findViewById(R.id.detail_photos_textview);
            String[] photoURLs = mFriendDetail.getPhotoURLs();
            String photoLinks = "Photo Links:" + "<br />" + "<br />";
            for (int i = 0; i < photoURLs.length; i++) {
                String link = "<a href=\"" + photoURLs[i] + "\">Photo #" + (i + 1) + "</a>";
                photoLinks += link + "<br />" + "<br />";
            }
            mPhotoTextView.setText(Html.fromHtml(photoLinks));
            mPhotoTextView.setMovementMethod(LinkMovementMethod.getInstance());

            mStatusesTextView = (TextView) v.findViewById(R.id.detail_statuses_textview);
            mStatusesTextView.setMovementMethod(new ScrollingMovementMethod());
            String[] statuses = mFriendDetail.getStatuses();
            for (int j = 0; j < statuses.length; j++) {
                mStatusesTextView.append(statuses[j] + "\n" + "\n");
            }


            mAvailableImageView = (ImageView) v.findViewById(R.id.detail_availableCircle);
            if (mFriendDetail.isAvailable()) {
                mAvailableImageView.setBackgroundResource(R.drawable.greencircle);
            } else {
                mAvailableImageView.setBackgroundResource(R.drawable.redcircle);
            }

        }
        return v;
    }
}
