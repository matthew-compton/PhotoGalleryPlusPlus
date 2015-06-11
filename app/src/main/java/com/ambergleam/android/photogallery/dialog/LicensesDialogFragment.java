package com.ambergleam.android.photogallery.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.webkit.WebView;

import com.ambergleam.android.photogallery.BaseConstants;
import com.ambergleam.android.photogallery.R;

public class LicensesDialogFragment extends DialogFragment {

    public static LicensesDialogFragment newInstance() {
        return new LicensesDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WebView view = (WebView) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_licenses, null);
        view.loadUrl(BaseConstants.FILE_ANDROID_ASSET_OPEN_SOURCE_LICENSES_HTML);
        return new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(getString(R.string.title_licenses))
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

}