package com.ambergleam.android.photogallery.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.base.BaseActivity;
import com.ambergleam.android.photogallery.base.BaseFragment;
import com.ambergleam.android.photogallery.model.Photo;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import timber.log.Timber;

public class PhotoFragment extends BaseFragment {

    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 0;

    public static final String ARGS_PHOTO = "ARGS_PHOTO";

    @InjectView(R.id.fragment_photo_image) ImageView mImageView;

    private Photo mPhoto;

    public static PhotoFragment newInstance(Photo photo) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_PHOTO, photo);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoto = (Photo) getArguments().getSerializable(ARGS_PHOTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, parent, false);
        ButterKnife.inject(this, view);
        load();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo, menu);

        MenuItem shareMenuItem = menu.findItem(R.id.menu_item_photo_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        Uri uri = Uri.parse(mPhoto.getSmallUrl());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_photo_web:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mPhoto.getPhotoPageUrl()));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void load() {
        int size = mPhoto.getSmallestSide();
        Picasso.with(getActivity())
                .load(mPhoto.getLargeUrl())
                .resize(size, size)
                .centerCrop()
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        ((BaseActivity) getActivity()).scheduleStartPostponedTransition(mImageView);
                    }

                    @Override
                    public void onError() {
                        Timber.e("Error loading image.");
                        ((BaseActivity) getActivity()).scheduleStartPostponedTransition(mImageView);
                    }
                });
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