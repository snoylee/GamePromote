package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.personal.bean.ProIdentyBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.task.certification.AuthProModelInfo;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import java.io.File;
import java.util.LinkedList;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class ProModelIdentyActivity extends SGBaseActivity implements View.OnClickListener {
    private View backButton;
    private TextView titleName;
    private ProIdentyBean proIdentyBean;
    private ImageView model_card_iv;
    private Button submit_bt;
    private EditText brokername_et;
    private EditText brokerphone_et;
    private EditText agencycompany_et;
    private EditText companyphone_et;
    private EditText companyaddress_et;
    VisitUnit visitUnit = new VisitUnit();
    private String photoName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_pro_model_identy, null));
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getString(R.string.title_activity_pro_model_identy));
        model_card_iv = (ImageView) findViewById(R.id.model_card_iv);
        submit_bt = (Button) findViewById(R.id.submit_bt);
        brokername_et = (EditText) findViewById(R.id.brokername_et);
        brokerphone_et = (EditText) findViewById(R.id.brokerphone_et);
        agencycompany_et = (EditText) findViewById(R.id.agencycompany_et);
        companyphone_et = (EditText) findViewById(R.id.companyphone_et);
        companyaddress_et = (EditText) findViewById(R.id.companyaddress_et);
    }

    private void initDatas() {
        proIdentyBean = new ProIdentyBean();
        imageListeners();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        model_card_iv.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.model_card_iv:
                if (StringUtils.isEmpty(proIdentyBean.getModelCardPath())) {
                    showChoiceWays();
                } else {
                    Constants.imageBrower(this, 0, new String[]{proIdentyBean.getModelCardPath()}, true);
                }
                break;
            case R.id.submit_bt:
                toSubmit();
                break;
        }
    }

    private void requestData() {
        new Action(AuthProModelInfo.class, "${authProModelInfo}", this, null, visitUnit).run();
    }
    public void finishUpload(String msg) {
        ShowMsgDialog.cancel();
        Toast.makeText(getApplicationContext(), "申请认证成功！", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void imageListeners() {
        SGApplication.getInstance().addImageListener(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constants.IMAGE_BROADCAST_LISTENER.equals(intent
                        .getAction())) {
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
                                String photoName = Constants.getImageName(ProModelIdentyActivity.this);
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
        });
        SGApplication.getInstance().registerImagesReceiver();
    }

    private void toSubmit() {
        String brokerName = brokername_et.getText().toString();
        String brokerPhone = brokerphone_et.getText().toString();
        String agencyCompany = agencycompany_et.getText().toString();
        String companyPhone = companyphone_et.getText().toString();
        String companyAddress = companyaddress_et.getText().toString();

        proIdentyBean.setBrokerName(brokerName);
        proIdentyBean.setBrokerPhone(brokerPhone);
        proIdentyBean.setAgencyCompany(agencyCompany);
        proIdentyBean.setCompanyPhone(companyPhone);
        proIdentyBean.setCompanyAddress(companyAddress);

        if (StringUtils.isEmpty(proIdentyBean.getModelCardPath())) {
            Toast.makeText(this, "请上传您的模卡！", Toast.LENGTH_SHORT).show();
            return;
        }
        ShowMsgDialog.showNoMsg(this, false);
        doUploadImages(proIdentyBean.getModelCardPath());
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

        if (StringUtils.isEmpty(picPath)) {
            model_card_iv.setScaleType(ImageView.ScaleType.FIT_XY);
            model_card_iv.setImageResource(R.drawable.addpic);
        } else {
            model_card_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
//                    .displayImage("file://" + picPath, model_card_iv, new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(12)).build());
            ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                    picPath, model_card_iv);
        }
        proIdentyBean.setModelCardPath(picPath);
    }

    @Override
    public void onDestroy() {
        SGApplication.getInstance().unregisterImagesReceiver();
        super.onDestroy();
    }

    private void doUploadImages(String picPath) {
        AliySSOHepler.getInstance().uploadImageEngine(this,Constants.AVATAR_PATH, picPath, new HttpCallBack() {

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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 1:
                        String imageUrl = (String) msg.obj;
                        proIdentyBean.setModelCardUrl(imageUrl);
                        //调接口
                        requestData();
                        break;
                    case 2:
                        ShowMsgDialog.cancel();
                        Toast.makeText(getApplicationContext(), "模卡上传失败！", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public ProIdentyBean getProIdentyBean() {
        return proIdentyBean;
    }
}
