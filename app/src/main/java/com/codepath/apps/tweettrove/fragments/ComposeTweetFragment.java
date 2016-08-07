package com.codepath.apps.tweettrove.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.models.Tweet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("NewApi")
public class ComposeTweetFragment extends BottomSheetDialogFragment {


    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    Tweet tweet;
    boolean isReplyMode = false;
    public static ComposeTweetFragment newInstance(Tweet tweet, boolean isReplyMode)
    {
        ComposeTweetFragment frag = new ComposeTweetFragment();
        frag.tweet = tweet;
        frag.isReplyMode = isReplyMode;
        return frag;
    }

    @BindView(R.id.etTweet)
    public EditText etTweet;

    @BindView(R.id.btnTweet)
    public ImageButton btnTweet;

    @BindView(R.id.tvInReplyToName)
    public TextView tvInReplyToName;

    @BindView(R.id.tvInReply)
    public TextView tvInReply;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(this.isReplyMode)
        {
            String screenName = tweet.getUser().getScreenName();
            tvInReplyToName.setText(screenName);
            tvInReplyToName.setVisibility(View.VISIBLE);
            tvInReply.setVisibility(View.VISIBLE);
            etTweet.setText(screenName + " ");
        }
        else
        {
            tvInReplyToName.setVisibility(View.INVISIBLE);
            tvInReply.setVisibility(View.INVISIBLE);
        }
        etTweet.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @OnClick(R.id.btnTweet)
    public void finishComposeTweet()
    {

        ComposeTweetFragmentListener listener = (ComposeTweetFragmentListener) getActivity();
        String statusText = etTweet.getText().toString();
        listener.onFinishComposeTweetFragmentListener(statusText);
        dismiss();

    }


    public interface ComposeTweetFragmentListener
    {
        void onFinishComposeTweetFragmentListener(String statusText);
    }

}
