package com.ambergleam.android.photogallery.dialog;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ambergleam.android.photogallery.R;

public class ConnectionDialogFragment extends DialogFragment {

    public static ConnectionDialogFragment newInstance() {
        return new ConnectionDialogFragment();
    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_connection, null);
        return new AlertDialog.Builder(getActivity(), R.style.BaseDialog)
                .setView(view)
                .setCancelable(false)
                .create();
    }

}