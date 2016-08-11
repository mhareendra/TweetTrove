package com.codepath.apps.tweettrove.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.request.target.ViewTarget;
import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.adapters.FragmentTimelinePagerAdapter;
import com.codepath.apps.tweettrove.adapters.TweetsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity
implements TweetsAdapter.ProfileImageClickListener
{

    @BindView(R.id.viewpager)
    public ViewPager viewPager;

    @BindView(R.id.sliding_tabs)
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

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
            startProfileActivity("haritestprofile");
        }
        return super.onOptionsItemSelected(item);
    }

    private void startProfileActivity(String screenName)
    {
        Intent intent = new Intent(TimelineActivity.this,ProfileActivity.class );
        intent.putExtra("screenName", screenName);
        startActivity(intent);
    }

    @Override
    public void onProfileImageClick(String screenName) {
        try
        {
            if(screenName != null && !screenName.isEmpty())
            {
                startProfileActivity(screenName);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public class App extends Application {
        @Override public void onCreate() {
            super.onCreate();
            ViewTarget.setTagId(R.id.glide_tag);
        }
    }
}
