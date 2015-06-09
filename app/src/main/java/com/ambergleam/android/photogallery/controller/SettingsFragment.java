package com.ambergleam.android.photogallery.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.ambergleam.android.photogallery.R;
import com.ambergleam.android.photogallery.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import timber.log.Timber;

public class SettingsFragment extends BaseFragment {

    @InjectView(R.id.fragment_settings_notifications_switch) Switch mNotificationSwitch;

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

    private void updateUI() {
        mNotificationSwitch.setChecked(PollService.isServiceAlarmOn(getActivity()));
    }

    private void displayLicensesFragmentDialog() {
        LicensesDialogFragment dialog = LicensesDialogFragment.newInstance();
        dialog.show(getChildFragmentManager(), LicensesDialogFragment.class.getSimpleName());
    }

    @OnClick(R.id.fragment_settings_notifications)
    public void onClickNotifications() {
        boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
        PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
        updateUI();
    }

    @OnClick(R.id.fragment_settings_licenses)
    public void onClickLicenses() {
        displayLicensesFragmentDialog();
    }

    @OnClick(R.id.fragment_settings_about)
    public void onClickAbout() {
        Timber.i("About");
        // TODO
    }

}