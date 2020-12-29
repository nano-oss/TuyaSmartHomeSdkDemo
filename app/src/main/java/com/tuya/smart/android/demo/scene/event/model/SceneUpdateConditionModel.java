package com.tuya.smart.android.demo.scene.event.model;

import com.tuya.smart.home.sdk.bean.scene.SceneCondition;

/**
 * create by nielev on 2019-10-29
 */
public class SceneUpdateConditionModel {
    private SceneCondition mSceneConditon;
    public SceneUpdateConditionModel(SceneCondition sceneCondition) {
        mSceneConditon = sceneCondition;
    }

    public SceneCondition getmSceneConditon() {
        return mSceneConditon;
    }
}
