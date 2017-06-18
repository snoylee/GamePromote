/*
 * 文 件 名:  RegisterSecondPageActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.app.Dialog;
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

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.bean.MerchantRegisterBean;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.ChoiceDateView;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.PickPhotoesView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.task.init.ResponseAliParams;
import com.xygame.sg.task.init.UploadDeviceTokenTask;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.TakePhotoesUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.action.CenterRepo;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月3日
 * @action [用户注册第二个界面]
 */
public class MerchantRegSecStepActivity extends SGBaseActivity implements
        OnClickListener {
    private final String FLAG_HEAD="head";
    private final String FLAG_REG="reg";
    private MerchantRegisterBean merchBean;
    private View backButton, confrimButton,merchantRules;
    private ImageView imageZZ;
    private static final int TAKE_PICTURE = 2;
    private static final int CHOOSE_PICTURE = 3;
    private TextView titleName;
    private String photoName;
    private EditText registerNumText;
    private String regIdStr;
    public String photoPath="",serviceUrl;
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
        VisitUnit visitUnit = new VisitUnit(this);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.merchant_register_second_layout, null));
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
        confrimButton = findViewById(R.id.confrimButton);
        backButton = findViewById(R.id.backButton);
        merchantRules=findViewById(R.id.merchantRules);
        registerNumText=(EditText)findViewById(R.id.registerNumText);
        titleName=(TextView)findViewById(R.id.titleName);
        imageZZ = (ImageView) findViewById(R.id.imageZZ);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initListeners() {
        // TODO Auto-generated method stub
        confrimButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        imageZZ.setOnClickListener(this);
        merchantRules.setOnClickListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        titleName.setText("资料提交");
        merchBean=(MerchantRegisterBean)getIntent().getSerializableExtra("merchBean");
    }

    /**
     * 重载方法
     */
    @Override
    public void onClick(View v) {

        // TODO Auto-generated method stub
       if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.confrimButton) {
           regIdStr=registerNumText.getText().toString().trim();
           if (inNext()){
               if (AliPreferencesUtil.getBuckekName(this) == null) {
                   Toast.makeText(this, "加载配置稍后重试", Toast.LENGTH_SHORT)
                           .show();
                   new Action(ResponseAliParams.class, "${ali_params}", this, null, new VisitUnit()).run();
               } else {
                   ShowMsgDialog.showNoMsg(this, false);
                   merchBean.setRegIdStr(regIdStr);
                   merchBean.setRegImage(photoPath);
                   uploadHeadPic(FLAG_HEAD, merchBean.getHeadImage());
               }
           }
        } else if (v.getId() == R.id.imageZZ) {
            Intent intent = new Intent(this, PickPhotoesView.class);
            startActivityForResult(intent, 1);
        }else if (v.getId()==R.id.merchantRules){
           Intent intent = new Intent(this, UserRulsActivity.class);
           startActivity(intent);
       }
    }

    private void uploadHeadPic(final String flag,String imagePath) {
        if (!imagePath.contains("http://")){
            AliySSOHepler.getInstance().uploadImageEngine(this, Constants.AVATAR_PATH, imagePath, new HttpCallBack() {

                @Override
                public void onSuccess(String imageUrl, int requestCode) {
                    serviceUrl = imageUrl;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = flag;
                    handler.sendMessage(msg);
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
        }else{
            if (FLAG_HEAD.equals(flag)){
                uploadHeadPic(FLAG_REG, merchBean.getRegImage());
            }else if (FLAG_REG.equals(flag)){
                commitRegister();
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 0:
                        ShowMsgDialog.cancel();
                        Toast.makeText(MerchantRegSecStepActivity.this, "头像上传失败", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 1:
                        String flag=(String)msg.obj;
                        if (FLAG_HEAD.equals(flag)){
                            merchBean.setHeadImage(serviceUrl);
                            uploadHeadPic(FLAG_REG,merchBean.getRegImage());
                        }else if (FLAG_REG.equals(flag)){
                            merchBean.setRegImage(serviceUrl);
                            commitRegister();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void commitRegister() {
        RequestBean item = new RequestBean();
        try {
            String pwdScrect= DigestUtils.md5Hex(merchBean.getPassWordText().concat("sgappkey"));
            item.setData(new JSONObject().put("telephone", merchBean.getCellphoneText()).put("usernick", merchBean.getShortNameText()).put("userIcon", merchBean.getHeadImage()).put("loginName", "phone_".concat(merchBean.getCellphoneText())).put("loginPwd", pwdScrect).put("verifyCode", merchBean.getCerfyText()).put("registerType", "1").put("compName", merchBean.getMerchantNameText()).put("address", merchBean.getAddressText()).put("contactName", merchBean.getNameText()).put("contactPhone", merchBean.getPhoneText()).put("contactQQ", merchBean.getQqText()).put("compIntro", merchBean.getIntroduceText()).put("businLicense", merchBean.getRegIdStr()).put("businLicensePic", merchBean.getRegImage()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_MERCHANT);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_MERCHANT_COD);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.RESPOSE_MERCHANT_COD:
                if ("0000".equals(data.getCode())){
                    try {
                        JSONObject obj=new JSONObject(data.getRecord());
                        String userId=obj.getString("userId");
                        UserPreferencesUtil.setUserId(this, userId);
                        String usernick=obj.getString("usernick");
                        UserPreferencesUtil.setUserNickName(this, usernick);
                        String userIcon=obj.getString("userIcon");
                        UserPreferencesUtil.setHeadPic(this, userIcon);
//                        String gender=obj.getString("gender");
//                        UserPreferencesUtil.setSex(this, gender);
//                        String userPin=obj.getString("userPin");
//                        UserPreferencesUtil.setUserPin(this, userPin);
                        String userTypeStr=obj.getString("userTypes");
                        JSONArray array=new JSONArray(userTypeStr);
                        for (int i=0;i<array.length();i++){
                            JSONObject object=array.getJSONObject(i);
                            String utype=object.getString("utype");
                            UserPreferencesUtil.setUserType(this, utype);
                            String status=object.getString("status");
                            UserPreferencesUtil.setUserVerifyStatus(this, status);
                        }
                        closePage();
                    }catch (Exception e){
                        Toast.makeText(this,"数据解析失败",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
        switch (requestCode) {
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
                photoPath= FileUtil.getPhotopath(photoName);
                photoName = Constants.getImageName(this);
                try{
                    photoPath= TakePhotoesUtils.compressImage(photoPath, FileUtil.CHAT_IMAGES_ROOT_PATH,photoName,60);
                }catch (Exception e){
                    e.printStackTrace();
                }
                imageZZ.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                        photoPath, imageZZ);
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
                        photoPath=tempStrs.get(i);
                    }
                    photoName = Constants.getImageName(this);
                    try{
                        photoPath= TakePhotoesUtils.compressImage(photoPath, FileUtil.CHAT_IMAGES_ROOT_PATH,photoName,60);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    imageZZ.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(
                            photoPath, imageZZ);
                }
                break;
            default:
                break;
        }
    }

    public void closePage() {
        ShowMsgDialog.showNoMsg(this, false);
        XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
    }

    @Override
    protected void xmppRespose() {
        super.xmppRespose();
        ShowMsgDialog.cancel();
        UserPreferencesUtil.setIsOnline(this, true);
       showDialog();
    }

    private Boolean inNext() {
        boolean flag = true;
        if ("".equals(photoPath)) {
            flag = false;
            Toast.makeText(this, "请上传您的营业执照",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        if ("".equals(regIdStr)) {
            flag = false;
            Toast.makeText(this, "营业执照注册号不能为空",
                    Toast.LENGTH_SHORT).show();
            return flag;
        }
        return flag;
    }

   private void showDialog(){
       OneButtonDialog dialog = new OneButtonDialog(this, getResources().getString(R.string.merchant_register_tip), R.style.dineDialog,
               new ButtonOneListener() {

                   @Override
                   public void confrimListener(Dialog dialog) {
                       exitRegitser();
                   }
               });
       dialog.show();
   }

    private void exitRegitser(){
        Intent intent = new Intent();
        intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
        intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
