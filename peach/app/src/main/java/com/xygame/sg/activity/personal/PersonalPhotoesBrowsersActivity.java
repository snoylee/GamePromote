/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.personal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.image.ImagePagerActivity;
import com.xygame.sg.activity.image.ImagePagerBrowersActivity;
import com.xygame.sg.activity.personal.adapter.BrowersPhotoAdapter;
import com.xygame.sg.activity.personal.bean.BrowerPhotoesBean;
import com.xygame.sg.activity.personal.bean.TransferBrowersBean;
import com.xygame.sg.bean.comm.PhotoesTotalBean;
import com.xygame.sg.define.view.ChoiceShareReportActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.indivual.EditAttention;
import com.xygame.sg.task.indivual.GetModelData;
import com.xygame.sg.task.personal.LoadGalleryInfoTask;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.RecycleBitmap;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class PersonalPhotoesBrowsersActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

    private TextView titleName, rightButtonText, userName, picNumCount, priseNumCount, heartText, galeryTimer,
            userLocation, attentionText,sex_age_tv;
    private TextView type_icon_tv;//用户类型icon（模特or摄影师）
    private TextView type_tv;//用户类型text（模特or摄影师）
    private ImageView rightbuttonIcon,locationImage;
    private View backButton, rightButton, intoPriseView,localStyleView;
    private List<BrowerPhotoesBean> browersDatas;
    private CircularImage userImage;
    private BrowersPhotoAdapter browersAdapter;
    private GridView photoList;
    private PhotoesTotalBean newItem;
    private int currIndex = 0, currCount;
    private String glaryId, tipText;
    private String editorText;
    private ImageView  attentionIcon;
    private LinearLayout userStyleView, attentionBackground;
    private String seeUserId;
    private TransferBrowersBean tsBean;
    private LayoutParams lp;
    private boolean isGanZhu = false, isFirstLoad = true,isUpdate=false;
    private ImageLoader mImageLoader;

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
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_personal_photoes_browers_layout, null));

        initViews();
        initListeners();
        initDatas();


    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initListeners() {
        // TODO Auto-generated method stub
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        attentionBackground.setOnClickListener(this);
        photoList.setOnItemClickListener(this);
        intoPriseView.setOnClickListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        photoList = (GridView) findViewById(R.id.photoList);
        type_icon_tv = (TextView) findViewById(R.id.type_icon_tv);
        type_tv = (TextView) findViewById(R.id.type_tv);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);

        userName = (TextView) findViewById(R.id.userName);
        picNumCount = (TextView) findViewById(R.id.picNumCount);
        priseNumCount = (TextView) findViewById(R.id.priseNumCount);
        heartText = (TextView) findViewById(R.id.heartText);
        galeryTimer = (TextView) findViewById(R.id.galeryTimer);
        attentionText = (TextView) findViewById(R.id.attentionText);
        userLocation = (TextView) findViewById(R.id.userLocation);
        attentionIcon = (ImageView) findViewById(R.id.attentionIcon);
        locationImage=(ImageView)findViewById(R.id.locationImage);
        userStyleView = (LinearLayout) findViewById(R.id.userStyleView);
        attentionBackground = (LinearLayout) findViewById(R.id.attentionBackground);
        sex_age_tv = (TextView) findViewById(R.id.sex_age_tv);

        intoPriseView = findViewById(R.id.intoPriseView);
        localStyleView=findViewById(R.id.localStyleView);

        userImage = (CircularImage) findViewById(R.id.userImage);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText(getResources().getString(R.string.sg_personal_photo_title));
        rightButton.setVisibility(View.VISIBLE);
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.more_icon);
        newItem = new PhotoesTotalBean();
        tsBean = new TransferBrowersBean();
        lp = new LayoutParams(8, 8);
        tipText = getIntent().getStringExtra("oral");
        glaryId = getIntent().getStringExtra("id");
        seeUserId = getIntent().getStringExtra("seeUserId");
        if (!"null".equals(tipText) && tipText != null) {
            heartText.setText(tipText);
        } else {
            heartText.setVisibility(View.GONE);
        }
        browersAdapter = new BrowersPhotoAdapter(this, null);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(browersAdapter);
        alphaInAnimationAdapter.setAbsListView(photoList);
        photoList.setAdapter(alphaInAnimationAdapter);
        loadModelInfo();
        loadZuoPingDatas();

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("action.update.photoes.page");
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("action.update.photoes.page".equals(intent.getAction())) {
                // 添加你的业务
                isUpdate=intent.getBooleanExtra("isUpdate",false);
                isFirstLoad = false;
                if (isUpdate){
                    loadZuoPingDatas();
                }
            }

        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
//        int[] ids = new int[]{R.id.id_img};
//        RecycleBitmap.recycleAbsList(photoList, ids);
        System.gc();
        super.onDestroy();
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
    private void loadModelInfo() {
        RequestBean item = new RequestBean();
        try {
            item.setData(new JSONObject().put("seeUserId", seeUserId).put("userId", UserPreferencesUtil.getUserId(this)));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_MODEL_INFO);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_MODEL_INFO);
    }


    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.RESPOSE_MODEL_INFO:
                if ("0000".equals(data.getCode())) {
                    parsePersonRefresh(data);
                }else{
                    Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.RESPOSE_MODEL_GALLERY_INFO:
                if ("0000".equals(data.getCode())) {
                    parseDatasRefresh(data);
                }else{
                    Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
    private void loadZuoPingDatas() {
        String userId = "";
        if (UserPreferencesUtil.isOnline(this)){
            userId = UserPreferencesUtil.getUserId(this);
        }
        RequestBean item = new RequestBean();
        try {
            item.setData(new JSONObject().put("seeUserId", getSeeUserId()).put("userId",userId).put("galId",getGlaryId()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_MODEL_GALLERY_INFO);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_MODEL_GALLERY_INFO);
    }

    public String getSeeUserId() {
        return seeUserId;
    }

    public String getGlaryId() {
        return glaryId;
    }

    public void parsePersonRefresh(ResponseBean data) {
        try{
            JSONObject pMap=new JSONObject(data.getRecord());
            userName.setText(pMap.getString("usernick"));
            JSONArray array1=new JSONArray(pMap.getString("userTypes"));
            String userType;
            if (array1.length()>1){
                userType=array1.getJSONObject(1).getString("utype");
            }else{
                userType=array1.getJSONObject(0).getString("utype");
            }
            if (Constants.CARRE_MODEL.equals(userType)) {

                Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
                type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
                type_tv.setText("模特");
                type_tv.setBackgroundResource(R.drawable.identy_type_bg);
            } else if (Constants.CARRE_PHOTOR.equals(userType)) {
                Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
                type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
                type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
                type_tv.setText("摄影师");
                type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);

            }else if(userType.equals(Constants.PRO_MODEL)){
                Drawable modelDrawable = getResources().getDrawable(R.drawable.model_identy_icon);
                type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
                type_icon_tv.setBackgroundResource(R.drawable.identy_type_icon_bg);
                type_tv.setText("模特");
                type_tv.setBackgroundResource(R.drawable.identy_type_bg);
//                Drawable modelDrawable = getResources().getDrawable(R.drawable.model_pro_identy_icon);
//                type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(modelDrawable, null, null, null);
//                type_icon_tv.setBackgroundResource(R.drawable.identy_pro_type_icon_bg);
//                type_tv.setText("高级模特");
//                type_tv.setBackgroundResource(R.drawable.identy_pro_type_bg);
            }
            String sexStr = pMap.getString("gender");
            if (Constants.SEX_WOMAN.equals(sexStr)) {
                Drawable femaleDrawable = getResources().getDrawable(R.drawable.female);
                sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(femaleDrawable, null, null, null);
                sex_age_tv.setBackgroundResource(R.drawable.sex_bg);
            } else if (Constants.SEX_MAN.equals(sexStr)) {
                Drawable maleDrawable = getResources().getDrawable(R.drawable.male);
                sex_age_tv.setCompoundDrawablesWithIntrinsicBounds(maleDrawable, null, null, null);
                sex_age_tv.setBackgroundResource(R.drawable.sex_male_bg);
            }
            sex_age_tv.setText(pMap.getString("age"));

            String area = "";
            if (!pMap.isNull("city")){
                area = pMap.getString("city");
                if (!TextUtils.isEmpty(area) && !area.equals("null")) {
                    CityBean it = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(area));
                    userLocation.setText(it.getName());
                }else{
                    userLocation.setVisibility(View.GONE);
                    locationImage.setVisibility(View.GONE);
                }
            }

            JSONArray array2=new JSONArray(pMap.getString("modelStyles"));
            if (array2.length()==0){
                userStyleView.setVisibility(View.GONE);
            }else{
                int j=0;
                for (int i=0;i<array2.length();i++){
                    j=j+1;
                    if (j<=3){
                        View subView = LayoutInflater.from(this).inflate(R.layout.sg_syle_lable_item, null);
                        TextView styleName = (TextView) subView.findViewById(R.id.styleName);
                        styleName.setText(array2.get(i).toString());
                        View mView = new View(this);
                        mView.setLayoutParams(lp);
                        userStyleView.addView(subView);
                        userStyleView.addView(mView);
                    }
                }
            }

            if (TextUtils.isEmpty(area) || area.equals("null")&&array2.length()==0) {
                localStyleView.setVisibility(View.GONE);
            }

            String headStr = pMap.getString("userIcon");
            mImageLoader.loadImage(headStr, userImage, false);
            String isFans = pMap.getString("isFans");
            if ("false".equals(isFans)) {
                isGanZhu = false;
                attentionText.setText("关注");
                attentionIcon.setImageResource(R.drawable.attention_icon);
            } else {
                isGanZhu = true;
                attentionText.setText("取消");
                attentionIcon.setImageResource(R.drawable.attentioned_icon);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setAttention(Map resultMap) {
        isUpdate=true;
        isGanZhu = !isGanZhu;
        String status = (String) resultMap.get("status");
        if (status.equals("1")) {
            Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
            attentionText.setText("取消");
            attentionIcon.setImageResource(R.drawable.attentioned_icon);
        } else if (status.equals("3")) {
            Toast.makeText(this, "取消成功", Toast.LENGTH_SHORT).show();
            attentionText.setText("关注");
            attentionIcon.setImageResource(R.drawable.attention_icon);
        }
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @see [类、类#方法、类#成员]
     */
    public void parseDatasRefresh(ResponseBean data) {
        try {
            JSONObject pMap=new JSONObject(data.getRecord());
            picNumCount.setText(pMap.getString("count"));
            String countStr = pMap.getString("praiseCount");
            if (countStr == null || "".equals(countStr) || "null".equals(countStr)) {
                priseNumCount.setText("0");
            } else {
                priseNumCount.setText(countStr);
            }
            JSONArray array=new JSONArray(pMap.getString("pics"));
            browersDatas = new ArrayList<BrowerPhotoesBean>();
            for (int i=0;i<array.length();i++){
                JSONObject firstMap=array.getJSONObject(i);
                BrowerPhotoesBean ptBean = new BrowerPhotoesBean();
                ptBean.setImageId(firstMap.getString("resId"));
                ptBean.setImageUrl(firstMap.getString("resUrl"));
                ptBean.setPriseCount(firstMap.getString("praiseCount"));
                ptBean.setLengthPx(firstMap.getString("lengthPx"));
                ptBean.setPicSize(firstMap.getString("size"));
                ptBean.setWidthPx(firstMap.getString("widthPx"));
                if ("0".equals(firstMap.getString("isPraise"))) {
                    ptBean.setPrise(false);
                } else {
                    ptBean.setPrise(true);
                }

                browersDatas.add(ptBean);
            }
            if (isFirstLoad) {
                browersAdapter.addDatas(browersDatas);
                photoList.setAdapter(browersAdapter);
            } else {
                browersAdapter.addDatas(browersDatas);
                browersAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        boolean islogin = UserPreferencesUtil.isOnline(this);
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.attentionBackground) {
            if (!islogin) {
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                startActivity(intent);
            } else {
                if (isGanZhu) {
                    editAttention(3);
                } else {
                    editAttention(1);
                }
            }
        } else if (v.getId() == R.id.rightButton) {
            if (!islogin) {
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                startActivity(intent);
            } else {

                Intent shareintent = new Intent(this, ChoiceShareReportActivity.class);
                startActivityForResult(shareintent, 0);
            }
        } else if (v.getId() == R.id.intoPriseView) {
            Intent intent = new Intent(this, PrisePersonActivity.class);
            intent.putExtra("seeUserId", seeUserId);
            intent.putExtra("glaryId", glaryId);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (Activity.RESULT_OK != resultCode || null == data) {
                        return;
                    }
                    String flag = data.getStringExtra(Constants.COMEBACK);
                    if ("share".equals(flag)) {
                        Intent intent = new Intent(this, ShareBoardView.class);
                        intent.putExtra(Constants.SHARE_TITLE_KEY, getString(R.string.share_sub_title_model));
                        intent.putExtra(Constants.SHARE_SUBTITLE_KEY, userName.getText().toString() + "--来自【模范儿】");
                        startActivity(intent);
                    } else if ("report".equals(flag)) {
                        Intent intent = new Intent(this, ReportFristActivity.class);
                        intent.putExtra("resType", Constants.JUBAO_TYPE_XIANGCE);
                        intent.putExtra("userId", seeUserId);
                        intent.putExtra("resourceId", glaryId);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    /**
     * 重载方法
     */
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
        intent.putExtra("flagStr", isUpdate);
        sendBroadcast(intent);
        super.finish();
    }

    /**
     * 1:关注 3：取消
     *
     * @param i
     */
    private void editAttention(int i) {
        VisitUnit visit = new VisitUnit();
        visit.getDataUnit().getRepo().put("userId", UserPreferencesUtil.getUserId(this));
        visit.getDataUnit().getRepo().put("seeUserId", seeUserId);
        if (i == 1) {
            visit.getDataUnit().getRepo().put("status", "1");
        } else if (i == 3) {
            visit.getDataUnit().getRepo().put("status", "3");
        }

        new Action(EditAttention.class,"${editAttention},seeUserId=${seeUserId},userId=${userId},status=${status}",
                this, null, visit).run();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(Constants.ACTION_EDITOR_USER_INFO);
//            sendBroadcast(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重载方法
     *
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(this, ImagePagerBrowersActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            tsBean.setBrowersDatas(browersAdapter.getDatas());
            intent.putExtra("bean", tsBean);
            intent.putExtra("seeUserId", seeUserId);
            intent.putExtra("glaryId", glaryId);
            intent.putExtra("userName", userName.getText().toString());
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, arg2);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }
}
