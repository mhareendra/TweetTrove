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

@Table(name="Variant")
@Parcel(analyze={Variant.class})
public class Variant extends Model {

    public String getUrl() {
        return url;
    }

    @Column(name = "url")
    public String url;

    public static Variant fromJSON(JSONObject jsonObject)
    {
        Variant variant = new Variant();
        try {

            if(jsonObject.getString("url")!=null && jsonObject.getString("url").contains("mp4"))
                variant.url = jsonObject.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return variant;
    }

    public static ArrayList<Variant> fromJSONArray(JSONArray jsonArray)
    {
        ArrayList<Variant> variants = new ArrayList<>();

        try
        {

            for (int i =0; i< jsonArray.length(); i++)
            {
                variants.add(Variant.fromJSON(jsonArray.getJSONObject(i)));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return variants;
    }

}
