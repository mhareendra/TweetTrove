package com.codepath.apps.tweettrove.fragments;


import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.models.Tweet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("NewApi")
public class ComposeTweetFragment extends DialogFragment {


    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    Tweet tweet;
    public static ComposeTweetFragment newInstance()
    {
        ComposeTweetFragment frag = new ComposeTweetFragment();
//        frag.tweet = tweet;
        return frag;
    }

    @BindView(R.id.etTweet)
    public EditText etTweet;

    @BindView(R.id.btnTweet)
    public Button btnTweet;

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
