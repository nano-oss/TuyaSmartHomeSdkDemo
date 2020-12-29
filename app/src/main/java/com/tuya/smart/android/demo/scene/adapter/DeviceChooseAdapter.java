package com.tuya.smart.android.demo.scene.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public class DeviceChooseAdapter extends RecyclerView.Adapter<DeviceChooseAdapter.DeviceChoosViewHolder> {
    private Context mContext;
    private List<DeviceBean> deviceBeans = new ArrayList<>();
    private OnItemClickListener mListener;
    public DeviceChooseAdapter(Context context){
        mContext = context;
    }

    public void setDatas(List<DeviceBean> devices){
        deviceBeans.clear();
        deviceBeans.addAll(devices);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceChoosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DeviceChoosViewHolder holder = new DeviceChoosViewHolder(View.inflate(mContext, R.layout.layout_device_choose_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceChoosViewHolder viewHolder, int i) {
        final DeviceBean deviceBean = deviceBeans.get(i);
        Picasso.with(mContext).load(deviceBean.getIconUrl()).into(viewHolder.iv);
        viewHolder.tv.setText(deviceBean.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener) mListener.onItemClick(deviceBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceBeans.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(DeviceBean deviceBean);
    }
    class DeviceChoosViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        public DeviceChoosViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_device);
            tv = itemView.findViewById(R.id.tv_device);
        }
    }
}
