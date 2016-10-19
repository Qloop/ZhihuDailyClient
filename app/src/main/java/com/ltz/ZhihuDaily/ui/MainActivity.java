package com.ltz.ZhihuDaily.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ltz.ZhihuDaily.GlobalContants.AppConfig;
import com.ltz.ZhihuDaily.NetInterface.ThemeDrawerInterface;
import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.adapter.DrawerAdapter;
import com.ltz.ZhihuDaily.bean.ThemeListInfo;
import com.ltz.ZhihuDaily.utils.PrefUtils;
import com.ltz.ZhihuDaily.views.CircleImageView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.recy_menu)
    RecyclerView recyMenu;
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

        llMainPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回主页
            }
        });

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

        //设置reclycerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyMenu.setLayoutManager(linearLayoutManager);
        recyMenu.setItemAnimator(new DefaultItemAnimator());

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
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(5000, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ThemeDrawerInterface themeDrawerInterface = retrofit.create(ThemeDrawerInterface.class);
        themeDrawerInterface.getThemeListInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ThemeListInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ThemeListInfo themeListInfo) {
                        recyMenu.setAdapter(new DrawerAdapter(MainActivity.this,themeListInfo.getOthers()));
                        //设置相应主题跳转
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
