package com.tuya.smart.android.demo.config;

import android.os.Bundle;
import android.view.View;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.camera.QrCodeConfigActivity;

public class AddDeviceTypeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_type);
        initToolbar();
        initView();
        setTitle(getString(R.string.ty_add_device_sort));
        setDisplayHomeAsUpEnabled();
    }

    private void initView() {
        findViewById(R.id.wifi_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiDevConfig();
            }
        });
        findViewById(R.id.gateway_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGatewayDevConfig();
            }
        });
        findViewById(R.id.grcode_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCodeDevConfig();
            }
        });
    }

    private void startGatewayDevConfig() {
        ActivityUtils.gotoActivity(this, ZigbeeConfigActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }

    private void startWifiDevConfig() {
        ActivityUtils.gotoActivity(this, AddDeviceTipActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }

    private void startQrCodeDevConfig() {
        ActivityUtils.gotoActivity(this, QrCodeConfigActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }
}
