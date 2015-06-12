package com.ambergleam.android.photogallery.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.ambergleam.android.photogallery.R;

public class ClearDialogFragment extends DialogFragment {

    public static ClearDialogFragment newInstance() {
        return new ClearDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), R.style.BaseDialog)
                .setTitle(R.string.dialog_clear_title)
                .setMessage(R.string.dialog_clear_content)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    sendResult();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    dismiss();
                })
                .create();
    }

    private void sendResult() {
        if (getTargetFragment() == null) {
            return;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
    }

}