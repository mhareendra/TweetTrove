package com.codepath.apps.tweettrove.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.adapters.TweetsAdapter;
import com.codepath.apps.tweettrove.fragments.ProfileHeaderFragment;
import com.codepath.apps.tweettrove.fragments.UserTimelineFragment;
import com.codepath.apps.tweettrove.network.TwitterClient;

import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements TweetsAdapter.ProfileImageClickListener{

    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        String screenName = getIntent().getStringExtra("screenName");

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ProfileHeaderFragment fragmentProfileHeader = ProfileHeaderFragment.newInstance(screenName);
            ft.replace(R.id.flProfileHeader, fragmentProfileHeader);

            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            ft.replace(R.id.flContainer, fragmentUserTimeline);

            ft.commit();
        }
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

    private void startProfileActivity(String screenName)
    {
        Intent intent = new Intent(this, ProfileActivity.class );
        intent.putExtra("screenName", screenName);
        startActivity(intent);
    }

}
