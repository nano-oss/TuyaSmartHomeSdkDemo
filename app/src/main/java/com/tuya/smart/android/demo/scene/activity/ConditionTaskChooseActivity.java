package com.tuya.smart.android.demo.scene.activity;

import android.os.Bundle;
import android.view.View;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.scene.presenter.ConditionTaskChoosePresenter;

import static com.tuya.smart.android.demo.scene.presenter.ScenePresenter.IS_CONDITION;

/**
 * create by nielev on 2019-10-29
 */
public class ConditionTaskChooseActivity extends BaseActivity implements View.OnClickListener {

    private boolean isCondition;
    private ConditionTaskChoosePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_task_choose);
        initToolbar();
        initMenu();
        initView();
        initPresenter();
    }



    private void initMenu() {
        isCondition = getIntent().getBooleanExtra(IS_CONDITION, false);
        setTitle(isCondition ? getString(R.string.ty_scene_select_condition) : getString(R.string.ty_scene_select_task));
    }

    private void initView() {
        findViewById(R.id.btn_device).setOnClickListener(this);
    }

    private void initPresenter() {
        mPresenter = new ConditionTaskChoosePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_device:
                mPresenter.selectDeviceTask(isCondition);
                break;
                default:break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mPresenter)mPresenter.onDestroy();
    }
}
