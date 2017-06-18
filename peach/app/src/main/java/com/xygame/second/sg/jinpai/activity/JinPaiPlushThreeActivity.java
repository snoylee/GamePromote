package com.xygame.second.sg.jinpai.activity;


import android.app.Activity;
import android.app.Dialog;
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
import com.xygame.second.sg.comm.activity.PublicProvinceActivity;
import com.xygame.second.sg.comm.bean.FreeTimeBean;
import com.xygame.second.sg.comm.bean.TimerDuringBean;
import com.xygame.second.sg.defineview.DymicDatePicker;
import com.xygame.second.sg.jinpai.JinPaiLowerPriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.second.sg.jinpai.bean.TransActionBean;
import com.xygame.second.sg.localvideo.LoadedImage;
import com.xygame.second.sg.localvideo.Video;
import com.xygame.second.sg.utils.Contant;
import com.xygame.second.sg.utils.RecordResult;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.define.view.TimeDuringView;
import com.xygame.sg.define.view.TimeFreeDuringView;
import com.xygame.sg.define.view.TimeFreeDuringView1;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageTools;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class JinPaiPlushThreeActivity extends SGBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener ,View.OnFocusChangeListener {
    private TextView titleName, stopTimeText, actAreaText, dangDateText, timeDuringText;
    private EditText actTitleText, oralText;
    private View backButton, nextStep, stopTime, actArea, dangDate, timeDuring, jianButton, addButton;
    private GridView photoList;
    private MyGridViewAdapter adapter;
    private EditText jpLowerPriceText;
    private LinkedList<String> picDatas;
    private String photoName, path, imagePath;
    private List<PhotoesBean> uploadImages;
    private int currIndex = 0;
    private Video videoBean;
    private ImageView videoButton;
    private TransActionBean transActionBean;
    private JinPaiBigTypeBean bigTypeBean;
    private List<JinPaiSmallTypeBean> smallTypeBeans;
    private String actTitleStr, oralTitleStr;
    private TimerDuringBean timerDuringBean;
    private ProvinceBean areaBean;
    private PlushNoticeBean pnBean;
    private int timeDuringInt=0;
    private  List<JinPaiLowerPriceBean> lowerPriceBeans;
    private JinPaiLowerPriceBean jinPaiLowerPriceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_plush_three_layout);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.IMAGE_BROADCAST_LISTENER);
        myIntentFilter.addAction(Constants.ACTION_RECORDER_SUCCESS);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        videoButton = (ImageView) findViewById(R.id.videoButton);
        stopTime = findViewById(R.id.stopTime);
        actArea = findViewById(R.id.actArea);
        dangDate = findViewById(R.id.dangDate);
        timeDuring = findViewById(R.id.timeDuring);
        jianButton = findViewById(R.id.jianButton);
        addButton = findViewById(R.id.addButton);
        stopTimeText = (TextView) findViewById(R.id.stopTimeText);
        jpLowerPriceText = (EditText) findViewById(R.id.jpLowerPriceText);
        oralText = (EditText) findViewById(R.id.oralText);
        actAreaText = (TextView) findViewById(R.id.actAreaText);
        dangDateText = (TextView) findViewById(R.id.dangDateText);
        timeDuringText = (TextView) findViewById(R.id.timeDuringText);
        actTitleText = (EditText) findViewById(R.id.actTitleText);

        nextStep = findViewById(R.id.nextStep);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        photoList = (GridView) findViewById(R.id.gridview);
    }

    private void initDatas() {
        transActionBean = (TransActionBean) getIntent().getSerializableExtra("bean");
        bigTypeBean = transActionBean.getBigTypeBean();
        smallTypeBeans = transActionBean.getSmallTypeBeans();
        pnBean = new PlushNoticeBean();
        titleName.setText("发布活动");
        picDatas = new LinkedList<String>();
        uploadImages = new ArrayList<PhotoesBean>();
        videoButton.setImageResource(R.drawable.shiping_icon);
        updateChoiceImages();
        lowerPriceBeans=CacheService.getInstance().getCacheJbLowerPrice(ConstTaskTag.CACHE_JP_LOWER_PRICE);
        if (lowerPriceBeans==null){
            loadBiggestPriceOfType();
        }else{
            for (JinPaiLowerPriceBean it:lowerPriceBeans){
                if (bigTypeBean.getId().equals(it.getTypeId())){
                    jinPaiLowerPriceBean=it;
                    break;
                }
            }
            if (jinPaiLowerPriceBean!=null){
                jpLowerPriceText.setHint(String.valueOf(jinPaiLowerPriceBean.getLowPrice()).concat("~").concat(String.valueOf(jinPaiLowerPriceBean.getHighPrice())));
            }
        }
    }

    private void addListener() {
        nextStep.setOnClickListener(this);
        backButton.setOnClickListener(this);
        jianButton.setOnClickListener(this);
        stopTime.setOnClickListener(this);
        actArea.setOnClickListener(this);
        dangDate.setOnClickListener(this);
        timeDuring.setOnClickListener(this);
        addButton.setOnClickListener(this);
        photoList.setOnItemClickListener(this);
        videoButton.setOnClickListener(this);
        jpLowerPriceText.setOnFocusChangeListener(this);
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
                lowerPriceBeans=CacheService.getInstance().getCacheJbLowerPrice(ConstTaskTag.CACHE_JP_LOWER_PRICE);
                if (lowerPriceBeans==null){
                    loadBiggestPriceOfType();
                    Toast.makeText(this,"出价检测中，稍后重试",Toast.LENGTH_SHORT).show();
                }else{
                    if (isGo()) {
                        uploadVideo();
                    }
                }
                break;
            case R.id.stopTime:
                Intent firstIntent4 = new Intent(this, DymicDatePicker.class);
                firstIntent4.putExtra("dateTime",System.currentTimeMillis());
                firstIntent4.putExtra("dateDistance",7);
                startActivityForResult(firstIntent4, 7);
                break;
            case R.id.actArea:
                Intent firstIntent = new Intent(this, PublicProvinceActivity.class);
                startActivityForResult(firstIntent, 6);
                break;
            case R.id.dangDate:
                timerDuringBean=new TimerDuringBean();
                Intent intent3 = new Intent(this, TimeFreeDuringView1.class);
                intent3.putExtra("timerDuringBean",timerDuringBean);
                startActivityForResult(intent3, 9);
                break;
            case R.id.timeDuring:
                Intent intent2 = new Intent(this, TimeDuringView.class);
                intent2.putExtra("timeDuringInt", timeDuringInt);
                startActivityForResult(intent2, 5);
                break;
            case R.id.jianButton:
                updatePriceEditor(0);
                break;
            case R.id.addButton:
                updatePriceEditor(1);
                break;
        }
    }

    private void updatePriceEditor(int falg) {
        String currPrice = jpLowerPriceText.getText().toString().trim();
        int currPriceValue;
        if (!TextUtils.isEmpty(currPrice)) {
            currPriceValue = Integer.parseInt(currPrice);
            ;
        } else {
            currPriceValue = 0;
        }

        switch (falg) {
            case 0:
                if (currPriceValue > 100) {
                    currPriceValue = currPriceValue - 100;
                    jpLowerPriceText.setText(String.valueOf(currPriceValue));
                }
                break;
            case 1:
                currPriceValue = currPriceValue + 100;
                jpLowerPriceText.setText(String.valueOf(currPriceValue));
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
        if (timeDuringInt == 0) {
            Toast.makeText(this, "请添选择时长", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (timerDuringBean==null){
            Toast.makeText(this, "请选择您的空闲时间段", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (timerDuringBean.getTimers()==null){
            Toast.makeText(this, "请选择您的空闲时间段", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (areaBean == null) {
            Toast.makeText(this, "请添选择活动区域", Toast.LENGTH_SHORT).show();
            return false;
        }
        oralTitleStr = oralText.getText().toString().trim();
        if (TextUtils.isEmpty(oralTitleStr)) {
            Toast.makeText(this, "请添加活动说明", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(jpLowerPriceText.getText().toString().trim())) {
            Toast.makeText(this, "请添加竟拍底价", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (jinPaiLowerPriceBean==null){
            for (JinPaiLowerPriceBean it:lowerPriceBeans){
                if (bigTypeBean.getId().equals(it.getTypeId())){
                    jinPaiLowerPriceBean=it;
                    break;
                }
            }
        }

        int currPrice=Integer.parseInt(jpLowerPriceText.getText().toString().trim());
        if (currPrice<jinPaiLowerPriceBean.getLowPrice()){
            Toast.makeText(this, "竟拍底价最少为".concat(String.valueOf(jinPaiLowerPriceBean.getLowPrice())), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (currPrice>jinPaiLowerPriceBean.getHighPrice()) {
            Toast.makeText(this, "竟拍底价不能超过".concat(String.valueOf(jinPaiLowerPriceBean.getHighPrice())), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pnBean.getEndTimeDes() == null) {
            Toast.makeText(this, "请选择竟拍截止时间", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            jugmentPrice();
        }
    }

    private void jugmentPrice(){
        int currPrice=Integer.parseInt(jpLowerPriceText.getText().toString().trim());
        if (currPrice<jinPaiLowerPriceBean.getLowPrice()){
            Toast.makeText(this, "竟拍底价最少为".concat(String.valueOf(jinPaiLowerPriceBean.getLowPrice())), Toast.LENGTH_SHORT).show();
        }
        if (currPrice>jinPaiLowerPriceBean.getHighPrice()) {
            Toast.makeText(this, "竟拍底价不能超过".concat(String.valueOf(jinPaiLowerPriceBean.getHighPrice())), Toast.LENGTH_SHORT).show();
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
                                if (AliPreferencesUtil.getBuckekName(JinPaiPlushThreeActivity.this) == null) {
                                    requestAliyParams();
                                    Toast.makeText(JinPaiPlushThreeActivity.this, "加载配置稍后重试", Toast.LENGTH_SHORT)
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
                timerDuringBean=(TimerDuringBean)data.getSerializableExtra(Constants.COMEBACK);
                dangDateText.setText("已设定");
                dangDateText.setTextColor(getResources().getColor(R.color.dark_green));
                break;
            case 5:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                timeDuringInt = data.getIntExtra(Constants.COMEBACK, 0);
                if (timeDuringInt != 0) {
                    String flag1 = data.getStringExtra("flag");
                    timeDuringText.setText(flag1);
                }
                break;
            case 6: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                areaBean = (ProvinceBean) data.getSerializableExtra(Constants.COMEBACK);
                actAreaText.setText(areaBean.getProvinceName().concat("..."));
                break;
            }
            case 7:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                FeedbackDateBean ftBean = (FeedbackDateBean) data.getSerializableExtra("bean");
                if (ftBean != null) {
                    if (StringUtil.isBigger(CalendarUtils.getHenGongDateDis(Long.parseLong(ftBean.getTimeLong())),CalendarUtils.getHenGongDateDis(System.currentTimeMillis()))){
                        String timeStr = CalendarUtils.getDateDistance(System.currentTimeMillis(), 7);
                        String lastTime=timeStr.concat("00:00");
                        if (StringUtil.isBigger(lastTime,CalendarUtils.getHenGongDateDis(Long.parseLong(ftBean.getTimeLong())))){
                            pnBean.setEndTime(ftBean.getTimeLong());
                            pnBean.setEndTimeDes(ftBean.getDateAllDesc());
                            pnBean.setFormatEndTime(ftBean.getFormatDateStr());
                            stopTimeText.setText(pnBean.getEndTimeDes());
                        }else{
                            Toast.makeText(this,"截止时间超出一周范围",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this,"截止时间不能小于当前时间",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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

            case 9:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                timerDuringBean=(TimerDuringBean)data.getSerializableExtra(Constants.COMEBACK);
                Intent intent3 = new Intent(this, TimeFreeDuringView.class);
                intent3.putExtra("timerDuringBean", timerDuringBean);
                startActivityForResult(intent3, 3);
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
            String mFileName= FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP4Name(JinPaiPlushThreeActivity.this));
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
                                String photoName = Constants.getImageName(JinPaiPlushThreeActivity.this);
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
                    if (Constants.DEBUG){
                        System.out.println("*************" + path);
                    }
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
//        ShowMsgDialog.show(this, "发布中...", false);
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
            actObj.put("holdTime", timeDuringInt);
            String currPrice = jpLowerPriceText.getText().toString().trim();
            actObj.put("startPrice", Integer.parseInt(currPrice)*100);
            actObj.put("endTime", pnBean.getEndTime());
            actObj.put("actDesc", oralTitleStr);
            if (smallTypeBeans!=null){
                actObj.put("subType", smallTypeBeans.get(0).getTypeId());
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

            //档期
            JSONArray dqArray = new JSONArray();
            List<FreeTimeBean> timers= timerDuringBean.getTimers();
            for (FreeTimeBean it:timers){
                JSONObject obj3 = new JSONObject();
                obj3.put("timeType", it.getId());
                obj3.put("timeIntervalType",timerDuringBean.getTimersOfDate().getId());
                dqArray.put(obj3);
            }
            obj.put("schedules", dqArray);
            item.setData(obj);
//            ShowMsgDialog.show(this, "发布中...", false);
            item.setServiceURL(ConstTaskTag.QUEST_ACT_PLUSH);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_PLUSH);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ACT_PLUSH:
                if ("0000".equals(data.getCode())) {
                    UMImage image = new UMImage(this, UserPreferencesUtil.getHeadPic(this));
                    new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                            .withMedia(image).withText("你我之间，就差一次相遇").withTitle("你我之间，就差一次相遇。".concat(UserPreferencesUtil.getUserNickName(this).concat("在喵伴伴上发了一个活动"))).withTargetUrl(Constants.TARG_URL).share();
                } else {
                    showMessageDiloag(data.getMsg());
                }
                break;
        }
    }

    private void showMessageDiloag(String tip){
        OneButtonDialog dialog = new OneButtonDialog(this, tip, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						dialog.dismiss();
					}
				});
				dialog.show();
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
