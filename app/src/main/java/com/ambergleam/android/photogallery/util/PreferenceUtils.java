package com.ambergleam.android.photogallery.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.ambergleam.android.photogallery.web.FlickrFetchr;

public class PreferenceUtils {

    public static void clear(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .commit();
    }

    public static String getSearchQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
    }

    public static void setSearchQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
                .commit();
    }

}