package com.ambergleam.android.photogallery.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ambergleam.android.photogallery.R;

public class AboutDialogFragment extends DialogFragment {

    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null);
        view.setOnClickListener(v -> {
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity(), R.style.BaseDialog)
                .setView(view)
                .create();
    }

}