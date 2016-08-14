package com.codepath.apps.tweettrove.models;

import com.activeandroid.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hari on 8/12/2016.
 */
public class Message extends Model {



    public long senderId;

    public String senderScreenName;

    public String text;

    public String profileImageUrlHttps;

    public static Message fromJsonObject (JSONObject jsonObject)
    {
        Message message = new Message();
        try
        {
            message.senderId = jsonObject.getLong("sender_id");
            message.senderScreenName = jsonObject.getString("sender_screen_name");
            message.text = jsonObject.getString("text");

            if(jsonObject.toString().contains("sender")) {
                message.profileImageUrlHttps = jsonObject.getJSONObject("sender")
                        .getString("profile_image_url_https");
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

}
