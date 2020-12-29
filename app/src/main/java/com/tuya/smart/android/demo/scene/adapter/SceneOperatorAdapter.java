package com.tuya.smart.android.demo.scene.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.scene.bean.OperatorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * create by nielev on 2019-10-30
 */
public class SceneOperatorAdapter extends RecyclerView.Adapter<SceneOperatorAdapter.SceneOperatorViewHolder> {
    private Context mContext;
    private List<OperatorBean> datas = new ArrayList<>();
    private OnItemClickListener listener;
    public SceneOperatorAdapter(Context context){
        this.mContext = context;
    }

    public void setDatas(List<OperatorBean> operatorBeans){
        datas.clear();
        datas.addAll(operatorBeans);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SceneOperatorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SceneOperatorViewHolder(View.inflate(mContext, R.layout.layout_scene_operator_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SceneOperatorViewHolder viewHolder, int i) {
        final OperatorBean bean = datas.get(i);
        viewHolder.cb.setChecked(bean.isChecked());
        viewHolder.tv.setText(bean.getValue());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OperatorBean b : datas) {
                    b.setChecked(false);
                }
                bean.setChecked(true);
                notifyDataSetChanged();
                if(null != listener){
                    listener.onItemClick(bean);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(OperatorBean bean);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    class SceneOperatorViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        CheckBox cb;
        public SceneOperatorViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_operator_value);
            cb = itemView.findViewById(R.id.cb);
        }
    }
}
