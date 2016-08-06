package com.codepath.apps.tweettrove.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.TwitterApplication;
import com.codepath.apps.tweettrove.TwitterClient;
import com.codepath.apps.tweettrove.adapters.TweetsAdapter;
import com.codepath.apps.tweettrove.fragments.ComposeTweetFragment;
import com.codepath.apps.tweettrove.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweettrove.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity
implements ComposeTweetFragment.ComposeTweetFragmentListener
{



    private ArrayList<Tweet> tweets;
    private TweetsAdapter aTweets;

    @BindView(R.id.rvTweets)
    public RecyclerView rvTweets;

    @BindView(R.id.fabComposeTweet)
    public FloatingActionButton fabComposeTweet;

    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeContainer;

    @BindView(R.id.rotateLoading)
    public RotateLoading rotateLoading;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);


        tweets = new ArrayList<>();
        aTweets = new TweetsAdapter(this, tweets);
        rvTweets.setAdapter(aTweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);

        client = TwitterApplication.getRestClient();
        populateTimeline();
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                aTweets.clear();
                populateTimeline();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


    @OnClick(R.id.fabComposeTweet)
    public void startComposeTweetFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance();
        composeTweetFragment.show(fm,"compose_tweet_fragment");
    }


    private void populateTimeline()
    {

        rotateLoading.start();
        client.getHomeTimeline(new JsonHttpResponseHandler()
        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Timeline success: ", response.toString());
                try {

                    if(rotateLoading.isStart())
                        rotateLoading.stop();
                    int curSize = aTweets.getItemCount();
                    tweets.addAll(Tweet.fromJSONArray(response));
                    aTweets.notifyItemRangeInserted(curSize, aTweets.getItemCount());
                    swipeContainer.setRefreshing(false);

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();

                    if(rotateLoading.isStart())
                        rotateLoading.stop();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                if(rotateLoading.isStart())
                    rotateLoading.stop();

                Toast.makeText(TimelineActivity.this, "There was an error when attempting to connect to Twitter", Toast.LENGTH_SHORT).show();
                Log.e("Timeline error: ", errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }

        });

    }

    @Override
    public void onFinishComposeTweetFragmentListener(String statusText) {

        if (statusText.isEmpty())
            return;

        client.postStatus(new JsonHttpResponseHandler() {
              @Override
              public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                  Log.d("onSuccess Compose:", response.toString());
              }

          }, statusText

        );
        aTweets.clear();
        populateTimeline();

    }
}
