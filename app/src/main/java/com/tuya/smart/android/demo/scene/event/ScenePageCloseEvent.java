package com.tuya.smart.android.demo.scene.event;

import com.tuya.smart.android.demo.scene.event.model.ScenePageCloseModel;

/**
 * Created by nielev on 2018/5/31.
 */

public interface ScenePageCloseEvent {
    void onEvent(ScenePageCloseModel model);
}
