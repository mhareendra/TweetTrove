package com.codepath.apps.tweettrove.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.helpers.PatternEditableBuilder;
import com.codepath.apps.tweettrove.models.Tweet;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hari on 8/4/2016.
 */
public class TweetsAdapter extends
        RecyclerView.Adapter<TweetsAdapter.ViewHolder>
{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View tweetView = LayoutInflater.from(context)
                .inflate(R.layout.item_tweet, parent, false);

        ViewHolder holder = new ViewHolder(tweetView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Tweet tweet = mTweets.get(position);

        holder.tvUserName.setText(tweet.getUser().getName());
        holder.tvScreenName.setText(tweet.getUser().getScreenName());
        holder.tvBody.setText(tweet.getBody());

        // Style clickable spans based on pattern
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getContext(), "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(holder.tvBody);


        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getContext(), "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(holder.tvBody);


        holder.tvTimestamp.setText(tweet.getRelativeTimeStamp());

        Glide.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
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
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    private List<Tweet> mTweets;
    private Context mContext;

    public TweetsAdapter(Context context,List<Tweet> tweets)
    {
        mTweets = tweets;
        mContext = context;
    }

    private Context getContext()
    {
        return mContext;
    }


    public void clear()
    {
        mTweets.clear();
        notifyDataSetChanged();
    }


    public void addAll (List<Tweet> list)
    {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }


}
