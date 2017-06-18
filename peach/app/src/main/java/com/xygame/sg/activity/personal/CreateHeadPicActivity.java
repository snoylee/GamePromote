/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.personal;

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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.personal.bean.VideoBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.draggrid.DragGrid;
import com.xygame.sg.define.draggrid.DragGrid.OnChanageListener;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.define.view.PickVideoView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.init.ResponseAliParams;
import com.xygame.sg.task.personal.EditUserLogoVideo;
import com.xygame.sg.task.personal.UploadHeadPic;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.VideoImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * @author 王琪
 * @date 2015年11月9日
 * @action [编辑头像页面]
 */
public class CreateHeadPicActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

    private TextView titleName, rightButtonText;
    private View backButton, rightButton,addHeadPicButton;
    private DragGrid modelPicGridView;
    private LinkedList<String> picDatas,oldPicDatas;
    private List<PhotoesBean> uploadImages;
    private DragAdapter adapter;
    private int currIndex = 0;
    private String photoName;
    private Uri imageUri, originalUri;

    /**
     * 头像宽
     */
    private int w = 300;
    /**
     * 头像高
     */
    private int h = 300;

    private ImageLoader imageLoader;
    private LinearLayout video_ll;
    private RelativeLayout add_video;
    private ImageView add_video_iv;
    private ImageView preview_play_iv;

    //视频地址
    private String path;
    //视频第一帧地址
    private String imagePath;
    private Long videoSize;
    private Long videoTime;

    private VideoBean videoBean;
    private VideoImageLoader videoImageLoader;
    private int videoPosType = 3;//1:本地刚录制  2：网络  3：无视频

    private boolean isCm = false;
    private boolean isUpdate=false;
    public VideoBean getVideoBean() {
        return videoBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VisitUnit visitUnit = new VisitUnit(this);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_ediotr_headpic_layout, null));
        if (getIntent().hasExtra("isCm")){
            isCm = getIntent().getBooleanExtra("isCm",true);
        }
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_RECORDER_SUCCESS);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        initViews();
        initListeners();
        initDatas();
    }


    private void initListeners() {
        addHeadPicButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        add_video.setOnClickListener(this);
        modelPicGridView.setOnItemClickListener(this);
        modelPicGridView.setOnChangeListener(new OnChanageListener() {
            @Override
            public void isEdit(boolean isEdit) {
                if (isEdit) {
                    setEditis(true);
                }
            }
        });
    }

    public void setEditis(boolean b) {
        if (b) {
            modelPicGridView.setEditState(true);
            adapter.setEdit(true);
        } else {
            modelPicGridView.setEditState(false);
            adapter.setEdit(false);
        }
    }


    private void initViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        addHeadPicButton=findViewById(R.id.add);
        modelPicGridView = (DragGrid) findViewById(R.id.modelPicGridView);
        video_ll = (LinearLayout) findViewById(R.id.video_ll);
        add_video = (RelativeLayout) findViewById(R.id.add_video);
        add_video_iv = (ImageView) findViewById(R.id.add_video_iv);
        preview_play_iv = (ImageView) findViewById(R.id.preview_play_iv);
    }

    private void initDatas() {
        titleName.setText(getResources().getString(R.string.sg_eidtor_headpic_title));
        rightButton.setVisibility(View.VISIBLE);
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText("保存");

        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        TransferImagesBean dataBean = (TransferImagesBean) getIntent().getSerializableExtra("images");
        picDatas = dataBean.getSelectImagePath();
        if (picDatas == null) {
            picDatas = new LinkedList<String>();
        }
        oldPicDatas=new LinkedList<>();
        oldPicDatas.addAll(picDatas);
        uploadImages = new ArrayList<PhotoesBean>();
        videoBean = dataBean.getVideoBean();
        videoImageLoader  = VideoImageLoader.getInstance();
        if (isCm){
            video_ll.setVisibility(View.GONE);
        } else {
            video_ll.setVisibility(View.VISIBLE);
        }
        updateChoiceImages();
        initVideo();
        imageListeners();
    }

    private void initVideo() {
        if (!StringUtils.isEmpty(videoBean.getVideoUrl()) &&!StringUtils.isEmpty(videoBean.getId())){
            videoPosType = 2;
            preview_play_iv.setVisibility(View.VISIBLE);
            videoImageLoader.DisplayImage(videoBean.getVideoUrl(), add_video_iv);
        }
    }

    private void isChangeImage(){
        List<String> datas = adapter.getImages();
        if (oldPicDatas.size()==datas.size()){
            for (int i = 0; i < datas.size(); i++) {
                boolean subFlag;
                if (i<datas.size()){
                    subFlag=false;
                }else{
                    subFlag=true;
                }

                for (int j=0;j<oldPicDatas.size();j++){
                    if (datas.get(i).equals(oldPicDatas.get(j))){
                        subFlag=true;
                    }
                }
                if (!subFlag){
                    isUpdate=true;
                    break;
                }
            }
        }else{
            isUpdate=true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            if ((adapter.getCount()) > 0) {
                isChangeImage();
                if (isUpdate){
                    uploadImages();
                }else{
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "至少上传一张头像", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.add_video) {
            if (AliPreferencesUtil.getBuckekName(this)==null){
                new Action(ResponseAliParams.class,"${ali_params}", this, null, new VisitUnit()).run();
                Toast.makeText(this, "加载配置稍后重试", Toast.LENGTH_SHORT)
                        .show();
            }else{
                if (videoPosType == 3){
                    toRecorderVideo();
                } else {
                    Intent intent = new Intent(this, PickVideoView.class);
                    startActivityForResult(intent, 6);
                }
            }
        } else if (v.getId()==R.id.add){
            if (AliPreferencesUtil.getBuckekName(this)==null){
                new Action(ResponseAliParams.class,"${ali_params}", this, null, new VisitUnit()).run();
                Toast.makeText(this, "加载配置稍后重试", Toast.LENGTH_SHORT)
                        .show();
            }else{
                if (picDatas.size() == Constants.MODEL_HEADPIC_NUM) {
                    Toast.makeText(getApplicationContext(), "已达到最多图片数量", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    showChoiceWays();
                }
            }
        }
    }

    private void uploadImages() {
        uploadImages.clear();
        List<String> datas = adapter.getImages();
        //视频不是必须的
        if (!StringUtils.isEmpty(path) && videoPosType == 1){
            PhotoesBean pb = new PhotoesBean();
            pb.setType(1);
            pb.setImageIndex(1 + "");
            pb.setImageUrl(path);
            pb.setVideoSize(videoSize);
            pb.setVideoTime(videoTime);
            if (!StringUtils.isEmpty(videoBean.getVideoUrl()) &&!StringUtils.isEmpty(videoBean.getId())){
                pb.setLastVideoId(videoBean.getId());
            }
            uploadImages.add(pb);
        }

        for (int i = 0; i < datas.size(); i++) {
            PhotoesBean pb = new PhotoesBean();
            pb.setImageIndex(String.valueOf(i + 1));
            pb.setImageUrl(datas.get(i));
            uploadImages.add(pb);
        }
        ShowMsgDialog.show(this, "保存中...", false);
        doUploadImages();
    }

    public List<PhotoesBean> getUploadImages() {
        return uploadImages;
    }

    private void doUploadImages() {
        if (currIndex < uploadImages.size()) {
            if (!uploadImages.get(currIndex).getImageUrl().contains("http://")) {
                if (uploadImages.get(currIndex).getType() == 1){
                    AliySSOHepler.getInstance().uploadMedia(this,Constants.AVATAR_PATH,
                            uploadImages.get(currIndex).getImageUrl(), new HttpCallBack() {

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
                } else {
                    AliySSOHepler.getInstance().uploadImageEngine(this,Constants.AVATAR_PATH,
                            uploadImages.get(currIndex).getImageUrl(), new HttpCallBack() {

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
                }

            } else {
                currIndex = currIndex + 1;
                doUploadImages();
            }
        } else {
            transferLocationService();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 1:
                        String imageUrl = (String) msg.obj;
                        uploadImages.get(currIndex).setImageUrl(imageUrl);
                        currIndex = currIndex + 1;
                        doUploadImages();
                        break;
                    case 2:
                        if (currIndex > 0) {
                            transferLocationService();
                        } else {
                            ShowMsgDialog.cancel();
                            Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public void finishUpload() {
        UserPreferencesUtil.setHeadPic(this, uploadImages.get(0).getImageUrl());
        ShowMsgDialog.cancel();
        Toast.makeText(getApplicationContext(), "成功上传".concat(String.valueOf(currIndex)).concat("张图片"),
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
        intent.putExtra("flagStr", true);
        sendBroadcast(intent);
        finish();
    }

    private void transferLocationService() {
        VisitUnit visit = new VisitUnit();
        new Action(UploadHeadPic.class,"${editUserLogoVideo}", this, null, visit).run();
    }


    @Override
    public void onDestroy() {
        SGApplication.getInstance().unregisterImagesReceiver();
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private void chicePhoto() {
        LinkedList<String> picDatas = new LinkedList<String>();
        TransferImagesBean dataBean = new TransferImagesBean();
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, 1);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Object result = data.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        photoName=FileUtil.getNewPhotoPath(tempStrs.get(i));
                        File out = new File(photoName);
                        originalUri = Uri.fromFile(out);
                    }
                    cropPickImageUri(originalUri, w, h, 4);
                }
                break;
            }
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

            case 2:
                File out = new File(FileUtil.getPhotopath(photoName));
                imageUri = Uri.fromFile(out);
                cropImageUri(imageUri, w, h, 3);
                break;
            case 3:
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getPhotopath(photoName));
                    if (bitmap != null) {
                        photoName = Constants.getImageName(this);
                        String photoPath = FileUtil.getPhotopath(photoName);
                        FileUtil.saveScalePhoto(photoPath, bitmap);
                        picDatas.add(photoPath);
                        updateChoiceImages();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    Bitmap photo = BitmapFactory.decodeFile(photoName);
                    if (photo != null) {
                        int angle = FileUtil.getOrientation(this, originalUri);
                        if (angle > 0) {
                            photo = FileUtil.rotaingImageView(angle, photo);
                        }
                        photoName = Constants.getImageName(this);
                        String photoPath = FileUtil.getPhotopath(photoName);
                        FileUtil.saveScalePhoto(photoPath, photo);
                        picDatas.add(photoPath);
                        updateChoiceImages();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String videoPath = data.getStringExtra(Constants.COMEBACK);
                Toast.makeText(getApplicationContext(), "录制成功" + videoPath, Toast.LENGTH_SHORT).show();
                break;
            case 6:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String returnFlag = data.getStringExtra(Constants.COMEBACK);
                if ("delete".equals(returnFlag)) {
                    deleteVideo();
                } else if ("recorder".equals(returnFlag)) {
                    toRecorderVideo();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void deleteVideo() {
        if (videoPosType == 1){//本地
            videoPosType = 3;
            path = "";
            //视频第一帧地址
            imagePath = "";
            videoSize = 0L;
            videoTime = 0L;
            add_video_iv.setImageResource(R.drawable.addpicture_gray);
        } else if (videoPosType == 2){//网络
            VisitUnit visit = new VisitUnit();
            new Action(EditUserLogoVideo.class,"${editUserLogoVideo}", this, null, visit).run();
        }
    }

    public void deleteVideoSuccess() {
        isUpdate=true;
        videoPosType = 3;
        add_video_iv.setImageResource(R.drawable.addpicture_gray);
        preview_play_iv.setVisibility(View.GONE);
        Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
        intent.putExtra("flagStr", true);
        sendBroadcast(intent);
    }

    private void toRecorderVideo() {
//        Intent intent = new Intent(getApplicationContext(), FFmpegRecorderActivity.class);
//        startActivityForResult(intent, 5);
    }

    private void cropPickImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    private void takePhoto() {
        // 跳转至拍照界面
        photoName = Constants.getImageName(this);
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        File out = new File(FileUtil.getPhotopath(photoName));
        imageUri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentPhote, 2);

    }

    private void updateChoiceImages() {
        adapter = new DragAdapter(this, picDatas);
        modelPicGridView.setAdapter(adapter);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void imageListeners() {
        SGApplication.getInstance().addImageListener(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent.getAction())) {
                    boolean flag = intent.getBooleanExtra(Constants.IS_DELE_IMAGES, false);
                    if (!flag) {
                        return;
                    }
                    Object result = intent.getSerializableExtra(Constants.COMEBACK);
                    if (result != null) {
                        TransferImagesBean dataBean = (TransferImagesBean) result;
                        picDatas.clear();
                        LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                        for (int i = 0; i < tempStrs.size(); i++) {
                            if (tempStrs.get(i).contains("http://")) {
                                picDatas.add(tempStrs.get(i));
                            } else if (tempStrs.get(i).contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                                picDatas.add(tempStrs.get(i));
                            } else {
                                Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
                                if (subBitmap != null) {
                                    String photoName = Constants.getImageName(CreateHeadPicActivity.this);
                                    FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), subBitmap);
                                    subBitmap.recycle();
                                    picDatas.add(FileUtil.getPhotopath(photoName));
                                }
                            }
                        }
                        updateChoiceImages();
                    }
                }
            }
        });
        SGApplication.getInstance().registerImagesReceiver();
    }

    public class DragAdapter extends BaseAdapter {
        /**
         * TAG
         */
        private final static String TAG = "DragAdapter";
        /**
         * 是否显示底部的ITEM
         */
        private boolean isItemShow = false;
        private Context context;
        /**
         * 控制的postion
         */
        private int holdPosition;
        /**
         * 是否改变
         */
        private boolean isChanged = false;
        /**
         * 是否可见
         */
        boolean isVisible = true;
        /**
         * 是否编辑状态
         */
        boolean isEdiState = false;
        /**
         * 要删除的position
         */
        public int remove_position = -1;
        List<String> list;
        ImageView iv;

        public DragAdapter(Context context, List<String> list2) {
            this.context = context;
            this.list = list2;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public String getItem(int position) {
            if (list != null && list.size() != 0) {
                return list.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public List<String> getImages() {
            return list;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
            iv = (ImageView) view.findViewById(R.id.iv);
            String map = getItem(position);
            if (isEdiState) {
                if (isChanged && (position == holdPosition) && !isItemShow) {
                    iv.setVisibility(View.INVISIBLE);
                    isChanged = false;

                }
                if (!isVisible && (position == -1 + list.size())) {
                    iv.setVisibility(View.INVISIBLE);
                }
            }

            if (map.contains("http://")) {
                imageLoader.loadImage(map, iv, false);
            } else {
                ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(map, iv);
            }
            return view;
        }

        /**
         * 拖动变更排序
         */
        public void exchange(int dragPostion, int dropPostion) {
            holdPosition = dropPostion;
            String dragItem = getItem(dragPostion);

            if (dragPostion < dropPostion) {
                list.add(dropPostion + 1, dragItem);
                list.remove(dragPostion);
            } else {
                list.add(dropPostion, dragItem);
                list.remove(dragPostion + 1);
            }
            isChanged = true;
            notifyDataSetChanged();
        }

        /**
         * 显示放下的ITEM
         */
        public void setShowDropItem(boolean show) {
            isItemShow = show;
        }

        public void setEdit(boolean b) {
            isEdiState = b;
            notifyDataSetChanged();
        }
    }

    private void showChoiceWays() {// chicePhoto();
        Intent intent = new Intent(this, PickPhotoesView.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        List<String> tempImages = adapter.getImages();
        if (position < tempImages.size()) {
            Constants.imageBrower(CreateHeadPicActivity.this, position,
                    tempImages.toArray(new String[tempImages.size()]), true);
        }
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_RECORDER_SUCCESS.equals(intent.getAction())) {
                isUpdate=true;
                videoPosType = 1;
                //视频地址
                path = intent.getStringExtra("path");
                //视频第一帧地址
                imagePath = intent.getStringExtra("imagePath");
                videoSize = intent.getLongExtra("videoSize", 0);
                videoTime = intent.getLongExtra("videoTime",0);
                preview_play_iv.setVisibility(View.VISIBLE);
                ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(imagePath, add_video_iv);
            }
        }
    };
}
