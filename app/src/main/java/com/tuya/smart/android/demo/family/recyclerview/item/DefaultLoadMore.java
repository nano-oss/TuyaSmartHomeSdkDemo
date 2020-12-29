package com.tuya.smart.android.demo.family.recyclerview.item;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.family.recyclerview.anntations.LoadMoreStatus;

public class DefaultLoadMore extends BaseLoadMore<Integer> {

    public static final int PULL_LOAD_TYPE = 440000;

    public static final int LOAD_MORE_TYPE = 550000;

    private TextView mEndTextView;
    private TextView mLoadingView;

    private @LoadMoreStatus
    int mLoadMoreStatus;

    public DefaultLoadMore(Integer viewType) {
        super(viewType);
    }

    @Override
    public int getViewType() {
        return getData();
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_common_load_more;
    }

    @Override
    public void onSetViewsData(BaseViewHolder holder) {
        if (null == mLoadingView) {
            mLoadingView = ((ViewGroup) holder.getItemView()).findViewById(R.id.loading_view);
        }
        if (null == mEndTextView) {
            mEndTextView = ((ViewGroup) holder.getItemView()).findViewById(R.id.end_tv);
        }

        switch (mLoadMoreStatus) {
            case LoadMoreStatus.END:
                end();
                break;
            case LoadMoreStatus.IDLE:
                holder.getItemView().setVisibility(View.GONE);
                break;
            case LoadMoreStatus.LOADING:
                startAnim();
                break;
        }
    }

    /**
     * Loading 状态 动画转起来
     */
    public void startAnim() {
        if (null == mLoadingView) {
            Log.w(TAG,"The animator is empty, so do not start anim.");
            return;
        }
        mLoadMoreStatus = LoadMoreStatus.LOADING;
        mLoadingView.setVisibility(View.VISIBLE);
        mEndTextView.setVisibility(View.GONE);
    }


    /**
     * 并且状态为idle 隐藏视图
     */
    public void stopAnim() {
        if (null == mLoadingView) {
            return;
        }
        mLoadMoreStatus = LoadMoreStatus.IDLE;
        mLoadingView.setVisibility(View.GONE);
    }

    /**
     * 分页结束的情况
     */
    public void end() {
        if (null == mEndTextView) {
            return;
        }
        mLoadMoreStatus = LoadMoreStatus.END;
        mEndTextView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }
}
