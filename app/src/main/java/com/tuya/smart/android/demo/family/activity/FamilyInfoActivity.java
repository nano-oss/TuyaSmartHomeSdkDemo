package com.tuya.smart.android.demo.family.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.base.utils.CollectionUtils;
import com.tuya.smart.android.demo.family.item.FamilyInfoItem;
import com.tuya.smart.android.demo.family.item.FamilyInfoMemberHead;
import com.tuya.smart.android.demo.family.item.FamilyInfoSpaceHead;
import com.tuya.smart.android.demo.family.item.FamilyMemberItem;
import com.tuya.smart.android.demo.family.presenter.FamilyInfoPresenter;
import com.tuya.smart.android.demo.family.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.android.demo.family.recyclerview.item.BaseItem;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.bean.MemberBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FamilyInfoActivity extends BaseActivity implements IFamilyInfoView {

    @BindView(R.id.family_info_rv)
    RecyclerView rv;

    public static final String KEY_HOME_ID = "home_id";

    private BaseRVAdapter<BaseItem> mAdapter;
    private FamilyInfoPresenter mPresenter;
    private long mHomeId;
    Unbinder unbinder;

    public static final int SECTION_ONE = 1;
    public static final int SECTION_TWO = 2;
    //家庭名称
    private FamilyInfoItem familyNameItem;
    //房间管理
    private FamilyInfoItem roomsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info);
        unbinder = ButterKnife.bind(this);
        mHomeId = getIntent().getLongExtra(KEY_HOME_ID, -1);

        initToolbar();
        initTitle();
        initAdapter();
        initPresenter();
    }


    private void initAdapter() {
        mAdapter = new BaseRVAdapter<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
    }

    private void initTitle() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.family_info_title));
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    private void initPresenter() {
        mPresenter = new FamilyInfoPresenter(this);
    }


    @OnClick(R.id.family_info_remove)
    public void onRemoveHomeClick() {
        mPresenter.removeHome();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public long getHomeId() {
        return mHomeId;
    }

    @Override
    public void setHomeData(HomeBean homeBean) {
        if (null == homeBean) {
            return;
        }

        familyNameItem = new FamilyInfoItem(Pair.create("家庭名称", homeBean.getName()));

        String roomValue = String.valueOf(null == homeBean.getRooms() ? 0 : homeBean.getRooms().size());
        roomsItem = new FamilyInfoItem(Pair.create("房间管理", roomValue));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addHeadView(SECTION_ONE, new FamilyInfoSpaceHead());
                mAdapter.addItemView(SECTION_ONE, familyNameItem);
                mAdapter.addItemView(SECTION_ONE, roomsItem);
            }
        });
    }

    @Override
    public void setMemberData(List<MemberBean> memberBeanList) {
        if (CollectionUtils.isEmpty(memberBeanList)) {
            return;
        }

        final List<BaseItem> memberItemList = new ArrayList<>();
        for (MemberBean memberBean : memberBeanList) {
            memberItemList.add(new FamilyMemberItem(memberBean));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addHeadView(SECTION_TWO, new FamilyInfoMemberHead());
                mAdapter.setItemViewList(SECTION_TWO, memberItemList);
            }
        });
    }

    @Override
    public void doRemoveView(boolean isSuccess) {
        hideLoading();
        if (isSuccess){
            finishActivity();
        }
        showToast(isSuccess?R.string.success:R.string.fail);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
        mPresenter.onDestroy();
    }
}
