package com.bignerdranch.android.photogallery.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.photogallery.base.BaseActivity;

public class PhotoActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(PhotoFragment.ARGS_URL);
        return PhotoFragment.newInstance(url);
    }

}