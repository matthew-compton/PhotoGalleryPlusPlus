package com.ambergleam.android.photogallery.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ambergleam.android.photogallery.BaseActivity;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.dialog.ConnectionDialogFragment;

public class ConnectionUtils {

    private static final String TAG_CONNECTION_DIALOG = "ConnectionUtils.TAG_CONNECTION_DIALOG";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void checkForConnection(final BaseActivity activity) {
        if (!isNetworkAvailable(activity)) {
            showNoConnectionDialog(activity);
        } else {
            hideNoConnectionDialog(activity);
        }
    }

    public static void showNoConnectionDialog(final BaseActivity activity) {
        int delay = activity.getResources().getInteger(R.integer.connection_check_delay_ms);
        new Handler().postDelayed(() -> {
            if (activity == null || activity.getSupportFragmentManager() == null) {
                return;
            }
            if (!activity.hasWindowFocus()) {
                return;
            }
            if (!isNetworkAvailable(activity)) {
                Fragment existingDialogFragment = activity.getSupportFragmentManager().findFragmentByTag(TAG_CONNECTION_DIALOG);
                if (existingDialogFragment != null) {
                    return;
                }

                ConnectionDialogFragment dialogFragment = ConnectionDialogFragment.newInstance();
                dialogFragment.setCancelable(false);

                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.add(dialogFragment, TAG_CONNECTION_DIALOG);
                transaction.commit();
            }
        }, delay);
    }

    public static void hideNoConnectionDialog(final BaseActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_CONNECTION_DIALOG);
        if (fragment instanceof DialogFragment) {
            ((DialogFragment) fragment).dismiss();
        }
    }

}