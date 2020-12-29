package com.tuya.smart.android.demo.scene.view;

import com.tuya.smart.home.sdk.bean.scene.SceneCondition;
import com.tuya.smart.home.sdk.bean.scene.SceneTask;

import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public interface ISceneView {
    void showSceneView();

    void showAutoView();

    void updateConditions(List<SceneCondition> conditions);

    void updateTasks(List<SceneTask> tasks);

    void showBg(String s);

    String getSceneName();
}
