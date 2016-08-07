package com.codepath.apps.tweettrove.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Hari on 8/6/2016.
 */
@Table(name="ExtendedEntity")
@Parcel(analyze={ExtendedEntity.class})
public class ExtendedEntity extends Model {


    public ArrayList<Media> getMedia() {
        return media;
    }

    @Column(name = "media", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public ArrayList<Media> media;


    public static ExtendedEntity fromJSON(JSONObject jsonObject)
    {
        ExtendedEntity extendedEntity = new ExtendedEntity();

        try {
            JSONArray mediaArray = jsonObject.getJSONArray("media");

            extendedEntity.media = Media.fromJSONArray(mediaArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return extendedEntity;
    }


}
