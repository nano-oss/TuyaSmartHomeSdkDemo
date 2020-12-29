package com.tuya.smart.android.demo.camera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.camera.bean.TimePieceBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by huangdaju on 2018/3/5.
 */

public class CameraPlaybackTimeAdapter extends RecyclerView.Adapter<CameraPlaybackTimeAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    private List<TimePieceBean> timePieceBeans;
    private OnTimeItemListener listener;

    public CameraPlaybackTimeAdapter(Context context, List<TimePieceBean> timePieceBeans) {
        mInflater = LayoutInflater.from(context);
        this.timePieceBeans = timePieceBeans;
    }

    public void setListener(OnTimeItemListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.activity_camera_playback_time_tem, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TimePieceBean ipcVideoBean = timePieceBeans.get(position);
        holder.mTvStartTime.setText(timeFormat(ipcVideoBean.getStartTime() * 1000L));
        int lastTime = ipcVideoBean.getEndTime() - ipcVideoBean.getStartTime();
        holder.mTvDuration.setText("Duration:" + changeSecond(lastTime));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(ipcVideoBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timePieceBeans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTvStartTime;
        TextView mTvDuration;

        public MyViewHolder(final View view) {
            super(view);
            mTvStartTime = view.findViewById(R.id.time_start);
            mTvDuration = view.findViewById(R.id.time_duration);
        }
    }

    public static String timeFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String changeSecond(int seconds) {
        int temp;
        StringBuilder timer = new StringBuilder();
        temp = seconds / 3600;
        timer.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = seconds % 3600 / 60;
        timer.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");

        temp = seconds % 3600 % 60;
        timer.append((temp < 10) ? "0" + temp : "" + temp);
        return timer.toString();
    }

    public interface OnTimeItemListener {
        void onClick(TimePieceBean o);
    }
}
