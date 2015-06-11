package com.ambergleam.android.photogallery.manager;

import com.ambergleam.android.photogallery.model.Search;

import java.util.List;

public interface LoadSearchesCallback {

    void onSearchesLoaded(List<Search> searchList);

}