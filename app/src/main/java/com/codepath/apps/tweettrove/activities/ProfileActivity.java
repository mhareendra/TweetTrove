package com.codepath.apps.tweettrove.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.fragments.UserTimelineFragment;
import com.codepath.apps.tweettrove.helpers.TwitterApplication;
import com.codepath.apps.tweettrove.models.User;
import com.codepath.apps.tweettrove.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setUserInfo();
        String screenName = getIntent().getStringExtra("screenName");

        if(savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void setUserInfo()
    {
        client = TwitterApplication.getRestClient();


        client.getUserInfo(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                user = User.fromJSON(response);

                if(user != null) {
                    if(getSupportActionBar()!=null) {
                        getSupportActionBar().setTitle(user.getScreenName());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ProfileActivity.this, "GetUserInfo error!", Toast.LENGTH_SHORT).show();

                Log.e("UserInfo error", responseString);
            }
        }
        );
    }
}
