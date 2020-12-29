package com.tuya.smart.android.demo.personal;


import com.tuya.smart.android.mvp.view.IView;

/**
 * Created by letian on 15/6/22.
 */
public interface IPersonalInfoView extends IView {

    void reNickName(String nickName);

    void onLogout();
}
