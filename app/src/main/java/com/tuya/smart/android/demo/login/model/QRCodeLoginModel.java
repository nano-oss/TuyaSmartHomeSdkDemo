package com.tuya.smart.android.demo.login.model;

import android.content.Context;
import android.text.TextUtils;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.android.user.api.IBooleanCallback;
import com.tuya.smart.android.user.api.IGetQRCodeTokenCallback;
import com.tuya.smart.android.user.api.IGetQRDeviceInfoCallBack;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.QRDeviceInfoBean;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

/**
 * @author xushun
 * @Des:
 * @data 2019/4/5.
 */
public class QRCodeLoginModel extends BaseModel implements IQRCodeModel {

    public static final int GET_TOKEN_SUCCESS = 0;
    public static final int GET_TOKEN_FAILED = 1;
    public static final int GET_LOOP_TOKEN_SUCCESS = 2;
    public static final int GET_LOOP_TOKEN_FAILED = 3;
    public static final int GET_QR_TOKEN_LOGIN_FAILED = 4;
    public static final int GET_QR_TOKEN_LOGIN_SUCCESS = 5;
    public static final int GET_QR_TOKEN_INFO_FAILED = 6;
    public static final int GET_QR_TOKEN_INFO_SUCCESS = 7;


    private static final String TAG = "QRCodeLoginModel";

    public QRCodeLoginModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
    }

    @Override
    public void getToken() {
        TuyaHomeSdk.getUserInstance().getQRCodeToken( "86", new IGetQRCodeTokenCallback() {
            @Override
            public void onSuccess(String token) {
                L.i(TAG, "getToken onSuccess");
                resultSuccess(GET_TOKEN_SUCCESS, token);
            }

            @Override
            public void onError(String code, String error) {
                L.i(TAG, "getToken onFailure: " + code + "   " + error);
                resultError(GET_TOKEN_FAILED, code, error);
            }
        });
    }

    @Override
    public void tokenUserGet(String token) {
        TuyaHomeSdk.getUserInstance().QRCodeLogin("86", token, new ILoginCallback() {
            @Override
            public void onSuccess(User user) {
                L.i(TAG, "tokenUserGet onSuccess");
                if (!TextUtils.isEmpty(user.getUid())) {
                    TuyaHomeSdk.getUserInstance().loginSuccess(user);
                    resultSuccess(GET_LOOP_TOKEN_SUCCESS, user);
                } else {
                    resultError(GET_LOOP_TOKEN_FAILED, "1001", "user token auth failed");
                }
            }

            @Override
            public void onError(String code, String error) {
                L.i(TAG, "tokenUserGet onFailure: " + code + "   " + error);
                resultError(GET_LOOP_TOKEN_FAILED, code, error);
            }
        });
    }

    @Override
    public void tokenLogin(long homeId, String token) {
        TuyaHomeSdk.getUserInstance().QRcodeAuth("86", homeId, token, new IBooleanCallback() {
            @Override
            public void onSuccess() {
                resultSuccess(GET_QR_TOKEN_LOGIN_SUCCESS, "");
            }

            @Override
            public void onError(String code, String error) {
                resultError(GET_QR_TOKEN_LOGIN_FAILED, code, error);

            }
        });
    }

    @Override
    public void getInfo(String token) {
        TuyaHomeSdk.getUserInstance().getQRDeviceAppInfo("86", token, new IGetQRDeviceInfoCallBack() {
            @Override
            public void onSuccess(QRDeviceInfoBean infoBean) {
                L.i(TAG, "success: " + infoBean.getApplicationName());
                resultSuccess(GET_QR_TOKEN_INFO_SUCCESS, infoBean.getApplicationName());
            }

            @Override
            public void onError(String code, String error) {
                L.i(TAG, "failure");
                resultError(GET_QR_TOKEN_INFO_FAILED, code, error);
            }
        });
    }


    @Override
    public void onDestroy() {
    }
}
