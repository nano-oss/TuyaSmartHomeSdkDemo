package com.tuya.smart.android.demo.scene.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.home.sdk.bean.scene.dev.TaskListBean;

import java.util.ArrayList;
import java.util.List;


/**
 * create by nielev on 2019-10-29
 */
public class OperatroListAdapter extends RecyclerView.Adapter<OperatroListAdapter.OperatorViewHolder> {
    private Context mContext;
    private List<TaskListBean> datas = new ArrayList<>();
    private OnItemClickListener mListener;
    public OperatroListAdapter(Context context){
        mContext = context;
    }
    public void setData(List<TaskListBean> data){
        datas.clear();
        datas.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OperatorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OperatorViewHolder(View.inflate(mContext, R.layout.layout_operator_list_item, null));

    }

    @Override
    public void onBindViewHolder(@NonNull OperatorViewHolder viewHolder, int i) {
        final TaskListBean taskListBean = datas.get(i);
        viewHolder.tv_operator.setText(taskListBean.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener) mListener.onItemClick(taskListBean);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(TaskListBean taskListBean);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    class OperatorViewHolder extends RecyclerView.ViewHolder{
        TextView tv_operator;
        public OperatorViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_operator = itemView.findViewById(R.id.tv_operator);
        }
    }
}
