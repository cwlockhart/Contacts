package com.bignerdranch.android.contacts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
fragment displaying the listview for all the friends
 */

public class FriendListFragment extends ListFragment {

    // member variables
    private ArrayList<Friend> mFriends;
    private FriendAdapter mFriendAdapter;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.friends_title);

        mFriends = FriendList.get(getActivity()).getFriends();

        mFriendAdapter = new FriendAdapter(mFriends);

        setListAdapter(mFriendAdapter);

        if (FriendList.get(getActivity()).getFriends().isEmpty()) {
            updateFriends();
            updateFriendDetails();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Friend f = ((FriendAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), FriendDetailActivity.class);
        i.putExtra(FriendDetailFragment.EXTRA_FRIEND_ID, f.getId());
        startActivity(i);
    }


    // adapter class for the listview, it gets the friends saved in the friendlist class to display
    private class FriendAdapter extends ArrayAdapter<Friend> {

        public FriendAdapter(ArrayList<Friend> friends) {
            super(getActivity(), 0, friends);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_friend, null);
            }

            Friend f = getItem(position);

            ImageView availableCircle = (ImageView)convertView.findViewById(R.id.friend_list_item_availableCircle);
            if (f.isAvailable()) {
                availableCircle.setBackgroundResource(R.drawable.greencircle);
            } else {
                availableCircle.setBackgroundResource(R.drawable.redcircle);
            }


            ImageView imageView = (ImageView)convertView.findViewById(R.id.friend_list_item_imgImageView);
            imageView.setImageBitmap(f.getImage());

            TextView fullNameTextView = (TextView)convertView.findViewById(R.id.friend_list_item_fullNameTextView);
            fullNameTextView.setText(f.getFirstName() + " " + f.getLastName());

            TextView statusTextView = (TextView)convertView.findViewById(R.id.friend_list_item_statusTextView);
            statusTextView.setText(f.getStatus());

            return convertView;
        }
    }

    // methods for executing the asynctasks
    private void updateFriends() {
        GetFriendsTask friendsTask = new GetFriendsTask();
        friendsTask.execute();
    }

    public void updateFriendDetails() {
        GetFriendDetailTask detailTask = new GetFriendDetailTask();
        detailTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((FriendAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FriendAdapter)getListAdapter()).notifyDataSetChanged();
    }

    // asynctask classes for fetching the data for the friends with network calls and parsing the JSON
    public class GetFriendsTask extends AsyncTask<Void, Void, Friend[]> {

        // JSON parsing tags
        private final String LOG_TAG = GetFriendsTask.class.getSimpleName();
        private final String TAG_ID = "id";
        private final String TAG_IMG = "img";
        private final String TAG_FIRST_NAME = "first_name";
        private final String TAG_LAST_NAME = "last_name";
        private final String TAG_STATUS = "status";
        private final String TAG_AVAILABLE = "available";

        // parses JSON string
        private Friend[] getFriendDataFromJson(String friendJsonStr) throws JSONException {

            JSONArray friendArray = new JSONArray(friendJsonStr);

            Friend[] friendResults = new Friend[friendArray.length()];
            for (int i = 0; i < friendArray.length(); i++) {

                JSONObject JSONfriend = friendArray.getJSONObject(i);
                Friend friend = new Friend();
                friend.setId(JSONfriend.getInt(TAG_ID));
                friend.setImageURL(JSONfriend.getString(TAG_IMG));
                try {
                    URL url = new URL(friend.getImageURL());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    friend.setImage(bmp);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error getting bitmap", e);
                }
                friend.setFirstName(JSONfriend.getString(TAG_FIRST_NAME));
                friend.setLastName(JSONfriend.getString(TAG_LAST_NAME));
                friend.setStatus(JSONfriend.getString(TAG_STATUS));
                friend.setAvailable(JSONfriend.getBoolean(TAG_AVAILABLE));
                friendResults[i] = friend;

                FriendList.get(getActivity()).addFriend(friend);
            }
            return friendResults;
        }

        // displays progress dialog as the data is loaded
        @Override
        protected void onPreExecute() {
            mProgress = new ProgressDialog(getContext());
            mProgress.setTitle("Loading...");
            mProgress.setMessage("Please wait.");
            mProgress.setCancelable(false);
            mProgress.show();
        }

        // adds friend objects to the listview adapter
        @Override
        protected void onPostExecute(Friend[] result) {
            if(result != null) {
                mFriendAdapter.clear();
                mFriendAdapter.addAll(result);
                ((FriendAdapter)getListAdapter()).notifyDataSetChanged();
            }
        }

        @Override
        protected Friend[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String friendJsonStr = null;

            try {

                final String FRIENDS_URL = "http://private-5bdb3-friendmock.apiary-mock.com/friends";
                URL url = new URL(FRIENDS_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                friendJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error getting friend data", e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getFriendDataFromJson(friendJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        return null;
        }
    }

    // asyctask for fetching and parsing the data for the friends detail page
    public class GetFriendDetailTask extends AsyncTask<Void, Void, FriendDetail> {

        // JSON parsing tags
        private final String LOG_TAG = GetFriendDetailTask.class.getSimpleName();
        private final String TAG_ID = "id";
        private final String TAG_IMG = "img";
        private final String TAG_FIRST_NAME = "first_name";
        private final String TAG_LAST_NAME = "last_name";
        private final String TAG_PHONE = "phone";
        private final String TAG_ADDRESS_1 = "address_1";
        private final String TAG_CITY = "city";
        private final String TAG_STATE = "state";
        private final String TAG_ZIPCODE = "zipcode";
        private final String TAG_BIO = "bio";
        private final String TAG_PHOTOS = "photos";
        private final String TAG_STATUSES = "statuses";
        private final String TAG_AVAILABLE = "available";

        @Override
        protected FriendDetail doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String detailJsonStr = null;

            try {

                final String DETAIL__URL = "http://private-5bdb3-friendmock.apiary-mock.com/friends/id";
                URL url = new URL(DETAIL__URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                detailJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error getting detail data", e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return(getDetailDataFromJson(detailJsonStr));
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        // parses JSON string for friend detail
        private FriendDetail getDetailDataFromJson(String detailJsonStr) throws JSONException {

            JSONObject detailObject = new JSONObject(detailJsonStr);
            JSONArray photoArray = detailObject.getJSONArray(TAG_PHOTOS);
            JSONArray statusesArray = detailObject.getJSONArray(TAG_STATUSES);

            FriendDetail friendDetailResult = new FriendDetail();

            friendDetailResult.setId(detailObject.getInt(TAG_ID));
            friendDetailResult.setImageURL(detailObject.getString(TAG_IMG));
            try {
                URL url = new URL(friendDetailResult.getImageURL());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                friendDetailResult.setImage(bmp);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error getting bitmap", e);
            }
            friendDetailResult.setFirstName(detailObject.getString(TAG_FIRST_NAME));
            friendDetailResult.setLastName(detailObject.getString(TAG_LAST_NAME));
            friendDetailResult.setPhone(detailObject.getString(TAG_PHONE));
            friendDetailResult.setAddress(detailObject.getString(TAG_ADDRESS_1),
                    detailObject.getString(TAG_CITY),
                    detailObject.getString(TAG_STATE),
                    detailObject.getString(TAG_ZIPCODE));
            friendDetailResult.setBio(detailObject.getString(TAG_BIO));

            String[] photoURLs = new String[photoArray.length()];
            Bitmap[] photos = new Bitmap[photoArray.length()];
            for (int i = 0; i < photoArray.length(); i++) {
                photoURLs[i] = photoArray.get(i).toString();
                try {
                    URL url = new URL(photoURLs[i]);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    photos[i] = bmp;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error getting bitmap", e);
                }
            }
            friendDetailResult.setPhotoURLs(photoURLs);
            friendDetailResult.setPhotos(photos);

            String[] statuses = new String[statusesArray.length()];
            for (int j = 0; j < statusesArray.length(); j++) {
                statuses[j] = statusesArray.get(j).toString();
            }
            friendDetailResult.setStatuses(statuses);

            friendDetailResult.setAvailable(detailObject.getBoolean(TAG_AVAILABLE));

            return friendDetailResult;
        }

        // dismisses the loading dialog after all the data is received and stored in the friendlist class
        @Override
        protected void onPostExecute(FriendDetail result) {
            if (result != null) {
                FriendList.get(getActivity()).addFriendDetail(result);
            }
            if (mProgress != null) {
                if (mProgress.isShowing()) {
                    mProgress.dismiss();
                }
                mProgress = null;
            }
        }
    }
}
