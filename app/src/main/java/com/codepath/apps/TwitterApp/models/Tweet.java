package com.codepath.apps.TwitterApp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by claireshu on 6/27/16.
 */
public class Tweet {
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    // list out the attributes
    private String body;
    private long uid; // unique id for the tweet
    private User user; // store embedded user obj
    private String createdAt;

    // Deserialize the JSON and build tweet objects
    // Tweet.fromJSON("(...)") -> Tweet
    public static Tweet fromJSON(JSONObject jsonObject) {

        Tweet tweet = new Tweet();
        // extract the values from the json, store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return the tweet object
        return tweet;
    }

    //Tweet.fromJSONArray([{...}, {..}]) -> List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
