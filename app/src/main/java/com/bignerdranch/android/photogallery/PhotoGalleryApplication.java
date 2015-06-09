package com.bignerdranch.android.photogallery;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PhotoGalleryApplication extends Application {

    public static final String TODO_GROUP_NAME = "ALL_TODOS";

    @Override
    public void onCreate() {
        super.onCreate();
        setupParse();
    }

    private void setupParse() {
        ParseObject.registerSubclass(Search.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "YOUR_APP_ID", "YOUR_CLIENT_KEY");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }

}