package com.ambergleam.android.photogallery.controller;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.base.BaseFragment;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PhotoFragment extends BaseFragment {

    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 0;

    public static final String ARGS_URL = "ARGS_URL";

    @InjectView(R.id.fragment_photo_image) ImageView mImageView;
    private String mUrl;

    public static PhotoFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_URL, url);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mUrl = getArguments().getString(ARGS_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, parent, false);
        ButterKnife.inject(this, view);
        load();
        return view;
    }

    private void load() {
        Picasso.with(getActivity())
                .load(mUrl)
                .into(mImageView);
    }

    private void share() {
        Drawable mDrawable = mImageView.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), mBitmap, "Image Description", null);
        Uri uri = Uri.parse(path);

        ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(
                uri,
                "image/*"
        ).build();

        MessengerUtils.shareToMessenger(getActivity(), REQUEST_CODE_SHARE_TO_MESSENGER, shareToMessengerParams);
    }

    @OnClick(R.id.fragment_photo_share)
    public void onClickShare() {
        share();
    }

}