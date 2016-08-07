package com.codepath.apps.tweettrove.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.databinding.ActivityImageTweetDetailsBinding;
import com.codepath.apps.tweettrove.helpers.PatternEditableBuilder;
import com.codepath.apps.tweettrove.models.Media;
import com.codepath.apps.tweettrove.models.Tweet;
import com.codepath.apps.tweettrove.models.User;

import org.parceler.Parcels;

import java.util.regex.Pattern;

public class ImageTweetDetailsActivity extends AppCompatActivity {

    private ActivityImageTweetDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_tweet_details);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_tweet_details);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        displayTweetDetails(tweet);
        setPatternSpanListeners();
    }

    private void displayTweetDetails(Tweet tweet)
    {
        try
        {
            User user = tweet.getUser();
            if(user != null) {
                binding.tvDetailUsername.setText(user.getName());
                binding.tvDetailScreenName.setText(user.getScreenName());
                Glide.with(this)
                        .load(tweet.getUser().getProfileImageUrl())
                        .into(binding.ivDetailProfileImage);
            }
            binding.tvDetailTweetText.setText(tweet.getBody());
            String createdAt = tweet.getCreatedAt();
            String createdAtToDisplay = formatCreatedTime(createdAt);
            binding.tvDetailCreatedAt.setText(createdAtToDisplay);

            Media media = tweet.getEntities().getMedia().get(0);

            if(media == null)
                return;
            Glide.with(this)
                    .load(media.getMediaUrlHttps())
                    .into(binding.ivDetailTweetImage);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setPatternSpanListeners()
    {
        // Style clickable spans based on pattern
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getApplicationContext(), "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(binding.tvDetailTweetText);


        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getApplicationContext(), "Clicked username: " + text,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).into(binding.tvDetailTweetText);
    }

    /// "created_at": "Tue Aug 28 21:16:23 +0000 2012"
    private String formatCreatedTime(String createdAt)
    {
        try
        {
            String[] timeParts = createdAt.split(" ");
            String[] timeStampParts = timeParts[3].split(":"); ///21, 16, 23
            String timeSuffix = "AM";
            if(Integer.parseInt(timeStampParts[0]) > 12) {
                timeStampParts[0] = String.valueOf(Integer.parseInt(timeStampParts[0]) - 12) ;
                timeSuffix = "PM";
            }

            String timeToDisplay = String.format("%s:%s %s",timeStampParts[0], timeStampParts[1], timeSuffix); ///9:16 PM

            String dateToDisplay = String.format("%s %s %s", timeParts[2], timeParts[1], timeParts[5].substring(2, 4)); ///28 Aug 12

            return String.format("%s  %s", timeToDisplay, dateToDisplay); ///9:16 PM  28 Aug 12
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


}
