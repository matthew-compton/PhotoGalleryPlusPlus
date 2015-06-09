package com.ambergleam.android.photogallery.controller;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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

import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.base.BaseFragment;
import com.ambergleam.android.photogallery.model.Photo;
import com.ambergleam.android.photogallery.web.FlickrFetchr;
import com.ambergleam.android.photogallery.web.ThumbnailDownloader;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GalleryFragment extends BaseFragment {

    private ThumbnailDownloader mDownloaderThread;
    private ArrayList<Photo> mPhotos;

    @InjectView(R.id.fragment_gallery_grid) GridView mGridView;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        updatePhotos();
        setupDownloaderThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.inject(this, view);

        setupAdapter();

        mGridView.setOnItemClickListener((gridView, v, pos, id) -> {
            Photo item = mPhotos.get(pos);
            Uri photoPageUri = Uri.parse(item.getUrl());
            Intent i = new Intent(getActivity(), PhotoActivity.class);
            i.setData(photoPageUri);
            startActivity(i);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDownloaderThread.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloaderThread.quit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_gallery, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            MenuItem searchItem = menu.findItem(R.id.menu_item_gallery_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            ComponentName name = getActivity().getComponentName();
            SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
            searchView.setSearchableInfo(searchInfo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_gallery_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_gallery_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_SEARCH_QUERY, null)
                        .commit();
                updatePhotos();
                return true;
            case R.id.menu_item_gallery_history:
                Intent i = new Intent(getActivity(), HistoryActivity.class);
                startActivity(i);
                return true;
            case R.id.menu_item_gallery_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getActivity().invalidateOptionsMenu();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem toggleItem = menu.findItem(R.id.menu_item_gallery_polling);
        if (PollService.isServiceAlarmOn(getActivity())) {
            toggleItem.setTitle(R.string.polling_stop);
        } else {
            toggleItem.setTitle(R.string.polling_start);
        }
    }

    public void updatePhotos() {
        new UpdatePhotosAsyncTask().execute();
    }

    private void setupDownloaderThread() {
        mDownloaderThread = new ThumbnailDownloader(new Handler());
        mDownloaderThread.start();
    }

    private void setupAdapter() {
        if (getActivity() == null || mGridView == null) {
            return;
        }
        if (mPhotos != null) {
            mGridView.setAdapter(new GalleryItemAdapter(mPhotos));
        } else {
            mGridView.setAdapter(null);
        }
    }

    private class UpdatePhotosAsyncTask extends AsyncTask<Void, Void, ArrayList<Photo>> {
        @Override
        protected ArrayList<Photo> doInBackground(Void... params) {
            if (getActivity() == null) {
                return new ArrayList<>();
            }
            String query = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
            if (query != null) {
                return new FlickrFetchr().searchPhotos(query);
            } else {
                return new FlickrFetchr().getRecentPhotos();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> items) {
            mPhotos = items;
            if (items.size() > 0) {
                String resultId = items.get(0).getId();
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_LAST_RESULT_ID, resultId)
                        .commit();
            }
            setupAdapter();
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter<Photo> {

        public GalleryItemAdapter(ArrayList<Photo> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item_gallery, parent, false);
            }

            Photo item = getItem(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_gallery_image);
            mDownloaderThread.queueThumbnail(imageView, item.getUrl());

            return convertView;
        }
    }

}