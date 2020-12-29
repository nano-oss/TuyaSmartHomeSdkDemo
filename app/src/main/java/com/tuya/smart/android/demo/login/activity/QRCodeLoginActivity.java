package com.tuya.smart.android.demo.login.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.base.app.Constant;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.base.utils.LoginHelper;
import com.tuya.smart.android.demo.login.IQRCodeLoginView;
import com.tuya.smart.android.demo.login.presenter.QRcodeLoginPresenter;


/**
 * @author xushun
 * @Des:
 * @data 2020/7/9.
 */

public class QRCodeLoginActivity extends BaseActivity implements IQRCodeLoginView {
    private static final String TAG = "QRCodeLoginActivity";
    ImageView ivQRCode;
    TextView tvQrAppInfo;
    Context context;
    QRcodeLoginPresenter presenter;
    private String token;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_qr_code);
        context = this;

        initViews();
        presenter = new QRcodeLoginPresenter(context, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.stopLoop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            L.i(TAG, "onResume");
            presenter.getToken();
        }
    }

    private void initViews() {
        ivQRCode = findViewById(R.id.iv_qrcode);
        tvQrAppInfo = findViewById(R.id.id_tv_qr_app_info);
        tvQrAppInfo.setText(context.getString(R.string.login_qr_app_info) + getAppName(context));
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void refreshQRCode(Bitmap qr) {
        ivQRCode.setImageBitmap(qr);
    }

    @Override
    public void loginSuccess() {
        Constant.finishActivity();
        LoginHelper.afterLogin();
        ActivityUtils.gotoHomeActivity((Activity) context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
