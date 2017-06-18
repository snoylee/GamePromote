package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.bean.IdentyTranBean;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.MovieRecorderView;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;

public class ModelIdentySecondActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private MovieRecorderView mRecorderView;
    private ImageView mShootBtn;
    private TextView time_tv;
    private LinearLayout ctrol_ll;
    private TextView restart;
    private TextView submit;
    private boolean isFinish = true;
    private IdentyTranBean identyTranBean;

    boolean showfinish;
    boolean showstart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_identy_second);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getString(R.string.title_activity_model_identy_second));
        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        mShootBtn = (ImageView) findViewById(R.id.shoot_button);
        ctrol_ll = (LinearLayout) findViewById(R.id.ctrol_ll);
        time_tv = (TextView) findViewById(R.id.time_tv);
        restart = (TextView) findViewById(R.id.restart);
        submit = (TextView) findViewById(R.id.submit);
    }

    private void initDatas() {
        identyTranBean = (IdentyTranBean) getIntent().getSerializableExtra("identyTranBean");
        mRecorderView.setHandler(handler);
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        mShootBtn.setOnClickListener(this);
        restart.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    private void submit(String videoPath) {
        ShowMsgDialog.showNoMsg(this, false);
        identyTranBean.setVideoPath(videoPath);
        doUploadImages(videoPath);
    }

    private void doUploadImages(String videoPath) {
        AliySSOHepler.getInstance().uploadMedia(this,Constants.VERIFICATION_PATH, videoPath, new HttpCallBack() {

            @Override
            public void onSuccess(String imageUrl, int requestCode) {
                Message msg = new Message();
                msg.obj = imageUrl;
                msg.what = 2;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int errorCode, String msg, int requestCode) {
                Message msg1 = new Message();
                msg1.what = 3;
                handler.sendMessage(msg1);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {

            }
        });
    }


    public void start() {
        showfinish = false;
        mRecorderView.onDestroy();
        mRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
            @Override
            public void onRecordFinish() {
                handler.sendEmptyMessage(1);
            }
        });
    }

    public void restart() {
        mShootBtn.setImageResource(R.drawable.startrecord);
        ctrol_ll.setVisibility(View.GONE);
        stop();
        start();
    }

    public void stop() {
        if (mRecorderView.getVecordFile() != null)
            mRecorderView.getVecordFile().delete();
        mRecorderView.stop();
        showstart = false;
        mShootBtn.setImageResource(R.drawable.stoprecord);
    }



    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ctrol_ll.setVisibility(View.VISIBLE);
                mShootBtn.setVisibility(View.VISIBLE);
                mShootBtn.setImageResource(R.drawable.stoprecord);
                showfinish = true;
            } else if (msg.what == 0) {
                mShootBtn.setVisibility(View.GONE);
                time_tv.setText(msg.arg1 + "'/5''");
                isFinish = false;
            } else if (msg.what == 2) {//上传成功
                String imageUrl = (String) msg.obj;
                identyTranBean.setVideoUrl(imageUrl);
                ShowMsgDialog.cancel();
                Intent intent = new Intent(ModelIdentySecondActivity.this,ModelIdentyThirdActivity.class);
                intent.putExtra("identyTranBean", identyTranBean);
                startActivity(intent);
                finish();
            } else if (msg.what ==3) {//上传失败
                ShowMsgDialog.cancel();
                Toast.makeText(getApplicationContext(), "视频上传失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                if (isFinish) {
                    mRecorderView.stop();
                }
                finish();
                break;
            case R.id.shoot_button:
                if (!showstart) {
                    showstart = true;
                    start();
                } else {
                    mRecorderView.play();
                }
                break;
            case R.id.restart:
                showstart = false;
                time_tv.setText("0'/5''");
                mShootBtn.setImageResource(R.drawable.startrecord);
                ctrol_ll.setVisibility(View.GONE);
                break;
            case R.id.submit:
                submit(mRecorderView.getVecordFile().toString());
                break;
        }
    }
}
