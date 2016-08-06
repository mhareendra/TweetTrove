package com.codepath.apps.tweettrove.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hari on 8/4/2016.
 */
public class User {



    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return String.format("@%s", screenName);
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJSON(JSONObject jsonObject)
    {
        User u = new User();

        try
        {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }

        return u;
    }
}
