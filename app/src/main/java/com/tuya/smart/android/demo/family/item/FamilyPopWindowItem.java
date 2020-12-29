package com.tuya.smart.android.demo.family.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.family.FamilyManager;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseItem;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseViewHolder;
import com.tuya.smart.home.sdk.bean.HomeBean;

import butterknife.BindView;

public class FamilyPopWindowItem extends BaseItem<HomeBean> {

    @BindView(R.id.family_popwindow_item_name_txt)
    TextView nameTxt;
    @BindView(R.id.family_popwindow_item_check_iv)
    ImageView checkIv;

    public FamilyPopWindowItem(HomeBean data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_family_popwindow_item;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        checkIv.setVisibility(FamilyManager.getInstance().getCurrentHomeId() == getData().getHomeId()
                ? View.VISIBLE
                : View.INVISIBLE);
        nameTxt.setText(getData().getName());
    }
}
