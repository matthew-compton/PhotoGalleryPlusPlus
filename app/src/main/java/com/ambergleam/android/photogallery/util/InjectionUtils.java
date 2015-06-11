package com.ambergleam.android.photogallery.util;

import android.content.Context;

import com.ambergleam.android.photogallery.BaseApplication;

public class InjectionUtils {

    public static void inject(Context context) {
        inject(context, context);
    }

    public static void inject(Context context, Object obj) {
        BaseApplication.get(context).inject(obj);
    }

}