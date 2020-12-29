package com.tuya.smart.android.demo.family;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.tuya.smart.android.demo.TuyaSmartApp;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;

public class FamilySpHelper {

    private SharedPreferences mPreferences;

    private static final String PREFERENCE_NAME = "tuya_Home";

    private static final String CURRENT_FAMILY_SUFFIX = "currentHome_";


    public static final String TAG = FamilySpHelper.class.getSimpleName();

    public FamilySpHelper() {
        mPreferences = TuyaSmartApp.getAppContext().getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putCurrentHome(HomeBean homeBean) {
        if (null == homeBean) {
            return;
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        String userId = null;
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (null != user) {
            userId = user.getUid();
        }
        editor.putString(CURRENT_FAMILY_SUFFIX + userId, JSON.toJSONString(homeBean));
        editor.commit();
    }


    public HomeBean getCurrentHome() {
        String userId = null;
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (null != user) {
            userId = user.getUid();
        }

        String currentFamilyStr = mPreferences.getString(CURRENT_FAMILY_SUFFIX + userId, "");
        if (TextUtils.isEmpty(currentFamilyStr)) {
            return null;
        }
        return JSON.parseObject(currentFamilyStr, HomeBean.class);
    }


}
