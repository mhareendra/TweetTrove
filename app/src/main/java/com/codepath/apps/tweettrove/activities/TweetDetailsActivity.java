package com.codepath.apps.tweettrove.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.tweettrove.fragments.ComposeTweetFragment;
import com.codepath.apps.tweettrove.helpers.PatternEditableBuilder;
import com.codepath.apps.tweettrove.helpers.TwitterApplication;
import com.codepath.apps.tweettrove.models.Media;
import com.codepath.apps.tweettrove.models.Tweet;
import com.codepath.apps.tweettrove.models.User;
import com.codepath.apps.tweettrove.network.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TweetDetailsActivity extends AppCompatActivity
        implements ComposeTweetFragment.ComposeTweetFragmentListener
{

    private ActivityTweetDetailsBinding binding;
    private TwitterClient client;
    private Tweet tweet;


    private boolean isRetweeted =false;
    private boolean isFavorited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_details);

        displayTweetDetails(tweet);
        setPatternSpanListeners();
        setClickListeners();
        client = TwitterApplication.getRestClient();


        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.twitter_launcher);
        }

    }

    private void setClickListeners()
    {

        binding.iBDetailReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkIsOnline())
                    return;
                binding.iBDetailReply.setImageResource(R.drawable.reply_pressed);
                FragmentManager fm = getSupportFragmentManager();
                ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance(null, tweet, true);
                composeTweetFragment.show(fm,"compose_tweet_fragment");
            }
        });

        binding.iBDetailFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.iBDetailFavorite.setImageResource(R.drawable.favorite_pressed);

                if(!checkIsOnline()) {

                     binding.iBDetailFavorite.setImageResource(R.drawable.favorite);

                    return;
                }
                String id = String.valueOf(tweet.getUid());
                client.postFavoriteCreate(new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        isFavorited = true;
                        Log.d("onFavorite Success:", String.valueOf(statusCode));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("OnFavorite failure", String.valueOf(statusCode));

                        binding.iBDetailFavorite.setImageResource(R.drawable.favorite);

                    }
                }, id);
            }
        });

        binding.iBDetailRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkIsOnline())
                    return;
                binding.iBDetailRetweet.setImageResource(R.drawable.retweet_pressed);
                String id = String.valueOf(tweet.getUid());

                client.postRetweet(new JsonHttpResponseHandler() {
                           @Override
                           public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                isRetweeted = true;
                               Log.d("onRetweet Success:", response.toString());
                           }
                }, id);
            }
        });
    }

    @Override
    public void onFinishComposeTweetFragmentListener(String statusText) {

        if (statusText.isEmpty())
            return;

        client.postStatusReply(new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                      Log.d("onSuccess Compose:", response.toString());
                  }

              }, statusText, String.valueOf(tweet.getUid())

        );

    }

    private boolean checkIsOnline()
    {
        if(!isOnline()) {
            Toast.makeText(this, "Please connect to the Internet", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
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

    private void displayTweetDetails(Tweet tweet)
    {
        try
        {
            User user = tweet.getUser();
            if(user != null) {
                binding.tvDetailUsername.setText(user.getName());
                binding.tvDetailScreenName.setText(user.getScreenName());
//                Glide.with(this)
//                        .load(tweet.getUser().getProfileImageUrl())
//                        .bitmapTransform(new RoundedCornersTransformation(this, 5, 5))
//                        .into(binding.ivDetailProfileImage);

                Picasso.with(this)
                        .load(tweet.getUser().getProfileImageUrl())
                        .transform(new RoundedCornersTransformation(5,5))
                        .into(binding.ivDetailProfileImage);

            }
            binding.tvDetailTweetText.setText(tweet.getBody());
            String createdAt = tweet.getCreatedAt();
            String createdAtToDisplay = formatCreatedTime(createdAt);
            binding.tvDetailCreatedAt.setText(createdAtToDisplay);


            if(tweet.isFavorited())
            {
                binding.iBDetailFavorite.setImageResource(R.drawable.favorite_pressed);
            }
            else
            {
                binding.iBDetailFavorite.setImageResource(R.drawable.favorite);
            }

            if(tweet.isRetweeted())
            {
                binding.iBDetailRetweet.setImageResource(R.drawable.retweet_pressed);
            }
            else
            {
                binding.iBDetailRetweet.setImageResource(R.drawable.retweet);
            }

            Media media = tweet.getEntities().getMedia().get(0);

            if(media == null)
                return;
//            Glide.with(this)
//                    .load(media.getMediaUrlHttps())
//                    .into(binding.ivDetailTweetImage);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setPatternSpanListeners()
    {
        // Style clickable spans based on pattern
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getApplicationContext(), "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(binding.tvDetailTweetText);


        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getApplicationContext(), "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(binding.tvDetailTweetText);
    }

    /// "created_at": "Tue Aug 28 21:16:23 +0000 2012"
    private String formatCreatedTime(String createdAt)
    {
        try
        {
            String[] timeParts = createdAt.split(" ");
            String[] timeStampParts = timeParts[3].split(":"); ///21, 16, 23
            String timeSuffix = "AM";
            if(Integer.parseInt(timeStampParts[0]) > 12) {
                timeStampParts[0] = String.valueOf(Integer.parseInt(timeStampParts[0]) - 12) ;
                timeSuffix = "PM";
            }

            String timeToDisplay = String.format("%s:%s %s",timeStampParts[0], timeStampParts[1], timeSuffix); ///9:16 PM

            String dateToDisplay = String.format("%s %s %s", timeParts[2], timeParts[1], timeParts[5].substring(2, 4)); ///28 Aug 12

            return String.format("%s  %s", timeToDisplay, dateToDisplay); ///9:16 PM  28 Aug 12
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed()
    {

        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("isFavorited", isFavorited);
        data.putExtra("isRetweeted", isRetweeted);
        // Activity finished ok, return the data
        setResult(0, data); // set result code and bundle data for response
        finish(); // cl
    }





}
