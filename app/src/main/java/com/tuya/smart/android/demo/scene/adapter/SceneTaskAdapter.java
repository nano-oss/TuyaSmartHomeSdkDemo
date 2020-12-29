package com.tuya.smart.android.demo.scene.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.scene.SceneTask;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create by nielev on 2019-10-29
 */
public class SceneTaskAdapter extends RecyclerView.Adapter<SceneTaskAdapter.SceneCondtionViewHolder> {
    private Context mContext;
    private List<SceneTask> mTasks = new ArrayList<>();
    public SceneTaskAdapter(Context context){
        mContext = context;
    }
    public void setData(List<SceneTask> tasks){
        mTasks = tasks;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SceneCondtionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SceneCondtionViewHolder(View.inflate(mContext, R.layout.layout_conditon_task_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SceneCondtionViewHolder viewHolder, int i) {
        SceneTask sceneTask = mTasks.get(i);
        String entityId = sceneTask.getEntityId();
        DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(entityId);
        if(null != deviceBean){
            Picasso.with(mContext).load(Uri.parse(deviceBean.getIconUrl())).into(viewHolder.iv);
        }
        viewHolder.tv_devices.setText(sceneTask.getEntityName());
        Map<String, List<String>> actionDisplayNew = sceneTask.getActionDisplayNew();
        //actionDisplayNew最多只有一个key,value
        for (Map.Entry<String, List<String>> entry : actionDisplayNew.entrySet()) {
            List<String> value = entry.getValue();
            if(null != value && value.size() > 1){
                viewHolder.tv_dp.setText(value.get(0)+":"+value.get(1));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    class SceneCondtionViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv_devices;
        TextView tv_dp;
        public SceneCondtionViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv_devices = itemView.findViewById(R.id.tv_devices);
            tv_dp = itemView.findViewById(R.id.tv_dp);
        }
    }
}
