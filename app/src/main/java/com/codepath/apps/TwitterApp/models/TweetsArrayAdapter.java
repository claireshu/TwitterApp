package com.codepath.apps.TwitterApp.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.TwitterApp.ProfileActivity;
import com.codepath.apps.TwitterApp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by claireshu on 6/27/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

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

        // populate data into the subview
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        ivProfileImage.setTag(tweet.getUser().getScreenName());
        tvTimestamp.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));
//        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl())
//                .bitmapTransform(new RoundedCornersTransformation(getContext(), 4, 4))
//                .into(ivProfileImage);
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


        // return the view to be inserted into the list
        return convertView;
    }
}
