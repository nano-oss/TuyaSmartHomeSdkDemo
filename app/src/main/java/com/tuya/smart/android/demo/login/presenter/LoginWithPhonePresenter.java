package com.tuya.smart.android.demo.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;

import com.tuya.smart.android.demo.login.activity.CountryListActivity;
import com.tuya.smart.android.demo.base.app.Constant;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.base.utils.CountryUtils;
import com.tuya.smart.android.demo.base.utils.LoginHelper;
import com.tuya.smart.android.demo.base.utils.MessageUtil;
import com.tuya.smart.android.demo.login.ILoginWithPhoneView;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.TuyaSdk;

/**
 * Created by letian on 15/5/30.
 */
public class LoginWithPhonePresenter extends BasePresenter {

    private static final String TAG = "LoginWithPhonePresenter";

    protected Activity mContext;

    private static final int GET_VALIDATE_CODE_PERIOD = 60 * 1000;

    protected ILoginWithPhoneView mView;


    private CountDownTimer mCountDownTimer;

    private boolean isCountDown;

    protected String mPhoneCode;

    private String mCountryName;
    protected boolean mSend;

    public LoginWithPhonePresenter(Context context, ILoginWithPhoneView view) {
        super();
        mContext = (Activity) context;
        mView = view;
        getCountry();
    }

    private void getCountry() {
        String countryKey = CountryUtils.getCountryKey(TuyaSdk.getApplication());
        if (!TextUtils.isEmpty(countryKey)) {
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mPhoneCode = CountryUtils.getCountryNum(countryKey);
        } else {
            countryKey = CountryUtils.getCountryDefault(TuyaSdk.getApplication());
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mPhoneCode = CountryUtils.getCountryNum(countryKey);
        }
        mView.setCountry(mCountryName, mPhoneCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0x01:
                if (resultCode == Activity.RESULT_OK) {
                    mCountryName = data.getStringExtra(CountryListActivity.COUNTRY_NAME);
                    mPhoneCode = data.getStringExtra(CountryListActivity.PHONE_CODE);
                    mView.setCountry(mCountryName, mPhoneCode);
                }
                break;
        }
    }

    public static final int MSG_SEND_VALIDATE_CODE_SUCCESS = 12;
    public static final int MSG_SEND_VALIDATE_CODE_ERROR = 13;
    public static final int MSG_LOGIN_SUCCESS = 15;
    public static final int MSG_LOGIN_ERROR = 16;

    /**
     * 获取验证码
     *
     * @return
     */
    public void getValidateCode() {
        mSend = true;
        TuyaHomeSdk.getUserInstance().getValidateCode(mPhoneCode, mView.getPhone(), new IValidateCallback() {
            @Override
            public void onSuccess() {
                mHandler.sendEmptyMessage(MSG_SEND_VALIDATE_CODE_SUCCESS);
            }

            @Override
            public void onError(String s, String s1) {
                getValidateCodeFail(s, s1);
            }
        });
    }

    protected void getValidateCodeFail(String errorCode, String errorMsg) {
        Message msg = MessageUtil.getCallFailMessage(MSG_SEND_VALIDATE_CODE_ERROR, errorCode, errorMsg);
        mHandler.sendMessage(msg);
        mSend = false;
    }

    /**
     * 登录
     *
     * @return
     */
    public void login() {
        String phoneNumber = mView.getPhone();
        String code = mView.getValidateCode();

        TuyaHomeSdk.getUserInstance().loginWithPhone(mPhoneCode, phoneNumber, code, new ILoginCallback() {
            @Override
            public void onSuccess(User user) {
                mHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);
            }

            @Override
            public void onError(String s, String s1) {
                Message msg = MessageUtil.getCallFailMessage(MSG_LOGIN_ERROR, s, s1);
                mHandler.sendMessage(msg);
            }
        });


    }

    public void selectCountry() {
        mContext.startActivityForResult(new Intent(mContext, CountryListActivity.class), 0x01);
    }

    /**
     * 构造倒计时
     */
    private void buildCountDown() {
        mCountDownTimer = new Countdown(GET_VALIDATE_CODE_PERIOD, 1000);
        mCountDownTimer.start();
        mView.disableGetValidateCode();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SEND_VALIDATE_CODE_SUCCESS:
                buildCountDown();
                mView.modelResult(msg.what, null);
                break;

            case MSG_SEND_VALIDATE_CODE_ERROR:
                mView.modelResult(msg.what, (Result) msg.obj);
                break;


            case MSG_LOGIN_ERROR:
                mView.modelResult(msg.what, (Result) msg.obj);
                break;

            case MSG_LOGIN_SUCCESS:
                mView.modelResult(msg.what, null);
                loginSuccess();
                break;
        }
        return super.handleMessage(msg);
    }

    private void loginSuccess() {
        Constant.finishActivity();
        LoginHelper.afterLogin();
        ActivityUtils.gotoHomeActivity(mContext);
    }

    @Override
    public void onDestroy() {
        mCountDownTimer = null;
    }

    public void validateBtResume() {
        if (mCountDownTimer != null) {
            mCountDownTimer.onFinish();
        }
    }

    public boolean isSended() {
        return mSend;
    }

    private class Countdown extends CountDownTimer {

        public Countdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mView.setCountdown((int) (millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            mView.enableGetValidateCode();
            mSend = false;
            mView.checkValidateCode();
        }
    }
}
