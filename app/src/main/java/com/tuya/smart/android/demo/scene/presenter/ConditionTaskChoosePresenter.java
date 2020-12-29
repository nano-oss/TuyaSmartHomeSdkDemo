package com.tuya.smart.android.demo.scene.presenter;

import android.app.Activity;
import android.content.Intent;

import com.tuya.smart.android.demo.scene.activity.DeviceChooseActivity;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.scene.event.ScenePageCloseEvent;
import com.tuya.smart.android.demo.scene.event.model.ScenePageCloseModel;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.TuyaSdk;

import static com.tuya.smart.android.demo.scene.presenter.ScenePresenter.IS_CONDITION;


/**
 * create by nielev on 2019-10-29
 */
public class ConditionTaskChoosePresenter extends BasePresenter implements ScenePageCloseEvent {
    private Activity mAc;

    public ConditionTaskChoosePresenter(Activity activity){
        mAc = activity;
        TuyaSdk.getEventBus().register(this);
    }

    public void selectDeviceTask(boolean isCondition){
        Intent intent = new Intent(mAc, DeviceChooseActivity.class);
        intent.putExtra(IS_CONDITION, isCondition);
        ActivityUtils.startActivity(mAc, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onEvent(ScenePageCloseModel model) {
        mAc.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }
}
