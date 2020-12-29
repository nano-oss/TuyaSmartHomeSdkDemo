package com.tuya.smart.android.demo.personal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tuya.smart.android.demo.R;


/**
 * Created by Kunyang.Lee on 2017/9/15.
 */

public class FeedbackActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        findViewById(R.id.bt_get_feedback_list).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_get_feedback_list:
//                ITuyaFeedbackMag msg = TuyaHomeSdk.getTuyaFeekback().getFeedbackMsg("vdevo150304928090362", 2);
//                msg.getMsgList(new ITuyaDataCallback<List<FeedbackMsgBean>>() {
//                    @Override
//                    public void onSuccess(List<FeedbackMsgBean> feedbackMsgBeans) {
//                        Log.d("Feedback getMsgList: ", "onSuccess: " + JSON.toJSONString(feedbackMsgBeans));
//                    }
//
//                    @Override
//                    public void onError(String errorCode, String errorMsg) {
//                        Log.d("Feedback getMsgList: ", "onError: " + errorCode + ":" + errorMsg);
//                    }
//                });
                break;
        }
    }

}
