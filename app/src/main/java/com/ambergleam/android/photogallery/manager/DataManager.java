package com.ambergleam.android.photogallery.manager;

import com.ambergleam.android.photogallery.callbacks.ClearSearchesCallback;
import com.ambergleam.android.photogallery.callbacks.LoadSearchesCallback;
import com.ambergleam.android.photogallery.model.Search;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class DataManager {

    public static final String GROUP_NAME_SEARCH = "ALL_SEARCHES";

    @Inject
    public DataManager() {
    }

    public static void registerParseObjects() {
        ParseObject.registerSubclass(Search.class);
    }

    public void saveSearch(String query) {
        if (checkSearchQueryExists(query)) {
            return;
        }

        Search search = new Search();
        search.setUuidString();
        search.setText(query);
        search.pinInBackground(GROUP_NAME_SEARCH);
    }

    public void loadSearches(final LoadSearchesCallback callback) {
        ParseQuery<Search> query = Search.getQuery();
        query.fromLocalDatastore();
        query.findInBackground((list, e) -> callback.onSearchesLoaded(list));
    }

    public void clearSearches(final ClearSearchesCallback callback) {
        ParseObject.unpinAllInBackground(GROUP_NAME_SEARCH, e -> {
            callback.onSearchesCleared();
        });
    }

    public boolean checkSearchQueryExists(String text) {
        ParseQuery<Search> query = Search.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(Search.SEARCH_TEXT, text);
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