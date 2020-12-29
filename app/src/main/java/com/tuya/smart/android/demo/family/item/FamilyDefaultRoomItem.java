package com.tuya.smart.android.demo.family.item;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseItem;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseViewHolder;

import butterknife.BindView;

public class FamilyDefaultRoomItem extends BaseItem<String> {

    @BindView(R.id.recycler_family_default_checkbox)
    CheckBox checkBox;
    @BindView(R.id.recycler_family_default_text)
    TextView familyNameTxt;

    private boolean check = true;


    public FamilyDefaultRoomItem(String data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return 1;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycler_family_default_item;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        familyNameTxt.setText("");
    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        familyNameTxt.setText(getData());
        checkBox.setChecked(check);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check = isChecked;
            }
        });
    }

    public boolean isCheck() {
        return check;
    }
}
