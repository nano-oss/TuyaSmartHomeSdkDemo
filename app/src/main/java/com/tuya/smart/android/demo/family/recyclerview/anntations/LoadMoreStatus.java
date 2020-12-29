package com.tuya.smart.android.demo.family.recyclerview.anntations;

import androidx.annotation.IntDef;

/**
 * Created by wangshuwen on 2018/6/2.
 */

@IntDef({LoadMoreStatus.LOADING, LoadMoreStatus.END, LoadMoreStatus.IDLE})
public @interface LoadMoreStatus {
    int LOADING = 1;
    int END = 2;
    int IDLE = 0;
}
