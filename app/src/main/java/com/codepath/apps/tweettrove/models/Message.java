package com.codepath.apps.tweettrove.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hari on 8/12/2016.
 */
public class Message extends Model {



    public long senderId;

    public String senderScreenName;

    public String text;

    public long getSenderId() {
        return senderId;
    }

    public String getSenderScreenName() {
        return senderScreenName;
    }

    public String getText() {
        return text;
    }

    public String getProfileImageUrlHttps() {
        return profileImageUrlHttps;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }

    public String profileImageUrlHttps;

    public String createdAt;

    public String formattedCreatedAt;

    public static Message fromJsonObject (JSONObject jsonObject)
    {
        Message message = new Message();
        try
        {
            message.senderId = jsonObject.getLong("sender_id");
            message.senderScreenName = jsonObject.getString("sender_screen_name");
            message.text = jsonObject.getString("text");
            message.createdAt = jsonObject.getString("created_at");
            if(jsonObject.toString().contains("sender")) {
                JSONObject senderJsonObject = jsonObject.getJSONObject("sender");
                message.profileImageUrlHttps = senderJsonObject.getString("profile_image_url_https");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return message;
    }

    public static ArrayList<Message> fromJSonArray(JSONArray jsonArray)
    {
        ArrayList<Message> messages = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Message message = Message.fromJsonObject(jsonObject);
                if (message != null) {
                    messages.add(message);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                continue;
            }
        }
        return messages;
    }


    public String relativeTimeStamp;

    public String getRelativeTimeStamp() {
        relativeTimeStamp = this.getRelativeTimeAgo(this.createdAt);
        return relativeTimeStamp;
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
        String relativeDateToReturn = String.format("%s %s", relativeDateNum, relativeDateChar);
        return relativeDateToReturn;
    }

}
