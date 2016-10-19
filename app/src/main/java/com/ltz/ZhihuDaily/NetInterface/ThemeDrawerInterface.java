package com.ltz.ZhihuDaily.NetInterface;

import com.ltz.ZhihuDaily.bean.ThemeListInfo;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 启动页网络数据
 * Created by Qloop on 2016/7/8.
 */
public interface ThemeDrawerInterface {

    @GET("4/themes")
    Observable<ThemeListInfo> getThemeListInfo();
}
