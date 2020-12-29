package com.tuya.smart.android.demo.family.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.base.utils.CollectionUtils;
import com.tuya.smart.android.demo.family.item.FamilyIndexEmpty;
import com.tuya.smart.android.demo.family.item.FamilyIndexFoot;
import com.tuya.smart.android.demo.family.item.FamilyIndexItem;
import com.tuya.smart.android.demo.family.presenter.FamilyIndexPresenter;
import com.tuya.smart.android.demo.family.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.home.sdk.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FamilyIndexActivity extends BaseActivity implements IFamilyIndexView {
    @BindView(R.id.family_index_rv)
    RecyclerView rv;

    private FamilyIndexPresenter mPresenter;
    private BaseRVAdapter<FamilyIndexItem> mAdapter;

    Unbinder unbinder;
    private FamilyIndexFoot addFamilyFoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_index);
        unbinder = ButterKnife.bind(this);
        initToolbar();
        initTitle();
        initAdapter();
        initListener();

        initPresenter();
    }

    private void initListener() {
        mAdapter.setOnItemViewClickListener(new BaseRVAdapter.OnItemViewClickListener<FamilyIndexItem>() {
            @Override
            public void onItemViewClick(FamilyIndexItem item, int sectionKey, int sectionItemPosition) {
                routeInfoActivity(item.getData().getHomeId());
            }
        });
    }

    private void initAdapter() {
        mAdapter = new BaseRVAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
    }

    private void initTitle() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.family_index_title));
        mToolBar.setTitleTextColor(Color.WHITE);
    }


    private void initPresenter() {
        mPresenter = new FamilyIndexPresenter(this);
    }


    private void routeAddActivity() {
        ActivityUtils.gotoActivity(this,
                FamilyAddActivity.class,
                ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM,
                false);
    }


    private void routeInfoActivity(long homeId) {
        Intent intent = new Intent(this, FamilyInfoActivity.class);
        intent.putExtra(FamilyInfoActivity.KEY_HOME_ID, homeId);
        ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM, false);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.getFamilyList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        mPresenter.onDestroy();
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setFamilyList(List<HomeBean> homeBeanList) {
        if (CollectionUtils.isEmpty(homeBeanList)) {
            return;
        }
        final List<FamilyIndexItem> homeItems = new ArrayList<>();
        for (HomeBean homeBean : homeBeanList) {
            homeItems.add(new FamilyIndexItem(homeBean));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setItemViewList(homeItems);
                if (null == addFamilyFoot) {
                    addFamilyFoot = new FamilyIndexFoot();
                    addFamilyFoot.setOnAddFamilyListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            routeAddActivity();
                        }
                    });
                    mAdapter.addFootView(addFamilyFoot);
                }
            }
        });

    }

    @Override
    public void showFamilyEmptyView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FamilyIndexEmpty emptyView = new FamilyIndexEmpty();
                emptyView.setOnAddFamilyListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        routeAddActivity();
                    }
                });
                mAdapter.showEmptyView(emptyView);
            }
        });
    }


}
