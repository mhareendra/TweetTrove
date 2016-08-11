package com.codepath.apps.tweettrove.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.adapters.FragmentTimelinePagerAdapter;
import com.codepath.apps.tweettrove.fragments.TimelineFragment;
import com.victor.loading.rotate.RotateLoading;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity

{
//    @BindView(R.id.rvTweets)
    public RecyclerView rvTweets;

//    @BindView(R.id.fabComposeTweet)
    public FloatingActionButton fabComposeTweet;

//    @BindView(R.id.swipeContainer)
    public SwipeRefreshLayout swipeContainer;

//    @BindView(R.id.rotateLoading)
    public RotateLoading rotateLoading;

    @BindView(R.id.viewpager)
    public ViewPager viewPager;

    @BindView(R.id.sliding_tabs)
    public TabLayout tabLayout;



//    @BindView(R.id.fragment_timeline)
    TimelineFragment timelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
//
//        if(savedInstanceState == null) {
//            timelineFragment = (TimelineFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.fragment_timeline);
//        }

        viewPager.setAdapter(new FragmentTimelinePagerAdapter(getSupportFragmentManager(),
                TimelineActivity.this));

        tabLayout.setupWithViewPager(viewPager);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.twitter_launcher);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if(id == R.id.action_profile)
        {
            startProfileActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startProfileActivity()
    {
        Intent intent = new Intent(TimelineActivity.this,ProfileActivity.class );
        startActivity(intent);
    }
}
