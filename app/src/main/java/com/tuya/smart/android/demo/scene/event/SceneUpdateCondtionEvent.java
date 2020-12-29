package com.tuya.smart.android.demo.scene.event;

import com.tuya.smart.android.demo.scene.event.model.SceneUpdateConditionModel;

/**
 * create by nielev on 2019-10-29
 */
public interface SceneUpdateCondtionEvent {
    void onEvent(SceneUpdateConditionModel model);
}
