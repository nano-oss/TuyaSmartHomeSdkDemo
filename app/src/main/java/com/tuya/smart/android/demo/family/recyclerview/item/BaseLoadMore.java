package com.tuya.smart.android.demo.family.recyclerview.item;

import android.view.View;


public abstract class BaseLoadMore<D> extends BaseItem<D> {

    private static final int BASE_TYPE_LOAD_MORE = 3300000;

    public BaseLoadMore(D data) {
        super(data);
    }

    @Override
    public int getViewType() {
        int viewType = BASE_TYPE_LOAD_MORE;
        return viewType;
    }

    @Override
    public final void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionItemPosition) {
        holder.getItemView().setVisibility(View.GONE);
    }

    @Override
    public final void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionItemPosition) {
        holder.getItemView().setVisibility(View.VISIBLE);
        onSetViewsData(holder);
    }

    public abstract void onSetViewsData(BaseViewHolder holder);

    public abstract void startAnim();

    public abstract void stopAnim();

    public abstract void end();
}
