package com.tuya.smart.android.demo.shortcut;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;

import java.util.ArrayList;
import java.util.List;

public class OperateAdapter extends RecyclerView.Adapter<OperateAdapter.ViewHolder> {
    private List<OperateBean> mData;
    private LayoutInflater mLayoutInflater;
    private OnOperateItemClickListener mOnOperateItemClickListener = null;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mOnOperateItemClickListener != null){
                mOnOperateItemClickListener.onClick((String)v.getTag());
            }
        }
    };

    public interface  OnOperateItemClickListener{
        void onClick(String dpId);
    }

    public OperateAdapter(Context context) {
        mData = new ArrayList<>();
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnOperateItemClickListener(OnOperateItemClickListener mOnOperateItemClickListener) {
        this.mOnOperateItemClickListener = mOnOperateItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.layout_operate_item,viewGroup,false));
        viewHolder.itemView.setOnClickListener(mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.update(mData.get(i));
    }

    public void updateData(List<OperateBean> data){
        mData.clear();
        mData.addAll(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void update(OperateBean data){
            itemView.setTag(data.getId());
            ((TextView)itemView).setText(data.getContent());
        }
    }
}
