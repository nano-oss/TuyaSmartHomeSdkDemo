package com.tuya.smart.android.demo.login;

import android.graphics.Bitmap;

/**
 * @author xushun
 * @Des:
 * @data 2019/4/5.
 */
public interface IQRCodeLoginView {
    void refreshQRCode(Bitmap qr);
    void loginSuccess();
}
