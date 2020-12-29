package com.tuya.smart.android.demo.scene.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.scene.adapter.OperatroListAdapter;
import com.tuya.smart.android.demo.scene.presenter.OperatorListPresenter;
import com.tuya.smart.android.demo.scene.view.IOperatorListView;
import com.tuya.smart.home.sdk.bean.scene.dev.TaskListBean;

import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public class OperateorListActivity extends BaseActivity implements IOperatorListView, OperatroListAdapter.OnItemClickListener {
    private RecyclerView mRcv_operators;
    private OperatorListPresenter mPresenter;
    private OperatroListAdapter mAdapter;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_list);
        initToolbar();
        initMenu();
        initView();
        initAdapter();
        initPresenter();
    }


    private void initMenu() {
        setTitle(getString(R.string.ty_scene_operator_list));
    }

    private void initView() {
        mRcv_operators = findViewById(R.id.rcv_operators);
        mRcv_operators.setLayoutManager(new LinearLayoutManager(this));
        mEmptyView = findViewById(R.id.rl_empty);
    }

    private void initAdapter() {
        mAdapter = new OperatroListAdapter(this);
        mRcv_operators.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initPresenter() {
        mPresenter = new OperatorListPresenter(this, this);
        mPresenter.getOperatorList();
    }


    @Override
    public void showEmpty() {
        mEmptyView.setVisibility(View.VISIBLE);
        mRcv_operators.setVisibility(View.GONE);
    }

    @Override
    public void showOperators(List<TaskListBean> result) {
        mAdapter.setData(result);
    }

    @Override
    public void onItemClick(TaskListBean taskListBean) {
        mPresenter.selectOperatorValue(taskListBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mPresenter)mPresenter.onDestroy();
    }
}
