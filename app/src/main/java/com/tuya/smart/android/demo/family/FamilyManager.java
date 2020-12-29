package com.tuya.smart.android.demo.family;

import androidx.annotation.NonNull;
import android.util.Log;

import com.tuya.smart.android.demo.base.utils.CollectionUtils;
import com.tuya.smart.android.demo.family.event.EventCurrentHomeChange;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FamilyManager {

    public static final String TAG = FamilyManager.class.getSimpleName();

    private static volatile FamilyManager instance;

    private HomeBean currentHomeBean;

    private FamilySpHelper mFamilySpHelper;


    private FamilyManager() {
        mFamilySpHelper = new FamilySpHelper();
    }

    public static FamilyManager getInstance() {
        if (null == instance) {
            synchronized (FamilyManager.class) {
                if (null == instance) {
                    instance = new FamilyManager();
                }
            }
        }
        return instance;
    }

    public void setCurrentHome(HomeBean homeBean) {
        if (null == homeBean) {
            return;
        }
        boolean isChange = false;

        if (null == currentHomeBean) {
            Log.i(TAG, "setCurrentHome  currentHome is null so push current home change event");
            isChange = true;
        } else {
            long currentHomeId = currentHomeBean.getHomeId();
            long targetHomeId = homeBean.getHomeId();
            Log.i(TAG, "setCurrentHome: currentHomeId=" + currentHomeId + " targetHomeId=" + targetHomeId);
            if (currentHomeId != targetHomeId) {
                isChange = true;
            }
        }
        //更新内存和sp
        currentHomeBean = homeBean;
        mFamilySpHelper.putCurrentHome(currentHomeBean);
        if (isChange) {
            EventBus.getDefault().post(new EventCurrentHomeChange(currentHomeBean));
        }
    }


    public HomeBean getCurrentHome() {
        if (null == currentHomeBean) {
            setCurrentHome(mFamilySpHelper.getCurrentHome());
        }
        return currentHomeBean;
    }


    public long getCurrentHomeId() {
        HomeBean currentHome = getCurrentHome();
        if (null == currentHome) {
            return -1;
        }
        return currentHome.getHomeId();
    }


    public void getHomeList(@NonNull final ITuyaGetHomeListCallback callback) {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> list) {
                if (!CollectionUtils.isEmpty(list) && null == getCurrentHome()) {
                    setCurrentHome(list.get(0));
                }
                callback.onSuccess(list);
            }

            @Override
            public void onError(String s, String s1) {
                callback.onError(s, s1);
            }
        });
    }


    public void createHome(String homeName,
                           List<String> roomList,
                           @NonNull final ITuyaHomeResultCallback callback) {
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName,
                0, 0, "", roomList, new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean homeBean) {
                        setCurrentHome(homeBean);
                        callback.onSuccess(homeBean);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        callback.onError(s, s1);
                    }
                });
    }




}
