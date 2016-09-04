package com.ltz.ZhihuDaily;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.ltz.ZhihuDaily.ui.PrefUtils;

/**
 * Created by Qloop on 2016/8/21.
 */
public class MyApplication extends Application {


    public static boolean isNightMode;

    @Override
    public void onCreate() {
        super.onCreate();
        isNightMode = PrefUtils.getBoolean(this.getApplicationContext(), "isNightMode", false);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
