package com.ambergleam.android.photogallery;

import android.app.Application;
import android.content.Context;

import com.ambergleam.android.photogallery.manager.ParseDataManager;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import dagger.ObjectGraph;
import timber.log.Timber;

public class BaseApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDagger();
        setupTimber();
        setupParse();
    }

    private void setupDagger() {
        mObjectGraph = ObjectGraph.create(new BaseModule(this));
    }

    private void setupTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, BaseConstants.PARSE_APP_ID, BaseConstants.PARSE_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
        ParseDataManager.registerParseObjects();
    }

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    public final void inject(Object object) {
        mObjectGraph.inject(object);
    }

}