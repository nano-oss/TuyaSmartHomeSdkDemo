package com.tuya.smart.android.demo.family.item;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseItem;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseViewHolder;
import com.tuya.smart.home.sdk.bean.MemberBean;

import butterknife.BindView;

public class FamilyMemberItem extends BaseItem<MemberBean> {

    @BindView(R.id.family_member_head_iv)
    ImageView headIv;
    @BindView(R.id.family_member_name_txt)
    TextView nameTxt;
    @BindView(R.id.family_member_account_txt)
    TextView accountTxt;
    @BindView(R.id.family_member_admin_txt)
    TextView adminTxt;

    public static final int FAMILY_MEMBER_TYPE = 100;

    public FamilyMemberItem(MemberBean data) {
        super(data);
    }

    @Override
    public int getViewType() {
        return FAMILY_MEMBER_TYPE;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recyler_family_member;
    }

    @Override
    public void onReleaseViews(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {

    }

    @Override
    public void onSetViewsData(BaseViewHolder holder, int sectionKey, int sectionViewPosition) {
        nameTxt.setText(getData().getNickName());
        accountTxt.setText(getData().getAccount());

        String path = getData().getHeadPic();
        if (path != null && path.isEmpty()) {
            path = null;
        }
        Picasso.with(holder.getContext())
                .load(path)
                .centerCrop()
                .placeholder(R.drawable.ty_nolist_logo)
                .into(headIv);

        adminTxt.setText(getData().isAdmin()
                ? R.string.family_info_member_admin_txt
                : R.string.family_info_member_normal_txt);
    }
}
