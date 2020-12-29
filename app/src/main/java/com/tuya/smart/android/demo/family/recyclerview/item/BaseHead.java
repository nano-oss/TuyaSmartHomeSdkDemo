package com.tuya.smart.android.demo.family.recyclerview.item;

import java.util.Random;

public abstract class BaseHead<D> extends BaseItem<D> {

    private static final int BASE_VIEW_TYPE_HEAD = 1000000 + new Random().nextInt(10000);

    public BaseHead(D data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return BASE_VIEW_TYPE_HEAD;
    }

    /**
     * 重置 Views 数据，避免重用时 数据冲突
     */
    @Override
    public abstract void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionHeadPosition);

    /**
     * 设置 Views 数据
     */
    @Override
    public abstract void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionHeadPosition);

}
