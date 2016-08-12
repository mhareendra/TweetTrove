package com.codepath.apps.tweettrove.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.activities.ImageTweetDetailsActivity;
import com.codepath.apps.tweettrove.activities.TweetDetailsActivity;
import com.codepath.apps.tweettrove.adapters.TweetsAdapter;
import com.codepath.apps.tweettrove.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweettrove.helpers.ItemClickSupport;
import com.codepath.apps.tweettrove.helpers.SpacesItemDecoration;
import com.codepath.apps.tweettrove.models.Media;
import com.codepath.apps.tweettrove.models.Tweet;
import com.victor.loading.rotate.RotateLoading;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Created by Hari on 8/8/2016.
 */
public abstract class TimelineFragment extends Fragment
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

    public LinearLayoutManager layoutManager;

    public boolean isDeviceOnline = true;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsAdapter(getActivity(), tweets);


    }

    public void clearTweets()
    {
        aTweets.clear();
        deleteTweetsFromDB();
    }

    private void setupRVTweets()
    {
        rvTweets.setAdapter(aTweets);
        layoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(layoutManager);


        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(isOnline())
                    populateTimeline(getLowestId());
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
                                Intent imageTweetIntent = new Intent(getActivity(), ImageTweetDetailsActivity.class);
                                imageTweetIntent.putExtra("tweet", Parcels.wrap(tweet));
                                imageTweetIntent.putExtra("position", position);

                                startActivityForResult(imageTweetIntent, 0);

                            }
                            else {

                                Intent intent = new Intent(getActivity(), TweetDetailsActivity.class);
                                intent.putExtra("tweet", Parcels.wrap(tweet));
                                intent.putExtra("position", position);
//                                startActivity(intent);
                                startActivityForResult(intent, 0);
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


    public void deleteTweetsFromDB()
    {
        if(tweets == null)
            return;
        int numTweets = tweets.size();
        for(int i = 0 ; i < numTweets; i++)
        {
            tweets.get(0).delete();
        }
    }

    public void saveTweetsToDB()
    {
        if(tweets == null)
            return;

        for (int i = 0; i < tweets.size(); i++)
        {
            tweets.get(i).getUser().save();
//            tweets.get(i).getEntities().save();
//            tweets.get(i).getEntities().getMedia().save();
//            tweets.get(i).getExtendedEntities().save();
            tweets.get(i).save();
        }
    }

    public void displayDBData()
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


    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        unbinder = ButterKnife.bind(this, view);
        clearTweets();
        setupRVTweets();



        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(isOnline())
                    clearTweets();
                populateTimeline(getLowestId());
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void addAll(ArrayList<Tweet> tweets)
    {
        aTweets.addAll(tweets);
    }



    @OnClick(R.id.fabComposeTweet)
    public void startComposeTweetFragment()
    {
        if(!isOnline())
        {
            Toast.makeText(getActivity(), "Please connect to the Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        FragmentManager fm = getFragmentManager(); //getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance(this, null, false);
        composeTweetFragment.show(fm,"compose_tweet_fragment");
    }


    @Override
    public void onFinishComposeTweetFragmentListener(String statusText) {

        if (statusText.isEmpty())
            return;

        finishComposeTweet(statusText);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == 0 && requestCode == 0) {

            boolean isRT  =  data.getBooleanExtra("isRetweeted", false);
            boolean isFav = data.getBooleanExtra("isFavorited", false);

            if(isFav || isRT)
            {
                aTweets.clear();
                    populateTimeline(getLowestId());
            }

        }
    }

    public long getLowestId()
    {
        long currentMinId = tweets.get(tweets.size() - 1).getUid();
        return currentMinId;
    }

    protected abstract void populateTimeline(long maxId);

    protected abstract void finishComposeTweet(String statusText);

}
