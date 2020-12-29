package com.tuya.smart.android.demo.family.model;

import com.tuya.smart.home.sdk.callback.ITuyaGetMemberListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.IResultCallback;

public interface IFamilyInfoModel {

    void getHomeBean(long homeId, ITuyaHomeResultCallback callback);

    void getMemberList(long homeId,ITuyaGetMemberListCallback callback);

    void removeHome(long homeId, IResultCallback callback);

}
