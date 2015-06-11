package com.ambergleam.android.photogallery.controller;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.BaseActivity;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.manager.DataManager;
import com.ambergleam.android.photogallery.util.PreferenceUtils;

import javax.inject.Inject;

public class GalleryActivity extends BaseActivity {

    @Inject DataManager mDataManager;

    @Override
    public Fragment createFragment() {
        return GalleryFragment.newInstance();
    }

    @Override
    protected boolean setNavIconAsLogo() {
        return true;
    }

    @Override
    protected boolean setPostponeEnter() {
        return false;
    }

    @Override
    protected boolean setPostponeReenter() {
        return false;
    }

    @Override
    protected boolean showsNetworkConnectionDialog() {
        return true;
    }

    @Override
    public void onNewIntent(Intent intent) {
        GalleryFragment fragment = (GalleryFragment) getSupportFragmentManager().findFragmentById(R.id.activity_fragment_container);
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) && fragment != null) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            PreferenceUtils.setSearchQuery(this, query);
            mDataManager.saveSearch(query);
            fragment.search();
        }
    }

}