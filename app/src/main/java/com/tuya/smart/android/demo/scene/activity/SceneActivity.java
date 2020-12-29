package com.tuya.smart.android.demo.scene.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.scene.adapter.SceneConditionAdapter;
import com.tuya.smart.android.demo.scene.adapter.SceneTaskAdapter;
import com.tuya.smart.android.demo.scene.presenter.ScenePresenter;
import com.tuya.smart.android.demo.scene.view.ISceneView;
import com.tuya.smart.home.sdk.bean.scene.SceneCondition;
import com.tuya.smart.home.sdk.bean.scene.SceneTask;

import java.util.List;

/**
 * create by nielev on 2019-10-28
 */
public class SceneActivity extends BaseActivity implements View.OnClickListener, ISceneView {


    private ScenePresenter mPresenter;
    private View mTv_condition;
    private View mTv_task;
    private View mBtn_add_condition;
    private View mBtn_add_task;
    private RecyclerView mRcv_condition;
    private RecyclerView mRcv_task;
    private SceneConditionAdapter mCondtionAdapter;
    private SceneTaskAdapter mTaskAdapter;
    private Button mBtn_add_bg;
    private ImageView mIv_bg;
    private EditText mEt_add_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        initToolbar();
        initMenu();
        initView();
        initAdapter();
        initPresenter();
    }

    private void initMenu() {
        setTitle(getString(R.string.home_scene));
        setMenu(R.menu.toolbar_save, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mPresenter.save();
                return false;
            }
        });
    }

    private void initView() {
        mBtn_add_condition = findViewById(R.id.btn_add_condition);
        mBtn_add_condition.setOnClickListener(this);
        mBtn_add_task = findViewById(R.id.btn_add_task);
        mBtn_add_task.setOnClickListener(this);
        mTv_condition = findViewById(R.id.tv_condition);
        mTv_task = findViewById(R.id.tv_task);
        mRcv_condition = findViewById(R.id.rcv_condition);
        mRcv_task = findViewById(R.id.rcv_task);
        mRcv_condition.setLayoutManager(new LinearLayoutManager(this));
        mRcv_task.setLayoutManager(new LinearLayoutManager(this));
        mBtn_add_bg = findViewById(R.id.btn_add_bg);
        mBtn_add_bg.setOnClickListener(this);
        mIv_bg = findViewById(R.id.iv_bg);
        mEt_add_name = findViewById(R.id.et_add_name);
    }

    private void initAdapter() {
        mCondtionAdapter = new SceneConditionAdapter(this);
        mTaskAdapter = new SceneTaskAdapter(this);
        mRcv_condition.setAdapter(mCondtionAdapter);
        mRcv_task.setAdapter(mTaskAdapter);
    }

    private void initPresenter() {
        mPresenter = new ScenePresenter(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_condition:
                mPresenter.addCondition();
                break;
            case R.id.btn_add_task:
                mPresenter.addTask();
                break;
            case R.id.btn_add_bg:
                mPresenter.addBg();
                default:break;
        }
    }

    @Override
    public void showSceneView() {
        mTv_condition.setVisibility(View.GONE);
        mBtn_add_condition.setVisibility(View.GONE);
    }

    @Override
    public void showAutoView() {
        mTv_condition.setVisibility(View.VISIBLE);
        mBtn_add_condition.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateConditions(List<SceneCondition> conditions) {
        mCondtionAdapter.setData(conditions);
    }

    @Override
    public void updateTasks(List<SceneTask> tasks) {
        mTaskAdapter.setData(tasks);
    }

    @Override
    public void showBg(String s) {
        Picasso.with(this).load(s).into(mIv_bg);
    }

    @Override
    public String getSceneName() {
        return mEt_add_name.getText().toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mPresenter)mPresenter.onDestroy();
    }
}
