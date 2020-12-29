package com.tuya.smart.android.demo.login.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;

import com.google.zxing.WriterException;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.login.IQRCodeLoginView;
import com.tuya.smart.android.demo.login.model.IQRCodeModel;
import com.tuya.smart.android.demo.login.model.QRCodeLoginModel;
import com.tuya.smart.android.demo.login.utils.QRCodeUtils;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;

import static com.tuya.smart.android.demo.login.model.QRCodeLoginModel.GET_LOOP_TOKEN_FAILED;
import static com.tuya.smart.android.demo.login.model.QRCodeLoginModel.GET_LOOP_TOKEN_SUCCESS;
import static com.tuya.smart.android.demo.login.model.QRCodeLoginModel.GET_TOKEN_FAILED;
import static com.tuya.smart.android.demo.login.model.QRCodeLoginModel.GET_TOKEN_SUCCESS;

/**
 * @author xushun
 * @Des:
 * @data 2019/4/5.
 */
public class QRcodeLoginPresenter extends BasePresenter {
    private IQRCodeLoginView mView;
    private IQRCodeModel model;
    private static final String TAG = "QRcodeLoginPresenter";
    private static final int LOOP_WHAT = 101;

    private String token;
    private Context context;

    private boolean isLooping = false;

    public QRcodeLoginPresenter(Context context, IQRCodeLoginView view) {
        this.context = context;
        mView = view;
        model = new QRCodeLoginModel(context, mHandler);
    }

    public void getToken() {
        model.getToken();
    }


    @Override
    public boolean handleMessage(Message msg) {
        L.d(TAG, "handleMessage : " + msg.what);
        switch (msg.what) {
            case GET_TOKEN_SUCCESS:
                token = ((Result) msg.obj).getObj().toString();
                L.d(TAG, "token: " + token);
                String scheme = "tuyaSmart--qrLogin?token=" + token;
                L.i(TAG, "scheme: " + scheme);
                try {
                    Bitmap bitmap = QRCodeUtils.createQRCode(scheme, 1080);
                    mView.refreshQRCode(bitmap);
//                    mView.setTestText(scheme);
//                    mView.setTestText1(token);

                    //开始轮询了
                    if (!isLooping){
                        startLoop();
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case GET_TOKEN_FAILED:
                String errorCode = ((Result) msg.obj).getErrorCode();
                L.e(TAG, "GET_TOKEN_FAILED: " + errorCode);

//                try {
//                    token = "tuyaSmart--qrLogin?token=release_afkuuanqaty8cmhtcfj8";
//                    Bitmap bitmap = QRCodeUtils.createQRCode(token, 1080);
//                    mView.refreshQRCode(bitmap);
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }
                break;
            case GET_LOOP_TOKEN_FAILED:
                String loopErrorCode = ((Result) msg.obj).getErrorCode();
                L.e(TAG, "GET_LOOP_TOKEN_FAILED: " + loopErrorCode + " " + ((Result) msg.obj).getError());

                L.i(TAG,loopErrorCode.equals("USER_QR_LOGIN_TOKEN_EXPIRE") + " equals");
                if (((Result) msg.obj).getErrorCode().equals("USER_QR_LOGIN_TOKEN_EXPIRE")){
                    L.i(TAG,"getToken");
                    // token 过期 重新刷新token
                    getToken();
                }

                startLoop();
                break;
            case GET_LOOP_TOKEN_SUCCESS:
                L.i(TAG, "GET_LOOP_TOKEN_SUCCESS");
                stopLoop();

                gotoHome();
                break;

            case LOOP_WHAT:
                model.tokenUserGet(token);
                break;
            default:
                break;
        }
        return super.handleMessage(msg);
    }

    private void gotoHome() {
        mView.loginSuccess();
    }

    public void startLoop() {
        isLooping = true;
        mHandler.sendEmptyMessageDelayed(LOOP_WHAT, 2000);
    }

    public void stopLoop() {
        isLooping = false;
        mHandler.removeMessages(LOOP_WHAT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (model != null) {
            model.onDestroy();
        }
    }
}
