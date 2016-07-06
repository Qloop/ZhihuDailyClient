package com.ltz.ZhihuDaily.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Qloop on 2016/7/4.
 */
public abstract class BaseFragment extends Fragment {

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return initViews();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    public void initData(){};

    public abstract View initViews();

    @Override
    public void onDestroy() {
        super.onDestroy();
        destory();
    }

    /*强制子类实现  做销毁处理*/
    public abstract void destory();
}
