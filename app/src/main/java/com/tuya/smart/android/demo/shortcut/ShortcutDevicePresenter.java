package com.tuya.smart.android.demo.shortcut;

import android.app.Activity;
import android.content.Intent;

import com.tuya.smart.android.base.event.NetWorkStatusEvent;
import com.tuya.smart.android.base.event.NetWorkStatusEventModel;
import com.tuya.smart.android.base.utils.PreferencesUtil;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.app.Constant;
import com.tuya.smart.android.demo.base.utils.ToastUtil;
import com.tuya.smart.android.demo.base.view.IDeviceListFragmentView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.shortcutparser.api.IShortcutParserManager;
import com.tuya.smart.android.shortcutparser.api.IClientParser;
import com.tuya.smart.android.shortcutparser.impl.ShortcutParserManager;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.api.ITuyaHomeStatusListener;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class ShortcutDevicePresenter extends BasePresenter implements NetWorkStatusEvent {
    private static final String TAG = "DeviceListFragmentPresenter";
    private static final int WHAT_JUMP_GROUP_PAGE = 10212;
    protected Activity mActivity;
    protected IDeviceListFragmentView mView;
    private IShortcutParserManager mIShortcutParserManager;

    public ShortcutDevicePresenter(Activity fragment, IDeviceListFragmentView view) {
        mActivity = fragment;
        mView = view;
        TuyaSdk.getEventBus().register(this);
        Constant.HOME_ID = PreferencesUtil.getLong("homeId", Constant.HOME_ID);
        mIShortcutParserManager = new ShortcutParserManager();
    }

    public void getData() {
        mView.loadStart();
        getDataFromServer();
    }



    public void getDataFromServer() {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                if (homeBeans.size() == 0) {
                    mView.gotoCreateHome();
                    return;
                }
                final long homeId = homeBeans.get(0).getHomeId();
                Constant.HOME_ID = homeId;
                PreferencesUtil.set("homeId", Constant.HOME_ID);
                TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {

                        updateDeviceData(bean.getDeviceList());
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }
                });
                TuyaHomeSdk.newHomeInstance(homeId).registerHomeStatusListener(new ITuyaHomeStatusListener() {
                    @Override
                    public void onDeviceAdded(String devId) {

                    }

                    @Override
                    public void onDeviceRemoved(String devId) {

                    }

                    @Override
                    public void onGroupAdded(long groupId) {

                    }

                    @Override
                    public void onGroupRemoved(long groupId) {

                    }

                    @Override
                    public void onMeshAdded(String meshId) {
                        L.d(TAG, "onMeshAdded: " + meshId);
                    }


                });

            }

            @Override
            public void onError(String errorCode, String error) {
                TuyaHomeSdk.newHomeInstance(Constant.HOME_ID).getHomeLocalCache(new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        L.d(TAG, com.alibaba.fastjson.JSONObject.toJSONString(bean));
                        updateDeviceData(bean.getDeviceList());
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }
                });
            }
        });
    }


    public void onDeviceClick(DeviceBean deviceBean) {
        IClientParser clientParserBean = mIShortcutParserManager.getDeviceParseData(deviceBean);
        if(clientParserBean.getDpShortcutControlList().isEmpty() && clientParserBean.getSwitchStatus() == IClientParser.SHORTCUT_SWITCH_STATUS.SWITCH_NONE){
            ToastUtil.shortToast(mActivity.getApplicationContext(),R.string.shortcut_device_none);
        }else {
            gotoShortcutOperateDeviceActivity(deviceBean.getDevId());
        }
    }

    public void gotoShortcutOperateDeviceActivity(String devId) {
        Intent intent = new Intent(mActivity, ShortcutOperateDeviceActivity.class);
        intent.putExtra(ShortcutOperateDeviceActivity.EXTRA_DEVICE_ID, devId);
        mActivity.startActivity(intent);
    }

    private void updateDeviceData(List<DeviceBean> list) {
        if (list.size() == 0) {
            mView.showBackgroundView();
        } else {
            mView.hideBackgroundView();
            mView.updateDeviceData(list);
            mView.loadFinish();
        }
    }

    @Override
    public void onEvent(NetWorkStatusEventModel eventModel) {
        netStatusCheck(eventModel.isAvailable());
    }

    public boolean netStatusCheck(boolean isNetOk) {
        networkTip(isNetOk, R.string.ty_no_net_info);
        return true;
    }

    private void networkTip(boolean networkok, int tipRes) {
        if (networkok) {
            mView.hideNetWorkTipView();
        } else {
            mView.showNetWorkTipView(tipRes);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }
}
