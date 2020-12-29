package com.tuya.smart.android.demo.family.model;

import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import java.util.List;

public interface IFamilyAddModel {
    void createHome(String homeName, List<String> roomList,ITuyaHomeResultCallback callback);
}
