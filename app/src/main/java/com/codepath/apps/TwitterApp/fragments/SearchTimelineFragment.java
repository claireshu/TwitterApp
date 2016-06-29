package com.codepath.apps.TwitterApp.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.TwitterApp.TwitterApplication;
import com.codepath.apps.TwitterApp.TwitterClient;
import com.codepath.apps.TwitterApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by claireshu on 6/29/16.
 */
public class SearchTimelineFragment extends TweetsListFragment {
    TwitterClient client;
    String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(); // singleton client
        query = getArguments().getString("query");
        populateTimeline();
    }

    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static SearchTimelineFragment newInstance(String query) {
        SearchTimelineFragment searchFragment = new SearchTimelineFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    // send an API request to get the timeline json
    // fill the listview by creating the tweet objects from the json
    private void populateTimeline() {
        client.searchTweets(query, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try {
                    Log.d("SEARCH_ACTIVITY", json.getJSONArray("statuses").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // deserialize json
                // create models
                // load model data into listview
                try {
                    addAll(Tweet.fromJSONArray(json.getJSONArray("statuses")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Log.d("DEBUG", aTweets.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SEARCH_ACTIVITY", errorResponse.toString());
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}
