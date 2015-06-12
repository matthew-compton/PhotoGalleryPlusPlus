package com.ambergleam.android.photogallery.manager;

import com.ambergleam.android.photogallery.callbacks.ClearFavoritesCallback;
import com.ambergleam.android.photogallery.callbacks.ClearSearchesCallback;
import com.ambergleam.android.photogallery.callbacks.LoadFavoritesCallback;
import com.ambergleam.android.photogallery.callbacks.LoadSearchesCallback;
import com.ambergleam.android.photogallery.model.Favorite;
import com.ambergleam.android.photogallery.model.Photo;
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
    public static final String GROUP_NAME_FAVORITES = "ALL_FAVORITES";

    @Inject
    public DataManager() {
    }

    public static void registerParseObjects() {
        ParseObject.registerSubclass(Search.class);
        ParseObject.registerSubclass(Favorite.class);
    }

    public void saveSearch(String query) {
        if (query == null || checkSearchQueryExists(query)) {
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

    public void addFavorite(Photo photo) {
        if (photo == null) {
            Timber.e("Photo is null.");
            return;
        }
        if (isFavorite(photo)) {
            Timber.e("Photo is already a favorite.");
            return;
        }

        Favorite favorite = new Favorite();
        favorite.setUuidString();
        favorite.setPhoto(photo);
        favorite.pinInBackground(GROUP_NAME_FAVORITES);
        Timber.i("Added photo to favorites.");
    }

    public void removeFavorite(Photo photo) {
        if (photo == null) {
            Timber.e("Photo is null.");
            return;
        }

        Favorite favorite = getFavorite(photo);
        if (favorite == null) {
            Timber.e("Photo not found in favorites.");
            return;
        }

        favorite.unpinInBackground(GROUP_NAME_FAVORITES);
        Timber.i("Removed photo from favorites.");
    }

    public void loadFavorites(final LoadFavoritesCallback callback) {
        ParseQuery<Favorite> query = Favorite.getQuery();
        query.fromLocalDatastore();
        query.findInBackground((list, e) -> callback.onFavoritesLoaded(list));
        Timber.i("Loaded favorites.");
    }

    public void clearFavorites(final ClearFavoritesCallback callback) {
        ParseObject.unpinAllInBackground(GROUP_NAME_FAVORITES, e -> {
            callback.onFavoritesCleared();
        });
        Timber.i("Cleared favorites.");
    }

    public boolean isFavorite(Photo photo) {
        if (photo == null) {
            Timber.e("Photo is null.");
            return false;
        }

        ParseQuery<Favorite> query = Favorite.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(Favorite.FAVORITE_PHOTO_ID, photo.getId());
        try {
            if (query.getFirst() != null) {
                return true;
            }
        } catch (ParseException e) {
            Timber.e("Parse Exception.");
        }
        return false;
    }

    public Favorite getFavorite(Photo photo) {
        if (photo == null) {
            Timber.e("Photo is null.");
            return null;
        }

        ParseQuery<Favorite> query = Favorite.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(Favorite.FAVORITE_PHOTO_ID, photo.getId());
        try {
            return query.getFirst();
        } catch (ParseException e) {
            Timber.e("Parse Exception.");
        }
        return null;
    }

}