package com.codepath.apps.tweettrove.fragments;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tweettrove.helpers.TwitterApplication;
import com.codepath.apps.tweettrove.models.Tweet;
import com.codepath.apps.tweettrove.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

//import com.codepath.apps.restclienttemplate.R;

public class HomeTimelineFragment extends TimelineFragment {


    private TwitterClient client;

    public static HomeTimelineFragment newInstance() {
        Bundle args = new Bundle();
        HomeTimelineFragment fragment = new HomeTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateTimeline(currentMaxId);

    }

    private long currentMaxId = -1;
    @Override
    protected void populateTimeline(long maxId)
    {

        if(!isOnline())
        {
            isDeviceOnline = false;
            Toast.makeText(getActivity(), "Could not connect to the Internet, displaying offline tweets", Toast.LENGTH_SHORT).show();
            displayDBData();
            if(swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }
            return;
        }
        else
        {
            isDeviceOnline =true;
        }

        if(rotateLoading != null)
            rotateLoading.start();
        client.getHomeTimeline(new JsonHttpResponseHandler()
        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Timeline success: ", response.toString());
                try {


                    if(rotateLoading != null && rotateLoading.isStart())
                        rotateLoading.stop();
//                    int curSize = aTweets.getItemCount();
//                    tweets.addAll(Tweet.fromJSONArray(response));
//                    aTweets.notifyItemRangeInserted(curSize, aTweets.getItemCount());
                    addAll(Tweet.fromJSONArray(response));
                    currentMaxId = getLowestId();
                    Log.d("currentMaxId: ", String.valueOf(currentMaxId));
                    swipeContainer.setRefreshing(false);
                    saveTweetsToDB();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();

                    if(rotateLoading != null && rotateLoading.isStart())
                        rotateLoading.stop();


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                if(rotateLoading!= null && rotateLoading.isStart())
                    rotateLoading.stop();

                Toast.makeText(getActivity(), "There was an error when attempting to connect to Twitter", Toast.LENGTH_SHORT).show();
                if(errorResponse!=null)
                    Log.e("Timeline error: ", errorResponse.toString());
                swipeContainer.setRefreshing(false);
                displayDBData();
            }

        }, maxId);

    }

    @Override
    protected void finishComposeTweet(String statusText) {
        client.postStatus(new JsonHttpResponseHandler() {
                              @Override
                              public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                  clearTweets();
                                  populateTimeline(-1);
                                  Log.d("onSuccess Compose:(arr)", response.toString());
                              }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    clearTweets();
                                    populateTimeline(-1);
                                    Log.d("onSuccess Compose:(obj)", response.toString());
                                }
        }, statusText );
    }


}
