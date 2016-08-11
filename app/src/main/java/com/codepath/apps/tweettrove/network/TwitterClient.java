package com.codepath.apps.tweettrove.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "pDcGjtmVAgJa41hrp2msMpZlK";       // Change this
	public static final String REST_CONSUMER_SECRET = "DR38q7NJsRcuOlpV6vmvrhPy8MhCFFRwm37yGuo04hr3gsw1vz"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://tweettrove"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("statuses/home_timeline.json");


        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);

        getClient().get(apiUrl, params, handler);
    }


	public void getMentionsTimeline(AsyncHttpResponseHandler handler)
	{
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25);

		getClient().get(apiUrl, params, handler);
	}

    public void getUserTimeline(AsyncHttpResponseHandler handler, String screenName)
    {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler)
    {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }


	///https://api.twitter.com/1.1/statuses/update.json?status=Maybe%20he%27ll%20finally%20find%20his%20keys.%20%23peterfalk

    public void postStatus(AsyncHttpResponseHandler handler, String statusText)
    {
        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("status", statusText);
        getClient().post(apiUrl, params, handler);
    }


	///https://api.twitter.com/1.1/statuses/update.json?status=Maybe%20he%27ll%20finally%20find%20his%20keys.%20%23peterfalk

	public void postStatusReply(AsyncHttpResponseHandler handler, String statusText, String id)
	{
		String apiUrl = getApiUrl("statuses/update.json");

		RequestParams params = new RequestParams();
		params.put("status", statusText);
		params.put("in_reply_to_status_id", id);
		getClient().post(apiUrl, params, handler);
	}


    public void postRetweet(AsyncHttpResponseHandler handler, String id)
    {
        String baseRetweetUrl = String.format("statuses/retweet/%s.json", id);
        String apiUrl = getApiUrl(baseRetweetUrl);
        getClient().post(apiUrl, handler);
    }


	public void postFavoriteCreate(AsyncHttpResponseHandler handler, String id)
	{
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params  = new RequestParams();
		params.put("id", id);
		getClient().post(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}