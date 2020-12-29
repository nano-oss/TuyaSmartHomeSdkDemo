package com.tuya.smart.android.demo.family.presenter;

import com.tuya.smart.android.demo.base.utils.CollectionUtils;
import com.tuya.smart.android.demo.family.activity.IFamilyAddView;
import com.tuya.smart.android.demo.family.model.FamilyAddModel;
import com.tuya.smart.android.demo.family.model.IFamilyAddModel;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import java.util.List;

public class FamilyAddPresenter extends BasePresenter {


    private IFamilyAddModel mFamilyAddModel;

    private IFamilyAddView mFamilyAddView;

    public FamilyAddPresenter(IFamilyAddView view) {
        super(view.getContext());
        this.mFamilyAddView = view;
        this.mFamilyAddModel = new FamilyAddModel();
    }


    public void addFamily(String homeName, List<String> roomList) {
        if (CollectionUtils.isEmpty(roomList)) {
            return;
        }
        mFamilyAddModel.createHome(homeName, roomList, new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                if (null == mFamilyAddView) {
                    return;
                }
                mFamilyAddView.doSaveSuccess();
            }

            @Override
            public void onError(String s, String s1) {
                if (null == mFamilyAddView) {
                    return;
                }
                mFamilyAddView.doSaveFailed();
            }
        });
    }


}
