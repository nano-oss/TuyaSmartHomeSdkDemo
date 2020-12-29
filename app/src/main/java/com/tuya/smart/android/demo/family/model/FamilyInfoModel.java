package com.tuya.smart.android.demo.family.model;

import android.content.Context;

import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.callback.ITuyaGetMemberListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.IResultCallback;

public class FamilyInfoModel extends BaseModel implements IFamilyInfoModel {

    public FamilyInfoModel(Context ctx) {
        super(ctx);
    }

    @Override
    public void getHomeBean(long homeId, ITuyaHomeResultCallback callback) {
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(callback);
    }

    @Override
    public void getMemberList(long homeId, ITuyaGetMemberListCallback callback) {
        TuyaHomeSdk.getMemberInstance().queryMemberList(homeId, callback);
    }

    @Override
    public void removeHome(long homeId, IResultCallback callback) {
        TuyaHomeSdk.newHomeInstance(homeId).dismissHome(callback);
    }


    @Override
    public void onDestroy() {

    }
}
