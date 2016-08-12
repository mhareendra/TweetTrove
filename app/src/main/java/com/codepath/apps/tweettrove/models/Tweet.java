package com.codepath.apps.tweettrove.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hari on 8/4/2016.
 */

@Table(name ="Tweets")
@Parcel(analyze={Tweet.class, ExtendedEntity.class, User.class, Entity.class})
public class Tweet extends Model {


    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Column(name = "body")
    public String body;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long uid;
    @Column(name = "created_at")
    public String createdAt;

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    @Column(name = "favorited")
    public boolean favorited;

    @Column(name ="retweeted")
    public boolean retweeted;

    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public User user;

    public Entity getEntities() {
        return entities;
    }

    @Column(name = "Entity", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Entity entities = null;

    public ExtendedEntity getExtendedEntities() {
        return extendedEntities;
    }

    @Column(name = "ExtendedEntity", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public ExtendedEntity extendedEntities = null;

    public String relativeTimeStamp;

    public String getRelativeTimeStamp() {
        relativeTimeStamp = this.getRelativeTimeAgo(this.createdAt);
        return relativeTimeStamp;
    }

    public Tweet()
    {

    }

    public User getUser() {
        return user;
    }


    public static Tweet fromJSON(JSONObject jsonObject)
    {
        Tweet tweet = new Tweet();
        try
        {
            tweet.body = jsonObject.getString("text");

            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt  = jsonObject.getString("created_at");

            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            //tweet.relativeTimeStamp = tweet.getRelativeTimeAgo(tweet.createdAt);
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

            tweet.entities = Entity.fromJSON(jsonObject.getJSONObject("entities"));
            if(jsonObject.toString().contains("extended_entities")) {
                JSONObject extendedEntitiesObject = jsonObject.getJSONObject("extended_entities");
                if (extendedEntitiesObject != null)
                    tweet.extendedEntities = ExtendedEntity.fromJSON(extendedEntitiesObject);
            }
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        return tweet;
    }

    public static List<Tweet> getAll()
    {
        List<Tweet> allItems = new Select().from(Tweet.class).execute();
//        Collections.reverse(allItems);
        return(allItems);
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
