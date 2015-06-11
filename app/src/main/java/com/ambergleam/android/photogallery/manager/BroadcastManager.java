package com.ambergleam.android.photogallery.manager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BroadcastManager {

    public static final String BROADCAST_ACTION_CONNECTION_UPDATE = "com.ambergleam.android.photogallery.connection";

    private Context mContext;

    @Inject
    public BroadcastManager(Context context) {
        mContext = context;
    }

    public void sendConnectionUpdate() {
        sendBroadcast(BROADCAST_ACTION_CONNECTION_UPDATE);
    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        manager.sendBroadcast(intent);
    }

}