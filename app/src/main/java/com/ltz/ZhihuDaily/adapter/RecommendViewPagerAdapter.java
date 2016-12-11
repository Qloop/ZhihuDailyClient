package com.ltz.ZhihuDaily.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.bean.LatestInfo;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

/**
 * 首页头部轮播适配器
 * Created by Qloop on 2016/12/11.
 */

public class RecommendViewPagerAdapter extends PagerAdapter {

    private List<LatestInfo.TopStoriesBean> mData;
    private Context mContext;
    private LinkedList<View> mViewCache;  //用于view复用

    public RecommendViewPagerAdapter(List<LatestInfo.TopStoriesBean> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mViewCache = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View rootView = null;
        if (mViewCache.isEmpty()) {
            rootView = View.inflate(mContext, R.layout.item_recommend_viewpager, null);

        } else {
            rootView = mViewCache.getFirst();
            mViewCache.removeFirst();
        }
        ImageView imageView = (ImageView) rootView.findViewById(R.id.iv_item_img);
        TextView textView = (TextView) rootView.findViewById(R.id.tv_item_title);
        Picasso.with(mContext)
                .load(mData.get(position).getImage())
                .into(imageView);
        container.addView(container);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if(object != null){
            mViewCache.addLast((View) object);
        }
    }
}
