package com.ltz.ZhihuDaily.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltz.ZhihuDaily.R;
import com.ltz.ZhihuDaily.bean.LatestInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页推荐列表适配器
 * Created by Qloop on 2016/12/11.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_FOOTER = 2;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<LatestInfo.StoriesBean> mContentData;
    private List<LatestInfo.TopStoriesBean> mHeaderData;
    private int mHeaderCount = 1;
    private int mBottomCount;

    private interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public RecommendAdapter(Context mContext, List<LatestInfo.StoriesBean> mContentData, List<LatestInfo.TopStoriesBean> mHeaderData) {
        this.mContext = mContext;
        this.mContentData = mContentData;
        this.mHeaderData = mHeaderData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容长度
    public int getContentItemCount() {
        return mContentData.size();
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= (mHeaderCount + getContentItemCount());
    }


    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            //底部View
            return ITEM_TYPE_FOOTER;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    //内容 ViewHolder
    public static class RecommendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_item_recommend)
        CardView cvItem;
        @BindView(R.id.tv_item_title)
        TextView tvTitle;
        @BindView(R.id.iv_item_img)
        ImageView ivImg;

        public RecommendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vp_header)
        ViewPager heardViewPager;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //底部 ViewHolder
    public static class BottomViewHolder extends RecyclerView.ViewHolder {

        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.header_recyclerview, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return new RecommendViewHolder(mLayoutInflater.inflate(R.layout.item_recommend_recyclerview, parent, false));
        } else if (viewType == ITEM_TYPE_FOOTER) {
//            return new BottomViewHolder(mLayoutInflater.inflate(R.layout.rv_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderViewHolder) {
            if(mHeaderData != null){
                ((HeaderViewHolder) holder).heardViewPager.setAdapter(new RecommendViewPagerAdapter(mHeaderData,mContext));
            }
            //设置时间。。。

            //设置item点击事件
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        }
        else if (holder instanceof RecommendViewHolder) {
            LatestInfo.StoriesBean storiesBean = mContentData.get(position - mHeaderCount);
            ((RecommendViewHolder) holder).tvTitle.setText(storiesBean.getTitle());
            Picasso.with(mContext)
                    .load(storiesBean.getImages().get(0))
                    .into(((RecommendViewHolder) holder).ivImg);

            //设置item点击事件
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }

        }
//        else if (holder instanceof BottomViewHolder) {
//
//        }
    }

    @Override
    public int getItemCount() {
        return mHeaderCount + getContentItemCount() + mBottomCount;
    }

}
