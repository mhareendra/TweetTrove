package com.codepath.apps.tweettrove.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Hari on 8/4/2016.
 */

@Table(name = "User")
@Parcel(analyze={User.class})
public class User extends Model {

    @Column(name = "name")
    public String name;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long uid;
    @Column(name = "screen_name")
    public String screenName;
    @Column(name = "profile_image_url")
    public String profileImageUrl;

    public String backgroundImageUrl;

    public String tagLine;

    public int followersCount;

    public int followingsCount;

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

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public String getTagLine() {
        return tagLine;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
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

            u.backgroundImageUrl=jsonObject.getString("profile_background_image_url");
            u.tagLine = jsonObject.getString("description");
            u.followersCount = jsonObject.getInt("followers_count");
            u.followingsCount = jsonObject.getInt("friends_count");
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        return u;
    }
}
