package com.codepath.apps.TwitterApp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by claireshu on 6/27/16.
 */
@Parcel
public class Tweet {

    public Tweet() {
    }

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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    // list out the attributes
    private String body;
    private long uid; // unique id for the tweet
    private User user; // store embedded user obj
    private String createdAt;
    private String mediaUrl;
    private boolean favorited;
    private boolean retweeted;

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
            JSONArray media = jsonObject.optJSONObject("entities").optJSONArray("media");
            if (media != null) {
                JSONObject mediaObj = (JSONObject) media.get(0);
                tweet.mediaUrl = mediaObj.optString("media_url");
            } else {
                tweet.mediaUrl = null;
            }
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return the tweet object
        return tweet;
    }

//    public static Tweet createTweet(String body, String createdAt, String user) {
//        Tweet tweet = new Tweet();
//
//        tweet.body = body;
//        tweet.createdAt = createdAt;
//        tweet.userName = user;
//
//        // return the tweet object
//        return tweet;
//
//    }

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
