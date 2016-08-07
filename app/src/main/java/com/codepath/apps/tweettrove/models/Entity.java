package com.codepath.apps.tweettrove.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Hari on 8/6/2016.
 */
@Table(name = "Entity")
@Parcel(analyze={Entity.class})
public class Entity extends Model {


    public ArrayList<Media> getMedia() {
        return media;
    }

    @Column(name = "media", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public ArrayList<Media> media;
//
//    @Column(name = "user_mentions")
//    public ArrayList<String> userMentions;
//
//    @Column(name = "hashtags")
//    public ArrayList<String> hashtags;
//
//    @Column(name = "symbols")
//    public ArrayList<String> symbols;


    public static Entity fromJSON(JSONObject jsonObject)
    {

        Entity e = new Entity();
        try
        {
            e.media = Media.fromJSONArray(jsonObject.getJSONArray("media"));

        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }

        return e;
    }
}
