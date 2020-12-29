package com.tuya.smart.android.demo.family.item;

import android.util.Pair;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseItem;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseViewHolder;

import butterknife.BindView;

public class FamilyInfoItem extends BaseItem<Pair<String,String>> {

    @BindView(R.id.recycler_family_info_name)
    TextView nameTxt;
    @BindView(R.id.recycler_family_info_value)
    TextView valueTxt;


    public FamilyInfoItem(Pair<String, String> data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_family_info;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        nameTxt.setText(getData().first);
        valueTxt.setText(getData().second);
    }
}
