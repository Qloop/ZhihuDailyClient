package com.ltz.ZhihuDaily.fragment;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ltz.ZhihuDaily.GlobalContants.AppConfig;
import com.ltz.ZhihuDaily.NetInterface.RecommendInterface;
import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.adapter.RecommendAdapter;
import com.ltz.ZhihuDaily.base.BaseFragment;
import com.ltz.ZhihuDaily.bean.LatestInfo;
import com.ltz.ZhihuDaily.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页
 * Created by Qloop on 2016/11/2.
 */

public class RecommendFragment extends BaseFragment {


    @BindView(R.id.rv_recommend)
    RecyclerView mRecommendRecyclerView;
    @BindView(R.id.srl_recommend)
    SwipeRefreshLayout mRefreshView;
    ViewPager vpHeader;
    TextView tvTitle;
    private Unbinder unBinder;
    private Unbinder unBinderHeard;
    private boolean isFreshing = true;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        View heardView = View.inflate(mActivity, R.layout.header_recyclerview, null);
        unBinder = ButterKnife.bind(this, view);
//        unBinderHeard = ButterKnife.bind(this, heardView);
        //butterknife不能绑定多个布局
        vpHeader = (ViewPager) heardView.findViewById(R.id.vp_header);
        tvTitle = (TextView) heardView.findViewById(R.id.tv_title);

        //设置首页展示列表RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecommendRecyclerView.setLayoutManager(linearLayoutManager);
        mRecommendRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRefreshView.setColorSchemeColors(new int[]{Color.parseColor("#56abe4")});
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFreshing = true;
                initData();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5000, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RecommendInterface recommendInterface = retrofit.create(RecommendInterface.class);
        recommendInterface.getLatestInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LatestInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showToastShort(mActivity, "数据错误");
                    }

                    @Override
                    public void onNext(LatestInfo latestInfo) {
                        mRecommendRecyclerView.setAdapter(new RecommendAdapter(mActivity,
                                latestInfo.getStories(), latestInfo.getTopStories()));
                        mRefreshView.setRefreshing(false);
                    }
                });
    }

    @Override
    public void destroy() {
        unBinder.unbind();
    }
}
