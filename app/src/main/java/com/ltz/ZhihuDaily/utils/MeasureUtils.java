package com.ltz.ZhihuDaily.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Qloop on 2016/7/8.
 */
public class MeasureUtils {


    /**
     * 获取屏幕宽高
     * @param context
     * @return int[0] 宽  int[1]高
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }
}
