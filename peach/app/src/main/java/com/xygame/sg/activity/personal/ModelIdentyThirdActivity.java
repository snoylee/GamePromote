package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.notice.PlushNoticePlaceActivity;
import com.xygame.sg.activity.notice.PlushNoticePlaceProvinceActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.activity.personal.bean.IdentyTranBean;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.define.view.SingleWheelView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.certification.AuthModelInfo;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageTools;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class ModelIdentyThirdActivity extends SGBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View backButton;
    private TextView titleName;
    private IdentyTranBean identyTranBean;
    VisitUnit visitUnit = new VisitUnit();

    private GridView modelPicGridView;
    private LinkedList<String> picDatas;
    private MyGridViewAdapter adapter;
    private List<PhotoesBean> uploadImages;
    private int currIndex = 0;
    private EditText introduceText;
    private String photoName = "";
    private ImageLoader imageLoader;
    private Button submit_bt;
    private List<ProvinceBean> datas;
    private View modifyBodyHightView, modifyBodyWeightView,
            modifyBodyHeartView, modifyBodyThreeHeartView, modifyBodyShoesView;
    private TextView bodyHightValues, bodyWeightValues, bodyHeartValues,
            bodyThreeHeartValues, bodyShoesValues;
    private PlushNoticeAreaBean areaBean;
    private String provinceName, cityName, provinceCode, cityCode;
    private boolean isStrictCity = false;
    private String userHeight, userWeight, userBust, userWaist, userHip, userCup, userShoesCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_model_identy_third, null));
        initViews();
        initDatas();
        addListener();
        setLocationPosion();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getString(R.string.title_activity_model_identy_third));

        introduceText = (EditText) findViewById(R.id.introduceText);
        modelPicGridView = (GridView) findViewById(R.id.modelPicGridView);
        modifyBodyHightView = findViewById(R.id.modifyBodyHightView);
        modifyBodyWeightView = findViewById(R.id.modifyBodyWeightView);
        modifyBodyHeartView = findViewById(R.id.modifyBodyHeartView);
        modifyBodyThreeHeartView = findViewById(R.id.modifyBodyThreeHeartView);
        modifyBodyShoesView = findViewById(R.id.modifyBodyShoesView);

        bodyHightValues = (TextView) findViewById(R.id.bodyHightValues);
        bodyWeightValues = (TextView) findViewById(R.id.bodyWeightValues);
        bodyHeartValues = (TextView) findViewById(R.id.bodyHeartValues);
        bodyThreeHeartValues = (TextView) findViewById(R.id.bodyThreeHeartValues);
        bodyShoesValues = (TextView) findViewById(R.id.bodyShoesValues);

        submit_bt = (Button) findViewById(R.id.submit_bt);
    }

    private void initDatas() {
        areaBean = new PlushNoticeAreaBean();
        datas = ((List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0));
        datas.remove(0);
        identyTranBean = (IdentyTranBean) getIntent().getSerializableExtra("identyTranBean");
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        picDatas = new LinkedList<String>();
        uploadImages = new ArrayList<PhotoesBean>();
        updateChoiceImages();
        imageListeners();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        modelPicGridView.setOnItemClickListener(this);
        modifyBodyHightView.setOnClickListener(this);
        modifyBodyWeightView.setOnClickListener(this);
        modifyBodyHeartView.setOnClickListener(this);
        modifyBodyThreeHeartView.setOnClickListener(this);
        modifyBodyShoesView.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String title;
        String[] scope = null;
        int j = 1;
        int[] init = new int[1];
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.modifyBodyHightView:
                title = ((TextView) findViewById(R.id.bodyHightTitle)).getText()
                        .toString();
                title += " (cm)";
                scope = genScope(150, 190, 1);
                init[0] = 20;
                Intent intent1 = new Intent(this, SingleWheelView.class);
                if (scope != null) {
                    for (int i = 1; i <= j; i++) {
                        intent1.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
                                .putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
                                .putExtra(Constants.SIGLE_WHEEL_ITEM + i,
                                        init[i - 1]);
                    }
                    startActivityForResult(intent1, 3);

                }
                break;
            case R.id.modifyBodyWeightView:
                title = ((TextView) findViewById(R.id.bodyWeightTitle)).getText()
                        .toString();
                title += " (kg)";
                scope = genScope(40, 75, 1);
                init[0] = 5;
                Intent intent2 = new Intent(this, SingleWheelView.class);
                if (scope != null) {
                    for (int i = 1; i <= j; i++) {
                        intent2.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
                                .putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
                                .putExtra(Constants.SIGLE_WHEEL_ITEM + i,
                                        init[i - 1]);
                    }
                    startActivityForResult(intent2, 4);


                }
                break;
            case R.id.modifyBodyThreeHeartView:
                title = ((TextView) findViewById(R.id.bodyThreeHeartTitle))
                        .getText().toString();
                scope = genScope(50, 110, 1);
                init = new int[3];
                init[0] = 20;
                init[1] = 0;
                init[2] = 20;
                j = 3;
                Intent intent3 = new Intent(this, SingleWheelView.class);
                if (scope != null) {
                    for (int i = 1; i <= j; i++) {
                        intent3.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
                                .putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
                                .putExtra(Constants.SIGLE_WHEEL_ITEM + i,
                                        init[i - 1]);
                    }
                    startActivityForResult(intent3, 5);

                }
                break;
            case R.id.modifyBodyHeartView:
                title = ((TextView) findViewById(R.id.bodyHeartTitle)).getText()
                        .toString();
                scope = new String[]{"A", "B", "C", "D", "E", "F", "G"};
                init[0] = 1;
                Intent intent4 = new Intent(this, SingleWheelView.class);
                if (scope != null) {
                    for (int i = 1; i <= j; i++) {
                        intent4.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
                                .putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
                                .putExtra(Constants.SIGLE_WHEEL_ITEM + i,
                                        init[i - 1]);
                    }
                    startActivityForResult(intent4, 6);

                }
                break;
            case R.id.modifyBodyShoesView:
                Intent firstIntent = new Intent(this, PlushNoticePlaceProvinceActivity.class);
                firstIntent.putExtra("noLimitFlag", true);
                firstIntent.putExtra("bean", areaBean);
                startActivityForResult(firstIntent, 7);
//
//                title = ((TextView) findViewById(R.id.bodyShoesTitle)).getText()
//                        .toString();
//                title += " (码)";
//                scope = genScope(30, 45, 1);
//                init[0] = 6;
//                Intent intent5 = new Intent(this, SingleWheelView.class);
//                if (scope != null) {
//                    for (int i = 1; i <= j; i++) {
//                        intent5.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
//                                .putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
//                                .putExtra(Constants.SIGLE_WHEEL_ITEM + i,
//                                        init[i - 1]);
//                    }
//                    startActivityForResult(intent5, 7);
//                }
                break;
            case R.id.submit_bt:
                toSubmit();
                break;
        }
    }

    private void toSubmit() {
        if (StringUtils.isEmpty(userHeight)) {
            Toast.makeText(this, "请选择您的身高！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(userWeight)) {
            Toast.makeText(this, "请选择您的体重！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(userCup) && UserPreferencesUtil.getSex(this).equals(Constants.SEX_WOMAN)) {
            Toast.makeText(this, "请选择您的罩杯！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(userBust) || StringUtils.isEmpty(userWaist) || StringUtils.isEmpty(userHip)) {
            Toast.makeText(this, "请选择您的三围！", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (StringUtils.isEmpty(userShoesCode)){
//            Toast.makeText(this,"请选择您的鞋码！",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (StringUtils.isEmpty(userShoesCode)){
//            Toast.makeText(this,"请选择您的鞋码！",Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (StringUtils.isEmpty(getOral())) {
            Toast.makeText(this, "请介绍下您的作品！", Toast.LENGTH_SHORT).show();
            return;
        }
        identyTranBean.setUserHeight(userHeight);
        identyTranBean.setUserWeight(userWeight);
        identyTranBean.setUserCup(userCup);
        identyTranBean.setUserBust(userBust);
        identyTranBean.setUserWaist(userWaist);
        identyTranBean.setUserHip(userHip);
        identyTranBean.setOpusDesc(getOral());
        if (adapter.getCount() - 1 > 0) {
            uploadImages();
        } else {
            Toast.makeText(this, "请上传您作品集！", Toast.LENGTH_SHORT).show();
        }
    }

    public String[] genScope(float b, float e, float step) {
        List<String> l = new ArrayList<String>();
        for (float i = b; i <= e; i += step) {
            l.add("" + new Float(i).intValue());
        }
        String[] a = new String[l.size()];
        l.toArray(a);
        return a;
    }

    @Override
    public void onDestroy() {
        SGApplication.getInstance().unregisterImagesReceiver(mBroadcastReceiver);
        super.onDestroy();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if (position == datas.size() - 1) {
                View addView = inflater.inflate(R.layout.identy_pic_item_add, null);
                addView.findViewById(R.id.add).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (datas.size() - 1 == Constants.MODEL_PIC_NUM) {
                                    Toast.makeText(getApplicationContext(), "已达到最多图片数量", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    showChoiceWays();
                                }
                            }
                        });
                return addView;
            } else {
                View picView = inflater.inflate(R.layout.identy_pic_item, null);
                ImageView picIBtn = (ImageView) picView.findViewById(R.id.pic);
                String headUrl = datas.get(position);
                if (headUrl.contains("http://")) {
                    imageLoader.loadImage(headUrl, picIBtn, true);
                } else {
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(headUrl, picIBtn);
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

    private void showChoiceWays() {
        Intent intent = new Intent(this, PickPhotoesView.class);
        startActivityForResult(intent, 1);
    }

    private void chicePhoto() {
        TransferImagesBean dataBean = new TransferImagesBean();
        picDatas.remove(picDatas.size() - 1);
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, Constants.MODEL_PIC_NUM);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String s = "";
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
            case 1://选择相册或相机的页面返回
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
            case 2://拍照返回
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
                if (data == null) {
                    return;
                }
                if (data.hasExtra(Constants.SIGLE_WHEEL_BACK_VALUE)) {
                    s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                }
                bodyHightValues.setText(s.concat("CM"));
                userHeight = s;
                break;
            case 4:
                if (data == null) {
                    return;
                }
                if (data.hasExtra(Constants.SIGLE_WHEEL_BACK_VALUE)) {
                    s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                }
                bodyWeightValues.setText(s.concat("KG"));
                userWeight = s;
                break;
            case 5:
                if (data == null) {
                    return;
                }
                if (data.hasExtra(Constants.SIGLE_WHEEL_BACK_VALUE)) {
                    s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                }
                bodyThreeHeartValues.setText(s);
                String[] array = s.split("-");
                userBust = array[0];
                userWaist = array[1];
                userHip = array[2];
                break;
            case 6:
                if (data == null) {
                    return;
                }
                if (data.hasExtra(Constants.SIGLE_WHEEL_BACK_VALUE)) {
                    s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                }
                bodyHeartValues.setText(s);
                userCup = s;
                break;
            case 7:
                if (data == null) {
                    return;
                }
                areaBean = (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
                updateAreaText();
//                if (data.hasExtra(Constants.SIGLE_WHEEL_BACK_VALUE)){
//                    s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
//                }
//                bodyShoesValues.setText(s.concat("码"));
//                userShoesCode=s;
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void takePhoto() {// 跳转至拍照界面
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
        modelPicGridView.setAdapter(adapter);
    }


    private void imageListeners() {
        SGApplication.getInstance().registerImagesReceiver(mBroadcastReceiver);
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
                                String photoName = Constants.getImageName(ModelIdentyThirdActivity.this);
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        List<String> tempImages = adapter.getImages();
        if (position < tempImages.size()) {
            Constants.imageBrower(ModelIdentyThirdActivity.this,
                    position, tempImages
                            .toArray(new String[tempImages.size()]), true);
        }
    }

    private void uploadImages() {
        ShowMsgDialog.showNoMsg(this, false);
        uploadImages.clear();
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

    public List<PhotoesBean> getUploadImages() {
        return uploadImages;
    }

    public String getOral() {
        return introduceText.getText().toString().trim();
    }

    private void doUploadImages() {
        if (currIndex < uploadImages.size()) {
            if (!uploadImages.get(currIndex).getImageUrl().contains("http://")) {

                AliySSOHepler.getInstance().uploadImageEngine(this, Constants.PHOTOBUM_PATH, uploadImages.get(currIndex).getImageUrl(), new HttpCallBack() {

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
                            Toast.makeText(getApplicationContext(), "作品上传失败！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public void finishUpload(String msg) {
        ShowMsgDialog.cancel();
        Toast.makeText(getApplicationContext(), "申请认证成功！", Toast.LENGTH_SHORT).show();
        UserPreferencesUtil.setUserVerifyStatus(this, Constants.USER_VERIFING_CODE);
        comfrimDialog();
    }

    private void comfrimDialog() {
        OneButtonDialog dialog = new OneButtonDialog(this, "审核中，预计1个工作日出结果", R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        finish();
                    }
                });
        dialog.show();
    }

    private void transferLocationService() {
        identyTranBean.setUploadImages(uploadImages);
        VisitUnit visit = new VisitUnit();
        new Action(AuthModelInfo.class, "${authModelInfo}", this, null, visit).run();
    }

    public IdentyTranBean getIdentyTranBean() {
        return identyTranBean;
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
            if (isStrictCity) {
                getCodeAction();
                updateAreaText();
            } else {
                cityName = BaiduPreferencesUtil.getCity(this);
                getCodeAction();
                updateAreaText();
            }

        }
    }

    private void getCodeAction() {
        // TODO Auto-generated method stub
        try {
            for (ProvinceBean it : datas) {
                it.get();
                if (provinceName.contains(it.getProvinceName())) {
                    provinceCode = it.getProvinceCode();
                    if (isStrictCity) {
                        List<CityBean> cityDatas = AssetDataBaseManager.getManager().queryCitiesByParentId(Integer.parseInt(provinceCode));
                        for (CityBean cIt : cityDatas) {
                            cIt.get();
                            for (String str : Constants.CITY_STRICT) {
                                if (cIt.getCityName().contains(str)) {
                                    cityCode = cIt.getCityCode();
                                    areaBean.setCityId(cityCode);
                                    areaBean.setCityName(cityName);
                                    areaBean.setProvinceId(provinceCode);
                                    areaBean.setProvinceName(provinceName);
                                }
                            }
                        }
                    } else {
                        areaBean.setProvinceId(provinceCode);
                        areaBean.setProvinceName(provinceName);
                        areaBean.setCityId(null);
                        areaBean.setCityName(null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlushNoticeAreaBean getAreaBean() {
        return areaBean;
    }

    private void updateAreaText() {
        if (areaBean.getProvinceName() != null) {
            if (areaBean.getCityName() != null) {
                if (areaBean.getProvinceName().equals(areaBean.getCityName())) {
                    bodyShoesValues.setText(areaBean.getProvinceName());
                } else {
                    bodyShoesValues.setText(areaBean.getProvinceName().concat(" ").concat(areaBean.getCityName()));
                }
            } else {
                bodyShoesValues.setText(areaBean.getProvinceName());
            }
        }
    }
}
