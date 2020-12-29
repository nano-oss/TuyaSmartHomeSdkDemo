package com.tuya.smart.android.demo.family.presenter;


import com.tuya.smart.android.demo.family.activity.IFamilyInfoView;
import com.tuya.smart.android.demo.family.model.FamilyInfoModel;
import com.tuya.smart.android.demo.family.model.IFamilyInfoModel;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.bean.MemberBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetMemberListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.IResultCallback;

import java.util.List;

public class FamilyInfoPresenter extends BasePresenter {

    private IFamilyInfoView mIFamilyInfoView;

    private IFamilyInfoModel mFamilyInfoModel;

    private HomeBean mHomeBean;

    public FamilyInfoPresenter(IFamilyInfoView view) {
        super(view.getContext());
        this.mIFamilyInfoView = view;
        this.mFamilyInfoModel = new FamilyInfoModel(view.getContext());

        getHomeInfo();
    }

    private void getHomeInfo() {
        mFamilyInfoModel.getHomeBean(mIFamilyInfoView.getHomeId(), new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                if (null==mIFamilyInfoView){
                    return;
                }
                mHomeBean = homeBean;
                mIFamilyInfoView.setHomeData(mHomeBean);
                getMemberInfo();
            }

            @Override
            public void onError(String s, String s1) {
                if (null==mIFamilyInfoView){
                    return;
                }
                getMemberInfo();
            }
        });

    }


    private void getMemberInfo() {
        mFamilyInfoModel.getMemberList(mIFamilyInfoView.getHomeId(), new ITuyaGetMemberListCallback() {
            @Override
            public void onSuccess(List<MemberBean> list) {
                if (null==mIFamilyInfoView){
                    return;
                }
                mIFamilyInfoView.setMemberData(list);
            }

            @Override
            public void onError(String s, String s1) {

            }
        });
    }


    public void removeHome(){
//        if (mIFamilyInfoView.getHomeId()==FamilyManager.getInstance().getCurrentHomeId()){
//            mIFamilyInfoView.showToast(R.string.family_info_remove_currenthome_tip);
//            return;
//        }
        mFamilyInfoModel.removeHome(mIFamilyInfoView.getHomeId(), new IResultCallback() {
            @Override
            public void onError(String s, String s1) {
                if (null==mIFamilyInfoView){
                    return;
                }
                mIFamilyInfoView.doRemoveView(false);
            }

            @Override
            public void onSuccess() {
                if (null==mIFamilyInfoView){
                    return;
                }
                mIFamilyInfoView.doRemoveView(true);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
