package com.codepath.apps.tweettrove.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.adapters.FragmentTimelinePagerAdapter;
import com.codepath.apps.tweettrove.adapters.TweetsAdapter;
import com.codepath.apps.tweettrove.fragments.ComposeTweetFragment;
import com.codepath.apps.tweettrove.helpers.TwitterApplication;
import com.codepath.apps.tweettrove.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity
implements TweetsAdapter.ProfileImageClickListener, ComposeTweetFragment.ComposeTweetFragmentListener
{

    @BindView(R.id.viewpager)
    public ViewPager viewPager;

    @BindView(R.id.sliding_tabs)
    public TabLayout tabLayout;

    private TwitterClient client;

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

        client = TwitterApplication.getRestClient();
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
        else if(id == R.id.action_messages)
        {
            Intent intent = new Intent(TimelineActivity.this, MessagesActivity.class);
            startActivity(intent);
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

    @Override
    public void onFinishComposeTweetFragmentListener(String statusText) {
        if (statusText.isEmpty())
            return;

        client.postStatus(new JsonHttpResponseHandler() {
                              @Override
                              public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                 // Log.d("onSuccess Compose:", response.toString());
                              }

                          }, statusText

        );

    }
}
