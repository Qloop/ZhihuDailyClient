package com.ltz.ZhihuDaily.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ltz.ZhihuDaily.GlobalContants.AppConfig;
import com.ltz.ZhihuDaily.NetInterface.SplashInterface;
import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.bean.SplashInfo;
import com.squareup.picasso.Picasso;

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
 * 启动页
 * Created by Qloop on 2016/7/7.
 */
public class SplashActivity extends Activity {

    @BindView(R.id.iv_splash_pic)
    ImageView ivSplashPic;
    @BindView(R.id.tv_pic_name)
    TextView tvPicName;
    @BindView(R.id.rl_spalsh_bottom)
    RelativeLayout rlSpalshBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initViews();
        initData();
    }

    private void initData() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(5000, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        SplashInterface splashInterface = retrofit.create(SplashInterface.class);
        splashInterface.getSplashInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SplashInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SplashInfo splashInfo) {
                        tvPicName.setText(splashInfo.getText());
                        Picasso.with(SplashActivity.this)
                                .load(splashInfo.getImg())
                                .into(ivSplashPic);
                    }
                });

    }

    private void initViews() {

    }
}
