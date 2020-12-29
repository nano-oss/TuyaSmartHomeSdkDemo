package com.tuya.smart.android.demo.family.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.utils.ActivityUtils;
import com.tuya.smart.android.demo.base.utils.CollectionUtils;
import com.tuya.smart.android.demo.base.utils.SizeUtils;
import com.tuya.smart.android.demo.family.FamilyManager;
import com.tuya.smart.android.demo.family.activity.FamilyIndexActivity;
import com.tuya.smart.android.demo.family.item.FamilyPopWindowItem;
import com.tuya.smart.android.demo.family.recyclerview.adapter.BaseRVAdapter;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;

import java.util.ArrayList;
import java.util.List;

public class FamilyDropActionSheet extends PopupWindow {

    private static final String TAG = FamilyDropActionSheet.class.getSimpleName();
    private static final int MAX_ROW_COUNT = 5;
    private static final int ROW_HEIGHT = SizeUtils.dp2px(60);

    private BaseRVAdapter<FamilyPopWindowItem> mAdapter;
    private Context mContext;
    private View mRootView;
    private RecyclerView familyRv;
    private TextView familyTxt;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    public FamilyDropActionSheet(Context context) {
        super(context);
        this.mContext = context;
        mRootView = View.inflate(context, R.layout.popwindow_family_management, null);
        setContentView(mRootView);
        initWindow();
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        familyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeToFamilyIndex();
            }
        });
        mAdapter.setOnItemViewClickListener(new BaseRVAdapter.OnItemViewClickListener<FamilyPopWindowItem>() {
            @Override
            public void onItemViewClick(FamilyPopWindowItem item, int sectionKey, int sectionItemPosition) {
                mAdapter.notifyDataSetChanged();
                FamilyManager.getInstance().setCurrentHome(item.getData());
                dismiss();
            }
        });

    }

    private void initView() {
        familyRv = mRootView.findViewById(R.id.popwindow_family_management_rv);
        familyTxt = mRootView.findViewById(R.id.popwindow_family_management_txt);

        mAdapter = new BaseRVAdapter<>();
        familyRv.setLayoutManager(new LinearLayoutManager(mContext));
        familyRv.setAdapter(mAdapter);
    }

    private void initWindow() {
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());
    }

    @Override
    public void dismiss() {
        Window window = ((Activity) mContext).getWindow();
        if (null == window) {
            return;
        }
        setBackgroundAlpha(1.0f, window);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        super.dismiss();
    }

    public void showAtLocation(int gravity, int x, int y) {
        if (null == mContext) {
            Log.w(TAG, "showAtLocation mContext is null");
            return;
        }
        if (!(mContext instanceof Activity) || ((Activity) mContext).isFinishing()) {
            Log.e(TAG, "The context is invalid.");
            return;
        }

        Window window = ((Activity) mContext).getWindow();
        View view = window.peekDecorView();
        // 并透明阴影
        setBackgroundAlpha(0.6f, window);
        super.showAtLocation(view, gravity, x, y);
    }

    private void setBackgroundAlpha(float bgAlpha, Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha;
        window.setAttributes(lp);
    }


    private void initData() {
        FamilyManager.getInstance().getHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(final List<HomeBean> list) {
                if (CollectionUtils.isNotEmpty(list)) {
                    final List<FamilyPopWindowItem> itemList = new ArrayList<>();
                    for (HomeBean homeBean : list) {
                        itemList.add(new FamilyPopWindowItem(homeBean));
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (list.size() > MAX_ROW_COUNT) {
                                ViewGroup.LayoutParams params = familyRv.getLayoutParams();
                                params.height = (MAX_ROW_COUNT * ROW_HEIGHT);
                                familyRv.setLayoutParams(params);
                            }
                            mAdapter.setItemViewList(itemList);
                        }
                    });
                }
            }

            @Override
            public void onError(String s, String s1) {

            }
        });
    }

    private void routeToFamilyIndex() {
        if (null == mContext) {
            Log.w(TAG, "showAtLocation mContext is null");
            return;
        }
        if (!(mContext instanceof Activity) || ((Activity) mContext).isFinishing()) {
            Log.e(TAG, "The context is invalid.");
            return;
        }
        ActivityUtils.gotoActivity(((Activity) mContext),
                FamilyIndexActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
        dismiss();
    }
}
