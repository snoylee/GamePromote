package com.xygame.second.sg.biggod.activity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.AudioRecorder2Mp3Util;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月3日
 * @action [登录欢迎页面]
 */
public class GodRecordVoiceActivity extends SGBaseActivity implements OnClickListener {
    private View closeLoginWel;
    private Button recordButton;
    private TextView longestTime, timeCount;

    /**
     * 录音相关
     *
     */
    private String fromVoicePath;
    private long startTime;
    private static final int MIN_INTERVAL_TIME = 2000;
    private AudioRecorder2Mp3Util util = null;
    private long intervalTime;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_record_voice_layout);
        initViews();
        initListeners();
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [初始化控件]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        recordButton = (Button) findViewById(R.id.recordButton);
        longestTime = (TextView) findViewById(R.id.longestTime);
        timeCount = (TextView) findViewById(R.id.timeCount);
        closeLoginWel = findViewById(R.id.closeLoginWel);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [初始化监听]
     */
    private void initListeners() {
        closeLoginWel.setOnClickListener(this);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (FileUtil.ExistSDCard()) {
                            recordButton.setBackgroundResource(R.drawable.shape_circle_gray);
                            recordButton.setTextColor(getResources().getColor(R.color.white));
                            recordButton.setText("松开完成");
                            initDialogAndStartRecord();
                        } else {
                            Toast.makeText(GodRecordVoiceActivity.this, "请插入SD卡", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (FileUtil.ExistSDCard()) {
                            recordButton.setBackgroundResource(R.drawable.shape_circle_green);
                            recordButton.setTextColor(getResources().getColor(R.color.white));
                            recordButton.setText("按住录音");
                            finishRecord();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (FileUtil.ExistSDCard()) {
                            recordButton.setBackgroundResource(R.drawable.shape_circle_green);
                            recordButton.setTextColor(getResources().getColor(R.color.white));
                            recordButton.setText("按住录音");
                            finishRecord();
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 重载方法
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.closeLoginWel) {
            sendFaithBroadcast();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sendFaithBroadcast();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendSuccessBroadcast() {
        Intent intent = new Intent(Constants.ACTION_GOD_RECORD);
        intent.putExtra("actionFlag",true);
        intent.putExtra("finalVoicePath",fromVoicePath);
        sendBroadcast(intent);
        finish();
    }

    private void sendFaithBroadcast() {
        Intent intent = new Intent(Constants.ACTION_GOD_RECORD);
        intent.putExtra("actionFlag",false);
        sendBroadcast(intent);
        finish();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void initDialogAndStartRecord() {
        startTime = System.currentTimeMillis();
        startRecording();
    }

    private void finishRecord() {

        intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(this, "时间太短！", Toast.LENGTH_SHORT).show();
            stopThread();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        fromVoicePath = "/sdcard/test_audio_recorder_for_mp3.mp3";
        if (util == null) {
            util = new AudioRecorder2Mp3Util(null,
                    "/sdcard/test_audio_recorder_for_mp3.raw", fromVoicePath);
        }
        util.startRecording();
        startTimeCount();
    }

    private void stopThread() {
        if (util!=null){
            util.cleanFile(AudioRecorder2Mp3Util.RAW);
            util.close();
            util = null;
        }
    }

    private void stopRecording() {
        if (util!=null){
            util.stopRecordingAndConvertFile();
        }
        endTimeCount();
        stopThread();
        sendSuccessBroadcast();
    }

    private void startTimeCount() {
        initTimerCount();
        countDownTimer.start();
    }

    private void endTimeCount() {
        if (countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void initTimerCount() {
        // TODO Auto-generated method stub
        countDownTimer = new CountDownTimer(15 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                long alreadyTime = 15 - millisUntilFinished / 1000;
                if (alreadyTime >= 10) {
                    timeCount.setText("00:".valueOf(alreadyTime));
                } else {
                    timeCount.setText("00:0".valueOf(alreadyTime));
                }
                longestTime.setText("时长".concat(String.valueOf(alreadyTime)).concat("秒"));
            }

            @Override
            public void onFinish() {
                stopRecording();
            }
        };

    }
}