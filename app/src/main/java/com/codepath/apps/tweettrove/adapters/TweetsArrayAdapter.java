package com.codepath.apps.tweettrove.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.models.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hari on 8/4/2016.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets)
    {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }


    public static class ViewHolder
    {
        @BindView(R.id.ivProfileImage)
        public ImageView ivProfileImage;

        @BindView(R.id.tvBody)
        public TextView tvBody;

        @BindView(R.id.tvUserName)
        public TextView tvUserName;

        @BindView(R.id.tvScreenName)
        public TextView tvScreenName;

        @BindView(R.id.tvTimestamp)
        public TextView tvTimestamp;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);
        ViewHolder holder;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvScreenName.setText(tweet.getUser().getScreenName());
        holder.tvBody.setText(tweet.getBody());
        holder.tvTimestamp.setText(tweet.getRelativeTimeStamp());

        Glide.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.ivProfileImage);

        return convertView;
    }
}
