package com.ambergleam.android.photogallery.controller;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ambergleam.android.photogallery.BaseActivity;
import com.ambergleam.android.photogallery.BaseFragment;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.manager.DataManager;
import com.ambergleam.android.photogallery.model.Photo;
import com.ambergleam.android.photogallery.util.PreferenceUtils;
import com.ambergleam.android.photogallery.web.FlickrFetchr;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Bind;
import timber.log.Timber;

public class GalleryFragment extends BaseFragment {

    @Inject DataManager mDataManager;

    @Bind(R.id.fragment_gallery_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.fragment_gallery_grid) GridView mGridView;

    private ArrayList<Photo> mPhotos;
    private String mSearch;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        setupAdapter();
        setupListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSwipeRefreshLayout.requestFocus();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_gallery, menu);

        MenuItem menuItemSearch = menu.findItem(R.id.menu_item_gallery_search);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setOnSearchClickListener(v -> {
            ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        });
        searchView.setOnCloseListener(() -> {
            ActionBar actionBar = ((BaseActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(true);
            }
            PreferenceUtils.setSearchQuery(getActivity(), null);
            return false;
        });

        if (mSearch != null && !mSearch.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(mSearch, false);
            searchView.clearFocus();
        } else {
            searchView.setIconified(true);
        }

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
        searchView.setSearchableInfo(searchInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_gallery_favorites:
                Intent intentFavorites = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intentFavorites);
                return true;
            case R.id.menu_item_gallery_history:
                Intent intentHistory = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intentHistory);
                return true;
            case R.id.menu_item_gallery_settings:
                Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intentSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupAdapter() {
        if (getActivity() == null || mGridView == null) {
            return;
        }
        if (mPhotos != null) {
            mGridView.setAdapter(new PhotoGridViewAdapter(mPhotos));
        } else {
            mGridView.setAdapter(null);
        }
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
        mSwipeRefreshLayout.setOnRefreshListener(this::search);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent);
    }

    public void search() {
        mSearch = PreferenceUtils.getSearchQuery(getActivity());
        search(mSearch);
    }

    public void search(String query) {
        mSearch = query;
        PreferenceUtils.setSearchQuery(getActivity(), mSearch);
        mDataManager.saveSearch(mSearch);
        new SearchAsyncTask(mSearch).execute();
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, ArrayList<Photo>> {

        private String mQuery;

        public SearchAsyncTask(String query) {
            Timber.i("Search Query: " + mQuery);
            mQuery = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().invalidateOptionsMenu();
        }

        @Override
        protected ArrayList<Photo> doInBackground(String... params) {
            if (getActivity() == null) {
                return new ArrayList<>();
            }
            if (mQuery != null) {
                return new FlickrFetchr().getPhotos(mQuery);
            } else {
                return new FlickrFetchr().getPhotos();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> items) {
            mPhotos = items;
            setupAdapter();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private class PhotoGridViewAdapter extends ArrayAdapter<Photo> {

        public PhotoGridViewAdapter(ArrayList<Photo> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item_gallery, parent, false);
            }

            Photo item = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_gallery_image);

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