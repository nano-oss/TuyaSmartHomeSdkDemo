package com.tuya.smart.android.demo.scene.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.scene.adapter.SceneOperatorAdapter;
import com.tuya.smart.android.demo.scene.bean.OperatorBean;
import com.tuya.smart.android.demo.scene.presenter.OperatorValuePresenter;
import com.tuya.smart.android.demo.scene.view.IOperatorValueView;
import com.tuya.smart.android.demo.test.widget.NumberPicker;
import com.tuya.smart.android.device.bean.ValueSchemaBean;
import com.tuya.smart.home.sdk.bean.scene.dev.TaskListBean;

import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public class OperatorValueActivity extends BaseActivity implements IOperatorValueView, SceneOperatorAdapter.OnItemClickListener {

    private RecyclerView mRcv_operator_value;
    private LinearLayout mLl_value;
    private NumberPicker mNp_operators;
    private NumberPicker mNp_operator_value;
    private OperatorValuePresenter mPresenter;
    private List<String> mOperators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_value);
        initToolbar();
        initMenu();
        initView();
        initPresenter();
    }


    private void initMenu() {
        setTitle(getString(R.string.ty_operator_value));
        setMenu(R.menu.toolbar_next, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mPresenter.saveOperator();
                return false;
            }
        });
    }

    private void initView() {
        mRcv_operator_value = findViewById(R.id.rcv_operator_value);
        mLl_value = findViewById(R.id.ll_value);
        mNp_operators = findViewById(R.id.np_operators);
        mNp_operator_value = findViewById(R.id.np_operator_value);
    }

    private void initPresenter() {
        mPresenter = new OperatorValuePresenter(this, this);
    }

    @Override
    public void showValueView(boolean isCondition, TaskListBean taskListBean) {
        mRcv_operator_value.setVisibility(View.GONE);
        mNp_operator_value.setVisibility(View.VISIBLE);
        if(isCondition){
            mOperators = taskListBean.getOperators();
            mNp_operators.setVisibility(View.VISIBLE);
            mNp_operators.setMinValue(0);
            mNp_operators.setMaxValue(mOperators.size() - 1);
            mNp_operators.setDisplayedValues(getChooseOperatorList(mOperators));
        } else {
            mNp_operators.setVisibility(View.GONE);
        }

        final ValueSchemaBean valueSchemaBean = taskListBean.getValueSchemaBean();
        if(null != valueSchemaBean){
            int max = valueSchemaBean.getMax();
            int min = valueSchemaBean.getMin();
            int npMaxValue = (max - min) / valueSchemaBean.getStep();
            final double scale = Math.pow(10,valueSchemaBean.getScale());
            NumberPicker.Formatter formatter  = new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    if (scale == 1){
                        return String.valueOf((int) getShowValue(valueSchemaBean, value , scale)) + valueSchemaBean.getUnit();
                    }
                    return String.format("%."+valueSchemaBean.getScale()+"f",getShowValue(valueSchemaBean, value , scale)) + valueSchemaBean.getUnit();
                }
            };
            mNp_operator_value.setFormatter(formatter);
            mNp_operator_value.setMinValue(0);
            mNp_operator_value.setMaxValue(npMaxValue);
        }

    }

    protected double getShowValue(ValueSchemaBean valueSchemaBean, double curValue, double scale) {
        double showValue = ( valueSchemaBean.getMin() + curValue * valueSchemaBean.getStep() ) / scale;
        if (showValue > valueSchemaBean.getMax()) {
            showValue = valueSchemaBean.getMax();
        }
        return showValue;
    }
    @Override
    public void showEnumOrBooleanValueView(List<OperatorBean> operatorBeans) {
        SceneOperatorAdapter adapter = new SceneOperatorAdapter(this);
        mRcv_operator_value.setVisibility(View.VISIBLE);
        mRcv_operator_value.setAdapter(adapter);
        mRcv_operator_value.setLayoutManager(new LinearLayoutManager(this));
        mLl_value.setVisibility(View.GONE);
        adapter.setDatas(operatorBeans);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public String getOperator() {
        if(null != mOperators && !mOperators.isEmpty()){
            return mOperators.get(mNp_operators.getValue());
        }
        return "=";
    }

    @Override
    public int getValueTypeChoose(ValueSchemaBean valueSchemaBean) {
        return valueSchemaBean.getMin() + mNp_operator_value.getValue() * valueSchemaBean.getStep();
    }


    public static String[] getChooseOperatorList(List<String> operators) {
        String[] ops = new String[operators.size()];
        for (int i = 0; i< operators.size() ; i++) {
            ops[i] = operators.get(i);
        }
        return ops;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mPresenter)mPresenter.onDestroy();
    }

    @Override
    public void onItemClick(OperatorBean bean) {
        mPresenter.selectItem(bean);
    }
}
