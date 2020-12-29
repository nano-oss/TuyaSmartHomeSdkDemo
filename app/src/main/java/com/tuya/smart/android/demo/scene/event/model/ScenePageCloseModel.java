package com.tuya.smart.android.demo.scene.event.model;

/**
 * Created by nielev on 2018/5/31.
 */

public class ScenePageCloseModel {
    public static final int DEFAULT_CLOSE = 999;
    public ScenePageCloseModel(int type) {
        this.mType = type;
    }

    private int mType;

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }
}
