package com.tuya.smart.android.demo.scene.presenter;

import android.app.Activity;
import android.content.Intent;

import com.tuya.smart.android.demo.family.FamilyManager;
import com.tuya.smart.android.demo.scene.activity.OperateorListActivity;
import com.tuya.smart.android.demo.base.app.Constant;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.base.utils.ToastUtil;
import com.tuya.smart.android.demo.scene.event.ScenePageCloseEvent;
import com.tuya.smart.android.demo.scene.event.model.ScenePageCloseModel;
import com.tuya.smart.android.demo.scene.view.IDeviceChooseView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

import static com.tuya.smart.android.demo.scene.presenter.ScenePresenter.IS_CONDITION;


/**
 * create by nielev on 2019-10-29
 */
public class DeviceChoosePresenter extends BasePresenter implements ScenePageCloseEvent {
    private Activity mAc;
    private IDeviceChooseView mView;

    public static final String DEV_ID = "dev_id";
    private boolean isCondition;

    public DeviceChoosePresenter(Activity activity, IDeviceChooseView view){
        mAc = activity;
        mView = view;
        Constant.HOME_ID = FamilyManager.getInstance().getCurrentHomeId();
        TuyaSdk.getEventBus().register(this);
    }
    public void getDevList(){
        isCondition = mAc.getIntent().getBooleanExtra(IS_CONDITION, false);
        if(isCondition){
            TuyaHomeSdk.getSceneManagerInstance().getConditionDevList(Constant.HOME_ID, new ITuyaResultCallback<List<DeviceBean>>() {
                @Override
                public void onSuccess(List<DeviceBean> result) {
                    if(null != result && !result.isEmpty()){
                        mView.showDevices(result);
                    } else {
                        mView.showEmpty();
                    }
                }

                @Override
                public void onError(String errorCode, String errorMessage) {
                    ToastUtil.shortToast(mAc, errorMessage);
                }
            });
        } else {
            TuyaHomeSdk.getSceneManagerInstance().getTaskDevList(Constant.HOME_ID, new ITuyaResultCallback<List<DeviceBean>>() {
                @Override
                public void onSuccess(List<DeviceBean> result) {
                    if(null != result && !result.isEmpty()){
                        mView.showDevices(result);
                    } else {
                        mView.showEmpty();
                    }
                }

                @Override
                public void onError(String errorCode, String errorMessage) {
                    ToastUtil.shortToast(mAc, errorMessage);
                }
            });
        }

    }

    public void getDeviceOperatorList(DeviceBean deviceBean) {
        Intent intent = new Intent(mAc, OperateorListActivity.class);
        intent.putExtra(IS_CONDITION, isCondition);
        intent.putExtra(DEV_ID, deviceBean.getDevId());
        ActivityUtils.startActivity(mAc, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }

    @Override
    public void onEvent(ScenePageCloseModel model) {
        mAc.finish();
    }
}
