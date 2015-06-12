package com.ambergleam.android.photogallery.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambergleam.android.photogallery.BaseFragment;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.callbacks.ClearFavoritesCallback;
import com.ambergleam.android.photogallery.callbacks.LoadFavoritesCallback;
import com.ambergleam.android.photogallery.manager.DataManager;
import com.ambergleam.android.photogallery.model.Favorite;
import com.ambergleam.android.photogallery.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FavoritesFragment extends BaseFragment implements LoadFavoritesCallback, ClearFavoritesCallback {

    @Inject DataManager mDataManager;

    @InjectView(R.id.fragment_favorites_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.fragment_favorites_grid) GridView mGridView;
    @InjectView(R.id.fragment_favorites_empty) TextView mEmptyView;

    private ArrayList<Photo> mPhotos;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.inject(this, view);
        setupAdapter();
        setupListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataManager.loadFavorites(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favorites, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_favorites_clear:
                mDataManager.clearFavorites(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItemClear = menu.findItem(R.id.menu_item_favorites_clear);
        menuItemClear.setVisible(!mPhotos.isEmpty());
    }

    private void setupAdapter() {
        if (getActivity() == null || mGridView == null) {
            return;
        }
        mGridView.setAdapter(new PhotoGridViewAdapter(mPhotos));
    }

    private void setupListeners() {
        mGridView.setOnItemClickListener((gridView, gridItem, position, id) -> {
            Intent intent = new Intent(getActivity(), PhotoActivity.class);
            intent.putExtra(PhotoFragment.ARGS_PHOTO, mPhotos.get(position));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    gridItem,
                    getString(R.string.transition_photo)
            );
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> mDataManager.loadFavorites(this));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
    }

    private void updateUI() {
        if (mPhotos.isEmpty()) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onFavoritesCleared() {
        mPhotos = new ArrayList<>();
        setupAdapter();
        updateUI();
    }

    @Override
    public void onFavoritesLoaded(List<Favorite> favoriteList) {
        mPhotos = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            mPhotos.add(favorite.getPhoto());
        }
        setupAdapter();
        updateUI();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private class PhotoGridViewAdapter extends ArrayAdapter<Photo> {

        public PhotoGridViewAdapter(ArrayList<Photo> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item_favorites, parent, false);
            }

            Photo item = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_favorites_image);

            Picasso.with(getActivity())
                    .load(item.getSmallUrl())
                    .into(imageView);

            int size = item.getSmallestSide();
            Picasso.with(getActivity())
                    .load(item.getLargeUrl())
                    .resize(size, size)
                    .centerCrop()
                    .fetch();

            return convertView;
        }

    }

}