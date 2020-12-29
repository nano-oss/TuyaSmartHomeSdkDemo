package com.tuya.smart.android.demo.shortcut;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.shortcutparser.api.IShortcutParserManager;
import com.tuya.smart.android.shortcutparser.api.IBoolDpControl;
import com.tuya.smart.android.shortcutparser.api.IClientParser;
import com.tuya.smart.android.shortcutparser.api.IDpControl;
import com.tuya.smart.android.shortcutparser.api.IEnumDpControl;
import com.tuya.smart.android.shortcutparser.api.INumDpControl;
import com.tuya.smart.android.shortcutparser.impl.ShortcutParserManager;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class ShortcutOperateDeviceActivity extends AppCompatActivity implements IShortcutOperateView{
    public static final String EXTRA_DEVICE_ID = "extra_device_id";

    private TextView mTvDeviceName;
    private TextView mTvShortcutSwitch;
    private RecyclerView mRvShortcutDp;
    private OperateAdapter mOperateAdapter;

    private DeviceBean mDev;

    private IShortcutParserManager mShortcutParserManager;
    private ShortcutOperatePresenter mShortcutOperatePresenter;
    private IClientParser mClientParserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_operate_device);
        initView();
        initData();
        updateView();
    }

    private void initView(){
        mRvShortcutDp = findViewById(R.id.rv_dp_operate);
        mRvShortcutDp.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mTvDeviceName = findViewById(R.id.tv_device_name);
        mTvShortcutSwitch = findViewById(R.id.tv_shortcut_switch);

        mTvShortcutSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShortcutOperatePresenter.operate(mClientParserBean.getSwitchDpParse().getDps(
                        !((boolean)mClientParserBean.getSwitchDpParse().getCurDpValue())));
            }
        });
    }

    private void initData(){
        mShortcutParserManager = new ShortcutParserManager();
        mOperateAdapter = new OperateAdapter(getApplicationContext());
        mOperateAdapter.setOnOperateItemClickListener(new OperateAdapter.OnOperateItemClickListener() {
            @Override
            public void onClick(String dpId) {
                operate(dpId);
            }
        });

        String devId = getIntent().getStringExtra(EXTRA_DEVICE_ID);
        mDev = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
        mClientParserBean = mShortcutParserManager.getDeviceParseData(mDev);

        mShortcutOperatePresenter = new ShortcutOperatePresenter(getApplicationContext(),mDev,this);
    }

    @Override
    public void updateView(){
        if(mDev == null){
            mTvDeviceName.setText("data illegal");
        }else {
            mTvDeviceName.setText(mDev.getName());
            mClientParserBean.update(mDev.getDps(),mDev.getDpName());
            if(mClientParserBean.getSwitchStatus().equals(IClientParser.SHORTCUT_SWITCH_STATUS.SWITCH_NONE)){
                BaseActivity.setViewGone(mTvShortcutSwitch);
            }else{
                mTvShortcutSwitch.setText(getBoolDpText((IBoolDpControl) mClientParserBean.getSwitchDpParse()));
            }

            if(!mClientParserBean.getDpShortcutControlList().isEmpty()){
                mRvShortcutDp.setAdapter(mOperateAdapter);
                mOperateAdapter.updateData(generateShowData(mClientParserBean.getDpShortcutControlList()));
                mOperateAdapter.notifyDataSetChanged();
            }
        }


    }

    private void operate(String dpId){
        IDpControl desControlBean = null;
        for (Object parseBean : mClientParserBean.getDpShortcutControlList()){
            if(dpId.equals(((IDpControl)parseBean).getDpId())){
                desControlBean = (IDpControl)parseBean;
                break;
            }
        }

        if(desControlBean != null){
            Object value = null;

            if(desControlBean instanceof IBoolDpControl){
                value = !((boolean)desControlBean.getCurDpValue());
            }else if(desControlBean instanceof IEnumDpControl){
                int index = ((IEnumDpControl) desControlBean).getRangList().indexOf(desControlBean.getCurDpValue());
                index ++;
                if(index >= ((IEnumDpControl) desControlBean).getRangList().size()){
                    index = 0;
                }
                value =  ((IEnumDpControl) desControlBean).getRangList().get(index);
            }else if(desControlBean instanceof INumDpControl){
                value = ((int)desControlBean.getCurDpValue()) + 1;
            }

            mShortcutOperatePresenter.operate(desControlBean.getDps(value));
        }
    }

    private List<OperateBean> generateShowData(List<IDpControl> parseBeanList ){
        List<OperateBean> data = new ArrayList<>();

        for (Object parseBean : parseBeanList){
            OperateBean operateBean = new OperateBean();
            if(parseBean instanceof IBoolDpControl){
                operateBean.setId(((IBoolDpControl) parseBean).getDpId());
                operateBean.setContent(new SpannableString(getBoolDpText((IBoolDpControl)parseBean)));
                data.add(operateBean);
            }else if(parseBean instanceof IEnumDpControl){
                operateBean.setId(((IEnumDpControl) parseBean).getDpId());
                operateBean.setContent(getEnumDpData((IEnumDpControl)parseBean));
                data.add(operateBean);
            }else if(parseBean instanceof INumDpControl){
                operateBean.setId(((INumDpControl) parseBean).getDpId());
                operateBean.setContent(getNumDpData((INumDpControl)parseBean));
                data.add(operateBean);
            }
        }

        return data;
    }

    private SpannableString getEnumDpData(IEnumDpControl enumDpControlBean){
        int startIndex = 0;
        int endIndex = 0;
        boolean found = false;

        for (int i = 0;i < enumDpControlBean.getRangList().size();i ++){
            startIndex = endIndex;
            endIndex += enumDpControlBean.getDisplayNameList().get(i).length();
            if(enumDpControlBean.getCurDpValue().equals(enumDpControlBean.getRangList().get(i))){
                found = true;
                break;
            }
        }

        if(found){
            StringBuilder content = new StringBuilder();
            for (String name : enumDpControlBean.getDisplayNameList()){
                content.append(name);
            }

            content.append("[click to shift]");
            SpannableString data = new SpannableString(content.toString());
            data.setSpan(new ForegroundColorSpan(Color.MAGENTA), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return data;
        }else {
            return  new SpannableString("enum data parse fail.");
        }
    }

    private SpannableString getNumDpData(INumDpControl numDpControlBean){
        return  new SpannableString(numDpControlBean.getStatus() + numDpControlBean.getUnit());
    }

    private String getBoolDpText(IBoolDpControl dpControlBean){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("shortcut switch:");
        stringBuilder.append(dpControlBean.getStatus());
        stringBuilder.append("[click to shift]");
        return stringBuilder.toString();
    }


}
