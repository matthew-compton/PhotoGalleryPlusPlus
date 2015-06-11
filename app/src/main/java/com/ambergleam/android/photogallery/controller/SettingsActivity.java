package com.ambergleam.android.photogallery.controller;

import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

    @Override
    protected boolean setupHomeButton() {
        return true;
    }

    @Override
    protected boolean postponeEnter() {
        return false;
    }

    @Override
    protected boolean postponeReenter() {
        return false;
    }

}