package com.tuya.smart.android.demo.scene.event.model;

import com.tuya.smart.home.sdk.bean.scene.SceneCondition;
import com.tuya.smart.home.sdk.bean.scene.SceneTask;

/**
 * create by nielev on 2019-10-29
 */
public class SceneUpdateTaskModel {
    private SceneTask mSceneTask;
    public SceneUpdateTaskModel(SceneTask sceneTask) {
        mSceneTask = sceneTask;
    }

    public SceneTask getSceneTask() {
        return mSceneTask;
    }
}
