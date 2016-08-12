package com.codepath.apps.tweettrove.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.helpers.TwitterApplication;
import com.codepath.apps.tweettrove.models.User;
import com.codepath.apps.tweettrove.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Hari on 8/11/2016.
 */
public class ProfileHeaderFragment extends Fragment
{

    TwitterClient client;
    User user;

    @BindView(R.id.ivBackgroundImage)
    ImageView ivBackgroundImage;

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @BindView(R.id.tvUserName)
    TextView tvUsername;

    @BindView(R.id.tvScreenName)
    TextView tvScreenName;

    @BindView(R.id.tvFollowersCount)
    TextView tvFollwersCount;

    @BindView(R.id.tvFollowingCount)
    TextView tvFollowingCount;

    @BindView(R.id.tvDescription)
    TextView tvTagline;


    public static ProfileHeaderFragment newInstance(String screenName) {
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        ProfileHeaderFragment fragment = new ProfileHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public String screenName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screenName = getArguments().getString("screenName");
        client = TwitterApplication.getRestClient();

        getUserLookupInfo();
    }


    private void getUserLookupInfo()
    {
        client = TwitterApplication.getRestClient();

        client.getUserLookup(new JsonHttpResponseHandler()
                           {
                               @Override
                               public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                   try {
                                       user = User.fromJSON(response.getJSONObject(0));
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                                   if(user != null) {
                                       populateUserHeader();
                                   }
                               }

                               @Override
                               public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                   Toast.makeText(getContext(), "GetUserInfo error!", Toast.LENGTH_SHORT).show();
                                   Log.e("UserInfo error", responseString);
                               }

                               @Override
                               public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject object)
                               {
                                   Log.e("UserInfo error 2:", object.toString());

                               }
                           }, screenName
        );
    }

    private void populateUserHeader()
    {
        try
        {
            tvUsername.setText(user.getName());
            tvScreenName.setText(user.getScreenName());

            tvFollowingCount.setText(user.getFollowingsCount() + " FOLLOWING");
            tvFollwersCount.setText(user.getFollowersCount() + " FOLLOWERS");


            tvTagline.setText(user.getTagLine());

            String backgroundImageUrl = user.getBackgroundImageUrl();
            String profileImageUrl = user.getProfileImageUrl();

            if(backgroundImageUrl!= null && !backgroundImageUrl.isEmpty()) {
//                Glide.with(this)
//                        .load(backgroundImageUrl)
//                        .bitmapTransform(new RoundedCornersTransformation(getContext(), 3, 3))
//                        .placeholder(R.drawable.twitter_placeholder)
//                        .into(ivBackgroundImage);

                Picasso.with(getActivity())
                        .load(backgroundImageUrl)
                        .transform(new RoundedCornersTransformation(5,5))
                        .placeholder(R.drawable.twitter_placeholder)
                        .into(ivBackgroundImage);


            }

            if(profileImageUrl != null && !profileImageUrl.isEmpty())
            {
//                Glide.with(this)
//                        .load(profileImageUrl)
//                        .bitmapTransform(new RoundedCornersTransformation(getContext(), 5, 5))
//                        .placeholder(R.drawable.twitter_placeholder)
//                        .into(ivProfileImage);

                Picasso.with(getActivity())
                        .load(profileImageUrl)
                        .transform(new RoundedCornersTransformation(5,5))
                        .placeholder(R.drawable.twitter_placeholder)
                        .into(ivProfileImage);

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}
