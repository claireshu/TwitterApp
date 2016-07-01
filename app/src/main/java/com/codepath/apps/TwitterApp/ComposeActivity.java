package com.codepath.apps.TwitterApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.TwitterApp.models.Tweet;
import com.codepath.apps.TwitterApp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    Tweet newTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        client = TwitterApplication.getRestClient();
        client.getUserInfo(null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                addProfileImage(user);

            }
        });

        final Button btnTweet = (Button) findViewById(R.id.btnTweet);
        final EditText etText = (EditText) findViewById(R.id.etText);
        final TextView tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tweetText = etText.getText().toString();
                int tweetLength = 140 - tweetText.length();
                tvCharCount.setText(Integer.toString(tweetLength));
                if (tweetLength <= 0) {
                    tvCharCount.setTextColor( Color.parseColor("#E54648"));
                    btnTweet.setEnabled(false);
                } else {
                    tvCharCount.setTextColor( Color.parseColor("#AAB8C2"));
                    btnTweet.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
            }
        });

    }

    private void addProfileImage(User user) {
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        Glide.with(this).load(user.getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 4, 4))
                .into(ivProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPostTweet(View view) {
        EditText editText = (EditText) findViewById(R.id.etText);
        String tweet = editText.getText().toString();
        client.postUserTweet(tweet, 0L, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Toast.makeText(getApplicationContext(), "Tweet Posted", Toast.LENGTH_SHORT).show();
                newTweet = Tweet.fromJSON(response);
                Intent i = new Intent();
                i.putExtra("tweet", Parcels.wrap(newTweet));
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
