package com.codepath.apps.TwitterApp.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by claireshu on 6/27/16.
 */

@Parcel
public class User {

    public User() {

    }

    // list attributes
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return followingsCount;
    }

    public String getDate() {
        return date;
    }

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int followersCount;
    private int followingsCount;
    private String date;

    //deserialize the user json -> user
    public static User fromJSON(JSONObject json) {
        // extract and fill the values
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.followersCount = json.getInt("followers_count");
            u.followingsCount = json.getInt("friends_count");
            u.date = json.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return the user
        return u;
    }
}
