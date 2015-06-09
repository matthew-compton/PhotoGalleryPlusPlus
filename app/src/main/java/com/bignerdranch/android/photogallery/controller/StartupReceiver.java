package com.bignerdranch.android.photogallery.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import timber.log.Timber;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("Received broadcast intent: " + intent.getAction());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isAlarmOn = prefs.getBoolean(PollService.PREF_IS_ALARM_ON, false);
        PollService.setServiceAlarm(context, isAlarmOn);
    }

}