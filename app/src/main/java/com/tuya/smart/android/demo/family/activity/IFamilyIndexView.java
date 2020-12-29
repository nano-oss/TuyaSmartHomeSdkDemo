package com.tuya.smart.android.demo.family.activity;

import android.content.Context;

import com.tuya.smart.home.sdk.bean.HomeBean;

import java.util.List;

public interface IFamilyIndexView {

     Context getContext();

     void  setFamilyList(List<HomeBean> homeBeanList);

     void  showFamilyEmptyView();

}
