package com.ambergleam.android.photogallery.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ambergleam.android.photogallery.BaseConstants;
import com.ambergleam.android.photogallery.R;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class LicensesDialogFragment extends DialogFragment {

    @Bind(R.id.dialog_licenses_webview) WebView mWebView;
    @Bind(R.id.dialog_licenses_image) ImageView mImageView;

    public static LicensesDialogFragment newInstance() {
        return new LicensesDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_licenses, null);
        ButterKnife.bind(this, view);

        mWebView.loadUrl(BaseConstants.FILE_ANDROID_ASSET_OPEN_SOURCE_LICENSES_HTML);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                mImageView.setVisibility(View.VISIBLE);
            }
        });

        return new AlertDialog.Builder(getActivity(), R.style.BaseDialog)
                .setView(view)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @OnClick(R.id.dialog_licenses_layout)
    public void onClickDialog() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}