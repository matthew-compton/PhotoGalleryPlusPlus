package com.ambergleam.android.photogallery.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ambergleam.android.photogallery.BaseFragment;
import com.ambergleam.android.photogallery.BuildConfig;
import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.dialog.AboutDialogFragment;
import com.ambergleam.android.photogallery.dialog.ClearDialogFragment;
import com.ambergleam.android.photogallery.dialog.LicensesDialogFragment;
import com.ambergleam.android.photogallery.manager.DataManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsFragment extends BaseFragment {

    private static final int REQUEST_DIALOG_CLEAR = 0;

    @Inject DataManager mDataManager;

    @InjectView(R.id.fragment_settings_notifications_switch) Switch mNotificationSwitch;
    @InjectView(R.id.fragment_settings_version_text) TextView mVersionTextView;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, parent, false);
        ButterKnife.inject(this, view);
        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings_clear:
                displayClearDialogFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DIALOG_CLEAR) {
            mDataManager.clearAppData(getActivity());
            Toast.makeText(getActivity(), getString(R.string.dialog_clear_toast), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        mNotificationSwitch.setChecked(PollService.isServiceAlarmOn(getActivity()));
        mVersionTextView.setText(BuildConfig.VERSION_NAME);
    }

    private void displayLicensesDialogFragment() {
        LicensesDialogFragment dialog = LicensesDialogFragment.newInstance();
        dialog.show(getFragmentManager(), LicensesDialogFragment.class.getSimpleName());
    }

    private void displayAboutDialogFragment() {
        AboutDialogFragment dialog = AboutDialogFragment.newInstance();
        dialog.show(getFragmentManager(), AboutDialogFragment.class.getSimpleName());
    }

    private void displayClearDialogFragment() {
        ClearDialogFragment dialog = ClearDialogFragment.newInstance();
        dialog.setTargetFragment(this, REQUEST_DIALOG_CLEAR);
        dialog.show(getFragmentManager(), ClearDialogFragment.class.getSimpleName());
    }

    @OnClick(R.id.fragment_settings_notifications)
    public void onClickNotifications() {
        boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
        PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
        updateUI();
    }

    @OnClick(R.id.fragment_settings_licenses)
    public void onClickLicenses() {
        displayLicensesDialogFragment();
    }

    @OnClick(R.id.fragment_settings_about)
    public void onClickAbout() {
        displayAboutDialogFragment();
    }

}