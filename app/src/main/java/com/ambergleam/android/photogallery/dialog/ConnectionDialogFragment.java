package com.ambergleam.android.photogallery.dialog;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.ambergleam.android.photogallery.R;

public class ConnectionDialogFragment extends DialogFragment {

    public static ConnectionDialogFragment newInstance() {
        return new ConnectionDialogFragment();
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_connection)
                .setMessage(getString(R.string.connection_content))
                .setCancelable(false)
                .create();
    }

}