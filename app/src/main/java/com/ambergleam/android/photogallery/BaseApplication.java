package com.ambergleam.android.photogallery;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.ambergleam.android.photogallery.manager.BroadcastManager;
import com.ambergleam.android.photogallery.manager.DataManager;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;

public class BaseApplication extends Application {

    @Inject BroadcastManager mBroadcastManager;

    private ObjectGraph mObjectGraph;

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupDagger();
        setupTimber();
        setupParse();
        registerConnectionUpdateReceiver();
    }

    private void setupDagger() {
        mObjectGraph = ObjectGraph.create(getModule());
        inject(this);
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
        DataManager.registerParseObjects();
    }

    private void registerConnectionUpdateReceiver() {
        IntentFilter connectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mBroadcastManager.sendConnectionUpdate();
            }
        }, connectionFilter);
    }

    public final void inject(Object object) {
        mObjectGraph.inject(object);
    }

    public Object getModule() {
        return new BaseModule(this);
    }

}