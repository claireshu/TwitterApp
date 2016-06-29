package com.codepath.apps.TwitterApp.models;

import android.icu.text.SimpleDateFormat;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by claireshu on 6/28/16.
 */
public class ParseRelativeDate {
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (relativeDate.contains("second")) {
            relativeDate = relativeDate.replaceAll("\\D+","") + "s";
            //relativeDate = relativeDate.substring(0,1) + "s";
        } else if (relativeDate.contains("minute")) {
            relativeDate = relativeDate.replaceAll("\\D+","") + "m";
            // relativeDate = relativeDate.substring(0,1) + "m";
        } else if (relativeDate.contains("hour")) {
            relativeDate = relativeDate.replaceAll("\\D+","") + "h";
            // relativeDate = relativeDate.substring(0,1) + "m";
        } else if (relativeDate.contains("day")) {
            relativeDate = relativeDate.replaceAll("\\D+","") + "d";
           // relativeDate = relativeDate.substring(0,1) + "d";
        }

        return relativeDate;
    }

}
