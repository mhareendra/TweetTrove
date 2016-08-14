package com.codepath.apps.tweettrove.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.adapters.MessagesAdapter;
import com.codepath.apps.tweettrove.helpers.TwitterApplication;
import com.codepath.apps.tweettrove.models.Message;
import com.codepath.apps.tweettrove.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MessagesActivity extends AppCompatActivity {

    @BindView(R.id.lvMessages)
    ListView lvMessages;

    TwitterClient client;

    ArrayList<Message> messages;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient();

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this, messages );
        lvMessages.setAdapter(adapter);
        getDirectMessages();
    }

    private void getDirectMessages()
    {


        client.getDirectMessages(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                ArrayList<Message> messageList = Message.fromJSonArray(response);

                messages.addAll(messageList);
                Log.d("onsuccess getmessages:", response.toString());
                Log.d("onsuccess getmessages:", messages.toString());
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {


                Log.e("messages failure:", errorResponse.toString());
            }



        }, 0);
    }




}
