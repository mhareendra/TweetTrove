package com.codepath.apps.tweettrove.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.helpers.PatternEditableBuilder;
import com.codepath.apps.tweettrove.models.ExtendedEntity;
import com.codepath.apps.tweettrove.models.Media;
import com.codepath.apps.tweettrove.models.Tweet;
import com.codepath.apps.tweettrove.models.Variant;
import com.codepath.apps.tweettrove.models.VideoInfo;
import com.malmstein.fenster.controller.MediaFensterPlayerController;
import com.malmstein.fenster.view.FensterVideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


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
                        .bitmapTransform(new RoundedCornersTransformation(getContext(), 5, 5))
                        .placeholder(R.drawable.twitter_placeholder)
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

                //Set profile image click listener
                try {
                    holder.listener = (ProfileImageClickListener) getContext();
                    holder.ivProfileImage.setTag(tweet.getUser().getScreenName());
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
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void configureTweetVideoViewHolder(final ViewHolderTweetVideo holder, int position)
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
                        .bitmapTransform(new RoundedCornersTransformation(getContext(), 5, 5))
                        .into(holder.ivProfileImage);



                //Set profile image click listener
                try {
                    holder.listener = (ProfileImageClickListener) getContext();
                    holder.ivProfileImage.setTag(tweet.getUser().getScreenName());
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

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

////http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4
//                holder.textureView.setVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", MediaFensterPlayerController.DEFAULT_VIDEO_START);
////                holder.textureView.start();
//
//                holder.textureView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        holder.textureView.start();
//                    }
//                });
//
//                holder.playerController.setVisibilityListener(new FensterPlayerControllerVisibilityListener() {
//                    @Override
//                    public void onControlsVisibilityChange(boolean value) {
//                        //setSystemUiVisibility(value, holder.playerController);
//                    }
//                });;

                holder.vvTweetVideo.setVideoURI(Uri.parse(mediaUrl));
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(holder.vvTweetVideo);
                holder.vvTweetVideo.setMediaController(mediaController);
                holder.vvTweetVideo.requestFocus();
                holder.vvTweetVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        holder.vvTweetVideo.start();
                    }
                });

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
                        .bitmapTransform(new RoundedCornersTransformation(getContext(), 10, 5))
                        .into(holder.ivProfileImage);


                //Set profile image click listener
                try {
                    holder.listener = (ProfileImageClickListener) getContext();
                    holder.ivProfileImage.setTag(tweet.getUser().getScreenName());
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
            ArrayList<Media>  media = tweet.getEntities().getMedia();

            if (media != null) {
                if (media.get(0).getType().equals("photo")) {

                    if(media.get(0).getMediaUrlHttps() != null && !media.get(0).getMediaUrlHttps().isEmpty())
                        return TWEET_IMAGE;
                }
                else
                    return TWEET;
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

    public interface ProfileImageClickListener
    {
        void onProfileImageClick(String screenName);
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

        public ProfileImageClickListener listener;

        @OnClick(R.id.ivProfileImage)
        public void profileImageClick()
        {
            listener.onProfileImageClick(ivProfileImage.getTag().toString());
        }

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

        public ProfileImageClickListener listener;

        @OnClick(R.id.ivProfileImage)
        public void profileImageClick()
        {
            listener.onProfileImageClick(ivProfileImage.getTag().toString());
        }

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

        //@BindView(R.id.vVTweetVideo)
        //public ScalableVideoView vVTweetVideo;

        @BindView(R.id.iBDetailFavorite)
        public ImageButton ibFavorite;

        @BindView(R.id.iBDetailRetweet)
        public ImageButton ibRetweet;

//        @BindView(R.id.play_video_texture)
        public FensterVideoView textureView;

//        @BindView(R.id.play_video_controller)
        public MediaFensterPlayerController playerController;

        @BindView(R.id.vVTweetVideo)
        public VideoView vvTweetVideo;

        public ProfileImageClickListener listener;

        @OnClick(R.id.ivProfileImage)
        public void profileImageClick()
        {
            listener.onProfileImageClick(ivProfileImage.getTag().toString());
        }

        public ViewHolderTweetVideo(View view)
        {
            super(view);
            ButterKnife.bind(this, view);

//            textureView = (FensterVideoView) view.findViewById(R.id.play_video_texture);
//            playerController = (MediaFensterPlayerController) view.findViewById(R.id.play_video_controller);
//            textureView.setMediaController(playerController);
//
//            textureView.setVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", MediaFensterPlayerController.DEFAULT_VIDEO_START);
////                holder.textureView.start();




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
