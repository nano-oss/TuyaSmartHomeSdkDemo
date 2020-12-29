package com.tuya.smart.android.demo.family.activity;

import android.content.Context;

import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.bean.MemberBean;

import java.util.List;

public interface IFamilyInfoView {

    Context getContext();

    long getHomeId();

    void setHomeData(HomeBean homeBean);

    void setMemberData(List<MemberBean> memberList);

    void doRemoveView(boolean isSuccess);

    void showToast(int res);
}
