package com.ambergleam.android.photogallery.util;

import com.ambergleam.android.photogallery.PhotoGalleryApplication;
import com.ambergleam.android.photogallery.model.Search;
import com.parse.ParseUser;

public class ParseUtils {

    public static void saveSearchQuery(String query) {
        Search search = new Search();
        search.setUuidString();
        search.setText(query);
        search.setAuthor(ParseUser.getCurrentUser());
        search.pinInBackground(PhotoGalleryApplication.getGroupNameSearch());
    }

}
