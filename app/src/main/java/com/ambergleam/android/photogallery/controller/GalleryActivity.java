package com.ambergleam.android.photogallery.controller;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.base.BaseActivity;
import com.ambergleam.android.photogallery.util.ParseUtils;
import com.ambergleam.android.photogallery.util.PreferenceUtils;

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
    protected boolean postponeEnter() {
        return false;
    }

    @Override
    protected boolean postponeReenter() {
        return false;
    }

    @Override
    public void onNewIntent(Intent intent) {
        GalleryFragment fragment = (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.activity_fragment_container);
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) && fragment != null) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PreferenceUtils.setSearchQuery(this, query);
            ParseUtils.saveSearchQuery(query);
            fragment.search();
        }
    }

}