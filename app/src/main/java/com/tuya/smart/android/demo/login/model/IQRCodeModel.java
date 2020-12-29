package com.tuya.smart.android.demo.login.model;

/**
 * @author xushun
 * @Des:
 * @data 2019/4/5.
 */
public interface IQRCodeModel {
    void getToken();

    void tokenUserGet(String token);

    void tokenLogin(long homeId, String token);

    void getInfo(String token);

    void onDestroy();
}
