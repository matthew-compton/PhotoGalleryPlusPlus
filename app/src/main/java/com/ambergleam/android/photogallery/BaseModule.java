package com.ambergleam.android.photogallery;

import com.ambergleam.android.photogallery.controller.GalleryActivity;
import com.ambergleam.android.photogallery.controller.GalleryFragment;
import com.ambergleam.android.photogallery.controller.HistoryActivity;
import com.ambergleam.android.photogallery.controller.HistoryFragment;
import com.ambergleam.android.photogallery.controller.PhotoActivity;
import com.ambergleam.android.photogallery.controller.PhotoFragment;
import com.ambergleam.android.photogallery.controller.SettingsActivity;
import com.ambergleam.android.photogallery.controller.SettingsFragment;
import com.ambergleam.android.photogallery.manager.BroadcastManager;
import com.ambergleam.android.photogallery.manager.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                BaseApplication.class,
                BaseActivity.class,
                BaseFragment.class,
                GalleryActivity.class,
                GalleryFragment.class,
                PhotoActivity.class,
                PhotoFragment.class,
                HistoryActivity.class,
                HistoryFragment.class,
                SettingsActivity.class,
                SettingsFragment.class,
        },
        complete = true)
public class BaseModule {

    private final BaseApplication mApplication;

    public BaseModule(BaseApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public DataManager provideDataManager() {
        return new DataManager();
    }

    @Provides
    @Singleton
    public BroadcastManager provideBroadcastManager() {
        return new BroadcastManager(mApplication);
    }

}