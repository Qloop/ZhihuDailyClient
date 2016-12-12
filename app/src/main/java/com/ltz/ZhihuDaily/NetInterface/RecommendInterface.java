package com.ltz.ZhihuDaily.NetInterface;

import com.ltz.ZhihuDaily.bean.LatestInfo;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 首页推荐内容
 * Created by Qloop on 2016/11/21.
 */

public interface RecommendInterface {

    @GET("4/news/latest")
    Observable<LatestInfo> getLatestInfo();
}