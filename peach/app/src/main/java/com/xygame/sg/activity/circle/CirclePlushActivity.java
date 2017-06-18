package com.xygame.sg.activity.circle;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.init.ResponseAliParams;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageTools;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.action.Action;
import base.frame.VisitUnit;


public class CirclePlushActivity extends SGBaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,TextWatcher {
    private View backButton,rightButton,add_video;
    private EditText circleContent;
    private GridView modelPicGridView;
    private ImageView add_video_iv,preview_play_iv;
    private String provinceName, cityName, provinceCode, cityCode;
    private boolean isStrictCity = false;
    private List<ProvinceBean> datas;
    private PhotoesTotalBean newItem;

    private LinkedList<String> picDatas;
    private MyGridViewAdapter adapter;
    private List<PhotoesBean> uploadImages;
    private int currIndex = 0;
    private String photoName;
    private ImageLoader imageLoader;

    //视频地址
    private String path;
    //视频第一帧地址
    private String imagePath;
    private Long videoSize;
    private Long videoTime;
    private TextView numCount;
    private int maxNum=600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_plush_layout);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_RECORDER_CIRCLE);
        myIntentFilter.addAction(Constants.IMAGE_BROADCAST_LISTENER);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        initViews();
        addListener();
        initDatas();

    }

    private void initDatas() {
        numCount.setText("0/".concat(String.valueOf(maxNum)));
        String flag=getIntent().getStringExtra("flag");
        if ("tp".equals(flag)){
            newItem=(PhotoesTotalBean)getIntent().getSerializableExtra("bean");
            modelPicGridView.setVisibility(View.VISIBLE);
            add_video.setVisibility(View.GONE);
            imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
            picDatas = new LinkedList<String>();
            uploadImages = new ArrayList<PhotoesBean>();
            for (int i=0;i<newItem.getImageObjects().size();i++){
                picDatas.add(newItem.getImageObjects().get(i).getImageUrls());
            }
            updateChoiceImages();
        }else{
            Intent intent =getIntent();
            //视频地址
            path = intent.getStringExtra("path");
            //视频第一帧地址
            imagePath = intent.getStringExtra("imagePath");
            videoSize = intent.getLongExtra("videoSize", 0);
            videoTime = intent.getLongExtra("videoTime", 0);
            modelPicGridView.setVisibility(View.GONE);
            add_video.setVisibility(View.VISIBLE);
            preview_play_iv.setVisibility(View.VISIBLE);
            ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(imagePath, add_video_iv);
        }
        datas = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
        datas.remove(0);
        ThreadPool.getInstance().excuseThread(new BaiduLocation());
    }

    private void updateChoiceImages() {
        picDatas.add(null);
        adapter = new MyGridViewAdapter(this, picDatas);
        modelPicGridView.setAdapter(adapter);
    }

    private void addListener() {
        circleContent.addTextChangedListener(this);
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        modelPicGridView.setOnItemClickListener(this);
        add_video.setOnClickListener(this);
        add_video.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog();
                return false;
            }
        });
    }

    private void initViews() {
        numCount = (TextView) findViewById(R.id.numCount);
        add_video=findViewById(R.id.add_video);
        backButton=findViewById(R.id.backButton);
        rightButton=findViewById(R.id.rightButton);
        circleContent=(EditText)findViewById(R.id.circleContent);
        modelPicGridView=(GridView)findViewById(R.id.gridview);
        add_video_iv=(ImageView)findViewById(R.id.add_video_iv);
        preview_play_iv=(ImageView)findViewById(R.id.preview_play_iv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backButton:
                finish();
                break;
            case R.id.rightButton:
                String flag=getIntent().getStringExtra("flag");
                if ("tp".equals(flag)){
                    if (adapter.getCount() - 1 > 0) {
                        uploadImages();
                    } else {
                        Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (imagePath!=null) {
                        uploadVideo();
                    } else {
                        Toast.makeText(this, "请录制视频", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.add_video:
                if (imagePath!=null){
                    Intent intent = new Intent(this, VideoPlayerActivity.class);
                    intent.putExtra("vidoUrl", path);
                    startActivity(intent);
                } else{
                    toRecorderVideo();
                }
                break;
        }
    }

    private  void showDeleteDialog(){
        TwoButtonDialog dialog = new TwoButtonDialog(this, "确定要删除该视频吗？", R.style.dineDialog,
						new ButtonTwoListener() {

					@Override
					public void confrimListener() {
                        path = null;
                        //视频第一帧地址
                        imagePath =null;
                        videoSize = 0L;
                        videoTime = 0L;
                        add_video_iv.setImageResource(R.drawable.addpicture_gray);
					}

					@Override
					public void cancelListener() {
					}
				});
				dialog.show();
    }

    private void setLocationPosion() {
        // TODO Auto-generated method stub
        provinceName = BaiduPreferencesUtil.getProvice(this);

        if (provinceName != null) {
            for (String str : Constants.CITY_STRICT) {
                if (provinceName.contains(str)) {
                    isStrictCity = true;
                    cityName = BaiduPreferencesUtil.getCity(this);
                }
            }
            if (!isStrictCity) {
                cityName = BaiduPreferencesUtil.getCity(this);
            }
        }
        getCodeAction();
    }

    private void getCodeAction() {
        // TODO Auto-generated method stub
        try {
            for (ProvinceBean it : datas) {
                it.get();
                if (provinceName.contains(it.getProvinceName())) {
                    provinceCode = it.getProvinceCode();
                    List<CityBean> cityDatas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(provinceCode));
                    if (isStrictCity) {
                        for (CityBean cIt : cityDatas) {
                            cIt.get();
                            for (String str : Constants.CITY_STRICT) {
                                if (cIt.getCityName().contains(str)) {
                                    cityCode = cIt.getCityCode();
                                }
                            }
                        }
                    }else{
                        for (CityBean cIt : cityDatas) {
                            cIt.get();
                            if (cityName.contains(cIt.getCityName())) {
                                cityCode = cIt.getCityCode();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String shortStr=s.toString();
        if("".equals(shortStr)){
            numCount.setText("0/".concat(String.valueOf(maxNum)));
        }else{
            numCount.setText(String.valueOf(shortStr.length()).concat("/").concat(String.valueOf(maxNum)));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class BaiduLocation implements Runnable{

        @Override
        public void run() {
            setLocationPosion();
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
                        R.layout.circle_plush_add_button, null);
                addView.findViewById(R.id.add).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (AliPreferencesUtil.getBuckekName(CirclePlushActivity.this)==null){
                                    new Action(ResponseAliParams.class,"${ali_params}", CirclePlushActivity.this, null, new VisitUnit()).run();
                                    Toast.makeText(CirclePlushActivity.this, "加载配置稍后重试", Toast.LENGTH_SHORT)
                                            .show();
                                }else{
                                    if (datas.size() - 1 == 9) {
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
                        R.layout.circle_plush_image_item, null);
                ImageView picIBtn = (ImageView) picView.findViewById(R.id.pic);
                String headUrl = datas.get(position);
                if (headUrl.contains("http://")) {
                    imageLoader.loadImage(headUrl, picIBtn, false);
                } else {
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            headUrl, picIBtn);
                }
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
        intent.putExtra(Constants.TRANS_PIC_NUM, 9);
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
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        List<String> tempImages = adapter.getImages();
        if (position < tempImages.size()) {
            Constants.imageBrower(CirclePlushActivity.this,
                    position, tempImages
                            .toArray(new String[tempImages.size()]), true);
        }
    }

    private void uploadImages() {
        ShowMsgDialog.show(this, "图片上传中...", false);
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

                AliySSOHepler.getInstance().uploadImageEngine(this,Constants.NEWS_PATH, uploadImages.get(currIndex).getImageUrl(), new HttpCallBack() {

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
            uploadInfo();
        }
    }

    private void uploadVideo(){
        ShowMsgDialog.show(this, "视频传中...", false);
        if (!path.contains("http://")) {
            AliySSOHepler.getInstance().uploadMedia(this, Constants.NEWS_PATH,path,new HttpCallBack() {

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
        } else {
            uploadInfo();
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
                        ShowMsgDialog.cancel();
                        Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        String imageUrl1 = (String) msg.obj;
                        path=imageUrl1;
                        uploadInfo();
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

    private void toRecorderVideo() {
//        Intent intent = new Intent(this, FFmpegRecorderActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_RECORDER_CIRCLE.equals(intent.getAction())) {
                //视频地址
                path = intent.getStringExtra("path");
                //视频第一帧地址
                imagePath = intent.getStringExtra("imagePath");
                videoSize = intent.getLongExtra("videoSize", 0);
                videoTime = intent.getLongExtra("videoTime",0);
            }else  if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent
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
                                String photoName = Constants.getImageName(CirclePlushActivity.this);
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
            }
        }
    };

    private void uploadInfo(){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            String circleCon=circleContent.getText().toString().trim();
            if (!TextUtils.isEmpty(circleCon)){
                obj.put("content",circleCon);
            }

            if (!TextUtils.isEmpty(provinceCode)){
                obj.put("locProvince",provinceCode);
            }

            if (!TextUtils.isEmpty(cityCode)){
                obj.put("locCity",cityCode);
            }

            String flag=getIntent().getStringExtra("flag");
            JSONArray array=new JSONArray();
            if ("tp".equals(flag)){
                for(PhotoesBean it:uploadImages){
                    JSONObject obj1=new JSONObject();
                    obj1.put("resUrl", it.getImageUrl());
                    obj1.put("resWidth", it.getWidthPx());
                    obj1.put("resHeight", it.getLengthPx());
                    obj1.put("resSize", it.getPicSize());
                    obj1.put("resType", "1");
                    array.put(obj1);
                }
            }else{
                JSONObject obj1=new JSONObject();
                obj1.put("resUrl", path);
                obj1.put("resType", "2");
                array.put(obj1);
            }
            obj.put("ress",array);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_CIRCLE_PLUSH);
        ThreadPool.getInstance().excuseAction(this,item,ConstTaskTag.QUERY_CIRCLE_PLUSH);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        if ("0000".equals(data.getCode())){
            Toast.makeText(this,"发布成功",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(Constants.COMEBACK,true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else {
            Toast.makeText(this,"发布失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this,"发布失败",Toast.LENGTH_SHORT).show();
    }
}
