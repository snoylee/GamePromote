package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.bean.TransferGift;
import com.xygame.second.sg.utils.ImageFactory;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.EditorTextContentActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.notice.PlushNoticePlaceProvinceActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.PhotoesSubBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.ChoiceDateView;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.define.view.SingleWheelView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class EditorInfoActivity extends SGBaseActivity implements OnClickListener {

    private TextView titleName, userNick, userSex, userAge, userLaction, userSign, userFeeling, userCarrier, userIncoming, userHeight, userWeight, userFeelingAbout, userSexAbout, userHalfAbout;
    private View userHeadImageView, backButton, userNickView, userAgeView, userLocationView, userSignView, userFeelingView, userCarrierView, userIncomingView, userHeightView, userWeightView, userFeelingAboutView, userSexAboutView, userHalfAboutView;
    private CircularImage headImage;
    private ImageLoader imageLoader;
    private String birthday, province, city, loveStatus, job, salary, loveOpinion, sexOpinion, mateOpinion, height, weight;
    private String photoName;
    private Uri imageUri, originalUri;
    private static final int TAKE_PICTURE = 1;
    private static final int CHOOSE_PICTURE = 2;
    private static final int CROP_PICTURE = 3;
    public String photoPath = "", serviceUrl, userNickName, birthDayVaule, newHearText, wheelStr, carrierStr, loveStr, sex_Str, mateStr;
    public int currOprator;
    private PlushNoticeAreaBean areaBean;
    /**
     * 头像款
     */
    private int w = 200;
    /**
     * 头像高
     */
    private int h = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_info_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        headImage = (CircularImage) findViewById(R.id.headImage);
        titleName = (TextView) findViewById(R.id.titleName);

        userNick = (TextView) findViewById(R.id.userNick);
        userSex = (TextView) findViewById(R.id.userSex);
        userAge = (TextView) findViewById(R.id.userAge);
        userLaction = (TextView) findViewById(R.id.userLaction);
        userSign = (TextView) findViewById(R.id.userSign);
        userFeeling = (TextView) findViewById(R.id.userFeeling);
        userCarrier = (TextView) findViewById(R.id.userCarrier);
        userIncoming = (TextView) findViewById(R.id.userIncoming);
        userHeight = (TextView) findViewById(R.id.userHeight);
        userWeight = (TextView) findViewById(R.id.userWeight);
        userFeelingAbout = (TextView) findViewById(R.id.userFeelingAbout);
        userSexAbout = (TextView) findViewById(R.id.userSexAbout);
        userHalfAbout = (TextView) findViewById(R.id.userHalfAbout);

        backButton = findViewById(R.id.backButton);
        userHeadImageView = findViewById(R.id.userHeadImageView);
        userNickView = findViewById(R.id.userNickView);
        userAgeView = findViewById(R.id.userAgeView);
        userLocationView = findViewById(R.id.userLocationView);
        userSignView = findViewById(R.id.userSignView);
        userFeelingView = findViewById(R.id.userFeelingView);
        userCarrierView = findViewById(R.id.userCarrierView);
        userIncomingView = findViewById(R.id.userIncomingView);
        userHeightView = findViewById(R.id.userHeightView);
        userWeightView = findViewById(R.id.userWeightView);
        userFeelingAboutView = findViewById(R.id.userFeelingAboutView);
        userSexAboutView = findViewById(R.id.userSexAboutView);
        userHalfAboutView = findViewById(R.id.userHalfAboutView);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
        userNickView.setOnClickListener(this);
        userAgeView.setOnClickListener(this);
        userLocationView.setOnClickListener(this);
        userSignView.setOnClickListener(this);
        userFeelingView.setOnClickListener(this);
        userCarrierView.setOnClickListener(this);
        userIncomingView.setOnClickListener(this);
        userHeightView.setOnClickListener(this);
        userWeightView.setOnClickListener(this);
        userFeelingAboutView.setOnClickListener(this);
        userSexAboutView.setOnClickListener(this);
        userHalfAboutView.setOnClickListener(this);
        userHeadImageView.setOnClickListener(this);
    }

    private void initDatas() {
        areaBean = new PlushNoticeAreaBean();
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("修改资料");
        userNick.setText(UserPreferencesUtil.getUserNickName(this));
        userAge.setText(UserPreferencesUtil.getUserAge(this));
        String sexStr = UserPreferencesUtil.getSex(this);
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            userSex.setText("女");
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            userSex.setText("男");
        }
        imageLoader.loadImage(UserPreferencesUtil.getHeadPic(this), headImage, true);

        loadUserInfo();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.userHeadImageView) {
            Intent intent = new Intent(this, PickPhotoesView.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.userNickView) {
            Intent intent = new Intent(this, EditorTextContentActivity.class);
            intent.putExtra(Constants.EDITOR_TEXT_TITLE, "修改昵称");
            intent.putExtra("oral", userNick.getText().toString());
            intent.putExtra("hint", "请输入昵称不超过10个字");
            intent.putExtra(Constants.TEXT_EDITOR_NUM, 10);
            startActivityForResult(intent, 5);
        } else if (v.getId() == R.id.userAgeView) {
            startActivityForResult(new Intent(this,
                    ChoiceDateView.class), 6);
        } else if (v.getId() == R.id.userLocationView) {
            Intent firstIntent = new Intent(this, PlushNoticePlaceProvinceActivity.class);
            firstIntent.putExtra("noLimitFlag", true);
            firstIntent.putExtra("bean", areaBean);
            startActivityForResult(firstIntent, 7);
        } else if (v.getId() == R.id.userSignView) {
            Intent intent = new Intent(this, EditorTextContentActivity.class);
            intent.putExtra(Constants.EDITOR_TEXT_TITLE, "修改个性签名");
            if (!TextUtils.isEmpty(userSign.getText().toString())) {
                intent.putExtra("oral", UserPreferencesUtil.getHeartText(this));
            } else {
                intent.putExtra("oral", "");
            }
            intent.putExtra("hint", "个性签名不超过30个字");
            intent.putExtra(Constants.TEXT_EDITOR_NUM, 30);
            startActivityForResult(intent, 8);
        } else if (v.getId() == R.id.userFeelingView) {
            TransferGift transferBean = new TransferGift();
            transferBean.setWhellStr(ConstTaskTag.LOVE_OPTION);
            Intent firstIntent = new Intent(this, SingleWheelView.class);
            firstIntent.putExtra("bean", transferBean);
            firstIntent.putExtra("titleStr", "感情状况");
            startActivityForResult(firstIntent, 9);
        } else if (v.getId() == R.id.userCarrierView) {
            Intent intent = new Intent(this, EditorTextContentActivity.class);
            intent.putExtra(Constants.EDITOR_TEXT_TITLE, "修改职业");
            intent.putExtra("oral", userCarrier.getText().toString());
            intent.putExtra("hint", "职业类型不超过10个字");
            intent.putExtra(Constants.TEXT_EDITOR_NUM, 10);
            startActivityForResult(intent, 10);
        } else if (v.getId() == R.id.userIncomingView) {
            TransferGift transferBean = new TransferGift();
            transferBean.setWhellStr(ConstTaskTag.SALARY_ARRAY);
            Intent firstIntent = new Intent(this, SingleWheelView.class);
            firstIntent.putExtra("bean", transferBean);
            firstIntent.putExtra("titleStr", "收入");
            startActivityForResult(firstIntent, 11);
        } else if (v.getId() == R.id.userHeightView) {
            TransferGift transferBean = new TransferGift();
            transferBean.setWhellStr(ConstTaskTag.getHeightArray());
            Intent firstIntent = new Intent(this, SingleWheelView.class);
            firstIntent.putExtra("bean", transferBean);
            firstIntent.putExtra("titleStr", "身高（CM）");
            startActivityForResult(firstIntent, 15);
        } else if (v.getId() == R.id.userWeightView) {
            TransferGift transferBean = new TransferGift();
            transferBean.setWhellStr(ConstTaskTag.getWeightArray());
            Intent firstIntent = new Intent(this, SingleWheelView.class);
            firstIntent.putExtra("bean", transferBean);
            firstIntent.putExtra("titleStr", "体重（KG）");
            startActivityForResult(firstIntent, 16);
        } else if (v.getId() == R.id.userFeelingAboutView) {
            Intent intent = new Intent(this, EditorTextContentActivity.class);
            intent.putExtra(Constants.EDITOR_TEXT_TITLE, "对爱情的看法");
            if (!TextUtils.isEmpty(userFeelingAbout.getText().toString())) {
                intent.putExtra("oral", loveOpinion);
            } else {
                intent.putExtra("oral", "");
            }
            intent.putExtra("hint", "请输入您对爱情的看法不超过50个字");
            intent.putExtra(Constants.TEXT_EDITOR_NUM, 50);
            startActivityForResult(intent, 12);
        } else if (v.getId() == R.id.userSexAboutView) {
            Intent intent = new Intent(this, EditorTextContentActivity.class);
            intent.putExtra(Constants.EDITOR_TEXT_TITLE, "对性的看法");
            if (!TextUtils.isEmpty(userSexAbout.getText().toString())) {
                intent.putExtra("oral", sexOpinion);
            } else {
                intent.putExtra("oral", "");
            }
            intent.putExtra("hint", "请输入您对性的看法不超过50个字");
            intent.putExtra(Constants.TEXT_EDITOR_NUM, 50);
            startActivityForResult(intent, 13);
        } else if (v.getId() == R.id.userHalfAboutView) {
            Intent intent = new Intent(this, EditorTextContentActivity.class);
            intent.putExtra(Constants.EDITOR_TEXT_TITLE, "对另一半的要求");
            if (!TextUtils.isEmpty(userHalfAbout.getText().toString())) {
                intent.putExtra("oral", mateOpinion);
            } else {
                intent.putExtra("oral", "");
            }
            intent.putExtra("hint", "请输入您对另一半的要求不超过50个字");
            intent.putExtra(Constants.TEXT_EDITOR_NUM, 50);
            startActivityForResult(intent, 14);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
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
            case TAKE_PICTURE:
                try {
                    Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
                    if (bitmap != null) {
                        photoName = Constants.getImageName(this);
                        photoPath = FileUtil.getPhotopath(photoName);
                        FileUtil.saveScalePhoto(photoPath, bitmap);
                        if (AliPreferencesUtil.getBuckekName(this) == null) {
                            requestAliyParams();
                            Toast.makeText(this, "配置加载中，稍后重试", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadHeadPic();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

//                File out1 = new File(FileUtil.getPhotopath(photoName));
//                imageUri = Uri.fromFile(out1);
//                cropImageUri(imageUri, w, h, CROP_PICTURE);
                break;
//            case CROP_PICTURE:
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getPhotopath(photoName));
//                    if (bitmap != null) {
//                        photoName = Constants.getImageName(this);
//                        photoPath = FileUtil.getPhotopath(photoName);
//                        FileUtil.saveScalePhoto(photoPath, bitmap);
//                        if (AliPreferencesUtil.getBuckekName(this) == null) {
//                            requestAliyParams();
//                            Toast.makeText(this, "配置加载中，稍后重试", Toast.LENGTH_SHORT).show();
//                        } else {
//                            uploadHeadPic();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } catch (OutOfMemoryError e) {
//                    e.printStackTrace();
//                }
//                break;
            case CHOOSE_PICTURE:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Object result = data.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
                        if (subBitmap != null) {
                            photoName = Constants.getImageName(this);
                            photoPath = FileUtil.getPhotopath(photoName);
                            FileUtil.saveScalePhoto(photoPath, subBitmap);
                        }
                    }
                    if (tempStrs!=null&&tempStrs.size()>0){
                        if (AliPreferencesUtil.getBuckekName(this) == null) {
                            requestAliyParams();
                            Toast.makeText(this, "配置加载中，稍后重试", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadHeadPic();
                        }
                    }
                }
//
//
//                Object result = data.getSerializableExtra(Constants.COMEBACK);
//                if (result != null) {
//                    TransferImagesBean dataBean = (TransferImagesBean) result;
//                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
//                    for (int i = 0; i < tempStrs.size(); i++) {
//                        photoName = FileUtil.getNewPhotoPath(tempStrs.get(i));
//                        File out = new File(photoName);
//                        originalUri = Uri.fromFile(out);
//                    }
//                    cropPickImageUri(originalUri, w, h, 4);
//                }
                break;
//            case 4:
//                try {
//                    Bitmap photo = BitmapFactory.decodeFile(photoName);
//                    if (photo == null) {
//                        photo = ImageFactory.getBitmap(photoName);
//                    }
//                    if (photo != null) {
//                        int angle = FileUtil.getOrientation(this, originalUri);
//                        if (angle > 0) {
//                            photo = FileUtil.rotaingImageView(angle, photo);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
            case 5:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                userNickName = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                currOprator = 2;
                updateBaseInfo();
                break;
            case 6:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Serializable item2 = data.getSerializableExtra("bean");
                if (item2 != null) {
                    FeedbackDateBean ftBean = (FeedbackDateBean) item2;
                    birthDayVaule = ftBean.getYear() + "-" + ftBean.getMonth()
                            + "-" + ftBean.getDay();
                    currOprator = 3;
                    updateBaseInfo();
                }
                break;
            case 7:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                areaBean = (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
                currOprator = 4;
                updateBaseInfo();
                break;
            case 8:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                newHearText = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                currOprator = 5;
                updateBaseInfo();
                break;
            case 9:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String finalStr = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                String[] tempArray = ConstTaskTag.LOVE_OPTION;
                for (int i = 0; i < tempArray.length; i++) {
                    if (finalStr.equals(tempArray[i])) {
                        wheelStr = String.valueOf(i + 1);
                        break;
                    }
                }
                currOprator = 6;
                updateBaseInfo();
                break;
            case 10:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                carrierStr = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                currOprator = 7;
                updateBaseInfo();
                break;
            case 11:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String finalStr1 = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                String[] tempArray1 = ConstTaskTag.SALARY_ARRAY;
                for (int i = 0; i < tempArray1.length; i++) {
                    if (finalStr1.equals(tempArray1[i])) {
                        wheelStr = String.valueOf(i + 1);
                        break;
                    }
                }
                currOprator = 8;
                updateBaseInfo();
                break;
            case 12:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                loveStr = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                currOprator = 9;
                updateBaseInfo();
                break;
            case 13:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                sex_Str = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                currOprator = 10;
                updateBaseInfo();
                break;
            case 14:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                mateStr = data.getStringExtra(Constants.EDITOR_TEXT_TITLE);
                currOprator = 11;
                updateBaseInfo();
                break;
            case 15:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                wheelStr = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                currOprator = 12;
                updateBaseInfo();
                break;
            case 16:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                wheelStr = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
                currOprator = 13;
                updateBaseInfo();
                break;
            default:
                break;
        }
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
        startActivityForResult(intentPhote, TAKE_PICTURE);
    }

    private void chicePhoto() {

        LinkedList<String> picDatas = new LinkedList<String>();
        TransferImagesBean dataBean = new TransferImagesBean();
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, 1);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, CHOOSE_PICTURE);
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

    private void uploadHeadPic() {
        ShowMsgDialog.show(this, "头像上传中...", false);
        AliySSOHepler.getInstance().uploadImageEngine(this, Constants.AVATAR_PATH, photoPath, new HttpCallBack() {

            @Override
            public void onSuccess(String imageUrl, int requestCode) {
                serviceUrl = imageUrl;
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int errorCode, String msg, int requestCode) {
                // TODO Auto-generated method stub
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(EditorInfoActivity.this, "头像上传失败", Toast.LENGTH_SHORT)
                                .show();
                        ShowMsgDialog.cancel();
                        break;
                    case 1:
                        ShowMsgDialog.cancel();
                        currOprator = 1;
                        updateBaseInfo();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void updateBaseInfo() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            switch (currOprator) {
                case 1:
                    object.put("userIcon", serviceUrl);
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_INFO);
                    break;
                case 2:
                    object.put("usernick", userNickName);
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_INFO);
                    break;
                case 3:
                    object.put("birthday", getTime(birthDayVaule));
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_INFO);
                    break;
                case 4:
                    object.put("province", areaBean.getProvinceId());
                    object.put("city", areaBean.getCityId());
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_INFO);
                    break;
                case 5:
                    if (TextUtils.isEmpty(newHearText)) {
                        object.put("introDesc", null);
                    } else {
                        object.put("introDesc", newHearText);
                    }
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_INTODESC);
                    break;
                case 6:
                    object.put("loveStatus", wheelStr);
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_OTHER);
                    break;
                case 7:
                    object.put("job", carrierStr);
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_OTHER);
                    break;
                case 8:
                    object.put("salary", wheelStr);
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_OTHER);
                    break;
                case 9:
                    if (TextUtils.isEmpty(loveStr)) {
                        object.put("loveOpinion", null);
                    } else {
                        object.put("loveOpinion", loveStr);
                    }
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_OTHER);
                    break;
                case 10:
                    if (TextUtils.isEmpty(sex_Str)) {
                        object.put("sexOpinion", null);
                    } else {
                        object.put("sexOpinion", sex_Str);
                    }
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_OTHER);
                    break;
                case 11:
                    if (TextUtils.isEmpty(mateStr)) {
                        object.put("mateOpinion", null);
                    } else {
                        object.put("mateOpinion", mateStr);
                    }
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_OTHER);
                    break;
                case 12:
                    object.put("height", wheelStr);
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_HEIGHT_WEIGHT);
                    break;
                case 13:
                    object.put("weight", wheelStr);
                    item.setData(object);
                    item.setServiceURL(ConstTaskTag.QUEST_BASE_HEIGHT_WEIGHT);
                    break;
            }
            ShowMsgDialog.show(this, "提交中...", false);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_BASE_INFO);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public String getTime(String arg) {
        String str = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(arg);
            long timeStemp = date.getTime();
            str = String.valueOf(timeStemp);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return str;
    }

    private void loadUserInfo() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_ME_INFO);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ME_INFO);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ME_INFO:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_BASE_INFO:
                if ("0000".equals(data.getCode())) {
                    updateUserBaseInfo();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void updateUserBaseInfo() {
        switch (currOprator) {
            case 1:
                UserPreferencesUtil.setHeadPic(this, serviceUrl);
                imageLoader.loadImage(UserPreferencesUtil.getHeadPic(this), headImage, true);
                break;
            case 2:
                UserPreferencesUtil.setUserNickName(this, userNickName);
                userNick.setText(UserPreferencesUtil.getUserNickName(this));
                break;
            case 3:
                try {
                    int ageInt = getAge(getTime(birthDayVaule));
                    UserPreferencesUtil.setUserAge(this, String.valueOf(ageInt));
                    userAge.setText(UserPreferencesUtil.getUserAge(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                CityBean provinceBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(areaBean.getProvinceId()));
                CityBean cityBean = null;
                if (!TextUtils.isEmpty(areaBean.getCityId()) && !"null".equals(areaBean.getCityId())) {
                    cityBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(areaBean.getCityId()));
                }
                if (cityBean != null) {
                    userLaction.setText(provinceBean.getName().concat(" ").concat(cityBean.getName()));
                } else {
                    userLaction.setText(provinceBean.getName());
                }
                break;
            case 5:
                UserPreferencesUtil.setHeartText(this, newHearText);
                userSign.setText(UserPreferencesUtil.getHeartText(this));
                break;
            case 6:
                if (!TextUtils.isEmpty(wheelStr)) {
                    String[] feelArray = ConstTaskTag.LOVE_OPTION;
                    userFeeling.setText(feelArray[Integer.parseInt(wheelStr) - 1]);
                }
                break;
            case 7:
                userCarrier.setText(carrierStr);
                break;
            case 8:
                if (!TextUtils.isEmpty(wheelStr)) {
                    String[] feelArray = ConstTaskTag.SALARY_ARRAY;
                    userIncoming.setText(feelArray[Integer.parseInt(wheelStr) - 1]);
                }
                break;
            case 9:
                loveOpinion = loveStr;
                userFeelingAbout.setText(loveOpinion);
                break;
            case 10:
                sexOpinion = sex_Str;
                userSexAbout.setText(sexOpinion);
                break;
            case 11:
                mateOpinion = mateStr;
                userHalfAbout.setText(mateOpinion);
                break;
            case 12:
                userHeight.setText(wheelStr.concat("CM"));
                break;
            case 13:
                userWeight.setText(wheelStr.concat("KG"));
                break;
        }
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object = new JSONObject(record);
                birthday = StringUtils.getJsonValue(object, "birthday");
                province = StringUtils.getJsonValue(object, "province");
                city = StringUtils.getJsonValue(object, "city");
                String other = StringUtils.getJsonValue(object, "other");
                if (!TextUtils.isEmpty(other) && !"null".equals(other)) {
                    JSONObject object1 = new JSONObject(other);
                    loveStatus = StringUtils.getJsonValue(object1, "loveStatus");
                    job = StringUtils.getJsonValue(object1, "job");
                    salary = StringUtils.getJsonValue(object1, "salary");
                    loveOpinion = StringUtils.getJsonValue(object1, "loveOpinion");
                    sexOpinion = StringUtils.getJsonValue(object1, "sexOpinion");
                    mateOpinion = StringUtils.getJsonValue(object1, "mateOpinion");
                }
                String userBody = StringUtils.getJsonValue(object, "body");
                if (!TextUtils.isEmpty(userBody) && !"null".equals(userBody)) {
                    JSONObject object2 = new JSONObject(userBody);
                    height = StringUtils.getJsonValue(object2, "height");
                    weight = StringUtils.getJsonValue(object2, "weight");
                }
                int ageInt = getAge(birthday);
                UserPreferencesUtil.setUserAge(this, String.valueOf(ageInt));
                updateViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateViews() {
        userNick.setText(UserPreferencesUtil.getUserNickName(this));
        userAge.setText(UserPreferencesUtil.getUserAge(this));
        String sexStr = UserPreferencesUtil.getSex(this);
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            userSex.setText("女");
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            userSex.setText("男");
        }
        CityBean provinceBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(province));
        CityBean cityBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(city));
        userLaction.setText(provinceBean.getName().concat(" ").concat(cityBean.getName()));
        userSign.setText(UserPreferencesUtil.getHeartText(this));
        if (!TextUtils.isEmpty(mateOpinion)) {
            userHalfAbout.setText(mateOpinion);
        }
        if (!TextUtils.isEmpty(loveOpinion)) {
            userFeelingAbout.setText(loveOpinion);
        }
        if (!TextUtils.isEmpty(sexOpinion)) {
            userSexAbout.setText(sexOpinion);
        }

        if (!TextUtils.isEmpty(loveStatus)) {
            String[] feelArray = ConstTaskTag.LOVE_OPTION;
            userFeeling.setText(feelArray[Integer.parseInt(loveStatus) - 1]);
        }
        if (!TextUtils.isEmpty(job)) {
            userCarrier.setText(job);
        }
        if (!TextUtils.isEmpty(salary)) {
            String[] incomArray = ConstTaskTag.SALARY_ARRAY;
            userIncoming.setText(incomArray[Integer.parseInt(salary) - 1]);
        }

        if (!TextUtils.isEmpty(height)) {
            userHeight.setText(height.concat("CM"));
        }
        if (!TextUtils.isEmpty(weight)) {
            userWeight.setText(weight.concat("KG"));
        }
        if (UserPreferencesUtil.getHeadPic(this) != null) {
            imageLoader.loadImage(UserPreferencesUtil.getHeadPic(this), headImage, true);
        }
    }

    private int getAge(String birthdayStr) throws ParseException {
//        SimpleDateFormat init = new SimpleDateFormat("yyyy-MM-dd");

//        Date birthDate = init.parse(birthdayStr);

        Date birthDate = new Date(Long.parseLong(birthdayStr));

        if (birthDate == null)
            throw new RuntimeException("出生日期不能为null");

        int age = 0;

        Date now = new Date();

        SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
        SimpleDateFormat format_M = new SimpleDateFormat("MM");

        String birth_year = format_y.format(birthDate);
        String this_year = format_y.format(now);

        String birth_month = format_M.format(birthDate);
        String this_month = format_M.format(now);

        // 初步，估算
        age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

        // 如果未到出生月份，则age - 1
        if (this_month.compareTo(birth_month) < 0)
            age -= 1;
        if (age < 0)
            age = 0;
        return age;
    }
}
