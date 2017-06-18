package com.xygame.sg.activity.personal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.bean.IdentyTranBean;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;

/**
 * Created by tony on 2016/2/24.
 */
public class CertifyVideoActivity extends SGBaseActivity implements Callback, View.OnClickListener {
    private View backButton;
    private String mVecordFile = null;// 文件
    private SurfaceView mSurfaceview;
    private boolean mStartedFlg = false;
    private TextView titleName;
    private MediaRecorder mRecorder;// 录制视频的类
    private SurfaceHolder mSurfaceHolder;// 显示视频
    private Camera camera;
    private TextView timeView;// 在屏幕顶部显示录制时间
    private int time = 0;
    private ImageView mShootBtn;
    private LinearLayout ctrol_ll;
    private TextView restart;
    private TextView submit;
    private boolean isPlay;
    private IdentyTranBean identyTranBean;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:// 开始录制
                    timeView.setText(time + "s");
                    break;
                case 2:// 录制8s结束
                    isPlay=true;
                    mRecorder.stop();
                    mRecorder.reset(); // You can reuse the object by
                    mStartedFlg = false;
                    time = 0;
//                    mShootBtn.setVisibility(View.VISIBLE);
//                    mShootBtn.setImageResource(R.drawable.stoprecord);
                    ctrol_ll.setVisibility(View.VISIBLE);
                    break;
                case 3:// 体检结束
                    String imageUrl = (String) msg.obj;
                    identyTranBean.setVideoUrl(imageUrl);
                    ShowMsgDialog.cancel();
                    Intent intent = new Intent(CertifyVideoActivity.this,ModelIdentyThirdActivity.class);
                    intent.putExtra("identyTranBean", identyTranBean);
                    startActivity(intent);
                    finish();
                    break;
                case 4:
                    ShowMsgDialog.cancel();
                    Toast.makeText(getApplicationContext(), "视频上传失败", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }

    };
    private CamcorderProfile mProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT); // 选择支持半透明模式,在有surfaceview的activity中使用。
        setContentView(R.layout.certify_vido_layout);// 加载布局
        ctrol_ll = (LinearLayout) findViewById(R.id.ctrol_ll);
        timeView = (TextView) findViewById(R.id.time_tv);
        mSurfaceview = (SurfaceView) findViewById(R.id.shipin);
        mShootBtn = (ImageView) findViewById(R.id.shoot_button);
        restart = (TextView) findViewById(R.id.restart);
        submit = (TextView) findViewById(R.id.submit);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getString(R.string.title_activity_model_identy_second));
        restart.setOnClickListener(this);
        submit.setOnClickListener(this);
        mShootBtn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        mSurfaceHolder = mSurfaceview.getHolder();// 取得holder
        identyTranBean = (IdentyTranBean) getIntent().getSerializableExtra("identyTranBean");
        mSurfaceHolder.addCallback(this); // holder加入回调接口

        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// setType必须设置，要不出错.
        mProfile = CamcorderProfile.get(0, 6);
        isPlay=false;
    }

    public void start() {
        timeView.setText("1s");
        // TODO Auto-generated method stub
        if (!mStartedFlg) {
            isPlay=false;
            // 开始
            if (mRecorder == null) {
                mRecorder = new MediaRecorder(); // 创建mediarecorder的对象
            }
            try {
                camera.unlock();
                mRecorder.setCamera(camera);
                mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);// 这两项需要放在setOutputFormat之前
                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 设置录制视频源为Camera(相机)
                mRecorder.setProfile(mProfile);
                mRecorder.setMaxDuration(5000);// 设置最大的录制时间
                mVecordFile= FileUtil.getVidoPath(Constants.getMP4Name(this));
                mRecorder.setOutputFile(mVecordFile);
                mRecorder.setOrientationHint(90);
                mShootBtn.setVisibility(View.INVISIBLE);
                mRecorder.prepare();// 准备录制
                mRecorder.start(); // 开始录制
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (time < 5) {
                            try {
                                time++;
                                Thread.sleep(1000);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                        handler.sendEmptyMessage(2);

                    }
                }).start();

                mStartedFlg = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用时间对录像起名
     *
     * @return
     */
    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sdf.format(ca.getTimeInMillis());
        return date;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        camera = Camera.open(); // 获取Camera实例
        try {
            camera.setPreviewDisplay(holder);
            mSurfaceview.setLayoutParams(new RelativeLayout.LayoutParams(width,
                    height));
        } catch (Exception e) {
            // 如果出现异常，则释放Camera对象
            camera.release();
        }
        camera.setDisplayOrientation(90);// 设置预览视频时时竖屏
        // 启动预览功能
        camera.startPreview();
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // surfaceDestroyed的时候同时对象设置为null
        mSurfaceview = null;
        mSurfaceHolder = null;
        if (mRecorder != null) {
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;
        }
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.shoot_button){
            if (isPlay){
                playMp4();
            }else{
                start();
            }
        }else if (v.getId()==R.id.restart){
            ctrol_ll.setVisibility(View.GONE);
            start();
        }else if (v.getId()==R.id.submit){
            submit();
        }else if (v.getId()==R.id.backButton){
            finish();
        }

    }

    private void submit() {
        ShowMsgDialog.showNoMsg(this, false);
        identyTranBean.setVideoPath(mVecordFile);
        doUploadImages();
    }

    private void doUploadImages() {
        AliySSOHepler.getInstance().uploadMedia(this,Constants.VERIFICATION_PATH, mVecordFile, new HttpCallBack() {

            @Override
            public void onSuccess(String imageUrl, int requestCode) {
                Message msg = new Message();
                msg.obj = imageUrl;
                msg.what = 3;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int errorCode, String msg, int requestCode) {
                Message msg1 = new Message();
                msg1.what = 4;
                handler.sendMessage(msg1);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playMp4(){
        try {
            stopPlay();
            mediaPlayer=new MediaPlayer();
            mediaPlayer
                    .setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDisplay(mSurfaceview.getHolder());
            mediaPlayer.setDataSource(mVecordFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void stopPlay() {
        // TODO Auto-generated method stub
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
