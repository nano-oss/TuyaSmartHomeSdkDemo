package com.tuya.smart.android.demo.personal;

import android.content.Context;
import android.text.TextUtils;

import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.demo.base.utils.CommonUtil;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;


/**
 * Created by letian on 15/6/18.
 */
public class PersonalCenterModel extends BaseModel implements IPersonalCenterModel {
    public PersonalCenterModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
    }

    @Override
    public String getNickName() {
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (user == null) return "";
        return user.getNickName();
    }

    @Override
    public String getUserName() {
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (user == null) return "";
        String mobile = user.getMobile();
        if (TextUtils.isEmpty(mobile)) {
            String userName = user.getUsername();
            if (CommonUtil.isEmail(userName)) {
                //邮箱
                return userName;
            } else {
                return "";
            }
        } else {
            return mobile;
        }
    }

    @Override
    public void onDestroy() {

    }
}
