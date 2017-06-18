/*
 * 文 件 名:  RegisterSecondPageActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import base.ViewBinder;
import base.frame.VisitUnit;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.activity.personal.JJRIdentyFirstActivity;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.ChoiceDateView;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月3日
 * @action [用户注册第二个界面]
 */
public class RegisterSecondPageActivity extends SGBaseActivity implements
        OnClickListener {

    private View closeLoginWel, comfirm, selectModelIcon, selectPhotorIcon,selectPhotorIcon1;

    private Uri imageUri, originalUri;

    private ImageView manIcon, womanIcon;

    private TextView manText, womanText, iamModel, iamPhotor, birthdayView,iamPhotor1;

    private View manViewBack, womanViewBack, modelBack, photorBack, photorBack1;

    private EditText inviteCode;

    private EditText nickName;
    /**
     * 头像款
     */
    private int w = 200;
    /**
     * 头像高
     */
    private int h = 200;

    private CircularImage userImage;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP_PICTURE = 3;
    private String photoName, cellPhone,pwd,nickNameValue,birthDayVaule,sexValue,loginPwd,loginName;

    public String photoPath = "", serviceUrl;

    private String sexStr = Constants.SEX_WOMAN;

    private String carrieType = Constants.CARRE_MODEL;

    public String getCellPhone() {
        return cellPhone;
    }

    public String getInviteCode() {
        return inviteCode.getText().toString().trim();
    }

    private String typeIndex;

    /**
     * 重载方法
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        VisitUnit visitUnit = new VisitUnit(this);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.sg_register_secondpage_layout, null));
        initViews();
        initListeners();
        initDatas();
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        closeLoginWel = findViewById(R.id.closeLoginWel);
        manViewBack = findViewById(R.id.manViewBack);
        womanViewBack = findViewById(R.id.womanViewBack);
        modelBack = findViewById(R.id.modelBack);
        photorBack = findViewById(R.id.photorBack);
        photorBack1 = findViewById(R.id.photorBack1);
        comfirm = findViewById(R.id.comfirm);
        manIcon = (ImageView) findViewById(R.id.manIcon);
        womanIcon = (ImageView) findViewById(R.id.womanIcon);
        inviteCode = (EditText) findViewById(R.id.inviteCode);
        manText = (TextView) findViewById(R.id.manText);
        womanText = (TextView) findViewById(R.id.womanText);
        iamModel = (TextView) findViewById(R.id.iamModel);
        iamPhotor = (TextView) findViewById(R.id.iamPhotor);
        iamPhotor1 = (TextView) findViewById(R.id.iamPhotor1);
        birthdayView = (TextView) findViewById(R.id.birthdayView);
        nickName = (EditText) findViewById(R.id.nickName);
        selectModelIcon = findViewById(R.id.selectModelIcon);
        selectPhotorIcon = findViewById(R.id.selectPhotorIcon);
        selectPhotorIcon1 = findViewById(R.id.selectPhotorIcon1);
        userImage = (CircularImage) findViewById(R.id.userImage);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initListeners() {
        // TODO Auto-generated method stub
        closeLoginWel.setOnClickListener(this);
        manViewBack.setOnClickListener(this);
        womanViewBack.setOnClickListener(this);
        modelBack.setOnClickListener(this);
        photorBack.setOnClickListener(this);
        photorBack1.setOnClickListener(this);
        userImage.setOnClickListener(this);
        birthdayView.setOnClickListener(this);
        comfirm.setOnClickListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        // TODO Auto-generated method stub
        cellPhone = getIntent().getStringExtra("cellPhone");
        typeIndex=getIntent().getStringExtra("typeIndex");
        pwd=getIntent().getStringExtra("pwd");
        switchMale(1);
        switchType(0);
        userImage.setImageResource(R.drawable.sg_login_userpic_icon);
    }

    /**
     * 重载方法
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.closeLoginWel) {
            Intent intent = new Intent();
            intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (v.getId() == R.id.birthdayView) {
            startActivityForResult(new Intent(this,
                    ChoiceDateView.class), 2);
        } else if (v.getId() == R.id.manViewBack) {
            switchMale(0);
        } else if (v.getId() == R.id.womanViewBack) {
            switchMale(1);
        } else if (v.getId() == R.id.modelBack) {
            switchType(0);
        } else if (v.getId() == R.id.photorBack) {
            switchType(1);
        } else if (v.getId() == R.id.photorBack1) {
            switchType(2);
        } else if (v.getId() == R.id.userImage) {
            Intent intent = new Intent(this, PickPhotoesView.class);
            startActivityForResult(intent, 5);
        } else if (v.getId() == R.id.comfirm) {
           nickNameValue = nickName.getText().toString().trim();
           birthDayVaule = birthdayView.getText().toString().trim();
           sexValue = getSexType();
            if (!"".equals(nickNameValue)) {
                if (!"".equals(birthDayVaule)) {
                    if (!"".equals(photoPath)) {
                        if (AliPreferencesUtil.getBuckekName(this) == null) {
                            Toast.makeText(this, "请稍后重试！", Toast.LENGTH_SHORT)
                                    .show();
                            requestAliyParams();
                        } else {
                            uploadHeadPic();
                        }
                    } else {
                        Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(this, "请选择生日",
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "请输入昵称",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadHeadPic() {
        ShowMsgDialog.show(this, "注册提交中...", false);
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
                        Toast.makeText(RegisterSecondPageActivity.this, "头像上传失败", Toast.LENGTH_SHORT)
                                .show();
                        ShowMsgDialog.cancel();
                        break;
                    case 1:
                        ShowMsgDialog.cancel();
                        commitRegister();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void commitRegister() {
        try {
            RequestBean item = new RequestBean();
            item.setIsPublic(false);
            JSONObject obj = new JSONObject();
            if("1".equals(typeIndex)){
                loginName="phone_".concat(getCellPhone());
                loginPwd=DigestUtils.md5Hex(pwd.concat("sgappkey"));
                obj.put("loginPwd",loginPwd);
                obj.put("loginName",loginName);
                obj.put("telephone",getCellPhone());
                obj.put("verifyCode", UserPreferencesUtil.getUserToken(this));
            }else if("2".equals(typeIndex)){
                loginName="weixin_".concat(UserPreferencesUtil.getOtherPlatfromId(this));
                loginPwd=DigestUtils.md5Hex(loginName.concat("sgappkey"));
                obj.put("loginPwd",loginPwd);
                obj.put("loginName",loginName);
            }else if("3".equals(typeIndex)){
                loginName="weibo_".concat(UserPreferencesUtil.getOtherPlatfromId(this));
                loginPwd=DigestUtils.md5Hex(loginName.concat("sgappkey"));
                obj.put("loginPwd",loginPwd);
                obj.put("loginName",loginName);
            }else if("4".equals(typeIndex)){
                loginName="qq_".concat(UserPreferencesUtil.getOtherPlatfromId(this));
                loginPwd=DigestUtils.md5Hex(loginName.concat("sgappkey"));
                obj.put("loginPwd",loginPwd);
                obj.put("loginName",loginName);
            }
            obj.put("usernick",nickNameValue);
            obj.put("userIcon",getPhotoPath());
            obj.put("gender",sexValue);
            obj.put("birthday",getTime(birthDayVaule));
            obj.put("registerType",typeIndex);
            ProvinceBean areaBean= CacheService.getInstance().getCacheCommenAreaBean(Constants.COMMEN_AREA_FLAG);
            if (areaBean!=null){
                obj.put("province", areaBean.getProvinceCode());
                CityBean cityBean=areaBean.getCityBean();
                if (cityBean!=null){
                    obj.put("city", cityBean.getCityCode());
                }
            }
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this,false);
            item.setServiceURL(ConstTaskTag.QUEST_REGISTER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_REGISTER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
            super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_REGISTER:
                if ("0000".equals(data.getCode())){
                    try {
                        UserPreferencesUtil.setLoginName(this,loginName);
                        UserPreferencesUtil.setLoginPwd(this, loginPwd);
                        UserPreferencesUtil.setUserLoginType(this,typeIndex);
                        UserPreferencesUtil.setBirthday(this, birthDayVaule);
                        UserPreferencesUtil.setCellPhone(this, getCellPhone());
                        UserPreferencesUtil.setPwd(this, loginPwd);
                        JSONObject obj=new JSONObject(data.getRecord());
                        String orderExpireTime= StringUtils.getJsonValue(obj,"orderExpireTime");
                        String payExpireTime=StringUtils.getJsonValue(obj, "payExpireTime");
                        UserPreferencesUtil.setorderExpireTime(this,orderExpireTime);
                        UserPreferencesUtil.setpayExpireTime(this,payExpireTime);
                        String userId=obj.getString("userId");
                        UserPreferencesUtil.setUserId(this, userId);
                        String usernick=obj.getString("usernick");
                        UserPreferencesUtil.setUserNickName(this, usernick);
                        String userIcon=obj.getString("userIcon");
                        UserPreferencesUtil.setHeadPic(this, userIcon);
                        String gender=obj.getString("gender");
                        UserPreferencesUtil.setSex(this, gender);
                        String checkOff= StringUtils.getJsonValue(obj, "chatOff");
                        UserPreferencesUtil.setCheckOff(this, checkOff);
                        String age=obj.getString("age");
                        String videoAuthStatus=null,identityAuthStatus=null;
                        if (!obj.isNull("videoAuthStatus")){
                            videoAuthStatus=obj.getString("videoAuthStatus");
                        }
                       if (!obj.isNull("identityAuthStatus")){
                           identityAuthStatus=obj.getString("identityAuthStatus");
                       }
                        UserPreferencesUtil.setUserVideoAuth(this, videoAuthStatus);
                        UserPreferencesUtil.setUserIDAuth(this, identityAuthStatus);
                        UserPreferencesUtil.setExpertAuth(this, StringUtils.getJsonValue(obj, "expertAuth"));
                        UserPreferencesUtil.setUserAge(this, age);
//                        ShowMsgDialog.showNoMsg(this, false);
                        XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
                        UserPreferencesUtil.setIsOnline(this, true);
                        Intent intent = new Intent();
                        intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
                        intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
    }

    public String getTime(String arg){
        String str=null;
        try {
            SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
            Date date=df.parse(arg);
            long timeStemp = date.getTime();
            str=String.valueOf(timeStemp);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 重载方法
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
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

    private void switchMale(int index) {
        switch (index) {
            case 0:
                manViewBack.setBackgroundResource(R.drawable.shape_rect_dark_green);
                manIcon.setImageResource(R.drawable.sg_man_light_icon);
                manText.setTextColor(getResources().getColor(R.color.white));

                womanViewBack.setBackgroundResource(0);// R.drawable.shape_rect_input_white);
                womanIcon.setImageResource(R.drawable.sg_woman_dark_icon);
                womanText.setTextColor(getResources().getColor(R.color.dark_green));

                sexStr = Constants.SEX_MAN;
                break;
            case 1:
                manViewBack.setBackgroundResource(0);// R.drawable.shape_rect_input_white);
                manIcon.setImageResource(R.drawable.sg_man_dark_icon);
                manText.setTextColor(getResources().getColor(R.color.dark_green));

                womanViewBack
                        .setBackgroundResource(R.drawable.shape_rect_dark_green);
                womanIcon.setImageResource(R.drawable.sg_woman_light_icon);
                womanText.setTextColor(getResources().getColor(R.color.white));
                sexStr = Constants.SEX_WOMAN;
                break;
            default:
                break;
        }
    }

    private void switchType(int index) {
        switch (index) {
            case 0:
                modelBack.setBackgroundResource(R.drawable.shape_rect_dark_green);
                iamModel.setTextColor(getResources().getColor(R.color.white));

                photorBack.setBackgroundResource(R.drawable.shape_rect_input_white);
                iamPhotor.setTextColor(getResources().getColor(R.color.dark_green));
                carrieType = Constants.CARRE_MODEL;
                selectPhotorIcon.setVisibility(View.GONE);
                selectModelIcon.setVisibility(View.VISIBLE);

                photorBack1.setBackgroundResource(R.drawable.shape_rect_input_white);
                iamPhotor1.setTextColor(getResources().getColor(R.color.dark_green));
                selectPhotorIcon1.setVisibility(View.GONE);
                break;
            case 1:
                modelBack.setBackgroundResource(R.drawable.shape_rect_input_white);
                iamModel.setTextColor(getResources().getColor(R.color.dark_green));

                photorBack.setBackgroundResource(R.drawable.shape_rect_dark_green);
                iamPhotor.setTextColor(getResources().getColor(R.color.white));
                carrieType = Constants.CARRE_PHOTOR;
                selectPhotorIcon.setVisibility(View.VISIBLE);
                selectModelIcon.setVisibility(View.GONE);

                photorBack1.setBackgroundResource(R.drawable.shape_rect_input_white);
                iamPhotor1.setTextColor(getResources().getColor(R.color.dark_green));
                selectPhotorIcon1.setVisibility(View.GONE);
                break;
            case 2:
                modelBack.setBackgroundResource(R.drawable.shape_rect_input_white);
                iamModel.setTextColor(getResources().getColor(R.color.dark_green));

                photorBack.setBackgroundResource(R.drawable.shape_rect_input_white);
                iamPhotor.setTextColor(getResources().getColor(R.color.dark_green));
                carrieType = Constants.CARRE_MERCHANT;
                selectPhotorIcon.setVisibility(View.GONE);
                selectModelIcon.setVisibility(View.GONE);

                photorBack1.setBackgroundResource(R.drawable.shape_rect_dark_green);
                iamPhotor1.setTextColor(getResources().getColor(R.color.white));
                selectPhotorIcon1.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                try {
                    Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
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
//                        userImage.setImageBitmap(bitmap);
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
                            userImage.setImageBitmap(subBitmap);
                        }
                    }
                }


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
//                    if (photo != null) {
//                        int angle = FileUtil.getOrientation(this, originalUri);
//                        if (angle > 0) {
//                            photo = FileUtil.rotaingImageView(angle, photo);
//                        }
//                        photoName = Constants.getImageName(this);
//                        photoPath = FileUtil.getPhotopath(photoName);
//                        FileUtil.saveScalePhoto(photoPath, photo);
//                        userImage.setImageBitmap(photo);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
            case 2:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
                Serializable item2 = data.getSerializableExtra("bean");
                if (item2 != null) {
                    FeedbackDateBean ftBean = (FeedbackDateBean) item2;
                    String szBirthday = ftBean.getYear() + "-" + ftBean.getMonth()
                            + "-" + ftBean.getDay();
                    birthdayView.setText(szBirthday);
                }
                break;
            case 5:
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

    public String getPhotoPath() {
        return serviceUrl;
    }

    public String getSexType() {
        return sexStr;
    }

    public String getCarrieType() {
        return carrieType;
    }

    @Override
    protected void xmppRespose() {
        super.xmppRespose();
        ShowMsgDialog.cancel();
        UserPreferencesUtil.setIsOnline(this, true);
        Intent intent = new Intent();
        intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
        intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
        setResult(Activity.RESULT_OK, intent);
        finish();
//        showTaGuoDialog();
    }

    private void showTaGuoDialog() {
        String tipStr="";
        if (Constants.CARRE_MODEL.equals(carrieType)){
            tipStr="恭喜您注册成功，认证模特可以提高通告报名成功率哦~！现在去认证吧！";
        }else if (Constants.CARRE_PHOTOR.equals(carrieType)){
            tipStr="恭喜您注册成功，认证摄影师可以免预付费发布通告哦~！现在去认证吧！";
        }else if(Constants.CARRE_MERCHANT.equals(carrieType)){
            tipStr="恭喜您注册成功，认证经纪人才能发布通告哦~！现在去认证吧！";
        }
        TwoButtonDialog dialog = new TwoButtonDialog(this,tipStr , "前往认证", "暂不认证", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        if (Constants.CARRE_MODEL.equals(carrieType)){
                            startActivity(new Intent(RegisterSecondPageActivity.this, ModelIdentyFirstActivity.class));
                        }else if (Constants.CARRE_PHOTOR.equals(carrieType)){
                            startActivity(new Intent(RegisterSecondPageActivity.this, CMIdentyFirstActivity.class));
                        }else if(Constants.CARRE_MERCHANT.equals(carrieType)){
                            startActivity(new Intent(RegisterSecondPageActivity.this, JJRIdentyFirstActivity.class));
                        }

                        Intent intent = new Intent();
                        intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
                        intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void cancelListener() {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
                        intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
        dialog.show();
    }

    private void dealWithResult(Integer data) {
        switch (data) {
            case XMPPUtils.LOGIN_SECCESS: // 登录成功
                ShowMsgDialog.cancel();
                UserPreferencesUtil.setIsOnline(this, true);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
                intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case XMPPUtils.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                Toast.makeText(this, "账户或者密码错误", Toast.LENGTH_SHORT).show();
                break;
            case XMPPUtils.SERVER_UNAVAILABLE:// 服务器连接失败
                Toast.makeText(this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                break;
            case XMPPUtils.LOGIN_ERROR:// 未知异常
                Toast.makeText(this, "未知异常", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
