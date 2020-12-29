package com.tuya.smart.android.demo.scene.view;

import com.tuya.smart.home.sdk.bean.scene.SceneBean;

import java.util.List;

/**
 * create by nielev on 2019-10-28
 */
public interface ISceneListFragmentView {
    void showSceneListView(List<SceneBean> scenes, List<SceneBean> autos);

    void loadFinish();

    void showEmptyView();
}
