package com.tuya.smart.android.demo.scene.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.scene.PlaceFacadeBean;
import com.tuya.smart.home.sdk.bean.scene.SceneBean;
import com.tuya.smart.home.sdk.bean.scene.SceneCondition;
import com.tuya.smart.home.sdk.bean.scene.SceneTask;
import com.tuya.smart.home.sdk.bean.scene.condition.ConditionListBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kunyang.Lee on 2017/9/11.
 */

public class SceneEditActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvPostData;

    private List<DeviceBean> devList;
    private List<PlaceFacadeBean> cityList;
    private long homeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_edit);

        findViewById(R.id.bt_add_scene).setOnClickListener(this);
        findViewById(R.id.bt_get_devList).setOnClickListener(this);
        findViewById(R.id.bt_get_city_list).setOnClickListener(this);
        findViewById(R.id.bt_get_scene_list).setOnClickListener(this);
        findViewById(R.id.bt_get_condition_list).setOnClickListener(this);
        tvPostData = (TextView) findViewById(R.id.tv_post_data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_scene:
                HashMap<String, Object> taskMap = new HashMap<>();
                taskMap.put("1", true);
                break;
            case R.id.bt_get_devList:
                getDevList();
                break;
            case R.id.bt_get_city_list:
                getCityList("cn");
                break;
            case R.id.bt_get_scene_list:
                getSceneList();
                break;
            case R.id.bt_get_condition_list:
                getConditionList();
                break;
        }
    }

    private void addScene(String name, List<SceneCondition> condition, List<SceneTask> tasks) {
//        TuyaHomeSdk.getSceneManagerInstance().createScene(homeId,
//                name,"url", condition
//                ,
//                tasks,
//                new ITuyaResultCallback<SceneBean>() {
//                    @Override
//                    public void onSuccess(SceneBean sceneBean) {
//                        Log.d("createScene", "createScene Success");
//                    }
//
//                    @Override
//                    public void onError(String errorCode, String errorMessage) {
//                        Log.d("createScene", "onError: " + errorCode + "\n" + errorMessage);
//                    }
//                });
    }

    private void getDevList() {
        TuyaHomeSdk.getSceneManagerInstance().getTaskDevList(homeId, new ITuyaResultCallback<List<DeviceBean>>() {
            @Override
            public void onSuccess(List<DeviceBean> sceneDevBeans) {
                devList = sceneDevBeans;
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
            }
        });
    }

    private void getCityList(String countryCode) {
        TuyaHomeSdk.getSceneManagerInstance().getCityListByCountryCode(countryCode, new ITuyaResultCallback<List<PlaceFacadeBean>>() {
            @Override
            public void onSuccess(List<PlaceFacadeBean> placeFacadeBeans) {
                cityList = placeFacadeBeans;
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
            }
        });
    }

    private void getConditionList() {
        TuyaHomeSdk.getSceneManagerInstance().getConditionList(false, new ITuyaResultCallback<List<ConditionListBean>>() {
            @Override
            public void onSuccess(List<ConditionListBean> conditionListBeans) {
                Log.d("getConditionList", "onSuccess: " + JSON.toJSONString(conditionListBeans));
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
            }
        });
    }

    private void getSceneList() {
        TuyaHomeSdk.getSceneManagerInstance().getSceneList(homeId, new ITuyaResultCallback<List<SceneBean>>() {
            @Override
            public void onSuccess(List<SceneBean> sceneBeans) {
                Log.d("getSceneList", "onSuccess: " + JSON.toJSONString(sceneBeans));
                SceneBean sceneBean = sceneBeans.get(0);
                TuyaHomeSdk.newSceneInstance(sceneBean.getId())
                        .modifyScene(sceneBean, new ITuyaResultCallback<SceneBean>() {
                            @Override
                            public void onSuccess(SceneBean sceneBean) {
                                L.d("modifyScene", "onSuccess: " + JSON.toJSONString(sceneBean));
                            }

                            @Override
                            public void onError(String errorCode, String errorMessage) {
                                L.e("modifyScene", "error" + errorCode + ":" + errorMessage);
                            }
                        });
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
            }
        });
    }
}
