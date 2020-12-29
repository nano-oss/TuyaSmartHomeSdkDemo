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
import com.tuya.smart.home.sdk.bean.scene.SceneCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * create by nielev on 2019-10-29
 */
public class SceneConditionAdapter extends RecyclerView.Adapter<SceneConditionAdapter.SceneCondtionViewHolder> {
    private Context mContext;
    private List<SceneCondition> mConditions = new ArrayList<>();
    public SceneConditionAdapter(Context context){
        mContext = context;
    }
    public void setData(List<SceneCondition> conditions){
        mConditions = conditions;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SceneCondtionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SceneCondtionViewHolder(View.inflate(mContext, R.layout.layout_conditon_task_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SceneCondtionViewHolder viewHolder, int i) {
        SceneCondition sceneCondition = mConditions.get(i);
        Picasso.with(mContext).load(Uri.parse(sceneCondition.getIconUrl())).into(viewHolder.iv);
        viewHolder.tv_devices.setText(sceneCondition.getEntityName());
        viewHolder.tv_dp.setText(sceneCondition.getExprDisplay());
    }

    @Override
    public int getItemCount() {
        return mConditions.size();
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
