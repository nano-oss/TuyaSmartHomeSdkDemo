package com.tuya.smart.android.demo.scene.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.home.sdk.bean.scene.SceneBean;

import java.util.ArrayList;
import java.util.List;

import static com.tuya.smart.android.demo.base.presenter.SceneListPresenter.SMART_TYPE_SCENE;


/**
 * create by nielev on 2019-10-28
 */
public class SmartAdapter extends RecyclerView.Adapter<SmartAdapter.SceneViewHolder> {
    private List<SceneBean> datas = new ArrayList<>();
    private int mType;
    private Context mContext;
    private OnExecuteListener mExecuteListener;
    private OnSwitchListener mSwitchListener;
    public SmartAdapter(Context context, int type){
        mType = type;
        mContext = context;
    }

    public void setData(List<SceneBean> scenes){

        datas.clear();
        datas.addAll(scenes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SceneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SceneViewHolder sceneViewHolder = new SceneViewHolder(View.inflate(mContext, R.layout.layout_scene_item, null));

        return sceneViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SceneViewHolder viewHolder, int i) {
        final SceneBean sceneBean = datas.get(i);
        Picasso.with(mContext).load(Uri.parse(sceneBean.getBackground())).into(viewHolder.scene_bg);
        viewHolder.tv_title.setText(sceneBean.getName());
        if(mType == SMART_TYPE_SCENE){
            viewHolder.tv_excute.setVisibility(View.VISIBLE);
            viewHolder.tv_excute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mExecuteListener) mExecuteListener.onExecute(sceneBean);
                }
            });
            viewHolder.swtich.setVisibility(View.GONE);
        } else {
            viewHolder.tv_excute.setVisibility(View.GONE);
            viewHolder.swtich.setVisibility(View.VISIBLE);
            viewHolder.swtich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mSwitchListener) mSwitchListener.onSwitchAutomation(sceneBean);
                }
            });
        }

    }

    public void setOnExecuteListener(OnExecuteListener listener){
        mExecuteListener = listener;
    }
    public void setOnSwitchListener(OnSwitchListener listener){
        mSwitchListener = listener;
    }
    public interface OnExecuteListener {
        void onExecute(SceneBean bean);
    }
    public interface OnSwitchListener {
        void onSwitchAutomation(SceneBean bean);
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    class SceneViewHolder extends RecyclerView.ViewHolder{
        ImageView scene_bg;
        TextView tv_title;
        TextView tv_excute;
        SwitchButton swtich;
        public SceneViewHolder(@NonNull View itemView) {
            super(itemView);
            scene_bg = itemView.findViewById(R.id.scene_bg);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_excute = itemView.findViewById(R.id.tv_excute);
            swtich = itemView.findViewById(R.id.swtich);
        }
    }

}
