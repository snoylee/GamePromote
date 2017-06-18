package com.xygame.second.sg.biggod.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.qupai.editor.TextDialog;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.utils.Contant;
import com.xygame.second.sg.utils.ImageFactory;
import com.xygame.second.sg.utils.RecordResult;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.EditorTextContentActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.GodChoiceActivity;
import com.xygame.sg.define.view.GodLalbeChoiceActivity;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GodApplactionSecondActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName,bgImageTip,oralText,lableText;
    private View backButton;
    private ImageView startRecord,bgImage,voiceIcon;
    //视频地址
    private String fenMianImage,photoName,videoPath,finalVoicePath,oralTextString;
    private View recordSound,lableTextView,oralTextView;
    private Button commitView;
    private MediaPlayer mediaPlayer;
    private String typeOpation,godFlag;
    private ImageLoader mImageLoader;
    private VideoImageLoader videoImageLoader;
    private GodLableBean godLableBean;
    private JinPaiBigTypeBean godTypeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_apl_second_layout);
        registerBoradcastReceiver();
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        commitView=(Button)findViewById(R.id.commitView);
        oralTextView=findViewById(R.id.oralTextView);
        lableTextView=findViewById(R.id.lableTextView);
        voiceIcon=(ImageView)findViewById(R.id.voiceIcon);
        recordSound=findViewById(R.id.recordSound);
        bgImage=(ImageView)findViewById(R.id.bgImage);
        startRecord =(ImageView) findViewById(R.id.startRecord);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        bgImageTip=(TextView)findViewById(R.id.bgImageTip);
        oralText=(TextView)findViewById(R.id.oralText);
        lableText=(TextView)findViewById(R.id.lableText);
    }

    private void initListensers() {
        oralTextView.setOnClickListener(this);
        lableTextView.setOnClickListener(this);
        recordSound.setOnClickListener(this);
        bgImage.setOnClickListener(this);
        startRecord.setOnClickListener(this);
        backButton.setOnClickListener(this);
        commitView.setOnClickListener(this);
    }

    private void initDatas() {
        godTypeBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("bean");
        titleName.setText(godTypeBean.getName());
        if (Constants.DEFINE_LOL_ID.equals(godTypeBean.getId())){
            bgImageTip.setText(Constants.BG_TIP_LOL);
        }else  if ("1200".equals(godTypeBean.getId())){
            bgImageTip.setText(Constants.BG_TIP_VOICE);
        }else  if ("1100".equals(godTypeBean.getId())){
            bgImageTip.setText(Constants.BG_TIP_VIDEO);
        }else  if ("800".equals(godTypeBean.getId())){
            bgImageTip.setText(Constants.BG_TIP_MOVING);
        }else  if ("700".equals(godTypeBean.getId())){
            bgImageTip.setText(Constants.BG_TIP_EATING);
        }else  if ("600".equals(godTypeBean.getId())){
            bgImageTip.setText(Constants.BG_TIP_LVYOUO);
        }else{
            bgImageTip.setText(Constants.BG_TIP_COMM);
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        videoImageLoader  = VideoImageLoader.getInstance();
        godFlag=getIntent().getStringExtra("godFlag");
        if ("editor".equals(godFlag)){
            commitView.setText("提交");
            loadInfo();
        }else{
            commitView.setText("提交");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
        stopMedia();
    }

    private void stopMedia(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.startRecord) {
            typeOpation="1";
            if (!TextUtils.isEmpty(videoPath)){
                Intent intent=new Intent(this,GodChoiceActivity.class);
                startActivityForResult(intent,4);
            }else{
                Contant.startRecordActivity(this,0);
            }
        }else if (v.getId()==R.id.bgImage){
            Intent intent = new Intent(this, PickPhotoesView.class);
            startActivityForResult(intent, 1);
        }else if (v.getId()==R.id.recordSound){
            typeOpation="2";
            if (!TextUtils.isEmpty(finalVoicePath)){
                Intent intent=new Intent(this,GodChoiceActivity.class);
                startActivityForResult(intent,4);
            }else{
                Intent intent=new Intent(this,GodRecordVoiceActivity.class);
                startActivity(intent);
            }
        }else if (v.getId()==R.id.lableTextView){
            if ("900".equals(godTypeBean.getId())||"1000".equals(godTypeBean.getId())||Constants.DEFINE_LOL_ID.equals(godTypeBean.getId())){
                List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                if (TextUtils.isEmpty(videoPath)){
                    for (JinPaiBigTypeBean item:typeDatas){
                        if ("900".equals(item.getId())){
                            godTypeBean=item;
                            break;
                        }
                    }
                }else{
                    for (JinPaiBigTypeBean item:typeDatas){
                        if ("1000".equals(item.getId())){
                            godTypeBean=item;
                            break;
                        }
                    }
                }
                Intent intent=new Intent(this,GodLalbeChoiceActivity.class);
                intent.putExtra("bean",godTypeBean);
                startActivityForResult(intent, 6);
            }else{
                Intent intent=new Intent(this,GodLalbeChoiceActivity.class);
                intent.putExtra("bean",godTypeBean);
                startActivityForResult(intent, 6);
            }
        }else if (v.getId()==R.id.oralTextView){
            Intent intent = new Intent(this, EditorTextContentActivity.class);
            intent.putExtra(Constants.EDITOR_TEXT_TITLE, "修改职业");
            intent.putExtra("oral", oralTextString);
            intent.putExtra("hint", "职业类型不超过100个字");
            intent.putExtra(Constants.TEXT_EDITOR_NUM, 100);
            startActivityForResult(intent, 5);
        }else if (v.getId()==R.id.commitView){
            if (isGo()){
                ShowMsgDialog.showNoMsg(this,false);
                doUploadImages();
            }
        }
    }

    private boolean isGo(){
        if (TextUtils.isEmpty(fenMianImage)){
            Toast.makeText(this,"请添加封面照",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (godLableBean==null){
            Toast.makeText(this,"请选择标签",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class MovingMp4 implements Runnable{
        private String videoFile;
        public MovingMp4(String videoFile){
            this.videoFile=videoFile;
        }
        @Override
        public void run() {
            String mFileName= FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP4Name(GodApplactionSecondActivity.this));
            int isSccuss=FileUtil.CopySdcardFile(videoFile,mFileName);
            if (isSccuss==0){
                Message msg = new Message();
                msg.what = 0;
                msg.obj=mFileName;
                handler.sendMessage(msg);
            }
        }
    }

    private void loadInfo(){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            if (Constants.DEFINE_LOL_ID.equals(godTypeBean.getId())){
                String typeId=getIntent().getStringExtra("typeId");
                obj.put("skillCode",typeId);
            }else{
                obj.put("skillCode",godTypeBean.getId());
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_ECHO_GOD);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ECHO_GOD);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void uploadInfo(){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            if (Constants.DEFINE_LOL_ID.equals(godTypeBean.getId())){
                obj.put("skillCode","900");
            }else{
                if (!TextUtils.isEmpty(videoPath)){
                    obj.put("videoUrl", videoPath);
                }
                obj.put("skillCode",godTypeBean.getId());
            }
            obj.put("skillTitle",godLableBean.getTitleId());
            obj.put("coverUrl", fenMianImage);
            if (!TextUtils.isEmpty(finalVoicePath)){
                obj.put("voiceUrl", finalVoicePath);
            }
            if (!TextUtils.isEmpty(oralTextString)){
                obj.put("skillDesc", oralTextString);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_AUTH_GOD);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_AUTH_GOD);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void uploadMedia() {
        if (!finalVoicePath.contains("http://")){
            AliySSOHepler.getInstance().uploadMedia(this, Constants.VERIFICATION_PATH, finalVoicePath, new HttpCallBack() {

                @Override
                public void onSuccess(String imageUrl, int requestCode) {
                    android.os.Message msg = new android.os.Message();
                    msg.obj = imageUrl;
                    msg.what = 5;
                    handler.sendMessage(msg);
                }

                @Override
                public void onFailure(int errorCode, String msg, int requestCode) {
                    // TODO Auto-generated method stub
                    android.os.Message msg1 = new android.os.Message();
                    msg1.what = 6;
                    handler.sendMessage(msg1);
                }

                @Override
                public void onProgress(String objectKey, int byteCount, int totalSize) {
                    // TODO Auto-generated method stub
                }
            });
        }else{
            uploadInfo();
        }
    }

    private void doUploadImages() {
        if (!fenMianImage.contains("http://")){
            AliySSOHepler.getInstance().uploadImageEngine(this, Constants.VERIFICATION_PATH, fenMianImage, new HttpCallBack() {

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
        }else {
            if(!TextUtils.isEmpty(videoPath)){
                doUploadVideo();
            }else{
                if (!TextUtils.isEmpty(finalVoicePath)){
                    uploadMedia();
                }else{
                    uploadInfo();
                }
            }
        }
    }

    private void doUploadVideo() {
        if (!videoPath.contains("http://")){
            AliySSOHepler.getInstance().uploadMedia(this, Constants.VERIFICATION_PATH, videoPath, new HttpCallBack() {

                @Override
                public void onSuccess(String imageUrl, int requestCode) {
                    Message msg = new Message();
                    msg.obj = imageUrl;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

                @Override
                public void onFailure(int errorCode, String msg, int requestCode) {
                    Message msg1 = new Message();
                    msg1.what = 2;
                    handler.sendMessage(msg1);
                }

                @Override
                public void onProgress(String objectKey, int byteCount, int totalSize) {

                }
            });
        }else{
            if (!TextUtils.isEmpty(finalVoicePath)){
                uploadMedia();
            }else{
                uploadInfo();
            }
        }
    }

    private void chicePhoto() {
        LinkedList<String> picDatas = new LinkedList<String>();
        TransferImagesBean dataBean = new TransferImagesBean();
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, 1);//后一个参数可选照片数量
        intent.putExtra("images", dataBean);
        startActivityForResult(intent,2);
    }

    private void takePhoto() {
        // 跳转至拍照界面
        photoName = Constants.getImageName(this);
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        File out = new File(FileUtil.getPhotopath(photoName));
        Uri imageUri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentPhote, 3);
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_GOD_RECORD);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
      unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_GOD_RECORD.equals(intent.getAction())) {
                boolean actionFlag=intent.getBooleanExtra("actionFlag",false);
                if (actionFlag){
                    finalVoicePath=intent.getStringExtra("finalVoicePath");
                    voiceIcon.setImageResource(R.drawable.icon_play);
                    ShowMsgDialog.showNoMsg(GodApplactionSecondActivity.this, false);
                    ThreadPool.getInstance().excuseThread(new CopyFile(finalVoicePath));
                }
            }
        }
    };

    private class CopyFile implements Runnable{
        private String sourcePath;
        public CopyFile(String sourcePath){
            this.sourcePath=sourcePath;
        }
        @Override
        public void run() {
            String mFileName = FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP3Name(GodApplactionSecondActivity.this));
            int isSccuss = FileUtil.CopySdcardFile(sourcePath, mFileName);
            if (isSccuss == 0) {
                android.os.Message msg1 = new android.os.Message();
                msg1.what = 7;
                msg1.obj = mFileName;
                handler.sendMessage(msg1);
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 0:
                        ShowMsgDialog.cancel();
                        videoPath=(String)msg.obj;
                        if ("900".equals(godTypeBean.getId())||"1000".equals(godTypeBean.getId())||Constants.DEFINE_LOL_ID.equals(godTypeBean.getId())){
                            godLableBean=null;
                            lableText.setText("");
                        }
                        break;
                    case 1:
                        videoPath=(String)msg.obj;
                        if (!TextUtils.isEmpty(finalVoicePath)){
                            uploadMedia();
                        }else{
                            uploadInfo();
                        }
                        break;
                    case 2:
                        ShowMsgDialog.cancel();
                        Toast.makeText(GodApplactionSecondActivity.this,"视频上传失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        fenMianImage=(String)msg.obj;
                        if(!TextUtils.isEmpty(videoPath)){
                            doUploadVideo();
                        }else{
                            if (!TextUtils.isEmpty(finalVoicePath)){
                                uploadMedia();
                            }else{
                                uploadInfo();
                            }
                        }
                        break;
                    case 4:
                        ShowMsgDialog.cancel();
                        Toast.makeText(GodApplactionSecondActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        finalVoicePath=(String)msg.obj;
                        uploadInfo();
                        break;
                    case 6:
                        ShowMsgDialog.cancel();
                        Toast.makeText(GodApplactionSecondActivity.this,"声音上传失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        ShowMsgDialog.cancel();
                        finalVoicePath = (String) msg.obj;
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                RecordResult result =new RecordResult(data);
                String videoFile;
                String [] thum;
                videoFile = result.getPath();
                thum = result.getThumbnail();
                result.getDuration();
                ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                        thum[0], startRecord);
                ShowMsgDialog.showNoMsg(this,true);
                ThreadPool.getInstance().excuseThread(new MovingMp4(videoFile));
                break;
            case 1:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flag = data.getStringExtra(Constants.COMEBACK);
                if ("galary".equals(flag)) {
                    chicePhoto();
                } else if ("camera".equals(flag)) {
                    takePhoto();
                }
                break;
            case 2: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Object result1 = data.getSerializableExtra(Constants.COMEBACK);
                if (result1 != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result1;
                    LinkedList<String> picDatas = new LinkedList<String>();
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        Bitmap subBitmap = FileUtil.compressImages(tempStrs
                                .get(i));
                        if (subBitmap != null) {
                            String photoName = Constants.getImageName(this);
                            FileUtil.saveScalePhoto(
                                    FileUtil.getPhotopath(photoName), subBitmap);
                            subBitmap.recycle();
                            picDatas.add(FileUtil.getPhotopath(photoName));
                        }
                    }
                    fenMianImage=picDatas.get(0);
                    bgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            fenMianImage, bgImage);
                }
                break;
            }

            case 3:
                try {
                    Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
                    if (bitmap != null) {
                        FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
                        fenMianImage = FileUtil.getPhotopath(photoName);
                        bgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                                fenMianImage, bgImage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flagGod=data.getStringExtra(Constants.COMEBACK);
                if ("player".equals(flagGod)){
                    if ("1".equals(typeOpation)){
                        if (videoPath.contains("http://")) {
                            Intent intent = new Intent(this, VideoPlayerActivity.class);
                            intent.putExtra("vidoUrl",videoPath);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(this, VideoPlayerActivity.class);
                            intent.putExtra("vidoUrl", videoPath);
                            startActivity(intent);
                        }
                    }else if ("2".equals(typeOpation)){
                        playerVoiceWorker();
                    }
                }else if ("delete".equals(flagGod)){
                    if ("1".equals(typeOpation)){
                        startRecord.setImageResource(R.drawable.video_vertify);
                        videoPath=null;
                        if ("900".equals(godTypeBean.getId())||"1000".equals(godTypeBean.getId())||Constants.DEFINE_LOL_ID.equals(godTypeBean.getId())){
                            godLableBean=null;
                            lableText.setText("");
                        }
                    }else if ("2".equals(typeOpation)){
                        voiceIcon.setImageResource(R.drawable.icon_record);
                        finalVoicePath=null;
                    }
                }
                break;
            case 5:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                oralTextString = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                oralText.setText(oralTextString);
                break;
            case 6:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flagStr=data.getStringExtra("flagStr");
                if ("have".equals(flagStr)){
                    godLableBean=(GodLableBean)data.getSerializableExtra(Constants.COMEBACK);
                    lableText.setText(godLableBean.getTitleName());
                }
                break;
            default:
                break;
        }
    }

    private void playerVoiceWorker() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(finalVoicePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_AUTH_GOD:
                if ("0000".equals(data.getCode())) {
                    String experAuth=UserPreferencesUtil.getExpertAuth(this);
                    if (!"2".equals(experAuth)){
                        UserPreferencesUtil.setExpertAuth(this, "1");
                    }
                    Toast.makeText(this, "申请成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ECHO_GOD:
                if ("0000".equals(data.getCode())) {
                    parseEchoInfo(data.getRecord());
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this, "申请失败", Toast.LENGTH_SHORT).show();
    }

    private void parseEchoInfo(String record){
        if (!TextUtils.isEmpty(record)){
            try {
                JSONObject jsonObject=new JSONObject(record);
                videoPath= StringUtils.getJsonValue(jsonObject, "videoUrl");
                fenMianImage=StringUtils.getJsonValue(jsonObject, "coverUrl");
                oralTextString=StringUtils.getJsonValue(jsonObject, "skillDesc");
                finalVoicePath=StringUtils.getJsonValue(jsonObject, "voiceUrl");
                if (!TextUtils.isEmpty(finalVoicePath)){
                    voiceIcon.setImageResource(R.drawable.icon_play);
                }else{
                    voiceIcon.setImageResource(R.drawable.icon_record);
                }
                if (!TextUtils.isEmpty(videoPath)){
                    videoImageLoader.DisplayImage(videoPath, startRecord);
                }else{
                    startRecord.setImageResource(R.drawable.video_vertify);
                }
                mImageLoader.loadImage(fenMianImage, bgImage, true);
                if (!TextUtils.isEmpty(oralTextString)){
                    oralText.setText(oralTextString);
                }else{
                    oralText.setText("");
                }
                String lableId=StringUtils.getJsonValue(jsonObject, "skillTitle");
                String subStr=null;
                if (Constants.DEFINE_LOL_ID.equals(godTypeBean.getId())){
                    String typeId=getIntent().getStringExtra("typeId");
                    List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                    for (JinPaiBigTypeBean bean:typeDatas){
                        if (typeId.equals(bean.getId())){
                            subStr=bean.getSubStr();
                            break;
                        }
                    }
                }else{
                    subStr=godTypeBean.getSubStr();
                }
                if (!TextUtils.isEmpty(subStr)){
                    JSONArray array = new JSONArray(subStr);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        GodLableBean item = new GodLableBean();
                        item.setTitleId(StringUtils.getJsonValue(object, "titleId"));
                        item.setTitleName(StringUtils.getJsonValue(object, "titleName"));
                        if (lableId.equals(item.getTitleId())){
                            godLableBean=item;
                            break;
                        }
                    }
                }
                if (godLableBean!=null){
                    lableText.setText(godLableBean.getTitleName());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
