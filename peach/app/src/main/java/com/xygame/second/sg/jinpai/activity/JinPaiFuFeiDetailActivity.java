package com.xygame.second.sg.jinpai.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.stevenhu.android.phone.bean.ADInfo;
import com.stevenhu.android.phone.utils.ViewFactory;
import com.xygame.second.sg.comm.bean.TimerDuringBean;
import com.xygame.second.sg.defineview.CustomScrollView;
import com.xygame.second.sg.defineview.DymicServiceTimeDialog1;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.jinpai.UsedTimeBean;
import com.xygame.second.sg.jinpai.adapter.AreaShowAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.second.sg.jinpai.bean.ScheduleTimeBean;
import com.xygame.second.sg.ordermsg.ChatOrderActivity;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.sendgift.activity.YuePaiDetailActivity;
import com.xygame.second.sg.sendgift.bean.OtherActBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.main.NewsGroupNoticeFragment;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lib.BaseViewPager;
import lib.CycleViewPagerHandler;


public class JinPaiFuFeiDetailActivity extends SGBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View backButton, rightButton, playButton, intoPersonal, sex_age_bg, bottomView, otherActTip, closeImage,locationView,areaBlockView,duanWeiView;
    private TextView titleName, bigTypeTex, userName, sexAge, loactionTex, actOral, actTitleText, rightButtonText, actPriceText,duanWeiText;
    private String actId, leftTimerStr;
    private ImageView sexIcon;
    private CustomScrollView scrollView;
    private CircularImage avatar_iv;
    private String mp4Url, publihserId, publishUserNick, publishUserImage, publicActTitle;
    private ImageLoader imageLoader;
    private LinearLayout actView;
    private BaseViewPager parentViewPager;
    private GridView areaGrid;
    private Button actButton;
    private List<OtherActBean> actBeans = new ArrayList<>();
    private String actStatus;//5和6关闭
    private String hasPending;//1挂起中
    private TimerDuringBean timerDuringBean;
    private List<ScheduleTimeBean> scheduleTimeBeans = new ArrayList<>();
    private List<UsedTimeBean> usedTimeBeans = new ArrayList<>();
    //binnerView
    private List<ImageView> views = new ArrayList<ImageView>();

    private List<ImageView> imageViews = new ArrayList<ImageView>();
    private ImageView[] indicators;
    private FrameLayout viewPagerFragmentLayout;
    private LinearLayout indicatorLayout; // 指示器
    private BaseViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int time = 5000; // 默认轮播时间
    private int currentPosition = 0; // 轮播当前位置
    private boolean isScrolling = false; // 滚动框是否滚动着
    private boolean isCycle = false; // 是否循环
    private boolean isWheel = false; // 是否轮播
    private long releaseTime = 0; // 手指松开、页面不滚动时间，防止手机松开后短时间进行切换
    private int WHEEL = 100; // 转动
    private int WHEEL_WAIT = 101; // 等待
    private ImageCycleViewListener mImageCycleViewListener;
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPagerHandler mHandler = new CycleViewPagerHandler(this) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHEEL && imageViews.size() != 0) {
                if (!isScrolling) {
                    int max = imageViews.size() + 1;
                    int position = (currentPosition + 1) % imageViews.size();
                    viewPager.setCurrentItem(position, true);
                    if (position == max) { // 最后一页时回到第一页
                        viewPager.setCurrentItem(1, false);
                    }
                }

                releaseTime = System.currentTimeMillis();
                mHandler.removeCallbacks(runnable);
                mHandler.postDelayed(runnable, time);
                return;
            }
            if (msg.what == WHEEL_WAIT && imageViews.size() != 0) {
                mHandler.removeCallbacks(runnable);
                mHandler.postDelayed(runnable, time);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_fufei_detail_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        duanWeiText=(TextView)findViewById(R.id.duanWeiText);
        duanWeiView=findViewById(R.id.duanWeiView);
        areaBlockView=findViewById(R.id.areaBlockView);
        locationView=findViewById(R.id.locationView);
        closeImage = findViewById(R.id.closeImage);
        scrollView = (CustomScrollView) findViewById(R.id.scrollView);
        otherActTip = findViewById(R.id.otherActTip);
        bottomView = findViewById(R.id.bottomView);
        actButton = (Button) findViewById(R.id.actButton);
        actTitleText = (TextView) findViewById(R.id.actTitleText);
        actPriceText = (TextView) findViewById(R.id.actPriceText);
        actOral = (TextView) findViewById(R.id.actOral);
        loactionTex = (TextView) findViewById(R.id.loactionTex);
        sexAge = (TextView) findViewById(R.id.sexAge);
        sexIcon = (ImageView) findViewById(R.id.sexIcon);
        sex_age_bg = findViewById(R.id.sex_age_bg);
        userName = (TextView) findViewById(R.id.userName);
        avatar_iv = (CircularImage) findViewById(R.id.avatar_iv);
        intoPersonal = findViewById(R.id.intoPersonal);
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        playButton = findViewById(R.id.playButton);
        bigTypeTex = (TextView) findViewById(R.id.bigTypeTex);
        viewPager = (BaseViewPager) findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) findViewById(R.id.layout_viewpager_indicator);
        viewPagerFragmentLayout = (FrameLayout) findViewById(R.id.layout_viewager_content);
        areaGrid = (GridView) findViewById(R.id.areaGrid);
        actView = (LinearLayout) findViewById(R.id.actView);
    }

    private void initDatas() {
        timerDuringBean = new TimerDuringBean();
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("活动详情");
        actId = getIntent().getStringExtra("actId");
        publihserId = getIntent().getStringExtra("userId");
        bottomView.setVisibility(View.GONE);
        if (publihserId.equals(UserPreferencesUtil.getUserId(this))) {
            titleName.setText("活动管理");
        } else {
            titleName.setText("活动详情");
            rightButton.setVisibility(View.GONE);
        }
        List<JinPaiBigTypeBean> vector = CacheService.getInstance().getCacheActDatas(ConstTaskTag.CACHE_ACT_TYPE);
        if (vector != null) {
            if (vector.size() > 0) {
                requestActData();
            } else {
                requestActType();
            }
        } else {
            requestActType();
        }

    }

    public void requestActType() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("ownerType", 1);
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_ACT_TYPE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_TYPE);
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        intoPersonal.setOnClickListener(this);
    }

    private void comfirmClose(String tip){
        TwoButtonDialog dialog = new TwoButtonDialog(this,tip, R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                        closeAction();
                    }

                    @Override
                    public void cancelListener() {
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
            case R.id.rightButton:
                boolean islogin1 = UserPreferencesUtil.isOnline(this);
                if (islogin1){
                    comfirmClose("确认关闭此活动？");
                }else{
                    Intent intent = new Intent(this, LoginWelcomActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.playButton:
                if (mp4Url != null) {
                    Intent intent = new Intent(JinPaiFuFeiDetailActivity.this, VideoPlayer.class);
                    intent.putExtra("vidoUrl", mp4Url);
                    startActivity(intent);
                }
                break;
            case R.id.intoPersonal:
                Intent intent = new Intent(this, PersonalDetailActivity.class);
                intent.putExtra("userNick",publishUserNick);
                intent.putExtra("userId", publihserId);
                startActivity(intent);

                break;
            case R.id.actButton:
                if (isGo()) {
                    timerDuringBean.setScheduleTimeBeans(scheduleTimeBeans);
                    timerDuringBean.setUsedTimeBeans(usedTimeBeans);
                    String singlePrice = actPriceText.getText().toString();
                    Intent intent3 = new Intent(this, DymicServiceTimeDialog1.class);
                    intent3.putExtra("dateTime", System.currentTimeMillis());
                    intent3.putExtra("dateDistance", 7);
                    intent3.putExtra("singlePrice",singlePrice);
                    intent3.putExtra("actId", actId);
                    intent3.putExtra("actTitle", actTitleText.getText().toString());
                    intent3.putExtra("userId", publihserId);
                    intent3.putExtra("timerDuringBean", timerDuringBean);
                    startActivityForResult(intent3, 1);
                }
                break;
        }
    }

    private Boolean isGo() {
        boolean islogin = UserPreferencesUtil.isOnline(this);
        if (!islogin) {
            Intent intent = new Intent(this, LoginWelcomActivity.class);
            startActivity(intent);
            return false;
        }

//        if ("1".equals(hasPending)) {
//            oneButtonDiloag("您参加的活动报名正在处理中，请稍耐心等待");
//            return false;
//        }
        return true;
    }

    private void oneButtonDiloag(String tip) {
        OneButtonDialog dialog = new OneButtonDialog(this, tip, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void closeAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);
            object.put("userId", publihserId);
            object.put("actTitle", actTitleText.getText().toString());
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_ACT_FUFEI_CLOSE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_FUFEI_CLOSE);
    }

    public void requestActData() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_ACT_FUFEI_DETAIL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_FUFEI_DETAIL);
    }

    private void parseTypeDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                List<JinPaiBigTypeBean> value = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JinPaiBigTypeBean item = new JinPaiBigTypeBean();
                    item.setIsSelect(false);
                    item.setName(StringUtils.getJsonValue(object, "typeName"));
                    item.setId(StringUtils.getJsonValue(object, "typeId"));
                    item.setSubStr(StringUtils.getJsonValue(object, "subTypes"));
                    item.setUrl(StringUtils.getJsonValue(object, "typeIconUrl"));
                    value.add(item);
                }
                CacheService.getInstance().cacheActDatas(ConstTaskTag.CACHE_ACT_TYPE, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ACT_TYPE:
                if ("0000".equals(data.getCode())) {
                    requestActData();
                    parseTypeDatas(data.getRecord());
                }
                break;
            case ConstTaskTag.QUERY_ACT_FUFEI_DETAIL:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                }else if ("7010".equals(data.getCode())){
                    showWaringTip("抱歉，该活动违规已被平台关闭");
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ACT_FUFEI_CLOSE:
                if ("0000".equals(data.getCode())) {
                    closeActionTip("活动关闭成功");
//                    closeImage.setVisibility(View.VISIBLE);
//                    rightButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showWaringTip(String s) {
        OneButtonDialog dialog = new OneButtonDialog(this,s, R.style.dineDialog,
                new ButtonOneListener() {
                    @Override
                    public void confrimListener(Dialog dialog) {
                        dialog.dismiss();
                        finish();
                    }
                });
        dialog.show();
    }

    private void closeActionTip(String tip) {
        OneButtonDialog dialog = new OneButtonDialog(this, tip, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("isClose", "close");
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
        dialog.show();
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object = new JSONObject(record);

                //publihser部分解析
                JSONObject object3 = new JSONObject(StringUtils.getJsonValue(object, "publisher"));
                publishUserImage = StringUtils.getJsonValue(object3, "userIcon");
                imageLoader.loadImage(publishUserImage, avatar_iv, true);
                publihserId = StringUtils.getJsonValue(object3, "userId");
                String city=StringUtils.getJsonValue(object3, "city");
                if (StringUtils.isEmpty(city)){
                    locationView.setVisibility(View.GONE);
                }else{
                    locationView.setVisibility(View.VISIBLE);
                    CityBean cb2 = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(city));
                    loactionTex.setText(cb2.getName());
                }
                publishUserNick = StringUtils.getJsonValue(object3, "usernick");
                userName.setText(publishUserNick);
                String sexStr = StringUtils.getJsonValue(object3, "gender");
                if (Constants.SEX_WOMAN.equals(sexStr)) {
                    sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
                    sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
                    sexAge.setText(StringUtils.getJsonValue(object3, "age"));
                } else if (Constants.SEX_MAN.equals(sexStr)) {
                    sexIcon.setImageResource(R.drawable.sg_man_light_icon);
                    sexAge.setText(StringUtils.getJsonValue(object3, "age"));
                    sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
                }

                //binner部分解析
                String res = StringUtils.getJsonValue(object, "res");
                List<BannerBean> resBannerList = new ArrayList<>();
                JSONArray array = new JSONArray(res);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    BannerBean item1 = new BannerBean();
                    if ("1".equals(StringUtils.getJsonValue(object1, "resType"))) {
                        mp4Url = StringUtils.getJsonValue(object1, "resUrl");
                    } else {
                        item1.setPicUrl(StringUtils.getJsonValue(object1, "resUrl"));
                        resBannerList.add(item1);
                    }
                }
                if (!TextUtils.isEmpty(mp4Url)){
                    BannerBean item1 = new BannerBean();
                    item1.setPicUrl(mp4Url);
                    resBannerList.add(0,item1);
                }
                if (resBannerList.size() > 0) {
                    initialize(resBannerList);
                }

                hasPending = StringUtils.getJsonValue(object, "hasPending");

                //已预定时间
                String bookTimes = StringUtils.getJsonValue(object, "bookTimes");
                if (ConstTaskTag.isTrueForArrayObj(bookTimes)) {
                    JSONArray array2 = new JSONArray(bookTimes);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        UsedTimeBean item = new UsedTimeBean();
                        item.setBookEndTime(StringUtils.getJsonLongValue(object1, "bookEndTime"));
                        item.setBookStartTime(StringUtils.getJsonLongValue(object1, "bookStartTime"));
                        usedTimeBeans.add(item);
                    }
                }
                //base部分解析
                String base = StringUtils.getJsonValue(object, "base");
                JSONObject object2 = new JSONObject(base);
                actOral.setText(StringUtils.getJsonValue(object2, "actDesc"));
                publicActTitle = StringUtils.getJsonValue(object2, "actTitle");
                actTitleText.setText(publicActTitle);
                actPriceText.setText(ConstTaskTag.getIntPrice(StringUtils.getJsonValue(object2, "price")));
                actStatus = StringUtils.getJsonValue(object2, "actStatus");
                String actType = StringUtils.getJsonValue(object2, "actType");
                String subType = StringUtils.getJsonValue(object2, "subType");
                List<JinPaiBigTypeBean> vector = CacheService.getInstance().getCacheActDatas(ConstTaskTag.CACHE_ACT_TYPE);
                JinPaiBigTypeBean biyTypeBean = null;
                for (JinPaiBigTypeBean it : vector) {
                    if (actType.equals(it.getId())) {
                        biyTypeBean = it;
                        if (!TextUtils.isEmpty(subType)){
                            String subStr = biyTypeBean.getSubStr();
                            JinPaiSmallTypeBean subBean = getSubTypeBean(subStr, subType);
                            bigTypeTex.setText(subBean.getTypeName());
                        }else{
                            bigTypeTex.setText(biyTypeBean.getName());
                        }
                        break;
                    }
                }
                leftTimerStr = StringUtils.getJsonValue(object2, "endTime");
                if ("6".equals(actStatus) || "5".equals(actStatus)) {
                    actButton.setText("活动已关闭");
                    actButton.setTextColor(getResources().getColor(R.color.black));
                    actButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                    closeImage.setVisibility(View.VISIBLE);
                    rightButton.setVisibility(View.GONE);
                    bottomView.setVisibility(View.GONE);
                } else {
                    if ("3".equals(actStatus)&&!CalendarUtils.isTimeGone(Long.parseLong(leftTimerStr), 0)) {
                        if (publihserId.equals(UserPreferencesUtil.getUserId(this))) {
                            bottomView.setVisibility(View.GONE);
                            rightButton.setVisibility(View.VISIBLE);
                            rightButtonText.setVisibility(View.VISIBLE);
                            rightButtonText.setText("关闭");
                            rightButton.setOnClickListener(this);
                        }else{
                            bottomView.setVisibility(View.VISIBLE);
                            bottomView.getBackground().setAlpha(50);
                            actButton.setText("参加活动");
                            actButton.setTextColor(getResources().getColor(R.color.white));
                            actButton.setBackgroundResource(R.drawable.shape_rect_dark_green);
                            actButton.setOnClickListener(this);
                        }
                    } else {
                        bottomView.setVisibility(View.GONE);
                        actButton.setText("活动已过期");
                        actButton.setTextColor(getResources().getColor(R.color.black));
                        actButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                    }
                }
                String duanweiPostion=StringUtils.getJsonValue(object2, "skillLevel");
                if (!TextUtils.isEmpty(duanweiPostion)&&!"0".equals(duanweiPostion)){
                    duanWeiView.setVisibility(View.VISIBLE);
                    String[] duanWei= Constants.DUAN_WEI;
                    duanWeiText.setText(duanWei[Integer.parseInt(duanweiPostion)-1]);
                }else{
                    duanWeiView.setVisibility(View.GONE);
                }

                //活动区域解析
                String areas = StringUtils.getJsonValue(object, "areas");
                JSONArray array1 = new JSONArray(areas);
                List<CityBean> datas = new ArrayList<>();
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject object1 = array1.getJSONObject(i);
                    String cityId = StringUtils.getJsonValue(object1, "city");
                    CityBean cb = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(cityId));

                    String region = StringUtils.getJsonValue(object1, "region");
                    if (region != null && !"".equals(region) && !"null".equals(region)) {
                        CityBean cb1 = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(region));
                        datas.add(cb1);
                    } else {
                        datas.add(cb);
                        break;
                    }
                }
                if (datas.size() > 0) {
                    AreaShowAdapter areaShowAdapter = new AreaShowAdapter(this, datas);
                    areaGrid.setAdapter(areaShowAdapter);
                } else {
                    areaBlockView.setVisibility(View.GONE);
                    areaGrid.setVisibility(View.GONE);
                }

                //其它活动列表
                String otherActions = StringUtils.getJsonValue(object, "otherActions");
                if (!TextUtils.isEmpty(otherActions) && !"[]".equals(otherActions) && !"null".equals(otherActions) && !"[null]".equals(otherActions)) {
                    JSONArray array2 = new JSONArray(otherActions);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        OtherActBean item = new OtherActBean();
                        item.setActId(StringUtils.getJsonValue(object1, "actId"));
                        item.setActNature(StringUtils.getJsonValue(object1, "actNature"));
                        item.setActTitle(StringUtils.getJsonValue(object1, "actTitle"));
                        item.setPrice(StringUtils.getJsonValue(object1, "price"));
                        item.setShowCoverUrl(StringUtils.getJsonValue(object1, "showCoverUrl"));
                        actBeans.add(item);
                    }
                    if (actBeans.size() > 0) {
                        otherActTip.setVisibility(View.VISIBLE);
                        actView.setVisibility(View.VISIBLE);
                        updateOtherActionViews();
                    } else {
                        otherActTip.setVisibility(View.GONE);
                        actView.setVisibility(View.GONE);
                    }
                } else {
                    otherActTip.setVisibility(View.GONE);
                    actView.setVisibility(View.GONE);
                }

                //购买日期
                String schedules = StringUtils.getJsonValue(object, "schedules");
                if (ConstTaskTag.isTrueForArrayObj(schedules)) {
                    JSONArray array2 = new JSONArray(schedules);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        ScheduleTimeBean item = new ScheduleTimeBean();
                        item.setServiceDay(StringUtils.getJsonLongValue(object1, "serviceDay"));
                        item.setHour9(StringUtils.getJsonValue(object1, "hour9"));
                        item.setHour10(StringUtils.getJsonValue(object1, "hour10"));
                        item.setHour11(StringUtils.getJsonValue(object1, "hour11"));
                        item.setHour12(StringUtils.getJsonValue(object1, "hour12"));
                        item.setHour13(StringUtils.getJsonValue(object1, "hour13"));
                        item.setHour14(StringUtils.getJsonValue(object1, "hour14"));
                        item.setHour15(StringUtils.getJsonValue(object1, "hour15"));
                        item.setHour16(StringUtils.getJsonValue(object1, "hour16"));
                        item.setHour17(StringUtils.getJsonValue(object1, "hour17"));
                        item.setHour18(StringUtils.getJsonValue(object1, "hour18"));
                        item.setHour19(StringUtils.getJsonValue(object1, "hour19"));
                        item.setHour20(StringUtils.getJsonValue(object1, "hour20"));
                        item.setHour21(StringUtils.getJsonValue(object1, "hour21"));
                        item.setHour22(StringUtils.getJsonValue(object1, "hour22"));
                        scheduleTimeBeans.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        delayScorll();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                    break;
                default:
                    break;
            }

        }
    };

    private void delayScorll() {
        new ThreadPool().excuseThread(new DelayScorllTime());
    }

    private class DelayScorllTime implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                Message m = handler.obtainMessage();
                m.what = 1;
                m.sendToTarget();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateOtherActionViews() {
        actView.removeAllViews();
        for (int i = 0; i < actBeans.size(); i++) {
            OtherActBean item = actBeans.get(i);
            View convertView = LayoutInflater.from(this).inflate(
                    R.layout.other_act_item, null);
            TextView titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
            TextView subTitle = (TextView) convertView.findViewById(R.id.subTitle);
            TextView taoziNums = (TextView) convertView.findViewById(R.id.taoziNums);
            View subLayout = convertView.findViewById(R.id.subLayout);
            ImageView avatar_iv = (ImageView) convertView
                    .findViewById(R.id.avatar_iv);
            View bottomLineView = convertView.findViewById(R.id.bottomLineView);

            if (actBeans.size() - 1 == i) {
                bottomLineView.setVisibility(View.GONE);
            }
            titleTxt.setText(item.getActTitle());
            imageLoader.loadImage(item.getShowCoverUrl(), avatar_iv, true);
            if ("1".equals(item.getActNature())) {//竞拍约
                subLayout.setVisibility(View.VISIBLE);
                subTitle.setVisibility(View.GONE);
                taoziNums.setText(ConstTaskTag.getIntPrice(item.getPrice()));
            } else if ("2".equals(item.getActNature())) {//直接约
                subLayout.setVisibility(View.VISIBLE);
                subTitle.setVisibility(View.GONE);
                taoziNums.setText(ConstTaskTag.getIntPrice(item.getPrice()));
            } else if ("3".equals(item.getActNature())) {//送礼约
                subLayout.setVisibility(View.GONE);
                subTitle.setVisibility(View.VISIBLE);
                subTitle.setText("免费");
            }
            convertView.setOnClickListener(new IntoSomeBodyActionDetail(item));
            actView.addView(convertView);
        }
    }

    private class IntoSomeBodyActionDetail implements View.OnClickListener{
        private  OtherActBean item;
        public IntoSomeBodyActionDetail( OtherActBean item){
            this.item=item;
        }

        @Override
        public void onClick(View v) {
            intoPersonalOtherActDetail(item);
        }
    }

    private void intoPersonalOtherActDetail(OtherActBean item) {
        if ("1".equals(item.getActNature())) {//竞拍约
            Intent intent=new Intent();
            intent.setClass(this, JinPaiDetailActivity.class);
            intent.putExtra("actId", item.getActId());
            intent.putExtra("userId",publihserId );
            startActivity(intent);
        } else if ("2".equals(item.getActNature())) {//直接约
            Intent intent=new Intent();
            intent.setClass(this, JinPaiFuFeiDetailActivity.class);
            intent.putExtra("actId", item.getActId());
            intent.putExtra("userId",publihserId );
            startActivity(intent);
        } else if ("3".equals(item.getActNature())) {//送礼约
            Intent intent = new Intent(this, YuePaiDetailActivity.class);
            intent.putExtra("fromFlag","browers");
            intent.putExtra("actId", item.getActId());
            intent.putExtra("userId",publihserId );
            startActivity(intent);
        }
    }

    private JinPaiSmallTypeBean getSubTypeBean(String record, String subTypeId) {
        JinPaiSmallTypeBean beanSub = null;
        if (!TextUtils.isEmpty(record)) {
            try {
                List<JinPaiSmallTypeBean> value = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JinPaiSmallTypeBean item = new JinPaiSmallTypeBean();
                    item.setTypeName(StringUtils.getJsonValue(object, "typeName"));
                    item.setTypeId(StringUtils.getJsonValue(object, "typeId"));
                    item.setIsSelected(false);
                    value.add(item);
                }
                if (value.size() > 0) {
                    for (JinPaiSmallTypeBean typeBean : value) {
                        if (subTypeId.equals(typeBean.getTypeId())) {
                            beanSub = typeBean;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return beanSub;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
                if (result) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.COMEBACK, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
            }
            case 1:
                boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
                String actDate = data.getStringExtra("actDate");
                String actStartTime = data.getStringExtra("actStartTime");
                String actEndTime = data.getStringExtra("actEndTime");
                String singlePrice = data.getStringExtra("singlePrice");
                String totalPrice = data.getStringExtra("totalPrice");
                String actReportId=data.getStringExtra("actId");
                if (result) {
                    hasPending = "1";
                    ToChatBean toChatBean = new ToChatBean();
                    toChatBean.setRecruitLocIndex("");
                    toChatBean.setNoticeId("");
                    toChatBean.setNoticeSubject(publishUserNick);
                    toChatBean.setUserIcon(publishUserImage);
                    toChatBean.setUserId(publihserId);
                    toChatBean.setUsernick(publishUserNick);
                    toChatBean.setRecruitId(actReportId);

                    Intent intent16 = new Intent(this, NewsGroupNoticeFragment.class);
                    startActivity(intent16);

                    Intent intent = new Intent(this, ChatOrderActivity.class);
                    intent.putExtra("toChatBean", toChatBean);
                    intent.putExtra("actDate", actDate);
                    intent.putExtra("actStartTime", actStartTime);
                    intent.putExtra("actEndTime", actEndTime);
                    intent.putExtra("singlePrice", singlePrice);
                    intent.putExtra("totalPrice", totalPrice);
                    intent.putExtra("actTitle", publicActTitle);
                    intent.putExtra("actionId",actId);
                    intent.putExtra("memberId",UserPreferencesUtil.getUserId(this));
                    startActivity(intent);
                    Intent intent1 = new Intent();
                    intent1.putExtra(Constants.COMEBACK, true);
                    setResult(Activity.RESULT_OK, intent1);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    //++++++++++++++++++++++binner部分+++++++++++++++++++++++++++++++++++++++++

    @SuppressLint("NewApi")
    private void initialize(List<BannerBean> resBannerList) {
        infos.clear();
        views.clear();
        for (int i = 0; i < resBannerList.size(); i++) {
            ADInfo info = new ADInfo();
            info.setUrl(resBannerList.get(i).getPicUrl());
            info.setBannerBean(resBannerList.get(i));
            infos.add(info);
        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        setCycle(true);

        // 在加载数据前设置是否循环
        setData(views, infos, mAdCycleViewListener);
        //设置轮播
        setWheel(true);

        // 设置轮播时间，默认5000ms
        setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        setIndicatorCenter();
    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        public void onImageClick(ADInfo info, int position, View imageView) {
            if (position==1){
                Intent intent = new Intent(JinPaiFuFeiDetailActivity.this, VideoPlayer.class);
                intent.putExtra("vidoUrl", mp4Url);
                startActivity(intent);
            }else{
                String[] tempImages=new String[infos.size()-1];
                for (int i=1;i<infos.size();i++){
                    tempImages[i-1]=infos.get(i).getUrl();
                }
                Constants.imageBrower(JinPaiFuFeiDetailActivity.this,position-2,tempImages,false);
            }
        }
    };

    /**
     * 设置指示器居中，默认指示器在右方
     */
    public void setIndicatorCenter() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        indicatorLayout.setLayoutParams(params);
    }

    public void setData(List<ImageView> views, List<ADInfo> list, ImageCycleViewListener listener) {
        setData(views, list, listener, 0);
    }

    /**
     * 初始化viewpager
     *
     * @param views        要显示的views
     * @param showPosition 默认显示位置
     */
    public void setData(List<ImageView> views, List<ADInfo> list, ImageCycleViewListener listener, int showPosition) {
        mImageCycleViewListener = listener;
        infos = list;
        this.imageViews.clear();

        if (views.size() == 0) {
            viewPagerFragmentLayout.setVisibility(View.GONE);
            return;
        }

        for (ImageView item : views) {
            this.imageViews.add(item);
        }

        int ivSize = views.size();

        // 设置指示器
        indicators = new ImageView[ivSize];
        if (isCycle)
            indicators = new ImageView[ivSize - 2];
        indicatorLayout.removeAllViews();
        for (int i = 0; i < indicators.length; i++) {
            View view = LayoutInflater.from(this).inflate(
                    R.layout.view_cycle_viewpager_indicator, null);
            indicators[i] = (ImageView) view.findViewById(R.id.image_indicator);
            indicatorLayout.addView(view);
        }

        adapter = new ViewPagerAdapter();

        // 默认指向第一项，下方viewPager.setCurrentItem将触发重新计算指示器指向
        setIndicator(0);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        if (showPosition < 0 || showPosition >= views.size())
            showPosition = 0;
        if (isCycle) {
            showPosition = showPosition + 1;
        }
        viewPager.setCurrentItem(showPosition);

    }

    /**
     * 是否循环，默认不开启，开启前，请将views的最前面与最后面各加入一个视图，用于循环
     *
     * @param isCycle 是否循环
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 是否处于循环状态
     *
     * @return
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 设置是否轮播，默认不轮播,轮播一定是循环的
     *
     * @param isWheel
     */
    public void setWheel(boolean isWheel) {
        this.isWheel = isWheel;
        isCycle = true;
        if (isWheel) {
            mHandler.postDelayed(runnable, time);
        }
    }

    /**
     * 是否处于轮播状态
     *
     * @return
     */
    public boolean isWheel() {
        return isWheel;
    }

    final Runnable runnable = new Runnable() {

        public void run() {
            if (!isFinishing()
                    && isWheel) {
                long now = System.currentTimeMillis();
                // 检测上一次滑动时间与本次之间是否有触击(手滑动)操作，有的话等待下次轮播
                if (now - releaseTime > time - 500) {
                    mHandler.sendEmptyMessage(WHEEL);
                } else {
                    mHandler.sendEmptyMessage(WHEEL_WAIT);
                }
            }
        }

    };

    /**
     * 设置轮播暂停时间，即没多少秒切换到下一张视图.默认5000ms
     *
     * @param time 毫秒为单位
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * 刷新数据，当外部视图更新后，通知刷新数据
     */
    public void refreshData() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * 隐藏CycleViewPager
     */
    public void hide() {
        viewPagerFragmentLayout.setVisibility(View.GONE);
    }

    /**
     * 返回内置的viewpager
     *
     * @return viewPager
     */
    public BaseViewPager getViewPager() {
        return viewPager;
    }

    /**
     * 页面适配器 返回对应的view
     *
     * @author Yuedong Li
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final ImageView v = imageViews.get(position);
            if (mImageCycleViewListener != null) {

                v.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        mImageCycleViewListener.onImageClick(infos.get(currentPosition - 1), currentPosition, v);
                    }
                });
            }
            container.addView(v);
            return v;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i]
                    .setBackgroundResource(R.drawable.icon_point);
        }
        if (indicators.length > selectedPosition)
            indicators[selectedPosition]
                    .setBackgroundResource(R.drawable.icon_point_pre);
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param imageView
         */
        void onImageClick(ADInfo info, int postion, View imageView);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int arg0) {
        int max = imageViews.size() - 1;
        int position = arg0;
        currentPosition = arg0;
        if (isCycle) {
            if (arg0 == 0) {
                currentPosition = max - 1;
            } else if (arg0 == max) {
                currentPosition = 1;
            }
            position = currentPosition - 1;
        }
        if (currentPosition==1){
            playButton.setVisibility(View.VISIBLE);
        }else{
            playButton.setVisibility(View.GONE);
        }
        setIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        if (arg0 == 1) { // viewPager在滚动
            isScrolling = true;
            return;
        } else if (arg0 == 0) { // viewPager滚动结束
            if (parentViewPager != null)
                parentViewPager.setScrollable(true);

            releaseTime = System.currentTimeMillis();

            viewPager.setCurrentItem(currentPosition, false);

        }
        isScrolling = false;
    }
}
