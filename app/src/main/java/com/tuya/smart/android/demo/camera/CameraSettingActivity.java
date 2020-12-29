package com.tuya.smart.android.demo.camera;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuyasmart.camera.devicecontrol.ITuyaCameraDevice;
import com.tuyasmart.camera.devicecontrol.TuyaCameraDeviceControlSDK;
import com.tuyasmart.camera.devicecontrol.api.ITuyaCameraDeviceControlCallback;
import com.tuyasmart.camera.devicecontrol.bean.DpBasicFlip;
import com.tuyasmart.camera.devicecontrol.bean.DpBasicIndicator;
import com.tuyasmart.camera.devicecontrol.bean.DpBasicOSD;
import com.tuyasmart.camera.devicecontrol.bean.DpBasicPrivate;
import com.tuyasmart.camera.devicecontrol.bean.DpMotionSensitivity;
import com.tuyasmart.camera.devicecontrol.bean.DpRestore;
import com.tuyasmart.camera.devicecontrol.bean.DpSDFormat;
import com.tuyasmart.camera.devicecontrol.bean.DpSDFormatStatus;
import com.tuyasmart.camera.devicecontrol.bean.DpSDRecordModel;
import com.tuyasmart.camera.devicecontrol.bean.DpSDRecordSwitch;
import com.tuyasmart.camera.devicecontrol.bean.DpSDStatus;
import com.tuyasmart.camera.devicecontrol.bean.DpSDStorage;
import com.tuyasmart.camera.devicecontrol.bean.DpWirelessBatterylock;
import com.tuyasmart.camera.devicecontrol.bean.DpWirelessElectricity;
import com.tuyasmart.camera.devicecontrol.bean.DpWirelessLowpower;
import com.tuyasmart.camera.devicecontrol.bean.DpWirelessPowermode;
import com.tuyasmart.camera.devicecontrol.model.DpNotifyModel;
import com.tuyasmart.camera.devicecontrol.model.MotionSensitivityMode;
import com.tuyasmart.camera.devicecontrol.model.RecordMode;

import java.util.ArrayList;
import java.util.List;

public class CameraSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> mData;
    ITuyaCameraDevice mTuyaCameraDevice;
    private Toolbar toolbar;
    private TextView showQueryTxt;
    private TextView showPublishTxt;
    private String devId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_setting);
        toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        showQueryTxt = findViewById(R.id.tv_show_query);
        showPublishTxt = findViewById(R.id.tv_show_publish);
        findViewById(R.id.btn_resetore).setOnClickListener(this);
        findViewById(R.id.btn_sdstatus).setOnClickListener(this);
        findViewById(R.id.btn_storage).setOnClickListener(this);
        findViewById(R.id.btn_basic_flip).setOnClickListener(this);
        findViewById(R.id.btn_motion_sensitivity).setOnClickListener(this);
        findViewById(R.id.btn_sd_format).setOnClickListener(this);
        findViewById(R.id.btn_sd_format_status).setOnClickListener(this);
        findViewById(R.id.btn_record_switch).setOnClickListener(this);
        findViewById(R.id.btn_record_model).setOnClickListener(this);
        findViewById(R.id.btn_wireless_powermode).setOnClickListener(this);
        findViewById(R.id.btn_wireless_lowpower).setOnClickListener(this);
        findViewById(R.id.btn_wireless_batterylock).setOnClickListener(this);
        findViewById(R.id.btn_wireless_electricity).setOnClickListener(this);
        initData();
        initDeviceControl();
        initAllDevicePointControl();
    }

    private void initDeviceControl() {
        devId = getIntent().getStringExtra("devId");
        mTuyaCameraDevice = TuyaCameraDeviceControlSDK.getCameraDeviceInstance(devId);
    }

    private void initAllDevicePointControl() {
        mTuyaCameraDevice.setRegisterDevListener(new IDevListener()  {
            @Override
            public void onDpUpdate(String s, String s1) {
                L.d("TuyaHomeSdk", "onDpUpdate devId:" + s + "  dps " + s1);
                //此处监听所有dp点的信息
            }

            @Override
            public void onRemoved(String s) {

            }

            @Override
            public void onStatusChanged(String s, boolean b) {

            }

            @Override
            public void onNetworkStatusChanged(String s, boolean b) {

            }

            @Override
            public void onDevInfoUpdate(String s) {

            }
        });
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(DpSDStatus.ID);
        mData.add(DpBasicFlip.ID);
        mData.add(DpMotionSensitivity.ID);
        mData.add(DpBasicOSD.ID);
        mData.add(DpBasicIndicator.ID);
        mData.add(DpBasicPrivate.ID);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTuyaCameraDevice != null) {
            mTuyaCameraDevice.onDestroy();
        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_sdstatus == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpSDStatus.ID)) {
                int o = mTuyaCameraDevice.queryIntegerCurrentCameraDps(DpSDStatus.ID);
                showQueryTxt.setText("local query result: " + o);
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpSDStatus.ID, new ITuyaCameraDeviceControlCallback<Integer>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Integer o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpSDStatus.ID, null);
            }
        } else if (R.id.btn_basic_flip == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpBasicFlip.ID)) {
                boolean o = mTuyaCameraDevice.queryBooleanCameraDps(DpBasicFlip.ID);
                showQueryTxt.setText("local query result: " + o);
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpBasicFlip.ID, new ITuyaCameraDeviceControlCallback<Boolean>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Boolean o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpBasicFlip.ID, true);
            }
        } else if (R.id.btn_motion_sensitivity == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpMotionSensitivity.ID)) {
                String o = mTuyaCameraDevice.queryStringCurrentCameraDps(DpMotionSensitivity.ID);
                showQueryTxt.setText("local query result: " + o);
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpMotionSensitivity.ID, new ITuyaCameraDeviceControlCallback<String>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpMotionSensitivity.ID, MotionSensitivityMode.HIGH.getDpValue());
            }
        } else if (R.id.btn_storage == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpSDStorage.ID)) {
                String o = mTuyaCameraDevice.queryStringCurrentCameraDps(DpSDStorage.ID);
                showQueryTxt.setText("local query result: " + o);
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpSDStorage.ID, new ITuyaCameraDeviceControlCallback<String>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpSDStorage.ID, null);
            }
        } else if (R.id.btn_sd_format == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpSDFormat.ID)) {
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpSDFormat.ID, new ITuyaCameraDeviceControlCallback<Boolean>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Boolean o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpSDFormat.ID, true);
            }
        } else if (R.id.btn_sd_format_status == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpSDFormatStatus.ID)) {
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpSDFormatStatus.ID, new ITuyaCameraDeviceControlCallback<Integer>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Integer o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpSDFormatStatus.ID, null);
            }
        } else if (R.id.btn_record_switch == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpSDRecordSwitch.ID)) {
                boolean o = mTuyaCameraDevice.queryBooleanCameraDps(DpSDRecordSwitch.ID);
                showQueryTxt.setText("local query result: " + o);
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpSDRecordSwitch.ID, new ITuyaCameraDeviceControlCallback<Boolean>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Boolean o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpSDRecordSwitch.ID, true);
            }
        } else if (R.id.btn_record_model == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpSDRecordModel.ID)) {
                String o = mTuyaCameraDevice.queryStringCurrentCameraDps(DpSDRecordModel.ID);
                showQueryTxt.setText("local query result: " + o);
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpSDRecordModel.ID, new ITuyaCameraDeviceControlCallback<String>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpSDRecordModel.ID, RecordMode.EVENT.getDpValue());
            }
        } else if (R.id.btn_wireless_batterylock == v.getId()) {
            if (mTuyaCameraDevice.isSupportCameraDps(DpWirelessBatterylock.ID)) {
                boolean o = mTuyaCameraDevice.queryBooleanCameraDps(DpWirelessBatterylock.ID);
                showQueryTxt.setText("local query result: " + o);
                mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpWirelessBatterylock.ID, new ITuyaCameraDeviceControlCallback<Boolean>() {
                    @Override
                    public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Boolean o) {
                        showPublishTxt.setText("LAN/Cloud query result: " + o);
                    }

                    @Override
                    public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                    }
                });
                mTuyaCameraDevice.publishCameraDps(DpWirelessBatterylock.ID, true);
            }
        } else if (R.id.btn_wireless_electricity == v.getId()) {
            int o = mTuyaCameraDevice.queryIntegerCurrentCameraDps(DpWirelessElectricity.ID);
            showQueryTxt.setText("local query result: " + o);
            mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpWirelessElectricity.ID, new ITuyaCameraDeviceControlCallback<Integer>() {
                @Override
                public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Integer o) {
                    showPublishTxt.setText("LAN/Cloud query result: " + o);
                }

                @Override
                public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                }
            });
            mTuyaCameraDevice.publishCameraDps(DpWirelessElectricity.ID, null);
        } else if (R.id.btn_wireless_lowpower == v.getId()) {
            int o = mTuyaCameraDevice.queryIntegerCurrentCameraDps(DpWirelessLowpower.ID);
            showQueryTxt.setText("local query result: " + o);
            mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpWirelessLowpower.ID, new ITuyaCameraDeviceControlCallback<Integer>() {
                @Override
                public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, Integer o) {
                    showPublishTxt.setText("LAN/Cloud query result: " + o);
                }

                @Override
                public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                }
            });
            mTuyaCameraDevice.publishCameraDps(DpWirelessLowpower.ID, 20);
        } else if (R.id.btn_wireless_powermode == v.getId()) {
            String o = mTuyaCameraDevice.queryStringCurrentCameraDps(DpWirelessPowermode.ID);
            showQueryTxt.setText("local query result: " + o);
            mTuyaCameraDevice.registorTuyaCameraDeviceControlCallback(DpWirelessPowermode.ID, new ITuyaCameraDeviceControlCallback<String>() {
                @Override
                public void onSuccess(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String o) {
                    showPublishTxt.setText("LAN/Cloud query result: " + o);
                }

                @Override
                public void onFailure(String s, DpNotifyModel.ACTION action, DpNotifyModel.SUB_ACTION sub_action, String s1, String s2) {

                }
            });
            mTuyaCameraDevice.publishCameraDps(DpWirelessPowermode.ID, null);
        } else if (R.id.btn_resetore == v.getId()) {
            mTuyaCameraDevice.publishCameraDps(DpRestore.ID, true);
        }
    }
}
