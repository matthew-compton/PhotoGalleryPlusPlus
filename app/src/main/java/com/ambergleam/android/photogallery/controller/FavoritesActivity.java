package com.ambergleam.android.photogallery.controller;

import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.BaseActivity;

public class FavoritesActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        return FavoritesFragment.newInstance();
    }

    @Override
    protected boolean setNavIconAsLogo() {
        return false;
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

}