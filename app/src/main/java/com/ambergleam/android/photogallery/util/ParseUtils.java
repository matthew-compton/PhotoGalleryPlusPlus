package com.ambergleam.android.photogallery.util;

import com.ambergleam.android.photogallery.PhotoGalleryApplication;
import com.ambergleam.android.photogallery.model.Search;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import timber.log.Timber;

public class ParseUtils {

    public static void saveSearchQuery(String query) {
        if (checkSearchQueryExists(query)) {
            return;
        }

        Search search = new Search();
        search.setUuidString();
        search.setText(query);
        search.setAuthor(ParseUser.getCurrentUser());
        search.pinInBackground(PhotoGalleryApplication.getGroupNameSearch());
    }

    private static boolean checkSearchQueryExists(String text) {
        ParseQuery<Search> query = Search.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("text", text);
        try {
            if (query.getFirst() != null) {
                return true;
            }
        } catch (ParseException e) {
            Timber.e("Parse Exception.");
        }
        return false;
    }

}
