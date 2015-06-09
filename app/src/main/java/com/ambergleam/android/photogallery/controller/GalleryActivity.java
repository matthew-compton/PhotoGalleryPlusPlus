package com.ambergleam.android.photogallery.controller;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.PhotoGalleryApplication;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.base.BaseActivity;
import com.ambergleam.android.photogallery.model.Search;
import com.ambergleam.android.photogallery.util.PreferenceUtils;
import com.parse.ParseUser;

import timber.log.Timber;

public class GalleryActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        return GalleryFragment.newInstance();
    }

    @Override
    protected boolean setupHomeButton() {
        return false;
    }

    @Override
    public void onNewIntent(Intent intent) {
        GalleryFragment fragment = (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.activity_fragment_container);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Timber.i("Received a new search query: " + query);

            Search search = new Search();
            search.setUuidString();
            search.setText(query);
            search.setAuthor(ParseUser.getCurrentUser());
            search.pinInBackground(PhotoGalleryApplication.getGroupNameSearch());

            PreferenceUtils.setSearchQuery(this, query);
        }
        fragment.updatePhotos();
    }

}