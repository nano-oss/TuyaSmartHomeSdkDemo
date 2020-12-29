package com.tuya.smart.android.demo.camera;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.camera.camerasdk.typlayer.callback.IRegistorIOTCListener;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OnP2PCameraListener;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationCallBack;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.ipccamerasdk.cloud.ITYCloudCamera;
import com.tuya.smart.camera.ipccamerasdk.cloud.TYCloudCamera;
import com.tuya.smart.camera.middleware.cloud.CameraCloudSDK;
import com.tuya.smart.camera.middleware.cloud.ICloudCacheManagerCallback;
import com.tuya.smart.camera.middleware.cloud.bean.CloudDayBean;
import com.tuya.smart.camera.middleware.cloud.bean.TimePieceBean;
import com.tuya.smart.camera.middleware.cloud.bean.TimeRangeBean;
import com.tuya.smart.camera.middleware.widget.AbsVideoViewCallback;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import com.tuya.smart.camera.utils.IPCCameraUtils;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.tuya.smart.android.demo.camera.utils.Constants.INTENT_P2P_TYPE;
import static com.tuya.smart.android.demo.device.common.CommonDeviceDebugPresenter.INTENT_DEVID;

/**
 * @author surgar
 */
public class CameraCloudStorageActivity extends AppCompatActivity implements ICloudCacheManagerCallback {

    private TuyaCameraView mVideoView;

    private String devId;
    private CameraCloudSDK cameraCloudSDK;
    private ITYCloudCamera cloudCamera;
    private List<CloudDayBean> dayBeanList = new ArrayList<>();
    private List<TimePieceBean> timePieceBeans = new ArrayList<>();
    private String mEncryptKey = "";
    private String mAuthorityJson = "";
    private int soundState;
    private int p2pType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_cloud_storage);

        devId = getIntent().getStringExtra(INTENT_DEVID);
        p2pType = getIntent().getIntExtra(INTENT_P2P_TYPE, -1);

        cameraCloudSDK = new CameraCloudSDK();
        cloudCamera = new TYCloudCamera();

        mVideoView = findViewById(R.id.camera_cloud_video_view);
        mVideoView.setViewCallback(new AbsVideoViewCallback() {
            @Override
            public void onCreated(Object o) {
                super.onCreated(o);
                if (o instanceof IRegistorIOTCListener) {
                    cloudCamera.generateCloudCameraView((IRegistorIOTCListener) o);
                }
            }
        });
        mVideoView.createVideoView(p2pType);

        String cachePath = getApplication().getCacheDir().getPath();
        cloudCamera.createCloudDevice(cachePath, devId);

        findViewById(R.id.status_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买页面
                cameraCloudSDK.getCameraCloudInfo(TuyaHomeSdk.getDataInstance().getDeviceBean(devId), CameraCloudStorageActivity.this);
            }
        });

        findViewById(R.id.buy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use cloud service purchase component
            }
        });

        findViewById(R.id.query_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询数据
                cameraCloudSDK.getCloudMediaCount(devId, TimeZone.getDefault().getID(), CameraCloudStorageActivity.this);
            }
        });

        findViewById(R.id.query_time_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里获取查询到的第一天时间数据
                if (dayBeanList.size() > 0) {
                    getAppointedDayCloudTimes(dayBeanList.get(0));
                }
            }
        });


        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePieceBeans.size() > 0) {
                    playCloudDataWithStartTime(timePieceBeans.get(0).getStartTime(), timePieceBeans.get(0).getEndTime(), true);
                }
            }
        });

        findViewById(R.id.pause_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausePlayCloudVideo();
            }
        });

        findViewById(R.id.resume_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumePlayCloudVideo();
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayCloudVideo();
            }
        });
        findViewById(R.id.camera_mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMuteValue(soundState == 0 ? 1 : 0);
            }
        });

        findViewById(R.id.snapshot_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snapshot();
            }
        });
        findViewById(R.id.record_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCloudRecordLocalMP4();
            }
        });
        findViewById(R.id.record_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCloudRecordLocalMP4();
            }
        });
    }

    private void getAppointedDayCloudTimes(CloudDayBean dayBean) {
        if (dayBean == null) {
            return;
        }
        getTimeLineInfoByTimeSlice(devId, String.valueOf(dayBean.getCurrentStartDayTime()), String.valueOf(dayBean.getCurrentDayEndTime()));
    }

    /**
     * 获取指定时间的时间片
     *
     * @param devId  设备id
     * @param timeGT 开始时间
     * @param timeLT 结束时间
     */
    void getTimeLineInfoByTimeSlice(String devId, String timeGT, String timeLT) {
        cameraCloudSDK.getTimeLineInfoByTimeSlice(devId, timeGT, timeLT, this);
    }

    /**
     * 根据时间片段始末获取相应的移动侦测数据
     *
     * @param devId  设备id
     * @param timeGT 开始时间
     * @param timeLT 结束时间
     * @param offset 第几页，默认0
     * @param limit  每次拉取条数，默认-1，表示所有数据
     */
    void getMotionDetectionByTimeSlice(String devId, final String timeGT, final String timeLT, int offset, int limit) {
        cameraCloudSDK.getMotionDetectionByTimeSlice(devId, timeGT, timeLT, offset, limit, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
        if (null != cloudCamera) {
            if (mVideoView.createdView() instanceof IRegistorIOTCListener) {
                cloudCamera.generateCloudCameraView((IRegistorIOTCListener) mVideoView.createdView());
            }
            cloudCamera.registorOnP2PCameraListener(new OnP2PCameraListener() {
                @Override
                public void receiveFrameDataForMediaCodec(int i, byte[] bytes, int i1, int i2, byte[] bytes1, boolean b, int i3) {

                }

                @Override
                public void onReceiveFrameYUVData(int i, ByteBuffer byteBuffer, ByteBuffer byteBuffer1, ByteBuffer byteBuffer2, int i1, int i2, int i3, int i4, long l, long l1, long l2, Object o) {

                }

                @Override
                public void onSessionStatusChanged(Object o, int i, int i1) {

                }

                @Override
                public void onReceiveAudioBufferData(int i, int i1, int i2, long l, long l1, long l2) {

                }

                @Override
                public void onReceiveSpeakerEchoData(ByteBuffer byteBuffer, int i) {

                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
        if (null != cloudCamera) {
            cloudCamera.removeOnP2PCameraListener();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != cameraCloudSDK) {
            cameraCloudSDK.onDestroy();
        }
        if (null != cloudCamera) {
            cloudCamera.deinitCloudCamera();
        }
    }

    @Override
    public void getCloudDayList(List<CloudDayBean> cloudDayBeanList) {
        //获取云存储有数据的日期
        dayBeanList.clear();
        dayBeanList.addAll(cloudDayBeanList);
    }

    @Override
    public void getCloudSecret(String encryKey) {
        mEncryptKey = encryKey;
    }

    @Override
    public void getAuthorityGet(String authorityJson) {
        mAuthorityJson = authorityJson;
    }

    @Override
    public void getTimePieceInfoByTimeSlice(List<TimePieceBean> list) {
        //获取指定时间相应的时间片段信息:对应getTimeLineInfoByTimeSlice
        timePieceBeans.clear();
        timePieceBeans.addAll(list);
    }

    @Override
    public void getMotionDetectionByTimeSlice(List<TimeRangeBean> list) {
        //获取指定时间相应的移动侦测数据
    }

    @Override
    public void onError(int errorCode) {
        Toast.makeText(this, "errorCode ==" + errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCloudStatusSuccess(int i) {
        //返回云存储状态
        Toast.makeText(this, "current state ==" + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCloudConfigDataTags(String config) {
        //获取云存储 对应的配置信息，需要传入sdk进行鉴权 ，getTimeLineInfoByTimeSlice
        cloudCamera.configCloudDataTagsV1(config, new OperationDelegateCallBack() {

            @Override
            public void onSuccess(int i, int i1, String s) {
                //成功之后开始播放
                if (timePieceBeans.size() > 0) {
                    int startTime = timePieceBeans.get(0).getStartTime();
                    playCloudDataWithStartTime(startTime, (int) (getTodayEnd(startTime * 1000L) / 1000) - 1, true);
                }
            }

            @Override
            public void onFailure(int i, int i1, int i2) {

            }
        });
    }

    long getTodayEnd(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentTime));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }


    /**
     * @param startTime
     * @param endTime
     * @param isEvent   是否是侦测事件
     */
    private void playCloudDataWithStartTime(int startTime, int endTime, final boolean isEvent) {
        cloudCamera.playCloudDataWithStartTime(startTime, endTime, isEvent,
                mAuthorityJson, mEncryptKey,
                new OperationCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data, Object camera) {
                        // 播放中的回调, playing
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode, Object camera) {

                    }
                }, new OperationCallBack() {
                    @Override
                    public void onSuccess(int sessionId, int requestId, String data, Object camera) {
                        //播放完成的回调, playCompleted
                    }

                    @Override
                    public void onFailure(int sessionId, int requestId, int errCode, Object camera) {
                    }
                });
    }

    /**
     * play resume
     */
    private void resumePlayCloudVideo() {
        cloudCamera.resumePlayCloudVideo(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {

            }
        });
    }

    /**
     * play pause
     */
    private void pausePlayCloudVideo() {
        cloudCamera.pausePlayCloudVideo(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {

            }
        });
    }

    /**
     * play stop
     */
    private void stopPlayCloudVideo() {
        cloudCamera.stopPlayCloudVideo(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
            }
        });
    }

    /**
     * record start
     */
    public void startCloudRecordLocalMP4() {
        cloudCamera.startRecordLocalMp4(IPCCameraUtils.recordPath(devId), String.valueOf(System.currentTimeMillis()), new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                Toast.makeText(CameraCloudStorageActivity.this, "record start success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
            }
        });
    }

    /**
     * record stop
     */
    public void stopCloudRecordLocalMP4() {
        cloudCamera.stopRecordLocalMp4(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                Toast.makeText(CameraCloudStorageActivity.this, "record end success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
            }
        });
    }

    /**
     * 截图
     */
    public void snapshot() {
        cloudCamera.snapshot(IPCCameraUtils.recordSnapshotPath(devId), new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                Toast.makeText(CameraCloudStorageActivity.this, "snapshot success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
            }
        });
    }

    /**
     * 设置音量
     *
     * @param mute 值：ICameraP2P.UNMUTE , ICameraP2P.MUTE
     */
    public void setMuteValue(int mute) {
        cloudCamera.setCloudMute(mute, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                soundState = Integer.valueOf(data);
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
            }
        });
    }

    /**
     * 获取音量
     */
    public void getMuteValue() {
        cloudCamera.getCloudMute();
    }

}
