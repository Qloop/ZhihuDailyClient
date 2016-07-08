package com.ltz.ZhihuDaily.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ltz.ZhihuDaily.GlobalContants.AppConfig;
import com.ltz.ZhihuDaily.NetInterface.SplashInterface;
import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.bean.SplashInfo;
import com.ltz.ZhihuDaily.utils.MeasureUtils;
import com.ltz.ZhihuDaily.utils.ToastUtils;
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
    @BindView(R.id.rl_spalsh_top)
    RelativeLayout rlSpalshTop;

    private static final int MSG_NET_ERROR = 0;
    private static final int MSG_NEXT_PAGER = 1;
    private static final int MSG_SHOW_PIC = 2;
    private static final int DEFAULT_DELAY = 2000;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NET_ERROR:
                    ToastUtils.showToastShort(SplashActivity.this, "网络错误");
                    break;
                case MSG_NEXT_PAGER:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case MSG_SHOW_PIC:
                    initData();
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initViews();
    }

    private void initData() {
        final Message msg = mHandler.obtainMessage();
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
                        msg.what = MSG_NET_ERROR;
                        mHandler.sendMessageDelayed(msg, DEFAULT_DELAY);
                    }

                    @Override
                    public void onNext(SplashInfo splashInfo) {
                        tvPicName.setText(splashInfo.getText());
                        Picasso.with(SplashActivity.this)
                                .load(splashInfo.getImg())
                                .into(ivSplashPic);
                        startAlphaAnimation();
                    }
                });

    }

    /**
     * 底部文字位移动画
     */
    private void initViews() {
        int[] screenSize = MeasureUtils.getScreenSize(SplashActivity.this);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(true);
        rlSpalshBottom.startAnimation(translateAnimation);

        //动画结束后  开始顶部图片的动画
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SHOW_PIC;
        mHandler.sendMessageDelayed(msg, 1000);

    }

    /**
     * 顶部图片透明度动画
     */
    private void startAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        rlSpalshTop.startAnimation(alphaAnimation);

        Message msg = mHandler.obtainMessage();
        msg.what = MSG_NEXT_PAGER;
        mHandler.sendMessageDelayed(msg, DEFAULT_DELAY);
    }
}
