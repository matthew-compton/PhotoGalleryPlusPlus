package com.ambergleam.android.photogallery;

import android.app.Application;

import com.ambergleam.android.photogallery.model.Search;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import timber.log.Timber;

public class PhotoGalleryApplication extends Application {

    private static final String GROUP_NAME_SEARCH = "ALL_SEARCHES";

    @Override
    public void onCreate() {
        super.onCreate();
        setupTimber();
        setupParse();
    }

    private void setupTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    private void setupParse() {
        ParseObject.registerSubclass(Search.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PhotoGalleryConstants.PARSE_APP_ID, PhotoGalleryConstants.PARSE_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }

    public static String getGroupNameSearch() {
        return GROUP_NAME_SEARCH;
    }

}