/*
 * 文 件 名:  RegisterFristPageActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.MerchantRegisterBean;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.PatternUtils;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月3日
 * @action [用户注册第一个界面]
 */
public class MerchantRegFristStepActivity extends SGBaseActivity implements
        OnClickListener {
    private static final int TAKE_PICTURE = 2;
    private static final int CHOOSE_PICTURE = 3;
    private static final int CROP_PICTURE = 4;
    private TextView titleName;
    private View backButton, NextButton,pwdControl,showPwdView,hidePwdView;
    private EditText cellphoneText, cerfyText, merchantNameText, addressText, nameText, phoneText, qqText, introduceText,shortNameText,passWordText;
    private Button verifyButton;
    private boolean isSend = true;
    private CircularImage userImage;
    private MerchantRegisterBean merchBean;
    private Uri imageUri,originalUri;
    private String photoName;

    public String photoPath="",cellPhoneStr,certifyStr,merchantNameStr,addressStr,nameStr,phoneStr,qqStr,intrStr,shortNameStr,pwdStr;
    /** 头像款 */
    private int w = 200;
    /** 头像高 */
    private int h = 200;
    /**
     * 重载方法
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_register_first_layout);
        initViews();
        initListeners();
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        showPwdView=findViewById(R.id.showPwdView);
        pwdControl=findViewById(R.id.pwdControl);
        hidePwdView=findViewById(R.id.hidePwdView);
        NextButton = findViewById(R.id.NextButton);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        cellphoneText = (EditText) findViewById(R.id.cellphoneText);
        cerfyText = (EditText) findViewById(R.id.cerfyText);
        merchantNameText = (EditText) findViewById(R.id.merchantNameText);
        addressText = (EditText) findViewById(R.id.addressText);
        nameText = (EditText) findViewById(R.id.nameText);
        passWordText=(EditText)findViewById(R.id.passWordText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        qqText = (EditText) findViewById(R.id.qqText);
        introduceText = (EditText) findViewById(R.id.introduceText);
        shortNameText=(EditText)findViewById(R.id.shortNameText);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        userImage = (CircularImage) findViewById(R.id.userImage);
        titleName.setText("商家注册");
        userImage.setImageResource(R.drawable.merchant_defualt_icon);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initListeners() {
        // TODO Auto-generated method stub
        merchBean=new MerchantRegisterBean();
        NextButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
        userImage.setOnClickListener(this);
        pwdControl.setOnClickListener(this);
    }

    /**
     * 重载方法
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        cellPhoneStr = cellphoneText.getText().toString().trim();
        certifyStr = cerfyText.getText().toString().trim();
        merchantNameStr= merchantNameText.getText().toString().trim();
        addressStr= addressText.getText().toString().trim();
        nameStr= nameText.getText().toString().trim();
        phoneStr= phoneText.getText().toString().trim();
        qqStr= qqText.getText().toString().trim();
        intrStr= introduceText.getText().toString().trim();
        shortNameStr= shortNameText.getText().toString().trim();
        pwdStr= passWordText.getText().toString().trim();
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.NextButton) {
            if (inNext()){
                merchBean.setAddressText(addressStr);
                merchBean.setCellphoneText(cellPhoneStr);
                merchBean.setCerfyText(certifyStr);
                merchBean.setHeadImage(photoPath);
                merchBean.setIntroduceText(intrStr);
                merchBean.setMerchantNameText(merchantNameStr);
                merchBean.setNameText(nameStr);
                merchBean.setShortNameText(shortNameStr);
                merchBean.setPhoneText(phoneStr);
                merchBean.setQqText(qqStr);
                merchBean.setPassWordText(pwdStr);
                Intent intent = new Intent(this, MerchantRegSecStepActivity.class);
                intent.putExtra("merchBean",merchBean);
                startActivityForResult(intent, 0);
//                ShowMsgDialog.showNoMsg(this, false);
//               checkVerifyCode();
            }
        } else if (v.getId() == R.id.verifyButton) {
            if (!"".equals(cellPhoneStr)) {
                if (cellPhoneStr.matches(PatternUtils.MOBILE_PHONE)) {
                    if (isSend) {
                        isSend = false;
                        timerCount(60 * 1000, 1000);
                        questCertify(cellPhoneStr);
                    }
                } else {
                    Toast.makeText(this, "请输入正确的手机号",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (v.getId() == R.id.userImage) {
            Intent intent = new Intent(this, PickPhotoesView.class);
            startActivityForResult(intent, 1);
        }else if(v.getId()==R.id.pwdControl){
            pwdControlViews();
        }
    }

    private void checkVerifyCode() {
        RequestBean item = new RequestBean();
        try {
            item.setData(new JSONObject().put("telephone", cellPhoneStr).put("verifyCode", certifyStr));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_CHECK_VERFIY);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_CHECK_VERFIY_COD);
    }

    private void pwdControlViews() {
        if(showPwdView.getVisibility()==View.VISIBLE){
            showPwdView.setVisibility(View.GONE);
            passWordText.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            passWordText.postInvalidate();
            setCoursePosion();
        }else{
            showPwdView.setVisibility(View.VISIBLE);
            passWordText.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
            passWordText.postInvalidate();
            setCoursePosion();
        }

        if(hidePwdView.getVisibility()==View.VISIBLE){
            hidePwdView.setVisibility(View.GONE);
        }else{
            hidePwdView.setVisibility(View.VISIBLE);
        }
    }

    private void setCoursePosion(){
        CharSequence text = passWordText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable)text;
            Selection.setSelection(spanText, text.length());
        }
    }

    private void questCertify(String phone) {
        RequestBean item = new RequestBean();
        try {
            item.setData(new JSONObject().put("telephone", phone));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_VERFY_COD);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_VERFY_COD);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.RESPOSE_VERFY_COD:
                Toast.makeText(this, "验证码发送成功", Toast.LENGTH_SHORT)
                        .show();
                break;
            case ConstTaskTag.RESPOSE_CHECK_VERFIY_COD:{
//                String token=data.getRecord();
//                UserPreferencesUtil.setUserToken(this, token);
//                merchBean.setAddressText(addressStr);
//                merchBean.setCellphoneText(cellPhoneStr);
//                merchBean.setCerfyText(certifyStr);
//                merchBean.setHeadImage(photoPath);
//                merchBean.setIntroduceText(intrStr);
//                merchBean.setMerchantNameText(merchantNameStr);
//                merchBean.setNameText(nameStr);
//                merchBean.setShortNameText(shortNameStr);
//                merchBean.setPhoneText(phoneStr);
//                merchBean.setQqText(qqStr);
//                merchBean.setPassWordText(pwdStr);
//                Intent intent = new Intent(this, MerchantRegSecStepActivity.class);
//                intent.putExtra("merchBean",merchBean);
//                startActivityForResult(intent, 0);
                break;
            }
        }

    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this, "验证码发送失败", Toast.LENGTH_SHORT)
                .show();
    }

    private void timerCount(int minute, int second) {
        // TODO Auto-generated method stub
        new CountDownTimer(minute, second) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                verifyButton.setText(String.valueOf(millisUntilFinished / 1000)
                        .concat("秒后重发"));
                verifyButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                isSend = false;
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                verifyButton.setText("发送验证码");
                verifyButton.setBackgroundResource(R.drawable.shape_rect_dark_green);
                isSend = true;
            }
        }.start();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                boolean result = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
                if (result) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
                    intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
                    intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
            }
            case 1: {
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                String flag = data.getStringExtra(Constants.COMEBACK);
                if ("galary".equals(flag)) {
                    chicePhoto();
                }else if("camera".equals(flag)){
                    takePhoto();
                }
                break;
            }
            case TAKE_PICTURE:
                File out1 = new File(FileUtil.getPhotopath(photoName));
                imageUri = Uri.fromFile(out1);
                cropImageUri(imageUri, w, h, CROP_PICTURE);
                break;
            case CROP_PICTURE:
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getPhotopath(photoName));
                    if (bitmap != null) {
                        photoName = Constants.getImageName(this);
                        photoPath = FileUtil.getPhotopath(photoName);
                        FileUtil.saveScalePhoto(photoPath, bitmap);
                        userImage.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                break;
            case CHOOSE_PICTURE:
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
                    cropPickImageUri(originalUri, w, h, 5);
                }
                break;
            case 5:
                try {
                    Bitmap photo = BitmapFactory.decodeFile(photoName);
                    if (photo != null) {
                        int angle = FileUtil.getOrientation(this, originalUri);
                        if (angle > 0) {
                            photo = FileUtil.rotaingImageView(angle, photo);
                        }
                        photoName = Constants.getImageName(this);
                        photoPath = FileUtil.getPhotopath(photoName);
                        FileUtil.saveScalePhoto(photoPath, photo);
                        userImage.setImageBitmap(photo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
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

    private Boolean inNext(){
        boolean flag=true;
        if ("".equals(cellPhoneStr)) {
            flag=false;
            Toast.makeText(this, "手机号码不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }

        if (!cellPhoneStr.matches(PatternUtils.MOBILE_PHONE)) {
            flag=false;
            Toast.makeText(this, "请输入正确的手机号",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }

        if ("".equals(pwdStr)) {
            flag=false;
            Toast.makeText(this, "登录密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }

        if (pwdStr.length()<6) {
            flag=false;
            Toast.makeText(this, "登录密码最少6位",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }

        if ("".equals(certifyStr)) {
            flag=false;
            Toast.makeText(this, "验证码不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(photoPath)) {
            flag=false;
            Toast.makeText(this, "请添加头像",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(shortNameStr)) {
            flag=false;
            Toast.makeText(this, "企业简称不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }

        if (shortNameStr.length()<6) {
            flag=false;
            Toast.makeText(this, "企业昵称最少6位",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }

        if ("".equals(merchantNameStr)) {
            flag=false;
            Toast.makeText(this, "企业名称不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(addressStr)) {
            flag=false;
            Toast.makeText(this, "联系地址不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(nameStr)) {
            flag=false;
            Toast.makeText(this, "联系人不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(phoneStr)) {
            flag=false;
            Toast.makeText(this, "联系电话不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(qqStr)) {
            flag=false;
            Toast.makeText(this, "联系人QQ不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(intrStr)) {
            flag=false;
            Toast.makeText(this, "企业简介不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        return flag;
    }
}
