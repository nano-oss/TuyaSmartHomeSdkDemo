package com.tuya.smart.android.demo.family.recyclerview.adapter;

import android.app.Activity;
import androidx.annotation.IntRange;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.utils.CollectionUtils;
import com.tuya.smart.android.demo.family.recyclerview.anntations.LoadMoreStatus;
import com.tuya.smart.android.demo.family.recyclerview.exception.RvAdapterException;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseEmpty;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseError;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseFoot;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseHead;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseItem;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseLoadMore;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseViewHolder;
import com.tuya.smart.android.demo.family.recyclerview.item.DefaultLoadMore;

import java.util.List;

public class BaseRVAdapter<I extends BaseItem> extends RecyclerView.Adapter<BaseViewHolder> {

    public static final String TAG = BaseRVAdapter.class.getSimpleName();

    private BaseEmpty mEmptyView;
    private BaseError mErrorView;
    private BaseLoadMore mLoadMoreView;
    private BaseLoadMore mPullLoadView;

    private boolean isShowEmptyView;
    private boolean isShowErrorView;

    private @LoadMoreStatus
    int mLoadMoreStatus;
    private boolean isOpenLoadMoreView;

    private boolean isShowPullLoadView;
    private boolean isOpenPullLoadView;

    private int mLastVisiblePosition = -1;
    private int mFirstVisiblePosition = -1;

    private SectionList<I> mSectionList = new SectionList<>();

    private OnItemViewClickListener<I> mOnItemViewClickListener;
    private onItemViewLongClickListener<I> mOnItemViewLongClickListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private onPullLoadListener mOnPullLoadListener;

    public BaseRVAdapter() {
    }

    public BaseRVAdapter(@NonNull List<I> itemList) {
        if (itemList.isEmpty()) {
            return;
        }
        this.mSectionList.setItemViewList(SectionList.SECTION_KEY_DEFAULT, itemList);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "viewType: " + viewType);

        // State View
        if (null != mEmptyView && mEmptyView.getViewType() == viewType) {
            Log.d(TAG, "This viewType is Empty view, so create to the EmptyView.");
            return mEmptyView.onCreateViewHolder(parent, viewType);
        }
        if (null != mErrorView && mErrorView.getViewType() == viewType) {
            Log.d(TAG, "This viewType is Error view, so create to the ErrorView.");
            return mErrorView.onCreateViewHolder(parent, viewType);
        }
        if (null != mLoadMoreView && mLoadMoreView.getViewType() == viewType) {
            Log.d(TAG, "This viewType is Load more view, so create to the LoadMoreView.");
            return mLoadMoreView.onCreateViewHolder(parent, viewType);
        }
        if (null != mPullLoadView && mPullLoadView.getViewType() == viewType) {
            Log.d(TAG, "This viewType is Drop more view, so create to the DropLoadView.");
            return mPullLoadView.onCreateViewHolder(parent, viewType);
        }
        // Header & Foot
        if (this.mSectionList.isHeadViewViewType(viewType)) {
            Log.d(TAG, "This viewType is Header view, so create to the HeaderView.");
            return this.mSectionList.getHeadViewByViewType(viewType).onCreateViewHolder(parent, viewType);
        }
        if (this.mSectionList.isFootViewViewType(viewType)) {
            Log.d(TAG, "This viewType is Foot view, so create to the FootView.");
            return this.mSectionList.getFootViewByViewType(viewType).onCreateViewHolder(parent, viewType);
        }

        Log.d(TAG, "This viewType is item view, so create to the itemView.");
        I item = this.mSectionList.getItemViewByViewType(viewType);
        if (null == item) {
            throw new RvAdapterException("error viewType");
        }

        final BaseViewHolder viewHolder = item.onCreateViewHolder(parent, viewType);

        // Click
        viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mOnItemViewClickListener) {
                    return;
                }

                int realPosition = viewHolder.getAdapterPosition() - getPullLoadCount();
                mOnItemViewClickListener.onItemViewClick(
                        mSectionList.getItemView(realPosition),
                        mSectionList.getSectionKey(realPosition),
                        mSectionList.getSectionItemPosition(realPosition));
            }
        });

        // LongClick
        viewHolder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null == mOnItemViewLongClickListener) {
                    return false;
                }

                int realPosition = viewHolder.getAdapterPosition() - getPullLoadCount();
                return mOnItemViewLongClickListener.onItemViewLongClick(
                        mSectionList.getItemView(realPosition),
                        mSectionList.getSectionKey(realPosition),
                        mSectionList.getSectionItemPosition(realPosition));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Log.d(TAG, "holderType: " + holder.getItemViewType() + " ; position: " + position);

        if (this.isShowEmptyView) {
            Log.d(TAG, "This is a EmptyView.");
            mEmptyView.onBindViewHolder(holder, SectionList.SECTION_KEY_DEFAULT, position);
            return;
        }

        if (this.isShowErrorView) {
            Log.d(TAG, "This is a ErrorView.");
            mErrorView.onBindViewHolder(holder, SectionList.SECTION_KEY_DEFAULT, position);
            return;
        }

        if (isDropLoadPosition(position)) {
            Log.d(TAG, "This is a DropLoadView.");
            mPullLoadView.onBindViewHolder(holder, SectionList.SECTION_KEY_DEFAULT, position);
            return;
        }

        if (isLoadMoreViewPosition(position)) {
            Log.d(TAG, "This is a LoadMoreView.");
            mLoadMoreView.onBindViewHolder(holder, SectionList.SECTION_KEY_DEFAULT, position);
            return;
        }

        int realPosition = position - getPullLoadCount();
        if (this.mSectionList.isHeadViewPosition(realPosition)) {
            Log.d(TAG, "This is a HeaderView.");

            if (null != this.mSectionList.getHeadView(realPosition)) {
                this.mSectionList.getHeadView(realPosition).onBindViewHolder(holder,
                        this.mSectionList.getSectionKey(realPosition),
                        this.mSectionList.getSectionHeadPosition(realPosition));
            }

            return;
        }

        if (this.mSectionList.isFootViewPosition(realPosition)) {
            Log.d(TAG, "This is a FooterView.");

            if (null != this.mSectionList.getFootView(realPosition)) {
                this.mSectionList.getFootView(realPosition).onBindViewHolder(holder,
                        this.mSectionList.getSectionKey(realPosition),
                        this.mSectionList.getSectionFootPosition(realPosition));
            }

            return;
        }

        Log.d(TAG, "This is a ItemView.");
        this.mSectionList.getItemView(realPosition).onBindViewHolder(holder,
                this.mSectionList.getSectionKey(realPosition),
                this.mSectionList.getSectionItemPosition(realPosition));
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "position: " + position);

        if (this.isShowEmptyView) {
            Log.d(TAG, "This is a EmptyView.");
            return mEmptyView.getViewType();
        }

        if (this.isShowErrorView) {
            Log.d(TAG, "This is a ErrorView.");
            return mErrorView.getViewType();
        }

        if (isDropLoadPosition(position)) {
            Log.d(TAG, "This is a DropLoadView.");
            return mPullLoadView.getViewType();
        }

        if (isLoadMoreViewPosition(position)) {
            Log.d(TAG, "This is a LoadMoreView.");
            return mLoadMoreView.getViewType();
        }

        int realPosition = position - getPullLoadCount();
        if (this.mSectionList.isHeadViewPosition(realPosition)) {
            Log.d(TAG, "This is a HeaderView.");
            return this.mSectionList.getViewType(realPosition);
        }

        if (this.mSectionList.isFootViewPosition(realPosition)) {
            Log.d(TAG, "This is a FooterView.");
            return this.mSectionList.getViewType(realPosition);
        }

        Log.d(TAG, "This is a ItemView.");
        return this.mSectionList.getViewType(realPosition);
    }

    @Override
    public int getItemCount() {
        int itemCount = (this.isShowEmptyView || this.isShowErrorView) ?
                1 : this.mSectionList.getCount() + getLoadMoreCount() + getPullLoadCount();
        Log.d(TAG, "The itemCount: " + itemCount);
        return itemCount;
    }


    private RecyclerView mRecyclerView;

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG, "onAttachedToRecyclerView");
        mRecyclerView = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    mLastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    mFirstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                } else if (layoutManager instanceof GridLayoutManager) {
                    mLastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    mFirstVisiblePosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {

                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];

                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    mLastVisiblePosition = findMax(lastPositions);

                    staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions);
                    mFirstVisiblePosition = findMin(lastPositions);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "newState: " + newState);

                View firstView = recyclerView.getChildAt(0);
                if (null == firstView) {
                    return;
                }

                if (null == recyclerView.getContext()) {
                    Log.e(TAG, "recyclerView getContext is null onScrollStateChanged do nothing");
                    return;
                }

                if (recyclerView.getContext() instanceof Activity
                        && ((Activity) recyclerView.getContext()).isFinishing()) {
                    Log.e(TAG, "recyclerView.getContext is activity and is finishing onScrollStateChanged do nothing");
                    return;
                }

                //判断RecyclerView 的ItemView是否满屏，如果不满一屏，上拉不会触发加载更多
                boolean isFullScreen = firstView.getTop() - firstView.getPaddingTop() < recyclerView.getPaddingTop();
//                Log.d("FirstView.getTop: " + firstView.getTop() +
//                        "FirstView.getPaddingTop:" + firstView.getPaddingTop() +
//                        " RecyclerView.getPaddingTop: " + recyclerView.getPaddingTop() +
//                        " ;isFullScreen: " + isFullScreen);

                int itemCount = recyclerView.getLayoutManager().getItemCount();
                //因为LoadMore View  是Adapter的一个Item,显示LoadMore 的时候，Item数量＋1了，导致 mLastVisiblePosition == itemCount-1
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisiblePosition == itemCount - 1
                        && isFullScreen && isOpenLoadMoreView
                        && mLoadMoreStatus == LoadMoreStatus.IDLE
                        && null != mLoadMoreView
                        && null != mLoadMoreView.getHolder()) {

                    mLoadMoreStatus = LoadMoreStatus.LOADING;
                    mLoadMoreView.getHolder().setVisibility(View.VISIBLE);
                    mLoadMoreView.startAnim();
                    if (null != mOnLoadMoreListener) {
                        mOnLoadMoreListener.onLoading();
                    }
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mFirstVisiblePosition == 0
                        && isOpenPullLoadView
                        && !isShowPullLoadView
                        && null != mPullLoadView
                        && null != mPullLoadView.getHolder()) {

                    isShowPullLoadView = true;
                    mPullLoadView.getHolder().setVisibility(View.VISIBLE);
                    mPullLoadView.startAnim();

                    if (null != mOnPullLoadListener) {
                                mOnPullLoadListener.onLoading();
                    }
                }
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        // 释放资源
        int realPosition = holder.getAdapterPosition() - getPullLoadCount();
        Log.d(TAG,"ViewType: " + holder.getItemViewType() + " Position: " + realPosition);

        //越界检查
        if (realPosition < 0 || realPosition >= this.mSectionList.getCount() ||
                null == this.mSectionList.getItemView(realPosition)) {
            Log.d(TAG,"The position is invalid or can't find the item.");
            return;
        }

        this.mSectionList.getItemView(realPosition).releaseResource();
    }

    public void showEmptyView() {
        showEmptyView(null);
    }

    public void showEmptyView(BaseEmpty emptyView) {
        Log.i(TAG,"Start to show the empty view.");
        resetState();
        this.isShowEmptyView = true;
        this.mEmptyView = null != emptyView ? emptyView : new BaseEmpty<String>("") {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.recycler_common_empty;
            }

            @Override
            public void onSetViewsData(BaseViewHolder holder) {
            }
        };

        Log.d(TAG,"The empty view: " + this.mEmptyView.toString());
        clearAllSectionItemViews();
    }

    public void hideEmptyView() {
        this.isShowEmptyView = false;
        notifyDataSetChanged();
    }

    public void showErrorView() {
        showErrorView(null);
    }

    public void showErrorView(BaseError errorView) {
        Log.i(TAG,"Start to show the empty view.");

        resetState();
        this.isShowErrorView = true;
        this.mErrorView = null != errorView ? errorView : new BaseError<String>("") {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.recycler_common_error;
            }

            @Override
            public void onSetViewsData(BaseViewHolder holder) {
            }
        };

        Log.d(TAG,"The error view: " + this.mErrorView.toString());
        clearAllSectionItemViews();
    }

    public void hideErrorView() {
        this.isShowErrorView = false;
        notifyDataSetChanged();
    }

    /**
     * 打开 Adapter 的 LoadMore 功能，可在 adapter 初始化时，或 有更多数据时 打开
     */
    public void openLoadMore() {
        openLoadMore(null);
    }


    /**
     * 打开 Adapter 的 LoadMore 功能，可在 adapter 初始化时，或 有更多数据时 打开
     */
    public void openLoadMore(BaseLoadMore loadMoreView) {
        if (this.isOpenLoadMoreView) {
            Log.d(TAG,"This adapter has been opened load more.");
            return;
        }

        this.isOpenLoadMoreView = true;
        this.mLoadMoreView = (null != loadMoreView ? loadMoreView : new DefaultLoadMore(DefaultLoadMore.LOAD_MORE_TYPE));

        if (this.mSectionList.getCount() > 0) {
            Log.d(TAG,"Show load more view, so Notify the load more view has inserted");
            notifyItemInserted(getItemCount() - 1);
        }
    }


    /**
     * 关闭 Adapter 的 LoadMore 功能，在 无更多数据时，可调用来关闭 此功能
     */
    public void closeLoadMore() {
        Log.i(TAG,"Close the load more view.");
        hideLoadMoreView();
        if (this.isOpenLoadMoreView) {
            this.isOpenLoadMoreView = false;
            Log.d(TAG,"Notify the load more view has removed.");
            notifyItemRemoved(getItemCount() - 1);
        }

        this.isOpenLoadMoreView = false;
        this.mLoadMoreView = null;
    }

    /**
     * 关闭 Adapter 的 LoadMore 功能，在 无更多数据时，可调用来关闭 此功能
     */
    public void endLoadMore() {
        if (!this.isOpenLoadMoreView
                || null == this.mLoadMoreView
                || null == this.mLoadMoreView.getHolder()
                || null == mRecyclerView) {
            return;
        }
        this.mLoadMoreStatus = LoadMoreStatus.END;
        this.mLoadMoreView.getHolder().setVisibility(View.VISIBLE);
        this.mLoadMoreView.end();
    }

    /**
     * 隐藏 LoadMoreView 视图，在 数据加载完成后，调用
     */
    public void hideLoadMoreView() {
        Log.i(TAG,"Hide the load more view.");

        this.mLoadMoreStatus = LoadMoreStatus.IDLE;
        if (null != this.mLoadMoreView && null != this.mLoadMoreView.getHolder()) {
            this.mLoadMoreView.getHolder().setVisibility(View.GONE);
            this.mLoadMoreView.stopAnim();
        }
    }

    public void openPullLoad() {
        openPullLoad(null);
    }

    public void openPullLoad(BaseLoadMore pullLoadView) {
        if (this.isOpenPullLoadView) {
            Log.d(TAG,"This adapter has been opened Drop more.");
            return;
        }

        this.isOpenPullLoadView = true;
        this.mPullLoadView = (null != pullLoadView ? pullLoadView : new DefaultLoadMore(DefaultLoadMore.PULL_LOAD_TYPE));

        Log.d(TAG,"Show Drop more view, so Notify the Drop more view has inserted");
        notifyItemInserted(0);
    }

    public void hidePullLoadView() {
        Log.i(TAG,"Hide the Drop Load view.");

        this.isShowPullLoadView = false;
        if (null != this.mPullLoadView && null != this.mPullLoadView.getHolder()) {
            this.mPullLoadView.getHolder().setVisibility(View.GONE);
            this.mPullLoadView.stopAnim();
        }
    }

    public void closePullLoad() {
        Log.i(TAG,"Close the Drop Load view.");
        hidePullLoadView();
        if (this.isOpenPullLoadView) {
            Log.d(TAG,"Notify the load more view has removed.");
            notifyItemRemoved(0);
        }

        this.isOpenPullLoadView = false;
        this.mPullLoadView = null;
    }


    @MainThread
    public void addHeadView(@NonNull BaseHead headView) {
        addHeadView(SectionList.SECTION_KEY_DEFAULT, headView);
    }

    @MainThread
    public void addHeadView(@IntRange(from = 0) int sectionKey, @NonNull BaseHead headView) {
        Log.i(TAG,"Start to add the headView. sectionKey: " + sectionKey + " ;headView: " + headView.toString());

        int position = this.mSectionList.addHeadView(sectionKey, headView);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.i(TAG,"Notify all item has inserted. position: " + position);
        notifyItemInserted(position);
    }

    @MainThread
    public void updateHeadView(@NonNull BaseHead headView) {
        updateHeadView(SectionList.SECTION_KEY_DEFAULT, headView);
    }

    @MainThread
    public void updateHeadView(@IntRange(from = 0) int sectionKey, @NonNull BaseHead headView) {
        Log.d(TAG,"Start to update the headView. sectionKey: " + sectionKey + " ;headView: " + headView.toString());

        int position = this.mSectionList.updateHeadView(sectionKey, headView);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify all item has changed. position: " + position);
        notifyItemChanged(position);
    }

    @MainThread
    public void removeHeadView(@NonNull BaseHead view) {
        removeHeadView(SectionList.SECTION_KEY_DEFAULT, view);
    }

    @MainThread
    public void removeHeadView(@IntRange(from = 0) int sectionKey, @NonNull BaseHead headView) {
        Log.d(TAG,"Start to remove the headView. sectionKey: " + sectionKey + " ;headView: " + headView.toString());

        int position = this.mSectionList.removeHeadView(sectionKey, headView);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify all item has removed. position: " + position);
        notifyItemRemoved(position);
    }

    public BaseHead getHeadView(@IntRange(from = 0) int headPosition) {
        return getHeadView(SectionList.SECTION_KEY_DEFAULT, headPosition);
    }

    public BaseHead getHeadView(@IntRange(from = 0) int sectionKey, @IntRange(from = 0) int headPosition) {
        return this.mSectionList.getHeadView(sectionKey, headPosition);
    }

    @MainThread
    public void clearAllHeadView(@IntRange(from = 0) int sectionKey) {
        int headCount = this.mSectionList.getHeadViewList(sectionKey).size();
        if (headCount < 1) {
            Log.w(TAG,"The head is empty, so can't to clear.");
            return;
        }
        int position = this.mSectionList.clearAllHeadView(sectionKey);

        notifyItemRangeRemoved(position, headCount);
    }

    @MainThread
    public void clearAllSectionHeadView() {
        Log.d(TAG,"Start to clear all item views.");
        this.mSectionList.clearAllSectionHeadView();
        Log.d(TAG,"Notify all data changed.");
        notifyDataSetChanged();
    }

    @MainThread
    public void addFootView(@NonNull BaseFoot view) {
        addFootView(SectionList.SECTION_KEY_DEFAULT, view);
    }

    @MainThread
    public void addFootView(@IntRange(from = 0) int sectionKey, @NonNull BaseFoot footView) {
        Log.d(TAG,"Start to add the footView. sectionKey: " + sectionKey + " ;footView: " + footView.toString());

        int position = this.mSectionList.addFootView(sectionKey, footView);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify all item has inserted. position: " + position);
        notifyItemInserted(position);
    }

    @MainThread
    public void updateFootView(@NonNull BaseFoot footView) {
        updateFootView(SectionList.SECTION_KEY_DEFAULT, footView);
    }

    @MainThread
    public void updateFootView(@IntRange(from = 0) int sectionKey, @NonNull BaseFoot footView) {
        Log.d(TAG,"Start to update the footView. sectionKey: " + sectionKey + " ;footView: " + footView.toString());

        int position = this.mSectionList.updateFootView(sectionKey, footView);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify all item has changed. position: " + position);
        notifyItemChanged(position);
    }

    @MainThread
    public void removeFootView(@NonNull BaseFoot view) {
        removeFootView(SectionList.SECTION_KEY_DEFAULT, view);
    }

    @MainThread
    public void removeFootView(@IntRange(from = 0) int sectionKey, @NonNull BaseFoot footView) {
        Log.d(TAG,"Start to remove the footView. sectionKey: " + sectionKey + " ;footView: " + footView.toString());

        int position = this.mSectionList.removeFootView(sectionKey, footView);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify all item has removed. position: " + position);
        notifyItemRemoved(position);
    }

    public BaseFoot getFootView(@IntRange(from = 0) int footPosition) {
        return getFootView(SectionList.SECTION_KEY_DEFAULT, footPosition);
    }

    public BaseFoot getFootView(@IntRange(from = 0) int sectionKey, @IntRange(from = 0) int footPosition) {
        return this.mSectionList.getFootView(sectionKey, footPosition);
    }

    @MainThread
    public void setItemViewList(@NonNull List<I> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            Log.e(TAG,"The item list is empty, so can't set.");
            return;
        }

        setItemViewList(SectionList.SECTION_KEY_DEFAULT, itemList);
    }

    @MainThread
    public void setItemViewList(@IntRange(from = 0) int sectionKey, @NonNull List<I> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            Log.e(TAG,"The item list is empty, so can't set.");
            return;
        }

        resetState();
        int position = this.mSectionList.setItemViewList(sectionKey, itemList);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify all item has changed. position: " + position + " ;itemList size: " + itemList.size());
        notifyDataSetChanged();
    }

    public List<I> getItemList() {
        return getItemList(SectionList.SECTION_KEY_DEFAULT);
    }

    public List<I> getItemList(@IntRange(from = 0) int sectionKey) {
        return this.mSectionList.getItemViewList(sectionKey);
    }

    public I getItemView(int position) {
        return this.mSectionList.getItemView(position);
    }

    public I getItemView(@IntRange(from = 0) int sectionKey, int sectionPosition) {
        return this.mSectionList.getItemViewBySection(sectionKey, sectionPosition);
    }

    @MainThread
    public void addItemView(@NonNull I item) {
        addItemView(SectionList.SECTION_KEY_DEFAULT, item);
    }

    @MainThread
    public void addItemView(@IntRange(from = 0) int sectionKey, @NonNull I item) {
        int position = this.mSectionList.addItemView(sectionKey, item);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify some item has inserted. position: " + position);
        if (this.mSectionList.getCount() <= 1) {
            resetState();
            notifyDataSetChanged();
        } else {
            notifyItemChanged(position);
        }
    }

    @MainThread
    public void addItemViewList(@NonNull List<I> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            Log.e(TAG,"The item list is empty, so can't add.");
            return;
        }

        addItemViewList(SectionList.SECTION_KEY_DEFAULT, itemList);
    }

    @MainThread
    public void addItemViewList(@IntRange(from = 0) int sectionKey, @NonNull List<I> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            Log.e(TAG,"The item list is empty, so can't add.");
            return;
        }

        int position = this.mSectionList.addItemViewList(sectionKey, itemList);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify some item has inserted. position: " + position + " ;itemList size: " + itemList.size());
        if (this.mSectionList.getCount() == 1) {
            resetState();
            notifyDataSetChanged();
        } else {
            int realPosition = position + getLoadMoreCount() + getPullLoadCount();
            notifyItemRangeInserted(realPosition, itemList.size());
        }
    }

    @MainThread
    public void insertItemView(@IntRange(from = 0) int index, @NonNull I item) {
        insertItemView(SectionList.SECTION_KEY_DEFAULT, index, item);
    }

    @MainThread
    public void insertItemView(@IntRange(from = 0) int sectionKey, @IntRange(from = 0) int index, @NonNull I item) {
        int position = this.mSectionList.insertItemView(sectionKey, index, item);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify the item has inserted. position: " + position);
        if (this.mSectionList.getCount() == 1) {
            resetState();
            notifyDataSetChanged();
        } else {
            int realPosition = position + getPullLoadCount();
            notifyItemInserted(realPosition);
            notifyItemRangeChanged(0, this.mSectionList.getCount());
        }
    }

    @MainThread
    public void insertItemViewList(@IntRange(from = 0) int index, @NonNull List<I> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            Log.e(TAG,"The item list is empty, so can't insert.");
            return;
        }

        insertItemViewList(SectionList.SECTION_KEY_DEFAULT, index, itemList);
    }

    @MainThread
    public void insertItemViewList(@IntRange(from = 0) int sectionKey, @IntRange(from = 0) int index, @NonNull List<I> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            Log.e(TAG,"The item list is empty, so can't insert.");
            return;
        }

        int position = this.mSectionList.insertItemViewList(sectionKey, index, itemList);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify some item has inserted. position: " + position + " ;itemList size: " + itemList.size());
        if (this.mSectionList.getCount() == 1) {
            resetState();
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(position, itemList.size());
            notifyItemRangeChanged(0, this.mSectionList.getCount());
        }
    }

    @MainThread
    public void updateItemView(@NonNull I item) {
        updateItemView(SectionList.SECTION_KEY_DEFAULT, item);
    }

    @MainThread
    public void updateItemView(@IntRange(from = 0) int sectionKey, @NonNull I item) {
        int position = this.mSectionList.updateItemView(sectionKey, item);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify the item has changed. position: " + position);
        notifyItemChanged(position);
    }

    @MainThread
    public void removeItemView(@NonNull I item) {
        removeItemView(SectionList.SECTION_KEY_DEFAULT, item);
    }

    @MainThread
    public void removeItemView(@IntRange(from = 0) int sectionKey, @NonNull I item) {
        int position = this.mSectionList.removeItemView(sectionKey, item);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify the item has removed. position: " + position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, this.mSectionList.getCount());
    }

    @MainThread
    public void removeItemView(@IntRange(from = 0) int index) {
        removeItemView(SectionList.SECTION_KEY_DEFAULT, index);
    }

    @MainThread
    public void removeItemView(@IntRange(from = 0) int sectionKey, @IntRange(from = 0) int index) {
        int position = this.mSectionList.removeItemView(sectionKey, index);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify the item has changed. position: " + position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, this.mSectionList.getCount());
    }

    @MainThread
    public void clearAllItemView() {
        clearAllItemView(SectionList.SECTION_KEY_DEFAULT);
    }

    @MainThread
    public void clearAllItemView(@IntRange(from = 0) int sectionKey) {
        Log.d(TAG,"Clear the item list.");

        int position = this.mSectionList.clearAllItemView(sectionKey);
        if (position < 0) {
            Log.w(TAG,"The position is invalid.");
            return;
        }

        Log.d(TAG,"Notify all data changed.");
        notifyDataSetChanged();
    }

    @MainThread
    public void clearAllSectionItemViews() {
        Log.d(TAG,"Start to clear all item views.");
        this.mSectionList.clearAllSectionItemViews();
        notifyDataSetChanged();
    }

    @MainThread
    public void clearAllSectionViews() {
        Log.d(TAG,"Start to clear all item views.");
        this.clearAllSectionItemViews();
        this.clearAllSectionHeadView();
        notifyDataSetChanged();
    }

    public void setOnItemViewClickListener(@NonNull OnItemViewClickListener<I> listener) {
        this.mOnItemViewClickListener = listener;
    }

    public void setOnItemViewLongClickListener(@NonNull onItemViewLongClickListener<I> listener) {
        this.mOnItemViewLongClickListener = listener;
    }

    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public void setOnPullLoadListener(@NonNull onPullLoadListener listener) {
        this.mOnPullLoadListener = listener;
    }

    private int getLoadMoreCount() {
        return isOpenLoadMoreView ? 1 : 0;
    }

    private int getPullLoadCount() {
        return isOpenPullLoadView ? 1 : 0;
    }

    private void resetState() {
        Log.d(TAG,"Reset the adapter state.");

        this.isShowEmptyView = false;
        this.isShowErrorView = false;

        this.mLoadMoreStatus = LoadMoreStatus.IDLE;
        if (null != this.mLoadMoreView && null != this.mLoadMoreView.getHolder()) {
            Log.d(TAG,"Hide the load more view.");
            this.mLoadMoreView.getHolder().setVisibility(View.GONE);
            this.mLoadMoreView.stopAnim();
        }
    }

    private boolean isLoadMoreViewPosition(int position) {
        return position >= mSectionList.getCount() && isOpenLoadMoreView;
    }

    private boolean isDropLoadPosition(int position) {
        return position == 0 && isOpenPullLoadView;
    }

    // TODO
    public boolean isNormalState() {
        return (!isShowEmptyView || !isShowErrorView);
    }

    public I getItemByRealPosition(int realPosition) {
        return mSectionList.getItemView(realPosition);
    }

    /**
     * 获取组数最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * 获取组数最大值
     */
    private int findMin(int[] lastPositions) {
        int min = lastPositions[0];
        for (int value : lastPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }


    public interface OnItemViewClickListener<I> {
        void onItemViewClick(I item, int sectionKey, int sectionItemPosition);
    }

    public interface onItemViewLongClickListener<I> {
        boolean onItemViewLongClick(I item, int sectionKey, int sectionItemPosition);
    }


    public interface OnLoadMoreListener {
        void onLoading();
    }

    public interface onPullLoadListener {
        void onLoading();
    }

}
