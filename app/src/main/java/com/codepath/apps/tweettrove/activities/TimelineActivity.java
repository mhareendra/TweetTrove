package com.codepath.apps.tweettrove.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.TwitterApplication;
import com.codepath.apps.tweettrove.TwitterClient;
import com.codepath.apps.tweettrove.adapters.TweetsAdapter;
import com.codepath.apps.tweettrove.fragments.ComposeTweetFragment;
import com.codepath.apps.tweettrove.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweettrove.helpers.ItemClickSupport;
import com.codepath.apps.tweettrove.helpers.SpacesItemDecoration;
import com.codepath.apps.tweettrove.models.Media;
import com.codepath.apps.tweettrove.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

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

    public boolean isDeviceOnline = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);


        tweets = new ArrayList<>();
        aTweets = new TweetsAdapter(this, tweets);
        clearTweets();
        setupRVTweets();
        client = TwitterApplication.getRestClient();

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        //binding.setTimelineActivity(this);
        populateTimeline();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                clearTweets();
                populateTimeline();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    private void setupRVTweets()
    {
        rvTweets.setAdapter(aTweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);


        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(isOnline())
                    populateTimeline();
            }
        });


        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        try {
                            Tweet tweet = tweets.get(position);

                            Media media = null;
                            if (tweet.getEntities().getMedia() != null) {
                                media = tweet.getEntities().getMedia().get(0);
                            }
                            if (media != null && media.getMediaUrlHttps() != null && !media.getMediaUrlHttps().isEmpty()) {
                                Intent imageTweetIntent = new Intent(TimelineActivity.this, ImageTweetDetailsActivity.class);
                                imageTweetIntent.putExtra("tweet", Parcels.wrap(tweet));
                                startActivity(imageTweetIntent);

                            }
                            else {

                                Intent intent = new Intent(TimelineActivity.this, TweetDetailsActivity.class);
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                startActivity(intent);
                            }
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
        );

        SpacesItemDecoration decoration = new SpacesItemDecoration(20);
        rvTweets.addItemDecoration(decoration);
        setupRVAnimator();

    }


    private void setupRVAnimator() {
        SlideInBottomAnimationAdapter animator = new SlideInBottomAnimationAdapter(aTweets);
        animator.setDuration(500);
        animator.setFirstOnly(false);
        animator.setInterpolator(new OvershootInterpolator(1f));

        rvTweets.setItemAnimator(new FadeInUpAnimator());

        AlphaInAnimationAdapter alphaAnimator = new AlphaInAnimationAdapter(animator);
        alphaAnimator.setFirstOnly(false);
        alphaAnimator.setDuration(500);
        alphaAnimator.setInterpolator(new AccelerateInterpolator(1f));
        rvTweets.setAdapter(alphaAnimator);

    }


    @OnClick(R.id.fabComposeTweet)
    public void startComposeTweetFragment()
    {
        if(!isOnline())
        {
            Toast.makeText(this, "Please connect to the Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance(null, false);
        composeTweetFragment.show(fm,"compose_tweet_fragment");
    }


    private void clearTweets()
    {
        aTweets.clear();
        deleteTweetsFromDB();
    }

    private void deleteTweetsFromDB()
    {
        if(tweets == null)
            return;
        int numTweets = tweets.size();
        for(int i = 0 ; i < numTweets; i++)
        {
            tweets.get(0).delete();
        }
    }

    private void saveTweetsToDB()
    {
        if(tweets == null)
            return;

        for (int i = 0; i < tweets.size(); i++)
        {
            tweets.get(i).getUser().save();
            tweets.get(i).getEntities().save();
//            tweets.get(i).getEntities().getMedia().save();
            tweets.get(i).getExtendedEntities().save();
            tweets.get(i).save();
        }
    }


    private void populateTimeline()
    {

        if(!isOnline())
        {
            isDeviceOnline = false;
            Toast.makeText(TimelineActivity.this, "Could not connect to the Internet, displaying offline tweets", Toast.LENGTH_SHORT).show();
            displayDBData();
            swipeContainer.setRefreshing(false);
            return;
        }
        else
        {
            isDeviceOnline =true;
        }

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
                    saveTweetsToDB();
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
                if(errorResponse!=null)
                    Log.e("Timeline error: ", errorResponse.toString());
                swipeContainer.setRefreshing(false);
                displayDBData();
            }

        });

    }

    private void displayDBData()
    {
        try
        {
            aTweets.addAll(Tweet.getAll());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
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
