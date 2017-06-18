package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.utils.ActionsFilter;
import com.xygame.second.sg.utils.Contant;
import com.xygame.second.sg.utils.RecordResult;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

public class VideoCertifyActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName;
    private View backButton;
    private ImageView startRecord;
    //视频地址
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_stone_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        startRecord =(ImageView) findViewById(R.id.startRecord);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
    }

    private void initListensers() {
        startRecord.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        titleName.setText("视频认证");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.startRecord) {
            Contant.startRecordActivity(this,0);
        }
    }



    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 0:
                        String localPath=(String)msg.obj;
                        doUploadImages(localPath);
                        break;
                    case 2:
                        path=(String)msg.obj;
                        uploadVideoUrlToLocalServie();
                        break;
                    case 3:
                        ShowMsgDialog.cancel();
                        Toast.makeText(VideoCertifyActivity.this,"视频上传失败",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    };


    private void doUploadImages(String videoPath) {
        AliySSOHepler.getInstance().uploadMedia(this, Constants.VERIFICATION_PATH, videoPath, new HttpCallBack() {

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


    private void uploadVideoUrlToLocalServie() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            obj.put("videoUrl", path);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_CERTIFY_VIDEO);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CERTIFY_VIDEO);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        ShowMsgDialog.cancel();
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_CERTIFY_VIDEO:
                if ("0000".equals(data.getCode())) {
                    UserPreferencesUtil.setUserVideoAuth(this,"4");
                    Toast.makeText(this, "上传视频成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActionsFilter.ACTION_VIDEO_SUCCESS);
                    sendBroadcast(intent);
                    finish();
                } else {
                    Toast.makeText(this,data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        ShowMsgDialog.cancel();
        Toast.makeText(this,data.getMsg(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0:
                RecordResult result =new RecordResult(data);
                String videoFile;
                String [] thum;
                videoFile = result.getPath();
                thum = result.getThumbnail();
                result.getDuration();
                ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                        thum[0], startRecord);
                ShowMsgDialog.showNoMsg(this,false);
                ThreadPool.getInstance().excuseThread(new MovingMp4(videoFile));
                break;
            default:
                break;
        }
    }

    private class MovingMp4 implements Runnable{
        private String videoFile;
        public MovingMp4(String videoFile){
            this.videoFile=videoFile;
        }
        @Override
        public void run() {
            String mFileName= FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP4Name(VideoCertifyActivity.this));
            int isSccuss=FileUtil.CopySdcardFile(videoFile,mFileName);
            if (isSccuss==0){
                Message msg = new Message();
                msg.what = 0;
                msg.obj=mFileName;
                handler.sendMessage(msg);
            }
        }
    }
}
