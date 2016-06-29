package com.codepath.apps.TwitterApp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.TwitterApp.R;
import com.codepath.apps.TwitterApp.TwitterClient;
import com.codepath.apps.TwitterApp.models.Tweet;
import com.codepath.apps.TwitterApp.models.TweetsArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by claireshu on 6/27/16.
 */
public class TweetsListFragment extends Fragment {
    private SwipeRefreshLayout swipeContainer;

    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter aTweets;
    protected ListView lvTweets;
    protected TwitterClient client;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Remember to CLEAR OUT old items before appending in the new ones
                aTweets.clear();
                // ...the data has come back, add new items to your adapter...
                aTweets.addAll(Tweet.fromJSONArray(response));
                aTweets.notifyDataSetChanged();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void addTweetStart(Tweet tweet) {
        tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();
        // add to beginning
        // notify adapter
    }
}
