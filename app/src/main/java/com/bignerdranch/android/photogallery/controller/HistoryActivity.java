package com.bignerdranch.android.photogallery.controller;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.photogallery.base.BaseActivity;

public class HistoryActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        return HistoryFragment.newInstance();
    }

    @Override
    protected boolean setupHomeButton() {
        return true;
    }

}