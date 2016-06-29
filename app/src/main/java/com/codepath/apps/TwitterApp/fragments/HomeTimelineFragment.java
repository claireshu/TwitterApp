package com.codepath.apps.TwitterApp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.TwitterApp.TwitterApplication;
import com.codepath.apps.TwitterApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by claireshu on 6/27/16.
 */
public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); // singleton client
        populateTimeline();
    }


    // send an API request to get the timeline json
    // fill the listview by creating the tweet objects from the json
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("TIMELINE_ACTIVITY", json.toString());
                // deserialize json
                // create models
                // load model data into listview
                addAll(Tweet.fromJSONArray(json));
                // Log.d("DEBUG", aTweets.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TIMELINE_ACTIVITY", errorResponse.toString());
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}
