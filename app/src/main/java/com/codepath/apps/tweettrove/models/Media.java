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
@Table(name = "media")
@Parcel(analyze={Media.class})
public class Media extends Model {


    public String getIdStr() {
        return idStr;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public String getUrl() {
        return Url;
    }

    public String getType() {
        return type;
    }

    @Column(name = "id_str")
    public String idStr;

    @Column(name = "media_url")
    public String mediaUrl;

    @Column(name = "medi_url_https")
    public String mediaUrlHttps;

    @Column(name = "expanded_url")
    public String expandedUrl;

    @Column(name = "display_url")
    public String displayUrl;

    @Column(name = "url")
    public String Url;

    @Column(name = "type")
    public String type;


    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    @Column(name = "video_info")
    public VideoInfo videoInfo;


    public static ArrayList<Media> fromJSONArray(JSONArray jsonArray)
    {
        ArrayList<Media> mediaList = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            try {
                JSONObject mediaObject = jsonArray.getJSONObject(i);
                Media media = Media.fromJSON(mediaObject);
                if(media != null)
                {
                    mediaList.add(media);
                }
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
                continue;
            }
        }

        return mediaList;
    }

    public static Media fromJSON(JSONObject jsonObject)
    {
        Media media = new Media();
        try
        {
            media.idStr = jsonObject.getString("id_str");
            media.displayUrl = jsonObject.getString("display_url");
            media.expandedUrl = jsonObject.getString("expanded_url");
            media.mediaUrl = jsonObject.getString("media_url");
            media.mediaUrlHttps = jsonObject.getString("media_url_https");
            media.type = jsonObject.getString("type");
            media.videoInfo = VideoInfo.fromJson(jsonObject.getJSONObject("video_info"));

        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        return media;
    }

}
