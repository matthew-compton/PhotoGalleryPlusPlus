package com.ambergleam.android.photogallery.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambergleam.android.photogallery.PhotoGalleryApplication;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.base.BaseFragment;
import com.ambergleam.android.photogallery.model.Search;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class HistoryFragment extends BaseFragment {

    private ParseQueryAdapter<Search> mParseQueryAdapter;

    @InjectView(R.id.fragment_history_list) ListView mListView;
    @InjectView(R.id.fragment_history_empty) TextView mEmptyView;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, parent, false);
        ButterKnife.inject(this, view);

        mListView.setEmptyView(mEmptyView);

        ParseQueryAdapter.QueryFactory<Search> factory = () -> {
            ParseQuery<Search> query = Search.getQuery();
            query.orderByDescending("createdAt");
            query.fromLocalDatastore();
            return query;
        };
        mParseQueryAdapter = new HistoryAdapter(getActivity(), factory);
        mListView.setAdapter(mParseQueryAdapter);
        mListView.setOnItemClickListener((parent1, view1, position, id) -> {
            Search search = mParseQueryAdapter.getItem(position);
            // TODO - repeat search
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromParse();
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            syncWithParse();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_history_sync:
                syncWithParse();
                return true;
            case R.id.menu_item_history_clear:
                // TODO - clear history
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void syncWithParse() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if ((networkInfo != null) && (networkInfo.isConnected())) {
            if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                ParseQuery<Search> query = Search.getQuery();
                query.fromPin(PhotoGalleryApplication.getGroupNameSearch());
                query.findInBackground((searches, e) -> {
                    if (e == null) {
                        for (final Search search : searches) {
                            search.saveInBackground(exception -> {
                                if (exception == null) {
                                    mParseQueryAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } else {
                        Timber.e("Sync Error: " + e.getMessage());
                    }
                });
            }
        } else {
            Toast.makeText(
                    getActivity(),
                    R.string.error_sync,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void loadFromParse() {
        ParseQuery<Search> query = Search.getQuery();
        query.whereEqualTo("author", ParseUser.getCurrentUser());
        query.findInBackground((searches, exception) -> {
            if (exception == null) {
                ParseObject.pinAllInBackground(
                        searches,
                        e -> {
                            if (e == null) {
                                mParseQueryAdapter.loadObjects();
                            } else {
                                Timber.e("Load Error: " + e.getMessage());
                            }
                        });
            } else {
                Timber.e("Load Error: " + exception.getMessage());
            }
        });
    }

    private class HistoryAdapter extends ParseQueryAdapter<Search> {

        public HistoryAdapter(Context context, ParseQueryAdapter.QueryFactory<Search> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(Search search, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.list_item_search, parent, false);
                holder = new ViewHolder();
                holder.mText = (TextView) view.findViewById(R.id.list_item_search_text);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            TextView todoTitle = holder.mText;
            todoTitle.setText(search.getText());
            return view;
        }
    }

    private static class ViewHolder {
        TextView mText;
    }

}