package com.codepath.apps.TwitterApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.TwitterApp.fragments.HomeTimelineFragment;
import com.codepath.apps.TwitterApp.fragments.MentionsTimelineFragment;
import com.codepath.apps.TwitterApp.fragments.SearchTimelineFragment;
import com.codepath.apps.TwitterApp.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {
    HomeTimelineFragment hTimelineFragment;
    MentionsTimelineFragment mTimelineFragment;
    SearchTimelineFragment sTimelineFragment;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        hTimelineFragment = new HomeTimelineFragment();
        mTimelineFragment = new MentionsTimelineFragment();
        sTimelineFragment = new SearchTimelineFragment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                //drawable.setColorFilter(Color.parseColor("#55ACEE"), PorterDuff.Mode.SRC_ATOP);
                drawable.setColorFilter(Color.parseColor("#55ACEE"), PorterDuff.Mode.SRC_IN);

            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.miProfile) {
            onProfileView(item);
        }

        if (id == R.id.miTweet) {
            onComposeTweet(item);
        }

        if (id == R.id.miSearch) {
            onSearchTweet(item);
        }
        return super.onOptionsItemSelected(item);
    }

    // return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        // adapter gets the mananger insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // the order and creation of fragments within the paper
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return hTimelineFragment;
            } else if (position == 1){
                return mTimelineFragment;
            } else {
                return null;
            }
        }

        // returns the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // number of fragments to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    private final int REQUEST_CODE = 20;

    public void onComposeTweet(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onSearchTweet(MenuItem mi) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            hTimelineFragment.addTweetStart(tweet);
        } else {
            // error handling
        }
    }
}
