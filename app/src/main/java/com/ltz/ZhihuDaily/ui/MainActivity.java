package com.ltz.ZhihuDaily.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.views.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Qloop on 2016/7/4.
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.civ_menu_avatar)
    CircleImageView civMenuAvatar;
    @BindView(R.id.tv_menu_name)
    TextView tvMenuName;
    @BindView(R.id.ll_sign_up)
    LinearLayout llSignUp;
    @BindView(R.id.ll_collection)
    LinearLayout llCollection;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.ll_main_pager)
    LinearLayout llMainPager;
    @BindView(R.id.lv_menu)
    ListView lvMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        initData();
    }

    private void initViews() {

        toolbar.setTitle(R.string.main_word);//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        drawerlayout.addDrawerListener(mDrawerToggle);
        Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_notification:
                        startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                        break;
                    case R.id.action_night_model:
                        reSetMode();
                        break;
                    case R.id.action_setting_choice:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    private void reSetMode() {
        boolean isNightMode = PrefUtils.getBoolean(MainActivity.this, "isNightMode", false);
        if (isNightMode) {
            PrefUtils.setBoolean(MainActivity.this,"isNightMode",false);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            PrefUtils.setBoolean(MainActivity.this,"isNightMode",true);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        recreate();
    }


    private void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
