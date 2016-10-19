package com.ltz.ZhihuDaily.base;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.WindowManager;

import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.utils.PrefUtils;
import com.ltz.ZhihuDaily.utils.SystemBarTintManager;

/**
 * Created by Qloop on 2016/7/4.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";
    private SystemBarTintManager systemBarTintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView(savedInstanceState);
        initTransparentStatusBar();
        initData();
    }

    /*
    * transparent status bar
    * */
    public void initTransparentStatusBar() {
        systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setNavigationBarTintEnabled(true);
        systemBarTintManager.setTintColor(0);
        final Drawable drawable = ContextCompat.getDrawable(this, R.color.colorMain);
        systemBarTintManager.setStatusBarTintDrawable(drawable);
    }

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initData();

    public void setMode() {
        boolean isNightMode = PrefUtils.getBoolean(this, "isNightMode", false);
        if (isNightMode) {
            PrefUtils.setBoolean(this, "isNightMode", false);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            PrefUtils.setBoolean(this, "isNightMode", true);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }
}
