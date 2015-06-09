package com.bignerdranch.android.photogallery.controller;

import android.app.SearchManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.photogallery.PhotoGalleryApplication;
import com.bignerdranch.android.photogallery.R;
import com.bignerdranch.android.photogallery.base.BaseActivity;
import com.bignerdranch.android.photogallery.model.Search;
import com.bignerdranch.android.photogallery.web.FlickrFetchr;
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
        GalleryFragment fragment = (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Timber.i("Received a new search query: " + query);

            Search search = new Search();
            search.setUuidString();
            search.setText(query);
            search.setAuthor(ParseUser.getCurrentUser());
            search.pinInBackground(PhotoGalleryApplication.getGroupNameSearch());

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
                    .commit();
        }

        fragment.updatePhotos();
    }

}