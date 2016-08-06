package com.codepath.apps.tweettrove.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hari on 8/4/2016.
 */
public class Tweet {


    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    private String body;
    private long uid;
    private String createdAt;

    public String getRelativeTimeStamp() {
        return relativeTimeStamp;
    }

    private String relativeTimeStamp;

    public User getUser() {
        return user;
    }

    private User user;

    public static Tweet fromJSON(JSONObject jsonObject)
    {
        Tweet tweet = new Tweet();
        try
        {
            tweet.body = jsonObject.getString("text");

            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt  = jsonObject.getString("created_at");
            tweet.relativeTimeStamp = tweet.getRelativeTimeAgo(tweet.createdAt);
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray)
    {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            try {
                JSONObject tweetObject = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetObject);
                if(tweet != null)
                {
                    tweets.add(tweet);
                }
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String relativeDateNum = relativeDate.split(" ")[0];
        char relativeDateChar = relativeDate.split(" ")[1].charAt(0);
        String relativeDateToReturn = String.format("%s%s", relativeDateNum, relativeDateChar);
        return relativeDateToReturn;
    }
}
