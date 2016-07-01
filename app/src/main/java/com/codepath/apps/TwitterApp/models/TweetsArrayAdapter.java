package com.codepath.apps.TwitterApp.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.TwitterApp.ProfileActivity;
import com.codepath.apps.TwitterApp.R;
import com.codepath.apps.TwitterApp.ReplyActivity;
import com.codepath.apps.TwitterApp.TwitterApplication;
import com.codepath.apps.TwitterApp.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

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
        final ImageView ivRetweet = (ImageView) convertView.findViewById(R.id.ivRetweet);
        final ImageView ivReply = (ImageView) convertView.findViewById(R.id.ivReply);
        final TextView tvFavoritesCount = (TextView) convertView.findViewById(R.id.tvFavoritesCount);
        final TextView tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);

        // sets photos
        if (tweet.getMediaUrl() == null) {
            ivTweetPhoto.setVisibility(View.GONE);
        } else {
            ivTweetPhoto.setVisibility(View.VISIBLE);

        }

        // sets favorites and retweet counts
        if (tweet.getFavoriteCount() != 0) {
            tvFavoritesCount.setText(Integer.toString(tweet.getFavoriteCount()));
        }

        if (tweet.getRetweetCount() != 0) {
            tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        }

        // sets favorites
        if (tweet.isFavorited()) {
            ivFavorite.setColorFilter(Color.parseColor("#E81C4F"), PorterDuff.Mode.SRC_ATOP);
        } else {
            ivFavorite.setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
        }

        // sets retweets
        if (tweet.isRetweeted()) {
            ivRetweet.setColorFilter(Color.parseColor("#19CF86"), PorterDuff.Mode.SRC_ATOP);
        } else {
            ivRetweet.setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
        }

        // populate textViews
        tvUserName.setText(tweet.getUser().getScreenName());

        // SET HTML?
        String parsedText = parseText(tweet.getBody());
        tvBody.setText(Html.fromHtml(parsedText));

        tvTimestamp.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));

        // populate likes
        ivFavorite.setTag(tweet.getUid());
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = TwitterApplication.getRestClient(); // singleton client
                long tagId = (long) ivFavorite.getTag();
                if (tweet.isFavorited()) {
                    unLikeTweet(tagId);
                    ivFavorite.setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
                    tweet.setFavorited(false);
                    int curr_count = Integer.parseInt((String) tvFavoritesCount.getText());
                    tvFavoritesCount.setText(Integer.toString(curr_count - 1));
                } else {
                    likeTweet(tagId);
                    ivFavorite.setColorFilter(Color.parseColor("#E81C4F"), PorterDuff.Mode.SRC_ATOP);
                    tweet.setFavorited(true);
                    int curr_count = Integer.parseInt((String) tvFavoritesCount.getText());
                    tvFavoritesCount.setText(Integer.toString(curr_count + 1));
                }
            }
        });

        // populate retweets
        ivRetweet.setTag(tweet.getUid());
        ivRetweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = TwitterApplication.getRestClient(); // singleton client
                long tagId = (long) ivRetweet.getTag();
                if (tweet.isRetweeted()) {
                    unretweet(tagId);
                    ivRetweet.setColorFilter(Color.parseColor("#AAB8C2"), PorterDuff.Mode.SRC_ATOP);
                    tweet.setRetweeted(false);
                    int curr_count = Integer.parseInt((String) tvRetweetCount.getText());
                    tvRetweetCount.setText(Integer.toString(curr_count - 1));

                } else {
                    retweet(tagId);
                    ivRetweet.setColorFilter(Color.parseColor("#19CF86"), PorterDuff.Mode.SRC_ATOP);
                    tweet.setRetweeted(true);
                    int curr_count = Integer.parseInt((String) tvRetweetCount.getText());
                    tvRetweetCount.setText(Integer.toString(curr_count + 1));
                }
            }
        });

        // reply to tweet
        ivReply.setTag(R.id.key1, tweet.getUid());
        ivReply.setTag(R.id.key2, tweet.getUser().getScreenName());
        ivReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                client = TwitterApplication.getRestClient(); // singleton client
                long tagId = (long) ivReply.getTag(R.id.key1);
                String user = (String) ivReply.getTag(R.id.key2);
                Intent i = new Intent(getContext(), ReplyActivity.class);
                i.putExtra("tag_id", tagId);
                i.putExtra("user", user);
                getContext().startActivity(i);
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
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("LIKE_TWEET", json.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("LIKE_TWEET", errorResponse.toString());
            }
        });
    }


    private void unLikeTweet(long tagId) {
        client.unLikeTweet(tagId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("UNLIKE_TWEET", json.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("UNLIKE_TWEET", errorResponse.toString());
            }
        });
    }

    private void unretweet(long tagId) {
        client.unretweetTweet(tagId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("UNRETWEET", json.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("UNRETWEET", errorResponse.toString());
            }
        });
    }

    private void retweet(long tagId) {
        client.retweetTweet(tagId, new JsonHttpResponseHandler() {
            // SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("RETWEET", json.toString());
            }

            // FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("RETWEET", errorResponse.toString());
            }
        });
    }

    private String parseText(String text) {
        String fStartTag = "<font color=#55ACEE>";
        String fEndTag = "</font>";
        String formattedText = "";
        int startInsert = 0;
        int endInsert = -1;

        for (int i = 0; i < text.length(); i++) {
            if (i < endInsert) {
                continue;
            }

            if ((i < text.length() - 1) && (text.substring(i, i + 1).equals("@") || text.substring(i, i + 1).equals("#"))) {
                startInsert = i;
                endInsert = text.indexOf(" ", i);
                if (endInsert == -1) {
                    endInsert = text.length();
                }
                formattedText += fStartTag;
                formattedText += text.substring(startInsert, endInsert);
                formattedText += fEndTag;
            } else {
                formattedText += text.substring(i, i + 1);
            }
        }
        return formattedText;
    }
}
