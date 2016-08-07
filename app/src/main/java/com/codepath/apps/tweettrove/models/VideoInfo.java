package com.codepath.apps.tweettrove.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Hari on 8/6/2016.
 */

@Table(name = "video_info")
@Parcel(analyze = VideoInfo.class)
public class VideoInfo extends Model {


    public ArrayList<Variant> getVariants() {
        return variants;
    }

    @Column(name="variants", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public ArrayList<Variant> variants;

    public static VideoInfo fromJson(JSONObject jsonObject)
    {
        VideoInfo videoInfo = new VideoInfo();
        try
        {
            videoInfo.variants = Variant.fromJSONArray(jsonObject.getJSONArray("variants"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return videoInfo;
    }



}
