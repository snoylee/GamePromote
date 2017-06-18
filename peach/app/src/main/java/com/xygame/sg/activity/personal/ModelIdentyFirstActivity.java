package com.xygame.sg.activity.personal;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.personal.activity.VideoCertifyActivity;
import com.xygame.second.sg.personal.utils.ActionsFilter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.personal.bean.IdentyTranBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.task.init.ResponseAliParams;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import base.action.Action;
import base.frame.VisitUnit;

public class ModelIdentyFirstActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private EditText realname_et;
    private EditText idcard_et;
    private ImageView half_iv;
    private ImageView front_iv;
    private ImageView back_iv;
    private Button submit_bt;
    private String fromType;

    private static final int HALF = 0;
    private static final int FRONT = 1;
    private static final int BACK = 2;
    private int whichSelected = 0;
    private String photoName = "";

    private IdentyTranBean identyTranBean;
    private List<String> picPaths = new ArrayList<String>();
    private int currIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_identy_first);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("身份认证");
        realname_et = (EditText) findViewById(R.id.realname_et);
        idcard_et = (EditText) findViewById(R.id.idcard_et);
        half_iv = (ImageView) findViewById(R.id.half_iv);
        front_iv = (ImageView) findViewById(R.id.front_iv);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        submit_bt = (Button) findViewById(R.id.submit_bt);
    }

    private void initDatas() {
        fromType=getIntent().getStringExtra("fromType");
        identyTranBean = new IdentyTranBean();
        imageListeners();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        front_iv.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
    }


    private void doUploadImages() {
//        ShowMsgDialog.showNoMsg(this, false);
//        AliySSOHepler.getInstance().uploadSecretImage(this, Constants.VERIFICATION_PATH, picPaths.get(currIndex), new HttpCallBack() {
//
//            @Override
//            public void onSuccess(String imageUrl, int requestCode) {
//                Message msg = new Message();
//                msg.obj = imageUrl;
//                msg.what = 1;
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onFailure(int errorCode, String msg, int requestCode) {
//                Message msg1 = new Message();
//                msg1.what = 2;
//                handler.sendMessage(msg1);
//            }
//
//            @Override
//            public void onProgress(String objectKey, int byteCount, int totalSize) {
//
//            }
//        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 1:
                        String imageUrl = (String) msg.obj;
                        if (currIndex == 0) {
                            identyTranBean.setFrontUrl(imageUrl);
                        } else if (currIndex == 1) {
                            identyTranBean.setBackUrl(imageUrl);
                            uploadIdInfo();
                            break;
                        }
                        currIndex = currIndex + 1;
                        doUploadImages();
                        break;
                    case 2:
                        ShowMsgDialog.cancel();
                        Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
//                        finish();
                        break;
                }
            }
        }
    };

    private void uploadIdInfo() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            obj.put("idCard", identyTranBean.getIdCard());
            obj.put("realName", identyTranBean.getRealName());
            obj.put("picCardFront", identyTranBean.getFrontUrl());
            obj.put("picCardBack", identyTranBean.getBackUrl());
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_CERTIFY_ID);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CERTIFY_ID);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        ShowMsgDialog.cancel();
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_CERTIFY_ID:
                if ("0000".equals(data.getCode())) {
                    UserPreferencesUtil.setUserIDAuth(this,"4");
                    if ("jjr".equals(fromType)){
                        Intent intent=new Intent(this,CMCardCertifyActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ShowMsgDialog.cancel();
                        Intent intent = new Intent(ActionsFilter.ACTION_ID_SUCCESS);
                        sendBroadcast(intent);
                        showTaGuoDialog("不拍摄视频，通告是不能报名的哦。准备一下我们现在开始拍摄了哦~！", "拍摄", "跳过");
                    }
                } else {
                    ShowMsgDialog.cancel();
                    Toast.makeText(ModelIdentyFirstActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        ShowMsgDialog.cancel();
        Toast.makeText(ModelIdentyFirstActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
    }

    private void showTaGuoDialog(String content, String leftText, String rightText) {
        TwoButtonDialog dialog = new TwoButtonDialog(this, content, leftText, rightText, R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        Intent intent = new Intent(ModelIdentyFirstActivity.this, VideoCertifyActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void cancelListener() {
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.half_iv:
                whichSelected = HALF;
//                if (StringUtils.isEmpty(identyTranBean.getHalfPath())) {
//                    if (AliPreferencesUtil.getBuckekName(this) == null || AliPreferencesUtil.getAuthPicKey(this) == null) {
//                        new Action(ResponseAliParams.class, "${ali_params}", this, null, new VisitUnit()).run();
//                        Toast.makeText(this, "加载配置稍后重试", Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        showChoiceWays();
//                    }
//                } else {
//                    Constants.imageBrower(this, 0, new String[]{identyTranBean.getHalfPath()}, true);
//                }
                break;
            case R.id.front_iv:
                whichSelected = FRONT;
//                if (StringUtils.isEmpty(identyTranBean.getFrontPath())) {
//                    if (AliPreferencesUtil.getBuckekName(this) == null || AliPreferencesUtil.getAuthPicKey(this) == null) {
//                        new Action(ResponseAliParams.class, "${ali_params}", this, null, new VisitUnit()).run();
//                        Toast.makeText(this, "加载配置稍后重试", Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        showChoiceWays();
//                    }
//                } else {
//                    Constants.imageBrower(this, 0, new String[]{identyTranBean.getFrontPath()}, true);
//                }
                break;
            case R.id.back_iv:
                whichSelected = BACK;
//                if (StringUtils.isEmpty(identyTranBean.getBackPath())) {
//                    if (AliPreferencesUtil.getBuckekName(this) == null || AliPreferencesUtil.getAuthPicKey(this) == null) {
//                        new Action(ResponseAliParams.class, "${ali_params}", this, null, new VisitUnit()).run();
//                        Toast.makeText(this, "加载配置稍后重试", Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        showChoiceWays();
//                    }
//                } else {
//                    Constants.imageBrower(this, 0, new String[]{identyTranBean.getBackPath()}, true);
//                }
                break;
            case R.id.submit_bt:
                toSubmit();
                break;
        }
    }

    private void imageListeners() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.IMAGE_BROADCAST_LISTENER);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent.getAction())) {
                boolean flag = intent.getBooleanExtra(
                        Constants.IS_DELE_IMAGES, false);
                if (!flag) {
                    return;
                }
                Object result = intent.getSerializableExtra(Constants.COMEBACK);
                if (result != null) {
                    TransferImagesBean dataBean = (TransferImagesBean) result;
                    LinkedList<String> picDatas = new LinkedList<String>();
                    LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                    for (int i = 0; i < tempStrs.size(); i++) {
                        Bitmap subBitmap = FileUtil.compressImages(tempStrs.get(i));
                        if (subBitmap != null) {
                            String photoName = Constants.getImageName(ModelIdentyFirstActivity.this);
                            FileUtil.saveScalePhoto(
                                    FileUtil.getPhotopath(photoName),
                                    subBitmap);
                            subBitmap.recycle();
                            picDatas.add(FileUtil.getPhotopath(photoName));
                        }
                    }
                    if (picDatas.size() > 0) {
                        setImage(picDatas.get(0));
                    } else {
                        setImage("");
                    }
                }
            }
        }
    };

    private void toSubmit() {
        String realName = realname_et.getText().toString();
        String idcard = idcard_et.getText().toString();
        identyTranBean.setRealName(realName);
        identyTranBean.setIdCard(idcard);
        if (StringUtils.isEmpty(identyTranBean.getRealName())) {
            Toast.makeText(this, "请填写您的真实姓名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(identyTranBean.getIdCard())) {
            Toast.makeText(this, "请填写您的身份证号！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtils.isEmpty(identyTranBean.getFrontPath())) {
            Toast.makeText(this, "请上传身份证人像面！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(identyTranBean.getBackPath())) {
            Toast.makeText(this, "请上传身份证国徽面！", Toast.LENGTH_SHORT).show();
            return;
        }
        responseIdCheck();
    }

    public void responseIdCheck() {
        //上传图片
        ShowMsgDialog.showNoMsg(this, false);
//        picPaths.add(identyTranBean.getHalfPath());
        picPaths.add(identyTranBean.getFrontPath());
        picPaths.add(identyTranBean.getBackPath());
        doUploadImages();

    }

    private void showChoiceWays() {//chicePhoto();
        Intent intent = new Intent(this, PickPhotoesView.class);
        startActivityForResult(intent, 1);
    }

    private void chicePhoto() {
        LinkedList<String> picDatas = new LinkedList<String>();
        TransferImagesBean dataBean = new TransferImagesBean();
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, 1);//后一个参数可选照片数量
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 0);
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
        startActivityForResult(intentPhote, 2);
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
                    setImage(picDatas.get(0));
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
                    Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
                    if (bitmap != null) {
                        FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
                        String photoPath = FileUtil.getPhotopath(photoName);
                        setImage(photoPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImage(String picPath) {
        switch (whichSelected) {
            case HALF:
                if (StringUtils.isEmpty(picPath)) {
                    half_iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    half_iv.setImageResource(R.drawable.addpic);
                } else {
                    half_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    com.nostra13.universalimageloader.core.ImageLoader.getInstance()
//                            .displayImage("file://" + picPath, half_iv, new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(12)).build());
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            picPath, half_iv);
                }
                identyTranBean.setHalfPath(picPath);
                break;
            case FRONT:
                if (StringUtils.isEmpty(picPath)) {
                    front_iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    front_iv.setImageResource(R.drawable.addpic);
                } else {
                    front_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            picPath, front_iv);
                }
                identyTranBean.setFrontPath(picPath);
                break;
            case BACK:
                if (StringUtils.isEmpty(picPath)) {
                    back_iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    back_iv.setImageResource(R.drawable.addpic);
                } else {
                    back_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            picPath, back_iv);
                }
                identyTranBean.setBackPath(picPath);
                break;
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

}
