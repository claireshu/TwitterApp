package com.codepath.apps.TwitterApp.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.TwitterApp.ProfileActivity;
import com.codepath.apps.TwitterApp.R;
import com.codepath.apps.TwitterApp.TwitterApplication;
import com.codepath.apps.TwitterApp.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by claireshu on 6/27/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private TwitterClient client;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    //Override and setup custom template


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the tweet
        final Tweet tweet = getItem(position);
        // find or inflate the template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        // find the subviews to fill with data in the template
        final ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
        ImageView ivTweetPhoto = (ImageView) convertView.findViewById(R.id.ivTweetPhoto);
        final ImageView ivFavorite = (ImageView) convertView.findViewById(R.id.ivFavorite);

        if (tweet.getMediaUrl() == null) {
            ivTweetPhoto.setVisibility(View.GONE);
        } else {
            ivTweetPhoto.setVisibility(View.VISIBLE);

        }

        // populate textViews
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));

        // populate likes
        ivFavorite.setTag(tweet.getUid());
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = TwitterApplication.getRestClient(); // singleton client
                long tagId = (long) ivFavorite.getTag();
                likeTweet(tagId);
                ivFavorite.setColorFilter(Color.parseColor("#E81C4F"), PorterDuff.Mode.SRC_ATOP);
            }
        });

        // populate tweet photo
        Picasso.with(getContext()).load(tweet.getMediaUrl())
                .into(ivTweetPhoto);

        // populate profile image
        ivProfileImage.setImageResource(android.R.color.transparent);
        ivProfileImage.setTag(tweet.getUser().getScreenName());
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                String tag = (String) ivProfileImage.getTag();
                i.putExtra("screen_name", tag);
                getContext().startActivity(i);
            }
        });

        //        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl())
//                .bitmapTransform(new RoundedCornersTransformation(getContext(), 4, 4))
//                .into(ivProfileImage);

        // return the view to be inserted into the list
        return convertView;
    }

    private void likeTweet(long tagId) {
        client.likeTweet(tagId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("LIKE_TWEET", json.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("LIKE_TWEET", errorResponse.toString());
            }
        });

    }
}
