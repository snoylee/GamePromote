package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by tony on 2016/2/24.
 */
public class ShowVideoActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName,rightButtonText;
    private View backButton,playButton,rightButton;
    private String mVecordFile = null;// 文件
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private ImageView defaultImage;
    private IdentyTranBean identyTranBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_vido_layout);// 加载布局
        mVecordFile=getIntent().getStringExtra("vidoPath");
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        backButton = findViewById(R.id.backButton);
        rightButtonText=(TextView)findViewById(R.id.rightButtonText);
        playButton=findViewById(R.id.playButton);
        rightButton=findViewById(R.id.rightButton);
        defaultImage=(ImageView)findViewById(R.id.defaultImage);
        backButton.setOnClickListener(this);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("视频预览");
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("提交");
        playButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        mediaPlayer=new MediaPlayer();
        surfaceView=(SurfaceView) this.findViewById(R.id.surfaceView);
        identyTranBean = (IdentyTranBean) getIntent().getSerializableExtra("identyTranBean");
        //设置SurfaceView自己不管理的缓冲区
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        defaultImage.setImageBitmap(getVideoThumbnail(mVecordFile, 480, 800,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playButton.setVisibility(View.VISIBLE);
                defaultImage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
       if (v.getId()==R.id.playButton){
           playButton.setVisibility(View.GONE);
           defaultImage.setVisibility(View.GONE);
           play();
       }else if (v.getId()==R.id.backButton){
           Intent intent = new Intent(this,CertifyVideoActivity.class);
           intent.putExtra("identyTranBean",identyTranBean);
           intent.putExtra("isRecord",true);
           startActivity(intent);
           finish();
        }else if (v.getId()==R.id.rightButton){
           doUploadImages();
       }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(this,CertifyVideoActivity.class);
            intent.putExtra("identyTranBean",identyTranBean);
            intent.putExtra("isRecord", true);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void play() {
        try {
            mediaPlayer.reset();
            mediaPlayer
                    .setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置需要播放的视频
            mediaPlayer.setDataSource(mVecordFile);
            //把视频画面输出到SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.prepare();
            //播放
            mediaPlayer.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
//        System.out.println("w"+bitmap.getWidth());
//        System.out.println("h"+bitmap.getHeight());
//        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private void doUploadImages() {
        ShowMsgDialog.showNoMsg(this, false);
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:// 体检结束
                    String imageUrl = (String) msg.obj;
                    identyTranBean.setVideoUrl(imageUrl);
                    ShowMsgDialog.cancel();
                    Intent intent = new Intent(ShowVideoActivity.this,ModelIdentyThirdActivity.class);
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

}
