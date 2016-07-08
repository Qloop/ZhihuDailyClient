package com.ltz.ZhihuDaily.NetInterface;

import com.ltz.ZhihuDaily.bean.SplashInfo;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 启动页网络数据
 * Created by Qloop on 2016/7/8.
 */
public interface SplashInterface {

    @GET("4/start-image/1080*1776")
    Observable<SplashInfo> getSplashInfo();
}
