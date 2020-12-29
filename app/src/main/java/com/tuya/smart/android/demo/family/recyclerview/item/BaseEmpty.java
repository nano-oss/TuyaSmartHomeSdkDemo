package com.tuya.smart.android.demo.family.recyclerview.item;

import java.util.Random;

public abstract class BaseEmpty<D> extends BaseItem<D> {

    private static final int BASE_TYPE_EMPTY = 3100000 + new Random().nextInt(1000);

    public BaseEmpty(D data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return BASE_TYPE_EMPTY;
    }

    @Override
    public final void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {}

    @Override
    public final void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        onSetViewsData(holder);
    }

    public abstract void onSetViewsData(BaseViewHolder holder);

}
