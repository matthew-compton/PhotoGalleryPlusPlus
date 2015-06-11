package com.ambergleam.android.photogallery.controller;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambergleam.android.photogallery.BaseFragment;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.manager.ClearSearchesCallback;
import com.ambergleam.android.photogallery.manager.DataManager;
import com.ambergleam.android.photogallery.manager.LoadSearchesCallback;
import com.ambergleam.android.photogallery.model.Search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoryFragment extends BaseFragment implements LoadSearchesCallback, ClearSearchesCallback {

    @Inject DataManager mDataManager;

    @InjectView(R.id.fragment_history_empty) TextView mEmptyView;
    @InjectView(R.id.fragment_history_recycler) RecyclerView mRecyclerView;

    private SearchAdapter mAdapter;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, parent, false);
        ButterKnife.inject(this, view);
        setupUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataManager.loadSearches(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_history_clear:
                mDataManager.clearSearches(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupUI() {
        mAdapter = new SearchAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
    }

    private void updateUI() {
        if (mAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void search(Search search) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(getActivity(), GalleryActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, search.getText());
        startActivity(intent);
    }

    @Override
    public void onSearchesLoaded(List<Search> searchList) {
        Collections.reverse(searchList);
        mAdapter.setList(searchList);
        mAdapter.notifyDataSetChanged();
        updateUI();
    }

    @Override
    public void onSearchesCleared() {
        mAdapter.setList(new ArrayList<>());
        mAdapter.notifyDataSetChanged();
        updateUI();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.list_item_search_text) TextView mTextView;

        private Search mSearch;

        public SearchHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            setupListeners(view);
        }

        public void bindSearch(Search search) {
            mSearch = search;
            mTextView.setText(mSearch.getText());
        }

        private void setupListeners(View view) {
            view.setOnClickListener(v -> {
                search(mSearch);
            });
        }

    }

    public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

        private List<Search> mSearchList;

        public SearchAdapter(List<Search> searchList) {
            mSearchList = searchList;
        }

        @Override
        public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_search, parent, false);
            return new SearchHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchHolder holder, int position) {
            Search search = mSearchList.get(position);
            holder.bindSearch(search);
        }

        @Override
        public int getItemCount() {
            return mSearchList.size();
        }

        public void setList(List<Search> searchList) {
            mSearchList = searchList;
        }
    }

}