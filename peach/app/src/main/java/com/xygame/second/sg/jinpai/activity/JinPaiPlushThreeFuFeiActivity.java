package com.xygame.second.sg.jinpai.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xygame.second.sg.UtilsWrapper;
import com.xygame.second.sg.comm.activity.PublicProvinceActivity;
import com.xygame.second.sg.comm.bean.ServiceTimeBean;
import com.xygame.second.sg.comm.bean.ServiceTimeDateBean;
import com.xygame.second.sg.comm.bean.TimerDuringBean;
import com.xygame.second.sg.defineview.DuanWeiCaputre;
import com.xygame.second.sg.defineview.DymicServiceTimeDialog;
import com.xygame.second.sg.jinpai.bean.DuanWeiBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.second.sg.jinpai.bean.TransActionBean;
import com.xygame.second.sg.localvideo.LoadedImage;
import com.xygame.second.sg.localvideo.LocalVideoActivity;
import com.xygame.second.sg.localvideo.Video;
import com.xygame.second.sg.utils.Contant;
import com.xygame.second.sg.utils.RecordResult;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageTools;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class JinPaiPlushThreeFuFeiActivity extends SGBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView titleName, actAreaText, dangDateText,duanWeiText;
    private EditText actTitleText, oralText;
    private View backButton, nextStep, actArea, dangDate,duanWeiView,duanWeiLine,quyuLine;
    private GridView photoList;
    private MyGridViewAdapter adapter;
    private EditText priceValue;
    private LinkedList<String> picDatas;
    private String photoName, path, imagePath;
    private List<PhotoesBean> uploadImages;
    private int currIndex = 0;
    private Video videoBean;
    private ImageView videoButton;
    private TransActionBean transActionBean;
    private JinPaiBigTypeBean bigTypeBean;
    private List<JinPaiSmallTypeBean> smallTypeBeans;
    private String actTitleStr, oralTitleStr, priceValueStr;
    private TimerDuringBean timerDuringBean;
    private ProvinceBean areaBean;
    private DuanWeiBean duanWeiBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_plush_three_fufei_layout);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.IMAGE_BROADCAST_LISTENER);
        myIntentFilter.addAction(Constants.ACTION_RECORDER_SUCCESS);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        quyuLine=findViewById(R.id.quyuLine);
        duanWeiLine=findViewById(R.id.duanWeiLine);
        duanWeiText=(TextView)findViewById(R.id.duanWeiText);
        duanWeiView=findViewById(R.id.duanWeiView);
        videoButton = (ImageView) findViewById(R.id.videoButton);
        actArea = findViewById(R.id.actArea);
        dangDate = findViewById(R.id.dangDate);
        priceValue = (EditText) findViewById(R.id.priceValue);
        oralText = (EditText) findViewById(R.id.oralText);
        actAreaText = (TextView) findViewById(R.id.actAreaText);
        dangDateText = (TextView) findViewById(R.id.dangDateText);
        actTitleText = (EditText) findViewById(R.id.actTitleText);

        nextStep = findViewById(R.id.nextStep);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        photoList = (GridView) findViewById(R.id.gridview);
    }

    private void initDatas() {
        timerDuringBean = new TimerDuringBean();
        transActionBean = (TransActionBean) getIntent().getSerializableExtra("bean");
        bigTypeBean = transActionBean.getBigTypeBean();
        smallTypeBeans = transActionBean.getSmallTypeBeans();
        controlViewDis();
        titleName.setText("发布活动");
        picDatas = new LinkedList<String>();
        uploadImages = new ArrayList<PhotoesBean>();
        videoButton.setImageResource(R.drawable.shiping_icon);
        updateChoiceImages();
    }

    private void controlViewDis() {
        //控制显示段位
        if ("900".equals(bigTypeBean.getId())||"1000".equals(bigTypeBean.getId())){
            duanWeiLine.setVisibility(View.VISIBLE);
            duanWeiView.setVisibility(View.VISIBLE);
        }else{
            duanWeiLine.setVisibility(View.GONE);
            duanWeiView.setVisibility(View.GONE);
        }

        //控制显示区域
        if ("900".equals(bigTypeBean.getId())||"1100".equals(bigTypeBean.getId())||"1200".equals(bigTypeBean.getId())){
            quyuLine.setVisibility(View.GONE);
            actArea.setVisibility(View.GONE);

        }else{
            quyuLine.setVisibility(View.VISIBLE);
            actArea.setVisibility(View.VISIBLE);
        }
    }

    private void addListener() {
        nextStep.setOnClickListener(this);
        backButton.setOnClickListener(this);
        actArea.setOnClickListener(this);
        dangDate.setOnClickListener(this);
        photoList.setOnItemClickListener(this);
        videoButton.setOnClickListener(this);
        duanWeiView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.videoButton:
                Contant.startRecordActivity(this, 8);
                break;
            case R.id.nextStep:
                if (isGo()) {
                    uploadVideo();
                }
                break;
            case R.id.actArea:
                Intent firstIntent = new Intent(this, PublicProvinceActivity.class);
                startActivityForResult(firstIntent, 6);
                break;
            case R.id.dangDate:
                Intent intent3 = new Intent(this, DymicServiceTimeDialog.class);
                intent3.putExtra("dateTime", System.currentTimeMillis());
                intent3.putExtra("dateDistance", 7);
                intent3.putExtra("timerDuringBean", timerDuringBean);
                startActivityForResult(intent3, 3);
                break;
            case R.id.duanWeiView:
                Intent intent=new Intent(this, DuanWeiCaputre.class);
                startActivityForResult(intent, 4);
                break;
        }
    }

    private Boolean isGo() {
        if (path == null) {
            Toast.makeText(this, "请添加活动视频", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (adapter.getCount() - 1 == 0) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return false;
        }
        actTitleStr = actTitleText.getText().toString().trim();
        if (TextUtils.isEmpty(actTitleStr)) {
            Toast.makeText(this, "请添加活动标题", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (timerDuringBean.getDateDatas() == null) {
            Toast.makeText(this, "请选择档期", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!"900".equals(bigTypeBean.getId())&&!"1100".equals(bigTypeBean.getId())&&!"1200".equals(bigTypeBean.getId())){
            if (areaBean == null) {
                Toast.makeText(this, "请选择活动区域", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        priceValueStr = priceValue.getText().toString().trim();
        if (TextUtils.isEmpty(priceValueStr)) {
            Toast.makeText(this, "请添加价格", Toast.LENGTH_SHORT).show();
            return false;
        }

        if ("900".equals(bigTypeBean.getId())||"1000".equals(bigTypeBean.getId())){
            if (duanWeiBean==null){
                Toast.makeText(this, "请选择段位", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        oralTitleStr = oralText.getText().toString().trim();
        if (TextUtils.isEmpty(oralTitleStr)) {
            Toast.makeText(this, "请添加活动说明", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        List<String> tempImages = adapter.getImages();
        if (position < tempImages.size()) {
            Constants.imageBrower(this,
                    position, tempImages
                            .toArray(new String[tempImages.size()]), true);
        }
    }

    public class MyGridViewAdapter extends BaseAdapter {

        private Context context;
        private List<String> datas;

        /**
         * <默认构造函数>
         */
        public MyGridViewAdapter(Context context, List<String> datas) {
            this.context = context;
            if (datas != null) {
                this.datas = datas;
            } else {
                this.datas = new ArrayList<String>();
            }
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public String getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clearDatas() {
            datas.clear();
        }

        public void addDatas(List<String> datas) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (position == datas.size() - 1) {
                View addView = inflater.inflate(
                        R.layout.second_dev_pic_add_button, null);
                addView.findViewById(R.id.add).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (AliPreferencesUtil.getBuckekName(JinPaiPlushThreeFuFeiActivity.this) == null) {
                                    requestAliyParams();
                                    Toast.makeText(JinPaiPlushThreeFuFeiActivity.this, "加载配置稍后重试", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    if (datas.size() - 1 == Constants.ACT_PIC_NUM) {
                                        Toast.makeText(getApplicationContext(),
                                                "已达到最多图片数量", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        showChoiceWays();
                                    }
                                }
                            }
                        });
                return addView;
            } else {
                View picView = inflater.inflate(
                        R.layout.item_fragment_list_imgs, null);
                ImageView picIBtn = (ImageView) picView.findViewById(R.id.id_img);
                String headUrl = datas.get(position);
                ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                        headUrl, picIBtn);
                return picView;
            }
        }

        public List<String> getImages() {
            List<String> tempImages = new ArrayList<String>();
            tempImages.addAll(datas);
            tempImages.remove(tempImages.size() - 1);
            return tempImages;
        }
    }

    private void showChoiceWays() {//chicePhoto();
        Intent intent = new Intent(this, PickPhotoesView.class);
        startActivityForResult(intent, 1);
    }

    private void chicePhoto() {
        TransferImagesBean dataBean = new TransferImagesBean();
        picDatas.remove(picDatas.size() - 1);
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, Constants.ACT_PIC_NUM);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    updateChoiceImages();
                    return;
                }
                Object result = data.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    picDatas = new LinkedList<String>();
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        if (tempStrs.get(i)
                                .contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                            picDatas.add(tempStrs.get(i));
                        } else {
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
                    }
                    updateChoiceImages();
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
                try {
                    Bitmap bitmap = FileUtil.compressImages(FileUtil
                            .getPhotopath(photoName));
                    if (bitmap != null) {
                        picDatas.remove(picDatas.size() - 1);
                        FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName),
                                bitmap);
                        String photoPath = FileUtil.getPhotopath(photoName);
                        picDatas.add(photoPath);
                        updateChoiceImages();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                timerDuringBean = (TimerDuringBean) data.getSerializableExtra(Constants.COMEBACK);
                dangDateText.setText("已设定");
                dangDateText.setTextColor(getResources().getColor(R.color.dark_green));
                break;
            case 4:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                duanWeiBean=(DuanWeiBean)data.getSerializableExtra("bean");
                duanWeiText.setText(duanWeiBean.getName());
                break;
            case 6: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                areaBean = (ProvinceBean) data.getSerializableExtra(Constants.COMEBACK);
                actAreaText.setText(areaBean.getProvinceName().concat("..."));
                break;
            }
            case 8:
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
                        thum[0], videoButton);
                ShowMsgDialog.showNoMsg(this,false);
                ThreadPool.getInstance().excuseThread(new MovingMp4(videoFile));
                break;
            default:
                UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MovingMp4 implements Runnable{
        private String videoFile;
        public MovingMp4(String videoFile){
            this.videoFile=videoFile;
        }
        @Override
        public void run() {
            String mFileName= FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP4Name(JinPaiPlushThreeFuFeiActivity.this));
            int isSccuss=FileUtil.CopySdcardFile(videoFile,mFileName);
            if (isSccuss==0){
                Message msg = new Message();
                msg.what = 6;
                msg.obj=mFileName;
                handler.sendMessage(msg);
            }
        }
    }

    private void takePhoto() {
        // 跳转至拍照界面
//        photoName = String.valueOf(System.currentTimeMillis()).concat(".png");
        photoName = Constants.getImageName(this);
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        File out = new File(FileUtil.getPhotopath(photoName));
        Uri imageUri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentPhote, 2);
    }

    private void updateChoiceImages() {
        picDatas.add(null);
        adapter = new MyGridViewAdapter(this, picDatas);
        photoList.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent
                    .getAction())) {
                boolean flag = intent.getBooleanExtra(
                        Constants.IS_DELE_IMAGES, false);
                if (!flag) {
                    return;
                }
                Object result = intent
                        .getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    picDatas = new LinkedList<String>();
                    LinkedList<String> tempStrs = dataBean
                            .getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        if (tempStrs.get(i).contains(
                                FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                            picDatas.add(tempStrs.get(i));
                        } else {
                            Bitmap subBitmap = FileUtil
                                    .compressImages(tempStrs.get(i));
                            if (subBitmap != null) {
                                String photoName = Constants.getImageName(JinPaiPlushThreeFuFeiActivity.this);
                                FileUtil.saveScalePhoto(
                                        FileUtil.getPhotopath(photoName),
                                        subBitmap);
                                subBitmap.recycle();
                                picDatas.add(FileUtil
                                        .getPhotopath(photoName));
                            }
                        }
                    }
                    updateChoiceImages();
                }
            } else if (Constants.ACTION_RECORDER_SUCCESS.equals(intent
                    .getAction())) {
                String flag = intent.getStringExtra("flag");
                if ("local".equals(flag)) {
                    videoBean = (Video) intent.getSerializableExtra("bean");
                    path = videoBean.getPath();
                    Message msg1 = new Message();
                    msg1.what = 0;
                    handler.sendMessage(msg1);
                } else {
                    path = intent.getStringExtra("path");
                    imagePath = intent.getStringExtra("imagePath");
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            imagePath, videoButton);
                }
            }
        }
    };

    private void uploadImages() {
//        ShowMsgDialog.show(this, "图片上传中...", false);
        uploadImages.clear();
        currIndex = 0;
        List<String> datas = adapter.getImages();
        for (int i = 0; i < datas.size(); i++) {
            Bitmap bitmap = ImageTools.getBitmap(datas.get(i));
            PhotoesBean pb = new PhotoesBean();
            pb.setImageIndex(String.valueOf(i + 1));
            pb.setImageUrl(datas.get(i));
            pb.setLengthPx(String.valueOf(bitmap.getHeight()));
            pb.setPicSize(FileUtil.getImageByteSize(datas.get(i)));
            pb.setWidthPx(String.valueOf(bitmap.getWidth()));
            bitmap.recycle();
            uploadImages.add(pb);
        }
        doUploadImages();
    }

    private void doUploadImages() {
        if (currIndex < uploadImages.size()) {
            if (!uploadImages.get(currIndex).getImageUrl().contains("http://")) {

                AliySSOHepler.getInstance().uploadImageEngine(this, Constants.ACTIVITY_PATH, uploadImages.get(currIndex).getImageUrl(), new HttpCallBack() {

                    @Override
                    public void onSuccess(String imageUrl, int requestCode) {
                        Message msg = new Message();
                        msg.obj = imageUrl;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(int errorCode, String msg, int requestCode) {
                        // TODO Auto-generated method stub
                        Message msg1 = new Message();
                        msg1.what = 2;
                        handler.sendMessage(msg1);
                    }

                    @Override
                    public void onProgress(String objectKey, int byteCount, int totalSize) {
                        // TODO Auto-generated method stub

                    }
                });
            } else {
                doUploadImages();
            }
        } else {
//            ShowMsgDialog.cancel();
            uploadInfo();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 6:
                        ShowMsgDialog.cancel();
                        path=(String)msg.obj;
                        break;
                    case 1:
                        String imageUrl = (String) msg.obj;
                        uploadImages.get(currIndex).setImageUrl(imageUrl);
                        currIndex = currIndex + 1;
                        doUploadImages();
                        break;
                    case 2:
                        ShowMsgDialog.cancel();
                        Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        LoadedImage image = (LoadedImage) msg.obj;
                        videoButton.setImageBitmap(image.getBitmap());
                        break;
                    case 5:
//                        ShowMsgDialog.cancel();
                        String imageUrl1 = (String) msg.obj;
                        path = imageUrl1;
                        uploadImages();
                        break;
                    case 4:
                        ShowMsgDialog.cancel();
                        Toast.makeText(getApplicationContext(), "视频上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void uploadVideo() {
        if (!path.contains("http://")) {
            ShowMsgDialog.show(this, "发布中...", false);
            AliySSOHepler.getInstance().uploadMedia(this, Constants.ACTIVITY_PATH, path, new HttpCallBack() {

                @Override
                public void onSuccess(String imageUrl, int requestCode) {
                    Message msg = new Message();
                    msg.obj = imageUrl;
                    msg.what = 5;
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
        } else {
            ShowMsgDialog.show(this, "发布中...", false);
            uploadImages();
        }
    }

    private void uploadInfo() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            //第一部分
            JSONObject actObj = new JSONObject();
            actObj.put("actTitle", actTitleStr);
            actObj.put("actType", bigTypeBean.getId());
            List<ServiceTimeDateBean> dateBeans = timerDuringBean.getDateDatas();
            ServiceTimeDateBean tempExist = null;
            ServiceTimeBean timeTemp = null;
            List<ServiceTimeDateBean> choicedDate = new ArrayList<>();
            for (ServiceTimeDateBean item1 : dateBeans) {
                boolean flag = false;
                List<ServiceTimeBean> timeBeans = item1.getTimeBeans();
                if (timeBeans.get(timeBeans.size()-1).isSelect()&&"-1".equals(timeBeans.get(timeBeans.size()-1).getId())){
                    flag = true;
                    tempExist = item1;
                    timeTemp = timeBeans.get(timeBeans.size()-2);
                }else{
                    for (ServiceTimeBean item2 : timeBeans) {
                        if (item2.isSelect()) {
                            flag = true;
                            tempExist = item1;
                            timeTemp = item2;
                        }
                    }
                }
                if (flag) {
                    choicedDate.add(tempExist);
                }
            }
            String tempShortTimeStr = tempExist.getDscStr().concat(" ").concat(timeTemp.getTime());
            actObj.put("endTime", CalendarUtils.getTimeLong(tempShortTimeStr));
            actObj.put("price", Integer.parseInt(priceValueStr)*100);
            if (smallTypeBeans!=null){
                actObj.put("subType", smallTypeBeans.get(0).getTypeId());
            }
            actObj.put("actDesc", oralTitleStr);

            if (duanWeiBean!=null){
                actObj.put("skillLevel", duanWeiBean.getId());
            }

            obj.put("action", actObj);

            //图片和视频
            JSONArray array = new JSONArray();
            for (int i = 0; i < uploadImages.size(); i++) {
                JSONObject obj1 = new JSONObject();
                obj1.put("resUrl", uploadImages.get(i).getImageUrl());
                obj1.put("resType", "2");
                array.put(obj1);
            }
            JSONObject obj2 = new JSONObject();
            obj2.put("resUrl", path);
            obj2.put("resType", "1");
            array.put(obj2);
            obj.put("ress", array);

            if (areaBean!=null){
                //已经选择的区域
                JSONArray areaArray = new JSONArray();

                List<CityBean> areaBeans = areaBean.getCityBean().getAreaBeans();
                if (areaBeans.get(0).isSelect()) {
                    JSONObject obj3 = new JSONObject();
                    obj3.put("province", areaBean.getProvinceCode());
                    obj3.put("city", areaBean.getCityBean().getCityCode());
                    areaArray.put(obj3);
                } else {
                    for (CityBean it : areaBeans) {
                        it.get();
                        if (it.isSelect()) {
                            JSONObject obj3 = new JSONObject();
                            obj3.put("province", areaBean.getProvinceCode());
                            obj3.put("city", areaBean.getCityBean().getCityCode());
                            obj3.put("region", it.getCityCode());
                            areaArray.put(obj3);
                        }
                    }
                }

                obj.put("areas", areaArray);
            }

            //档期
            JSONArray dqArray = new JSONArray();
            for (ServiceTimeDateBean it : choicedDate) {
                List<ServiceTimeBean> timeBeans = it.getTimeBeans();

                if (timeBeans.get(timeBeans.size() - 1).isSelect()&&!timeBeans.get(timeBeans.size() - 1).isUnUsed()) {
                    JSONObject obj3 = new JSONObject();
                    obj3.put("serviceDay", CalendarUtils.getHG_Y_M_DTimeLong(it.getDscStr()));
                    dqArray.put(obj3);
                } else {
                    JSONObject obj3 = new JSONObject();
                    obj3.put("serviceDay", CalendarUtils.getHG_Y_M_DTimeLong(it.getDscStr()));
                    for (ServiceTimeBean item2 : timeBeans) {
                        if (item2.isSelect()) {
                            if ("09:00".equals(item2.getTime())) {
                                obj3.put("hour9","1");
                            }
                            if ("10:00".equals(item2.getTime())) {
                                obj3.put("hour10", "1");
                            }
                            if ("11:00".equals(item2.getTime())) {
                                obj3.put("hour11","1");
                            }
                            if ("12:00".equals(item2.getTime())) {
                                obj3.put("hour12","1");
                            }
                            if ("13:00".equals(item2.getTime())) {
                                obj3.put("hour13","1");
                            }
                            if ("14:00".equals(item2.getTime())) {
                                obj3.put("hour14","1");
                            }
                            if ("15:00".equals(item2.getTime())) {
                                obj3.put("hour15","1");
                            }
                            if ("16:00".equals(item2.getTime())) {
                                obj3.put("hour16", "1");
                            }
                            if ("17:00".equals(item2.getTime())) {
                                obj3.put("hour17","1");
                            }
                            if ("18:00".equals(item2.getTime())) {
                                obj3.put("hour18", "1");
                            }
                            if ("19:00".equals(item2.getTime())) {
                                obj3.put("hour19", "1");
                            }
                            if ("20:00".equals(item2.getTime())) {
                                obj3.put("hour20", "1");
                            }
                            if ("21:00".equals(item2.getTime())) {
                                obj3.put("hour21","1");
                            }
                            if ("22:00".equals(item2.getTime())) {
                                obj3.put("hour22", "1");
                            }
                        }
                    }
                    dqArray.put(obj3);
                }
            }
            obj.put("schedules", dqArray);

            item.setData(obj);

//            ShowMsgDialog.show(this, "发布中...", false);
            item.setServiceURL(ConstTaskTag.QUEST_ACT_FUFEI);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_FUFEI);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ACT_FUFEI:
                if ("0000".equals(data.getCode())) {
                    UMImage image = new UMImage(this, UserPreferencesUtil.getHeadPic(this));
                    new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                            .withMedia(image).withText("").withTitle("").withTargetUrl("http://www.baidu.com").share();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Intent intent = new Intent();
            intent.putExtra(Constants.COMEBACK, true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Intent intent = new Intent();
            intent.putExtra(Constants.COMEBACK, true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Intent intent = new Intent();
            intent.putExtra(Constants.COMEBACK, true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}
