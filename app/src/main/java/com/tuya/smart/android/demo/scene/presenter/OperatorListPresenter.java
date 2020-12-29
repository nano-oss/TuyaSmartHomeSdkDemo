package com.tuya.smart.android.demo.scene.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tuya.smart.android.demo.scene.activity.OperatorValueActivity;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.scene.event.ScenePageCloseEvent;
import com.tuya.smart.android.demo.scene.event.model.ScenePageCloseModel;
import com.tuya.smart.android.demo.scene.view.IOperatorListView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.scene.dev.TaskListBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.sdk.TuyaSdk;

import java.util.List;

import static com.tuya.smart.android.demo.scene.presenter.ScenePresenter.IS_CONDITION;


/**
 * create by nielev on 2019-10-29
 */
public class OperatorListPresenter extends BasePresenter implements ScenePageCloseEvent {
    private Activity mAc;
    private IOperatorListView mView;

    public static final String TASK_LIST_BEAN = "taskListBean";
    private boolean isCondition;
    private String mDevid;

    public OperatorListPresenter(Activity ac, IOperatorListView view){
        mAc = ac;
        mView = view;
        TuyaSdk.getEventBus().register(this);
    }
    public void getOperatorList(){
        isCondition = mAc.getIntent().getBooleanExtra(IS_CONDITION, false);
        mDevid = mAc.getIntent().getStringExtra(DeviceChoosePresenter.DEV_ID);
        if(isCondition){
            TuyaHomeSdk.getSceneManagerInstance().getDeviceConditionOperationList(mDevid, new ITuyaResultCallback<List<TaskListBean>>() {
                @Override
                public void onSuccess(List<TaskListBean> result) {
                    if(null != result && !result.isEmpty()){
                        mView.showOperators(result);
                    } else {
                        mView.showEmpty();
                    }
                }

                @Override
                public void onError(String errorCode, String errorMessage) {

                }
            });
        } else {
            TuyaHomeSdk.getSceneManagerInstance().getDeviceTaskOperationList(mDevid, new ITuyaResultCallback<List<TaskListBean>>() {
                @Override
                public void onSuccess(List<TaskListBean> result) {
                    if(null != result && !result.isEmpty()){
                        mView.showOperators(result);
                    } else {
                        mView.showEmpty();
                    }
                }

                @Override
                public void onError(String errorCode, String errorMessage) {

                }
            });
        }

    }

    public void selectOperatorValue(TaskListBean taskListBean) {
        Intent intent = new Intent(mAc, OperatorValueActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_LIST_BEAN, taskListBean);
        bundle.putBoolean(IS_CONDITION, isCondition);
        bundle.putString(DeviceChoosePresenter.DEV_ID, mDevid);
        intent.putExtra("Bundle", bundle);
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
