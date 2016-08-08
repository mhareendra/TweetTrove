package com.codepath.apps.tweettrove.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.helpers.PatternEditableBuilder;
import com.codepath.apps.tweettrove.models.ExtendedEntity;
import com.codepath.apps.tweettrove.models.Media;
import com.codepath.apps.tweettrove.models.Tweet;
import com.codepath.apps.tweettrove.models.Variant;
import com.codepath.apps.tweettrove.models.VideoInfo;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Hari on 8/4/2016.
 */
public class TweetsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View tweetView;
        RecyclerView.ViewHolder holder;

        switch (viewType)
        {
            case TWEET:
                default:
                    tweetView = LayoutInflater.from(context)
                        .inflate(R.layout.item_tweet, parent, false);

                    holder = new ViewHolderTweet(tweetView);
                break;
            case TWEET_IMAGE:
                tweetView = LayoutInflater.from(context)
                        .inflate(R.layout.item_tweet_image, parent, false);

                holder = new ViewHolderTweetImage(tweetView);
                break;
            case TWEET_VIDEO:
                tweetView = LayoutInflater.from(context)
                        .inflate(R.layout.item_tweet_video, parent, false);

                holder = new ViewHolderTweetVideo(tweetView);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType())
        {
            case TWEET_IMAGE:

                ViewHolderTweetImage vh1 = (ViewHolderTweetImage)holder;
                configureTweetImageViewHolder(vh1, position);

                break;
            case TWEET_VIDEO:
                ViewHolderTweetVideo vh2 = (ViewHolderTweetVideo)holder;
                configureTweetVideoViewHolder(vh2, position);

                break;
            case TWEET:
            default:
                ViewHolderTweet vh3 = (ViewHolderTweet)holder;
                configureTweetViewHolder(vh3, position);
                break;
        }


    }

    private void configureTweetImageViewHolder(ViewHolderTweetImage holder, int position)
    {
        try
        {
            Tweet tweet = mTweets.get(position);

            if(tweet == null)
                return;

            try {
                holder.tvUserName.setText(tweet.getUser().getName());
                holder.tvScreenName.setText(tweet.getUser().getScreenName());
                holder.tvBody.setText(tweet.getBody());

                if(tweet.isFavorited())
                {
                    holder.ibFavorite.setImageResource(R.drawable.favorite_pressed);
                }
                else
                {
                    holder.ibFavorite.setImageResource(R.drawable.favorite);
                }

                if(tweet.isRetweeted())
                {
                    holder.ibRetweet.setImageResource(R.drawable.retweet_pressed);
                }
                else
                {
                    holder.ibRetweet.setImageResource(R.drawable.retweet);
                }

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

                ArrayList<Media> media = tweet.getEntities().getMedia();

                if(media==null || media.isEmpty())
                    return;
                String mediaUrl = media.get(0).getMediaUrlHttps();

                if(mediaUrl == null || mediaUrl.isEmpty())
                    return;

                Glide.with(getContext())
                        .load(mediaUrl)
                        .into(holder.ivTweetImage);

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void configureTweetVideoViewHolder(ViewHolderTweetVideo holder, int position)
    {
        try
        {
            Tweet tweet = mTweets.get(position);

            if(tweet == null)
                return;

            try {
                holder.tvUserName.setText(tweet.getUser().getName());
                holder.tvScreenName.setText(tweet.getUser().getScreenName());
                holder.tvBody.setText(tweet.getBody());

                if(tweet.isFavorited())
                {
                    holder.ibFavorite.setImageResource(R.drawable.favorite_pressed);
                }
                else
                {
                    holder.ibFavorite.setImageResource(R.drawable.favorite);
                }

                if(tweet.isRetweeted())
                {
                    holder.ibRetweet.setImageResource(R.drawable.retweet_pressed);
                }
                else
                {
                    holder.ibRetweet.setImageResource(R.drawable.retweet);
                }

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


                if(tweet.getExtendedEntities().getMedia() == null)
                    return;
                VideoInfo videoInfo = tweet.getExtendedEntities().getMedia().get(0).getVideoInfo();

                if(videoInfo==null)
                    return;
                if(videoInfo.getVariants() == null)
                    return;

                String mediaUrl = "";

                for(int i =0; i < videoInfo.getVariants().size(); i++)
                {
                    String url = videoInfo.getVariants().get(0).getUrl();
                    if(url!=null && !url.isEmpty())
                    {
                        if(url.contains("mp4")) {
                            mediaUrl = url;
                            break;
                        }
                    }
                }

                if(mediaUrl == null || mediaUrl.isEmpty())
                    return;


                holder.vVTweetVideo.setDataSource(getContext(), Uri.parse(mediaUrl));
                holder.vVTweetVideo.start();

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void configureTweetViewHolder(ViewHolderTweet holder, int position)
    {
        try
        {
            Tweet tweet = mTweets.get(position);

            if(tweet == null)
                return;

            try {
                holder.tvUserName.setText(tweet.getUser().getName());
                holder.tvScreenName.setText(tweet.getUser().getScreenName());
                holder.tvBody.setText(tweet.getBody());

                if(tweet.isFavorited())
                {
                    holder.ibFavorite.setImageResource(R.drawable.favorite_pressed);
                }
                else
                {
                    holder.ibFavorite.setImageResource(R.drawable.favorite);
                }

                if(tweet.isRetweeted())
                {
                    holder.ibRetweet.setImageResource(R.drawable.retweet_pressed);
                }
                else
                {
                    holder.ibRetweet.setImageResource(R.drawable.retweet);
                }

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
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private final int TWEET = 0, TWEET_IMAGE = 1, TWEET_VIDEO = 2;

    @Override
    public int getItemViewType(int position) {

        try {
            Tweet tweet = mTweets.get(position);

            ArrayList<Media>  media = tweet.getEntities().getMedia();
            if (media != null) {
                if (media.get(0).getType().equals("photo")) {

                    if(media.get(0).getMediaUrlHttps() != null && !media.get(0).getMediaUrlHttps().isEmpty())
                        return TWEET_IMAGE;
                }
                else
                    return TWEET;
            }

            ExtendedEntity extendedEntity = tweet.getExtendedEntities();
            if(extendedEntity != null)
            {
                if(extendedEntity.getMedia().get(0).getType().equals("video")) {
                    VideoInfo videoInfo = extendedEntity.getMedia().get(0).getVideoInfo();

                    if(videoInfo==null)
                        return TWEET;
                    ArrayList<Variant> variants = videoInfo.getVariants();
                    if (variants != null) {
                        if(variants.get(0).getUrl()!=null && !variants.get(0).getUrl().isEmpty())
                        {
                            return TWEET_VIDEO;
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return TWEET;
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public static class ViewHolderTweet extends RecyclerView.ViewHolder
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

        @BindView(R.id.iBDetailFavorite)
        public ImageButton ibFavorite;

        @BindView(R.id.iBDetailRetweet)
        public ImageButton ibRetweet;


        public ViewHolderTweet(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ViewHolderTweetImage extends RecyclerView.ViewHolder
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

        @BindView(R.id.ivTweetImage)
        public ImageView ivTweetImage;

        @BindView(R.id.iBDetailFavorite)
        public ImageButton ibFavorite;

        @BindView(R.id.iBDetailRetweet)
        public ImageButton ibRetweet;



        public ViewHolderTweetImage(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ViewHolderTweetVideo extends RecyclerView.ViewHolder
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

        @BindView(R.id.vVTweetVideo)
        public ScalableVideoView vVTweetVideo;

        @BindView(R.id.iBDetailFavorite)
        public ImageButton ibFavorite;

        @BindView(R.id.iBDetailRetweet)
        public ImageButton ibRetweet;



        public ViewHolderTweetVideo(View view)
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
