package com.tuya.smart.android.demo.shortcut;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.NetworkUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.base.utils.AnimationUtil;
import com.tuya.smart.android.demo.base.utils.ToastUtil;
import com.tuya.smart.android.demo.base.view.IDeviceListFragmentView;
import com.tuya.smart.android.demo.device.DeviceShortcutAdapter;
import com.tuya.smart.android.demo.family.view.SwitchFamilyText;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class ShortcutDeviceActivity extends BaseActivity implements IDeviceListFragmentView {
    private ShortcutDevicePresenter mDeviceListFragmentPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DeviceShortcutAdapter mCommonDeviceAdapter;
    private ListView mDevListView;
    private TextView mNetWorkTip;
    private View mRlView;
    private View mAddDevView;
    private View mBackgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_device);
        initView();
        initAdapter();
        initSwipeRefreshLayout();
        initPresenter();
        mDeviceListFragmentPresenter.getData();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    mDeviceListFragmentPresenter.getDataFromServer();
                } else {
                    loadFinish();
                }
            }
        });
    }

    private void initAdapter() {
        mCommonDeviceAdapter = new DeviceShortcutAdapter(this);
        mDevListView.setAdapter(mCommonDeviceAdapter);
        mDevListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDeviceListFragmentPresenter.onDeviceClick((DeviceBean) parent.getAdapter().getItem(position));
            }
        });
    }

    @Override
    public void updateDeviceData(List<DeviceBean> myDevices) {
        if (mCommonDeviceAdapter != null) {
            mCommonDeviceAdapter.setData(myDevices);
        }
    }

    @Override
    public void loadStart() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    protected void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mNetWorkTip = (TextView) findViewById(R.id.network_tip);
        mDevListView = (ListView) findViewById(R.id.lv_device_list);
        mRlView = findViewById(R.id.rl_list);
        mAddDevView = findViewById(R.id.tv_empty_func);
        mBackgroundView = findViewById(R.id.list_background_tip);
        mAddDevView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(ShortcutDeviceActivity.this, "Open soon");
            }
        });
    }

    protected void initPresenter() {
        mDeviceListFragmentPresenter = new ShortcutDevicePresenter(this, this);
    }

    protected void initMenu() {
        SwitchFamilyText switchFamilyText = new SwitchFamilyText(getApplicationContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getToolBar().addView(switchFamilyText, layoutParams);
    }

    @Override
    public void loadFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showNetWorkTipView(int tipRes) {
        mNetWorkTip.setText(tipRes);
        if (mNetWorkTip.getVisibility() != View.VISIBLE) {
            AnimationUtil.translateView(mRlView, 0, 0, -mNetWorkTip.getHeight(), 0, 300, false, null);
            mNetWorkTip.setVisibility(View.VISIBLE);
        }
    }

    public void hideNetWorkTipView() {
        if (mNetWorkTip.getVisibility() != View.GONE) {
            AnimationUtil.translateView(mRlView, 0, 0, mNetWorkTip.getHeight(), 0, 300, false, null);
            mNetWorkTip.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBackgroundView() {
        BaseActivity.setViewGone(mDevListView);
        BaseActivity.setViewVisible(mBackgroundView);
    }

    @Override
    public void hideBackgroundView() {
        BaseActivity.setViewVisible(mDevListView);
        BaseActivity.setViewGone(mBackgroundView);
    }

    @Override
    public void gotoCreateHome() {

    }
}
