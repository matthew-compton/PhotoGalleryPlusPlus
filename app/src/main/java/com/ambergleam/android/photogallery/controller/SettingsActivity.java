package com.ambergleam.android.photogallery.controller;

import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

    @Override
    protected boolean setupHomeButton() {
        return true;
    }

}