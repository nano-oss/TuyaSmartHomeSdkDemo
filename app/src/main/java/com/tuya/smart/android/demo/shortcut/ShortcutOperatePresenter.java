package com.tuya.smart.android.demo.shortcut;

import android.content.Context;

import com.tuya.smart.android.demo.base.utils.ToastUtil;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDeviceListener;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.Map;

public class ShortcutOperatePresenter {
    private ITuyaDevice mTuyaDevice = null;
    private Context mContext;
    private IShortcutOperateView mView;

    public ShortcutOperatePresenter(Context context,DeviceBean deviceBean,IShortcutOperateView view) {
        mContext = context;
        mView = view;

        if(deviceBean.isWifiDevice()){
            mTuyaDevice = TuyaHomeSdk.newDeviceInstance(deviceBean.getDevId());
            mTuyaDevice.registerDeviceListener(new IDeviceListener() {
                @Override
                public void onDpUpdate(String devId, Map<String, Object> dpStr) {
                    mView.updateView();
                }

                @Override
                public void onRemoved(String devId) {

                }

                @Override
                public void onStatusChanged(String devId, boolean online) {

                }

                @Override
                public void onNetworkStatusChanged(String devId, boolean status) {

                }

                @Override
                public void onDevInfoUpdate(String devId) {

                }
            });
        }
    }

    public void operate(String dps){
        if(mTuyaDevice != null){
            mTuyaDevice.publishDps(dps, new IResultCallback() {
                @Override
                public void onError(String code, String error) {
                    ToastUtil.shortToast(mContext,"code " + code + " error " + error);
                }

                @Override
                public void onSuccess() {
                    ToastUtil.shortToast(mContext,"operate success!");

                }
            });
        }else {
            ToastUtil.shortToast(mContext,"device type not exits.");
        }
    }


}
