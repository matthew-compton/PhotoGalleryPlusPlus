package com.ambergleam.android.photogallery.controller;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.BaseActivity;
import com.ambergleam.android.photogallery.R;

public class GalleryActivity extends BaseActivity {

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
            fragment.search(intent.getStringExtra(SearchManager.QUERY));
        }
    }

}