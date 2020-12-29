package com.tuya.smart.android.demo.camera.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.tuya.drawee.view.DecryptImageView;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.ipc.messagecenter.bean.CameraMessageBean;

import java.util.List;

/**
 * Created by huangdaju on 2018/3/5.
 */

public class AlarmDetectionAdapter extends RecyclerView.Adapter<AlarmDetectionAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private List<CameraMessageBean> cameraMessageBeans;
    private OnItemListener listener;

    public AlarmDetectionAdapter(Context context, List<CameraMessageBean> cameraMessageBeans) {
        mInflater = LayoutInflater.from(context);
        this.cameraMessageBeans = cameraMessageBeans;
    }

    public void updateAlarmDetectionMessage(List<CameraMessageBean> messageBeans) {
        if (null != cameraMessageBeans) {
            cameraMessageBeans.clear();
            cameraMessageBeans.addAll(messageBeans);
            notifyDataSetChanged();
        }
    }

    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.camera_newui_more_motion_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CameraMessageBean ipcVideoBean = cameraMessageBeans.get(position);
        holder.mTvStartTime.setText(ipcVideoBean.getDateTime());
        holder.mTvDescription.setText(ipcVideoBean.getMsgTypeContent());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null != listener) {
                    listener.onLongClick(ipcVideoBean);
                }
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listener) {
                    listener.onItemClick(ipcVideoBean);
                }
            }
        });
        holder.showPicture(ipcVideoBean);
    }

    @Override
    public int getItemCount() {
        return cameraMessageBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvStartTime;
        private TextView mTvDescription;
        private DecryptImageView mSnapshot;

        public MyViewHolder(final View view) {
            super(view);
            mTvStartTime = view.findViewById(R.id.tv_time_range_start_time);
            mTvDescription = view.findViewById(R.id.tv_alarm_detection_description);
            mSnapshot = view.findViewById(R.id.iv_time_range_snapshot);
        }


        private void showPicture(CameraMessageBean cameraMessageBean) {
            String attachPics = cameraMessageBean.getAttachPics();
            mSnapshot.setVisibility(View.VISIBLE);
            if (attachPics.contains("@")) {
                int index = attachPics.lastIndexOf("@");
                try {
                    String decryption = attachPics.substring(index + 1);
                    String imageUrl = attachPics.substring(0, index);
                    mSnapshot.setImageURI(imageUrl, decryption.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Uri uri = null;
                try {
                    uri = Uri.parse(attachPics);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri).build();
                mSnapshot.setController(controller);
            }
        }
    }


    public interface OnItemListener {
        void onLongClick(CameraMessageBean o);

        void onItemClick(CameraMessageBean o);
    }

}
