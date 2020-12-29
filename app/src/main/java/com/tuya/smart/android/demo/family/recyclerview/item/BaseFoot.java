package com.tuya.smart.android.demo.family.recyclerview.item;


import java.util.Random;

public abstract class BaseFoot<D> extends BaseItem<D> {

    public static final String TAG=BaseFoot.class.getSimpleName();

    private static int BASE_VIEW_TYPE_FOOT = 2000000 + new Random().nextInt(10000);

    public BaseFoot(D data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return BASE_VIEW_TYPE_FOOT;
    }

    /**
     * 重置 Views 数据，避免重用时 数据冲突
     */
    @Override
    public abstract void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionFootPosition);

    /**
     * 设置 Views 数据
     */
    @Override
    public abstract void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionFootPosition);

}
