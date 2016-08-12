package com.codepath.apps.tweettrove.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.helpers.TwitterApplication;
import com.codepath.apps.tweettrove.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class MessagesActivity extends AppCompatActivity {

    @BindView(R.id.lvMessages)
    ListView lvMessages;

    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        client = TwitterApplication.getRestClient();

        getDirectMessages();
    }

    private void getDirectMessages()
    {


        client.getDirectMessages(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                Log.d("onsuccess getmessages:", response.toString());
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                Log.e("messages failure:", errorResponse.toString());
            }



        }, 0);
    }




}
