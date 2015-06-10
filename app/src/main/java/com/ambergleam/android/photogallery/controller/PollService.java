package com.ambergleam.android.photogallery.controller;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.model.Photo;
import com.ambergleam.android.photogallery.web.FlickrFetchr;

import java.util.ArrayList;

import timber.log.Timber;

public class PollService extends IntentService {

    private static final int POLL_INTERVAL_MS = 1000 * 60 * 60; // 60 minutes

    public static final String PREF_IS_ALARM_ON = "isAlarmOn";
    public static final String ACTION_SHOW_NOTIFICATION = "com.bignerdranch.android.photogallery.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.bignerdranch.android.photogallery.PRIVATE";

    public PollService() {
        super(PollService.class.getSimpleName());
    }

    @Override
    public void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;
        if (!isNetworkAvailable) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String query = prefs.getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
        String lastResultId = prefs.getString(FlickrFetchr.PREF_LAST_RESULT_ID, null);

        ArrayList<Photo> items;
        if (query != null) {
            items = new FlickrFetchr().getPhotos(query);
        } else {
            items = new FlickrFetchr().getPhotos();
        }

        if (items.size() == 0)
            return;

        String resultId = items.get(0).getId();

        if (!resultId.equals(lastResultId)) {
            Timber.i("Got a new result: " + resultId);

            Resources r = getResources();
            PendingIntent pi = PendingIntent
                    .getActivity(this, 0, new Intent(this, GalleryActivity.class), 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(r.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.new_pictures_title))
                    .setContentText(r.getString(R.string.new_pictures_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            showBackgroundNotification(0, notification);
        }

        prefs.edit()
                .putString(FlickrFetchr.PREF_LAST_RESULT_ID, resultId)
                .commit();
    }

    public static void setServiceAlarm(Context context, boolean isAlarmOn) {
        Timber.i("isAlarmOn = " + isAlarmOn);
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isAlarmOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), POLL_INTERVAL_MS, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PollService.PREF_IS_ALARM_ON, isAlarmOn)
                .commit();
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    private static BroadcastReceiver sNotificationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            Timber.i("received result: " + getResultCode());
            if (getResultCode() != Activity.RESULT_OK) {
                return;
            }

            int requestCode = i.getIntExtra("REQUEST_CODE", 0);
            Notification notification = i.getParcelableExtra("NOTIFICATION");

            NotificationManager notificationManager = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(requestCode, notification);
        }
    };

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra("REQUEST_CODE", requestCode);
        i.putExtra("NOTIFICATION", notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, sNotificationBroadcastReceiver, null, Activity.RESULT_OK, null, null);
    }

}