package com.tuya.smart.android.demo.scene.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.scene.adapter.DeviceChooseAdapter;
import com.tuya.smart.android.demo.scene.presenter.DeviceChoosePresenter;
import com.tuya.smart.android.demo.scene.view.IDeviceChooseView;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public class DeviceChooseActivity extends BaseActivity implements IDeviceChooseView, DeviceChooseAdapter.OnItemClickListener {

    private RecyclerView mRcv_devices;
    private DeviceChoosePresenter mPresenter;
    private DeviceChooseAdapter mAdapter;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_choose);
        initToolbar();
        initMenu();
        initView();
        initAdapter();
        initPresenter();
    }


    private void initMenu() {
        setTitle(getString(R.string.ty_scene_select_device));
    }

    private void initView() {
        mRcv_devices = findViewById(R.id.rcv_devices);
        mRcv_devices.setLayoutManager(new LinearLayoutManager(this));
        mEmptyView = findViewById(R.id.rl_empty);
    }

    private void initAdapter() {
        mAdapter = new DeviceChooseAdapter(this);
        mRcv_devices.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initPresenter() {
        mPresenter = new DeviceChoosePresenter(this, this);
        mPresenter.getDevList();
    }

    @Override
    public void showDevices(List<DeviceBean> deviceBeans) {
        mEmptyView.setVisibility(View.GONE);
        mRcv_devices.setVisibility(View.VISIBLE);
        mAdapter.setDatas(deviceBeans);
    }

    @Override
    public void showEmpty() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRcv_devices.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(DeviceBean deviceBean) {
        mPresenter.getDeviceOperatorList(deviceBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mPresenter)mPresenter.onDestroy();
    }
}
