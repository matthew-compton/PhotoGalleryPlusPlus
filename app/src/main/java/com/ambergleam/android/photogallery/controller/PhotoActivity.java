package com.ambergleam.android.photogallery.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ambergleam.android.photogallery.BaseActivity;
import com.ambergleam.android.photogallery.model.Photo;

public class PhotoActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PhotoFragment.ARGS_PHOTO);
        return PhotoFragment.newInstance(photo);
    }

    @Override
    protected boolean setupHomeButton() {
        return true;
    }

    @Override
    protected boolean postponeEnter() {
        return true;
    }

    @Override
    protected boolean postponeReenter() {
        return false;
    }

}