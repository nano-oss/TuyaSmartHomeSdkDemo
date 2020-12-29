package com.tuya.smart.android.demo.scene.view;

import com.tuya.smart.home.sdk.bean.scene.dev.TaskListBean;

import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public interface IOperatorListView {
    void showEmpty();

    void showOperators(List<TaskListBean> result);
}
