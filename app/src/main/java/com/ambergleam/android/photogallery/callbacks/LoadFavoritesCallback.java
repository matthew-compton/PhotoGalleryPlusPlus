package com.ambergleam.android.photogallery.callbacks;

import com.ambergleam.android.photogallery.model.Favorite;

import java.util.List;

public interface LoadFavoritesCallback {

    void onFavoritesLoaded(List<Favorite> favoriteList);

}