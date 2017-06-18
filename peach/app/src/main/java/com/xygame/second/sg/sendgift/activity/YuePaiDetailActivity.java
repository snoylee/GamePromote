package com.xygame.second.sg.sendgift.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.xygame.second.sg.comm.activity.ReportDialog;
import com.xygame.second.sg.comm.bean.GiftBean;
import com.xygame.second.sg.comm.bean.TransferGift;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.jinpai.activity.JinPaiDetailActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiFuFeiDetailActivity;
import com.xygame.second.sg.jinpai.adapter.AreaShowAdapter;
import com.xygame.second.sg.jinpai.adapter.JinPaiDetailSmallTypeAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.second.sg.personal.activity.PayStoneActivity;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.bean.PopPresentBean;
import com.xygame.second.sg.sendgift.bean.GiftPresenter;
import com.xygame.second.sg.sendgift.bean.OtherActBean;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.PresentDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;
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

import static com.xygame.sg.R.id.iconOral;


public class YuePaiDetailActivity extends SGBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener , PresentDialog.PresentActionInterface ,TextWatcher{

    private View backButton, rightButton, playButton, intoPersonal, sex_age_bg,bottomView,otherActTip,slPhbView,slphbButton,closeImage,locationView;
    private TextView titleName, bigTypeTex, userName, sexAge, loactionTex, actOral, dangDateText, timeDuringText, actTitleText,chargeMethod,personNums,rightButtonText;
    private String actId,leftTimerStr,appliedFlag,hireUserId;
    private ImageView sexIcon;
    private GridView areaGrid;
    private JinPaiDetailSmallTypeAdapter smallTypeAdapter;
    private CircularImage avatar_iv;
    private String mp4Url, publihserId,fromFlag;
    private ImageLoader imageLoader;
    private Button actButton;
    private LinearLayout giftView,actView;
    private List<GiftPresenter> giftPresenterTotal;
    private List<OtherActBean> actBeans=new ArrayList<>();
    private double leaveStones=0;
    private PresentDialog presentDialog;
    private ViewPager popPager;
    private List<MyAdapter> mGridViewAdapters = new ArrayList<MyAdapter>();
    private List<View> mAllViews = new ArrayList<View>();
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout popDotView;
    private EditText giftNums;
    private TextView totalStone,leaveStone;
    private PopPresentBean tempPresentBean;
    private int pageCount = 1;
    private Presenter currPresenter;
    private String actStatus;//5和6关闭
    private ScrollView scrollView;
    private boolean isLvYunAialable=false;

    //binnerView
    private List<ImageView> views = new ArrayList<ImageView>();

    private List<ImageView> imageViews = new ArrayList<ImageView>();
    private ImageView[] indicators;
    private FrameLayout viewPagerFragmentLayout;
    private LinearLayout indicatorLayout; // 指示器
    private BaseViewPager viewPager;
    private BaseViewPager parentViewPager;
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
    private  List<GiftBean> giftDatas;
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
        setContentView(R.layout.yue_pai_detail_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        locationView=findViewById(R.id.locationView);
        scrollView=(ScrollView)findViewById(R.id.scrollView);
        closeImage=findViewById(R.id.closeImage);
        slphbButton=findViewById(R.id.slphbButton);
        slPhbView=findViewById(R.id.slPhbView);
        otherActTip=findViewById(R.id.otherActTip);
        bottomView=findViewById(R.id.bottomView);
        actTitleText = (TextView) findViewById(R.id.actTitleText);
        timeDuringText = (TextView) findViewById(R.id.timeDuringText);
        dangDateText = (TextView) findViewById(R.id.dangDateText);
        actOral = (TextView) findViewById(R.id.actOral);
        loactionTex = (TextView) findViewById(R.id.loactionTex);
        sexAge = (TextView) findViewById(R.id.sexAge);
        sexIcon = (ImageView) findViewById(R.id.sexIcon);
        sex_age_bg = findViewById(R.id.sex_age_bg);
        userName = (TextView) findViewById(R.id.userName);
        avatar_iv = (CircularImage) findViewById(R.id.avatar_iv);
        intoPersonal = findViewById(R.id.intoPersonal);
        actButton = (Button)findViewById(R.id.actButton);
        chargeMethod = (TextView) findViewById(R.id.chargeMethod);
        titleName = (TextView) findViewById(R.id.titleName);
        personNums = (TextView) findViewById(R.id.personNums);
        giftView = (LinearLayout) findViewById(R.id.giftView);
        actView = (LinearLayout) findViewById(R.id.actView);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        playButton = findViewById(R.id.playButton);
        bigTypeTex = (TextView) findViewById(R.id.bigTypeTex);
        viewPager = (BaseViewPager) findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) findViewById(R.id.layout_viewpager_indicator);
        viewPagerFragmentLayout = (FrameLayout) findViewById(R.id.layout_viewager_content);
        areaGrid = (GridView) findViewById(R.id.areaGrid);
    }

    private void initDatas() {
        slPhbView.setVisibility(View.GONE);
        otherActTip.setVisibility(View.GONE);
        giftPresenterTotal=new ArrayList<>();
        fromFlag=getIntent().getStringExtra("fromFlag");
        actId = getIntent().getStringExtra("actId");
        publihserId=getIntent().getStringExtra("userId");
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        bottomView.setVisibility(View.GONE);
        if (publihserId.equals(UserPreferencesUtil.getUserId(this))){
            titleName.setText("活动管理");
        }else{
            titleName.setText("活动详情");
            rightButton.setVisibility(View.GONE);
        }

        List<JinPaiBigTypeBean> vector = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
        if (vector != null) {
            giftDatas=CacheService.getInstance().getCacheGiftDatas(ConstTaskTag.CACHE_GIFT_DATAS);
            if (giftDatas != null) {
                requestActData();
            } else {
                requestGifts();
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
        playButton.setOnClickListener(this);
        intoPersonal.setOnClickListener(this);
        actButton.setOnClickListener(this);
        slphbButton.setOnClickListener(this);
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
            case R.id.actButton:
                if (!CalendarUtils.isTimeGone(Long.parseLong(leftTimerStr), 0)){
                    boolean islogin = UserPreferencesUtil.isOnline(this);
                    if (islogin){
                        if (!publihserId.equals(UserPreferencesUtil.getUserId(this))){
                            if ("1".equals(appliedFlag)){
                                getUserStones();
                            }else{
                                baoMingAct();
                            }
                        }else{
                            Toast.makeText(this,"本人不能参与报名",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Intent intent = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent);
                    }
                }else{
                    actButton.setText("报名已结束");
                    actButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                }
                break;
            case R.id.slphbButton:
                Intent intent1 = new Intent(YuePaiDetailActivity.this, GiftRankActivity.class);
                intent1.putExtra("fromFlag", fromFlag);
                intent1.putExtra("hireUserId",hireUserId);
                intent1.putExtra("publihserId", publihserId);
                intent1.putExtra("actTitle",actTitleText.getText().toString());
                intent1.putExtra("actId", actId);
                intent1.putExtra("isLvYunAialable",isLvYunAialable);
                startActivity(intent1);
                break;
            case R.id.playButton:
                if (mp4Url!=null){
                    Intent intent = new Intent(YuePaiDetailActivity.this, VideoPlayer.class);
                    intent.putExtra("vidoUrl", mp4Url);
                    startActivity(intent);
                }
                break;
            case R.id.intoPersonal:
                Intent intent = new Intent(this, PersonalDetailActivity.class);
                intent.putExtra("userNick",userName.getText().toString());
                intent.putExtra("userId", publihserId);
                startActivity(intent);

                break;
        }
    }

    private void closeAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);
            object.put("userId", publihserId);
            object.put("actTitle",actTitleText.getText().toString());
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_CLOSE_ACT);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CLOSE_ACT);
    }

    private void baoMingAct() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);
            object.put("userId", publihserId);
            object.put("actTitle",actTitleText.getText().toString());
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_FREE_ACT_APPLY);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_FREE_ACT_APPLY);
    }

    private void getUserStones(){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_LEAVE_STONES);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_LEAVE_STONES);
    }

    public void requestActData() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);
            object.put("userId", publihserId);
            if ("browers".equals(fromFlag)){
                object.put("fixTrace", "1");
            }
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_FREE_ACT_DETAIL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_FREE_ACT_DETAIL);
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
                CacheService.getInstance().cacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPresent() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", publihserId);
            obj.put("actId", actId);
            obj.put("actTitle", actTitleText.getText().toString());
            obj.put("number", giftNums.getText().toString().trim());
            Double d_s=new Double(Double.parseDouble(totalStone.getText().toString()) * 100);
            obj.put("totalAmount",String.valueOf(d_s.intValue()) );
            obj.put("giftId", tempPresentBean.getId());
            obj.put("giftName",tempPresentBean.getName());
            obj.put("sendUsernick",UserPreferencesUtil.getUserNickName(this));
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_SEND_GIFT);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SEND_GIFT);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GIFT:
                String record3=data.getRecord();
                try{
                    if (!TextUtils.isEmpty(record3)&&!"[]".equals(record3)&&!"null".equals(record3)&&!"[null]".equals(record3)){
                        JSONArray array2=new JSONArray(record3);
                        List<GiftBean> datas=new ArrayList<>();
                        for (int i=0;i<array2.length();i++){
                            JSONObject object1 = array2.getJSONObject(i);
                            GiftBean item=new GiftBean();
                            item.setName(StringUtils.getJsonValue(object1, "name"));
                            item.setId(StringUtils.getJsonValue(object1, "id"));
                            item.setShowUrl(StringUtils.getJsonValue(object1, "showUrl"));
                            item.setWorthAmount(StringUtils.getJsonValue(object1, "worthAmount"));
                            datas.add(item);
                        }
                        if (datas.size()>0){
                            CacheService.getInstance().cacheGiftDatas(ConstTaskTag.CACHE_GIFT_DATAS,datas);
                        }
                    }
                    requestActData();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case ConstTaskTag.QUERY_ACT_TYPE:
                if ("0000".equals(data.getCode())) {
                    giftDatas=CacheService.getInstance().getCacheGiftDatas(ConstTaskTag.CACHE_GIFT_DATAS);
                    if (giftDatas != null) {
                        requestActData();
                    } else {
                        requestGifts();
                    }
                    parseTypeDatas(data.getRecord());
                }
                break;
            case ConstTaskTag.QUERY_FREE_ACT_DETAIL:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                }else if ("7010".equals(data.getCode())){
                    showWaringTip("抱歉，该活动违规已被平台关闭");
                }else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_FREE_ACT_APPLY:
                if ("0000".equals(data.getCode())) {
                    appliedFlag="1";
                    actButton.setText("马上送礼");
                    GiftPresenter item=new GiftPresenter();
                    item.setGiftId("");
                    item.setGiftNums("");
                    item.setSendGiftTime(String.valueOf(System.currentTimeMillis()));
                    Presenter item1=new Presenter();
                    item1.setAge(UserPreferencesUtil.getUserAge(this));
                    item1.setGender(UserPreferencesUtil.getSex(this));
                    item1.setUserIcon(UserPreferencesUtil.getHeadPic(this));
                    item1.setUserId(UserPreferencesUtil.getUserId(this));
                    item1.setUserNick(UserPreferencesUtil.getUserNickName(this));
                    item.setPresenter(item1);
                    giftPresenterTotal.add(0, item);
                    List<GiftPresenter> tempDatas=new ArrayList<>();
                    if (giftPresenterTotal.size()>=3){
                        for (int i=0;i<3;i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }else{
                        for (int i=0;i<giftPresenterTotal.size();i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }
                    updateGiftViews(tempDatas);
                    showSuccessDialog();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_CLOSE_ACT:
                if ("0000".equals(data.getCode())) {
//                    closeImage.setVisibility(View.VISIBLE);
//                    rightButton.setVisibility(View.GONE);
                    closeActionTip("活动关闭成功");
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_LEAVE_STONES:
                if ("0000".equals(data.getCode())) {
                    leaveStones=ConstTaskTag.getDoublePrice(data.getRecord());
                    showPresentDialog();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SEND_GIFT:
                if ("0000".equals(data.getCode())) {
                    presentDialog.dismiss();
                    GiftPresenter item=new GiftPresenter();
                    item.setGiftId(String.valueOf(tempPresentBean.getId()));
                    item.setGiftNums(giftNums.getText().toString().trim());
                    item.setSendGiftTime(String.valueOf(System.currentTimeMillis()));
                    Presenter item1=new Presenter();
                    item1.setAge(UserPreferencesUtil.getUserAge(this));
                    item1.setGender(UserPreferencesUtil.getSex(this));
                    item1.setUserIcon(UserPreferencesUtil.getHeadPic(this));
                    item1.setUserId(UserPreferencesUtil.getUserId(this));
                    item1.setUserNick(UserPreferencesUtil.getUserNickName(this));
                    item.setPresenter(item1);
                    giftPresenterTotal.add(0, item);
                    List<GiftPresenter> tempDatas=new ArrayList<>();
                    if (giftPresenterTotal.size()>=3){
                        for (int i=0;i<3;i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }else{
                        for (int i=0;i<giftPresenterTotal.size();i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }
                    updateGiftViews(tempDatas);
                    Toast.makeText(this, "送礼成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_FREE_LQ2:
                if ("0000".equals(data.getCode())) {
                    hireUserId=currPresenter.getUserId();
                    List<GiftPresenter> tempDatas=new ArrayList<>();
                    if (giftPresenterTotal.size()>=3){
                        for (int i=0;i<3;i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }else{
                        for (int i=0;i<giftPresenterTotal.size();i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }
                    updateGiftViews(tempDatas);
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

    private void closeActionTip(String tip){
        OneButtonDialog dialog = new OneButtonDialog(this,tip, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra(Constants.COMEBACK, true);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
        dialog.show();
    }

    private void showPresentDialog() {
        giftDatas=CacheService.getInstance().getCacheGiftDatas(ConstTaskTag.CACHE_GIFT_DATAS);
        if (giftDatas != null) {
            if (giftDatas.size()>0) {
                presentDialog = new PresentDialog(this, R.style.dineDialog);
                presentDialog.setCancelable(false);
                presentDialog.addPresentActionListener(this);
                presentDialog.show();
                popPager = presentDialog.getPopPager();
                popDotView=presentDialog.getPopDotView();
                giftNums=presentDialog.getGiftNumsEditText();
                giftNums.addTextChangedListener(this);
                totalStone=presentDialog.getTotalStone();
                leaveStone=presentDialog.getStoneValues();
                leaveStone.setText(String.valueOf(leaveStones));
                initPresentDatas();
            }else{
                Toast.makeText(this,"稍后重试，礼物加载中...",Toast.LENGTH_SHORT).show();
                requestGifts();
            }
        } else {
            Toast.makeText(this,"稍后重试，礼物加载中...",Toast.LENGTH_SHORT).show();
            requestGifts();
        }
    }

    private void initPresentDatas() {
        List<PopPresentBean> datas=new ArrayList<>();
        for (GiftBean item:giftDatas){
            PopPresentBean subItem=new PopPresentBean();
            subItem.setId(Integer.parseInt(item.getId()));
            subItem.setName(item.getName());
            subItem.setShowUrl(item.getShowUrl());
            subItem.setWorthAmount(Long.parseLong(item.getWorthAmount()));
            subItem.setHasSlect(false);
            datas.add(subItem);
        }
        int k = 0, h = 8,l=0;
        if (datas.size() >= 8) {
            int yuShu = datas.size() % 8;
            pageCount = yuShu == 0 ? datas.size() / 8 : (datas.size() / 8) + 1;
        }

        mGridViewAdapters.clear();
        mAllViews.clear();
        LayoutInflater inflater = LayoutInflater.from(this);
        if (datas.size()<h){
            h=datas.size();
        }
        for (int i = 0; i < pageCount; i++) {
            List<PopPresentBean> tempDatas = new ArrayList<>();
            for (int j = k; j < h; j++) {
                tempDatas.add(datas.get(j));
                l = j;
            }
            h = h + 8;
            if (h>datas.size()){
                h=datas.size();
            }
            k =l + 1;
            View mView = inflater.inflate(R.layout.langsi_popup_gridview, null);
            GridView mGridView = (GridView) mView
                    .findViewById(R.id.langsi_popup_gridview);
            MyAdapter adapter = new MyAdapter(this, tempDatas);
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(new SelectPersentListener(i));
            adapter.notifyDataSetChanged();
            mGridViewAdapters.add(adapter);
            mAllViews.add(mView);
        }
        updatePopWindownDot(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        popPager.setAdapter(myViewPagerAdapter);
        myViewPagerAdapter.notifyDataSetChanged();
        popPager.setOnPageChangeListener(new PopPageChangeListener());
    }

    private class PopPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updatePopWindownDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void showSuccessDialog(){
        final ReportDialog mDialog=new ReportDialog(this, R.style.dineDialog);
        mDialog.setCancelable(false);
        mDialog.addPresentActionListener(new ReportDialog.PresentActionInterface() {
            @Override
            public void sendPresentAction() {
                mDialog.dismiss();
                getUserStones();
            }
        });
        mDialog.show();
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object = new JSONObject(record);

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

                appliedFlag=StringUtils.getJsonValue(object,"applied");

                //base部分解析
                String base = StringUtils.getJsonValue(object, "base");
                JSONObject object2 = new JSONObject(base);
                actOral.setText(StringUtils.getJsonValue(object2, "actDesc"));
                actTitleText.setText(StringUtils.getJsonValue(object2, "actTitle"));
                actStatus=StringUtils.getJsonValue(object2, "actStatus");
                String actType = StringUtils.getJsonValue(object2, "actType");
                String subType = StringUtils.getJsonValue(object2, "subType");
                List<JinPaiBigTypeBean> vector = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
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
                leftTimerStr=StringUtils.getJsonValue(object2, "endTime");
                if ("6".equals(actStatus)||"5".equals(actStatus)){
                    isLvYunAialable=false;
                    actButton.setText("已关闭");
                    actButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                    bottomView.setVisibility(View.GONE);
                }else {
                    if ("3".equals(actStatus)&&!CalendarUtils.isTimeGone(Long.parseLong(leftTimerStr), 0)){
                        if (publihserId.equals(UserPreferencesUtil.getUserId(this))) {
                            isLvYunAialable=true;
                            bottomView.setVisibility(View.GONE);
                            rightButton.setVisibility(View.VISIBLE);
                            rightButtonText.setVisibility(View.VISIBLE);
                            rightButtonText.setText("关闭");
                            rightButton.setOnClickListener(this);
                        }else {
                            bottomView.setVisibility(View.VISIBLE);
                            bottomView.getBackground().setAlpha(50);
                            actButton.setTextColor(getResources().getColor(R.color.white));
                            actButton.setBackgroundResource(R.drawable.shape_rect_dark_green);
                            if ("1".equals(appliedFlag)){//0未报名1已报名
                                actButton.setText("马上送礼");
                                actButton.setOnClickListener(this);
                            }else{
                                actButton.setText("现在报名");
                                actButton.setOnClickListener(this);
                            }
                        }
                    }else{
                        isLvYunAialable=false;
                        bottomView.setVisibility(View.GONE);
                        actButton.setText("已结束");
                        actButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                    }
                }

                dangDateText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(StringUtils.getJsonValue(object2, "serviceTime"))));
                timeDuringText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(StringUtils.getJsonValue(object2, "endTime"))));
//                timeDuringText.setText(CalendarUtils.getDateFromLong(Long.parseLong(StringUtils.getJsonValue(object2, "endTime")),-1));


                String holdTime = StringUtils.getJsonValue(object2, "feeType");
                switch (Integer.parseInt(holdTime)) {
                    case 1:
                        chargeMethod.setText("当然你买单");
                        break;
                    case 2:
                        chargeMethod.setText("我是土豪我请你");
                        break;
                    case 3:
                        chargeMethod.setText("AA比较公平");
                        break;
                }
                personNums.setText(StringUtils.getJsonValue(object2, "scanCount"));

                //publihser部分解析
                JSONObject object3 = new JSONObject(StringUtils.getJsonValue(object, "publisher"));
                imageLoader.loadImage(StringUtils.getJsonValue(object3, "userIcon"), avatar_iv, true);
                publihserId = StringUtils.getJsonValue(object3, "userId");
                String city=StringUtils.getJsonValue(object3, "city");
                if (StringUtils.isEmpty(city)){
                    locationView.setVisibility(View.GONE);
                }else{
                    locationView.setVisibility(View.VISIBLE);
                    CityBean cb2 = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(city));
                    loactionTex.setText(cb2.getName());
                }
                userName.setText(StringUtils.getJsonValue(object3, "usernick"));
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

                //活动区域解析
                String areas = StringUtils.getJsonValue(object, "areas");
                JSONArray array1 = new JSONArray(areas);
                List<CityBean> datas=new ArrayList<>();
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject object1 = array1.getJSONObject(i);
                    String cityId = StringUtils.getJsonValue(object1, "city");
                    CityBean cb = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(cityId));

                    String region = StringUtils.getJsonValue(object1, "region");
                    if (region != null && !"".equals(region) && !"null".equals(region)) {
                        CityBean cb1 = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(region));
                        datas.add(cb1);
                    }else{
                        datas.add(cb);
                        break;
                    }
                }
                if (datas.size()>0){
                    AreaShowAdapter areaShowAdapter=new AreaShowAdapter(this,datas);
                    areaGrid.setAdapter(areaShowAdapter);
                }else {
                    areaGrid.setVisibility(View.GONE);
                }

                //送礼列表
                hireUserId=StringUtils.getJsonValue(object, "hireUserId");
                String partDynamics=StringUtils.getJsonValue(object, "partDynamics");
                if (!TextUtils.isEmpty(partDynamics)&&!"[]".equals(partDynamics)&&!"null".equals(partDynamics)&&!"[null]".equals(partDynamics)){
                    List<GiftPresenter> giftPresenters=new ArrayList<>();
                    JSONArray array2=new JSONArray(partDynamics);
                    for (int i=0;i<array2.length();i++){
                        JSONObject object1 = array2.getJSONObject(i);
                        GiftPresenter item=new GiftPresenter();
                        item.setGiftId(StringUtils.getJsonValue(object1, "giftId"));
                        item.setGiftNums(StringUtils.getJsonValue(object1, "giftNumber"));
                        item.setSendGiftTime(StringUtils.getJsonValue(object1, "partTime"));
                        String participator=StringUtils.getJsonValue(object1, "participator");
                        JSONObject object4=new JSONObject(participator);
                        Presenter item1=new Presenter();
                        item1.setAge(StringUtils.getJsonValue(object4, "age"));
                        item1.setGender(StringUtils.getJsonValue(object4, "gender"));
                        item1.setUserIcon(StringUtils.getJsonValue(object4, "userIcon"));
                        item1.setUserId(StringUtils.getJsonValue(object4, "userId"));
                        item1.setUserNick(StringUtils.getJsonValue(object4, "usernick"));
                        item.setPresenter(item1);
                        giftPresenters.add(item);
                    }
                    if (giftPresenters.size()>0){
                        giftPresenterTotal.addAll(giftPresenters);
                        updateGiftViews(giftPresenters);
                    }else{
                        slPhbView.setVisibility(View.GONE);
                        giftView.setVisibility(View.GONE);
                    }
                }else{
                    slPhbView.setVisibility(View.GONE);
                    giftView.setVisibility(View.GONE);
                }

                //其它活动列表
                String otherActions=StringUtils.getJsonValue(object, "otherActions");
                if (!TextUtils.isEmpty(otherActions)&&!"[]".equals(otherActions)&&!"null".equals(otherActions)&&!"[null]".equals(otherActions)){
                    JSONArray array2=new JSONArray(otherActions);
                    for (int i=0;i<array2.length();i++){
                        JSONObject object1 = array2.getJSONObject(i);
                        OtherActBean item=new OtherActBean();
                        item.setActId(StringUtils.getJsonValue(object1, "actId"));
                        item.setActNature(StringUtils.getJsonValue(object1, "actNature"));
                        item.setActTitle(StringUtils.getJsonValue(object1, "actTitle"));
                        item.setPrice(StringUtils.getJsonValue(object1, "price"));
                        item.setShowCoverUrl(StringUtils.getJsonValue(object1, "showCoverUrl"));
                        actBeans.add(item);
                    }
                    if (actBeans.size()>0){
                        updateOtherActionViews();
                    }else{
                        otherActTip.setVisibility(View.GONE);
                        actView.setVisibility(View.GONE);
                    }
                }else{
                    otherActTip.setVisibility(View.GONE);
                    actView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        delayScorll();
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

    private class DelayScorllTime implements Runnable{
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

    private void updateOtherActionViews(){
        otherActTip.setVisibility(View.VISIBLE);
        actView.setVisibility(View.VISIBLE);
        actView.removeAllViews();
        for (int i=0;i<actBeans.size();i++) {
            OtherActBean item=actBeans.get(i);
            View convertView =LayoutInflater.from(this).inflate(
                    R.layout.other_act_item, null);
            TextView titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
            TextView subTitle=(TextView)convertView.findViewById(R.id.subTitle);
            TextView taoziNums = (TextView) convertView.findViewById(R.id.taoziNums);
            View subLayout = convertView.findViewById(R.id.subLayout);
            ImageView avatar_iv = (ImageView) convertView
                    .findViewById(R.id.avatar_iv);
            View bottomLineView=convertView.findViewById(R.id.bottomLineView);

            if (actBeans.size()-1==i){
                bottomLineView.setVisibility(View.GONE);
            }
            titleTxt.setText(item.getActTitle());
            imageLoader.loadImage(item.getShowCoverUrl(), avatar_iv, true);
            if ("1".equals(item.getActNature())){//竞拍约
                subLayout.setVisibility(View.VISIBLE);
                subTitle.setVisibility(View.GONE);
                taoziNums.setText(ConstTaskTag.getIntPrice(item.getPrice()));
            }else if ("2".equals(item.getActNature())){//直接约
                subLayout.setVisibility(View.VISIBLE);
                subTitle.setVisibility(View.GONE);
                taoziNums.setText(ConstTaskTag.getIntPrice(item.getPrice()));
            }else if ("3".equals(item.getActNature())){//送礼约
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

    private void updateGiftViews(List<GiftPresenter> giftPresenters) {
        slPhbView.setVisibility(View.VISIBLE);
        giftView.setVisibility(View.VISIBLE);
        giftView.removeAllViews();
        for (GiftPresenter item:giftPresenters){
           View convertView =LayoutInflater.from(this).inflate(
                    R.layout.gift_history_item, null);
            TextView lqText=(TextView)convertView.findViewById(R.id.lqText);
            TextView giftTimer = (TextView) convertView.findViewById(R.id.giftTimer);
            TextView giftNumText=(TextView)convertView.findViewById(R.id.giftNumText);
            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            TextView sexAge = (TextView) convertView.findViewById(R.id.sexAge);
            ImageView sexIcon = (ImageView) convertView
                    .findViewById(R.id.sexIcon);
            ImageView giftImage=(ImageView)convertView.findViewById(R.id.giftImage);
            View sex_age_bg =convertView
                    .findViewById(R.id.sex_age_bg);
            View giftTextFlag=convertView.findViewById(R.id.giftTextFlag);
            View giftViewSub=convertView.findViewById(R.id.giftView);
            View noGiftView=convertView.findViewById(R.id.noGiftView);
            View lvQuButton=convertView.findViewById(R.id.lvQuButton);
            CircularImage avatar_iv=(CircularImage)convertView.findViewById(R.id.avatar_iv);
            View bottomLineView=convertView.findViewById(R.id.bottomLineView);

            if (giftPresenters.size()==1){
                bottomLineView.setVisibility(View.GONE);
            }

            Presenter presenter=item.getPresenter();

            imageLoader.loadImage(presenter.getUserIcon(), avatar_iv, true);
            avatar_iv.setOnClickListener(new IntoPersonalDeltail(presenter));
            userName.setText(presenter.getUserNick());
            String sexStr =presenter.getGender();
            if (Constants.SEX_WOMAN.equals(sexStr)) {
                sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
                sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
                sexAge.setText(presenter.getAge());
            } else if (Constants.SEX_MAN.equals(sexStr)) {
                sexIcon.setImageResource(R.drawable.sg_man_light_icon);
                sexAge.setText(presenter.getAge());
                sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
            }

            if (!TextUtils.isEmpty(item.getGiftId())&&!"null".equals(item.getGiftId())){
                giftViewSub.setVisibility(View.VISIBLE);
                noGiftView.setVisibility(View.GONE);
                giftDatas=CacheService.getInstance().getCacheGiftDatas(ConstTaskTag.CACHE_GIFT_DATAS);
                if (giftDatas != null) {
                    for (GiftBean itemq:giftDatas){
                        if (item.getGiftId().equals(itemq.getId())){
                            imageLoader.loadOrigaImage(itemq.getShowUrl(), giftImage, true);
                            break;
                        }
                    }
                }
                giftNumText.setText("X".concat(item.getGiftNums()));
                giftTextFlag.setVisibility(View.VISIBLE);
            }else{
                giftTextFlag.setVisibility(View.GONE);
                giftViewSub.setVisibility(View.GONE);
                noGiftView.setVisibility(View.VISIBLE);
            }
            giftTimer.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getSendGiftTime())));

            if (publihserId.equals(UserPreferencesUtil.getUserId(this))){
                if (isLvYunAialable){
                    lvQuButton.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(hireUserId)){
                        if (presenter.getUserId().equals(hireUserId)){
                            lqText.setText("已录用");
                        }
                        lqText.setTextColor(getResources().getColor(R.color.gray));
                        lvQuButton.setBackgroundResource(R.drawable.shape_rect_gray);
                    }else{
                        lvQuButton.setOnClickListener(new LuQuActionListener(presenter));
                    }
                }else{
                    lvQuButton.setVisibility(View.GONE);
                }
            }else{
                lvQuButton.setVisibility(View.GONE);
            }
            giftView.addView(convertView);
        }
        if (giftView.getChildCount()>=3){
            View convertView =LayoutInflater.from(this).inflate(
                    R.layout.gift_more_item, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(YuePaiDetailActivity.this,SendGiftHistoryActivity.class);
                    intent.putExtra("fromFlag",fromFlag);
                    intent.putExtra("publihserId",publihserId);
                    intent.putExtra("actTitle",actTitleText.getText().toString());
                    intent.putExtra("actId",actId);
                    intent.putExtra("isLvYunAialable",isLvYunAialable);
                    startActivityForResult(intent, 1);
                }
            });
            giftView.addView(convertView);
        }
    }

    private class IntoPersonalDeltail implements View.OnClickListener{
        private Presenter presenter;
        public IntoPersonalDeltail(Presenter presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            intoPersonalAct(presenter);
        }
    }

    private void intoPersonalAct(Presenter presenter) {
        Intent intent = new Intent(this, PersonalDetailActivity.class);
        intent.putExtra("userNick", presenter.getUserNick() == null ? "" : presenter.getUserNick());
        intent.putExtra("userId", presenter.getUserId());
        startActivity(intent);
    }

    private class LuQuActionListener implements View.OnClickListener{

        private Presenter presenter;

        public LuQuActionListener(Presenter presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            luQuAction(presenter);
        }
    }

    private void luQuAction(Presenter presenter){
        currPresenter=presenter;
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("memberId",presenter.getUserId());
            obj.put("actId", actId);
            obj.put("userId",publihserId);
            obj.put("actTitle",actTitleText.getText().toString());
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_FREE_LQ);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_FREE_LQ2);
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
                giftPresenterTotal.clear();
                TransferGift item=(TransferGift)data.getSerializableExtra(Constants.COMEBACK);
                if (item!=null){
                    giftPresenterTotal.addAll(item.getVector());
                    List<GiftPresenter> tempDatas=new ArrayList<>();
                    if (giftPresenterTotal.size()>=3){
                        for (int i=0;i<3;i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }else{
                        for (int i=0;i<giftPresenterTotal.size();i++){
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }
                    updateGiftViews(tempDatas);
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
                Intent intent = new Intent(YuePaiDetailActivity.this, VideoPlayer.class);
                intent.putExtra("vidoUrl", mp4Url);
                startActivity(intent);
            }else{
                String[] tempImages=new String[infos.size()-1];
                for (int i=1;i<infos.size();i++){
                    tempImages[i-1]=infos.get(i).getUrl();
                }
                Constants.imageBrower(YuePaiDetailActivity.this, position - 2, tempImages, false);
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

    /**
     * 送礼物界面相关
     */
    private void updatePopWindownDot(int posion){
        popDotView.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < pageCount; i++) {
            View mView = inflater.inflate(R.layout.pop_dot_item, null);
            View dotView=mView.findViewById(R.id.dotView);
            if (i==posion){
                dotView.setBackgroundResource(R.drawable.shape_circle_green);
            }else{
                dotView.setBackgroundResource(R.drawable.shape_circle_white);
            }
            popDotView.addView(mView);
        }
    }

    private class SelectPersentListener implements AdapterView.OnItemClickListener {

        private int index;

        public SelectPersentListener(int index){
            this.index=index;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PopPresentBean item=mGridViewAdapters.get(index).getItem(position);
            upGetPresent(index,position,item);
        }
    }

    private void upGetPresent(int index,int postion,PopPresentBean bean) {
        giftNums.setText("1");
        this.tempPresentBean=bean;
        for (int i=0;i<mGridViewAdapters.size();i++){
            if (i==index){
                mGridViewAdapters.get(i).choiceItem(postion);
            }else{
                mGridViewAdapters.get(i).defaultChoice();
            }
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mAllViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            popPager.removeView((View) object);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mAllViews.get(position),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return mAllViews.get(position);
        }
    }

    private class MyAdapter extends BaseAdapter {

        private Context mContext;
        private List<PopPresentBean> datas;
        private LayoutInflater mInflater;
        private ImageLoader imageLoader;

        private MyAdapter(Context context, List<PopPresentBean> datas) {
            this.mContext = context;
            this.datas = datas;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        }

        public int getCount() {
            return datas.size();
        }

        public PopPresentBean getItem(int position) {
            return datas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public void choiceItem(int posion){
            for (int i=0;i<datas.size();i++){
                if (i==posion){
                    datas.get(i).setHasSlect(true);
                }else{
                    datas.get(i).setHasSlect(false);
                }
            }
            notifyDataSetChanged();
        }

        public void defaultChoice(){
            for (int i=0;i<datas.size();i++){
                datas.get(i).setHasSlect(false);
            }
            notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.pop_present_item, null);
                holder = new ViewHolder();
                holder.iconOral = (TextView) convertView.findViewById(iconOral);
                holder.iconImage = (ImageView) convertView.findViewById(R.id.iconImage);
                holder.stoneNums = (TextView) convertView.findViewById(R.id.stoneNums);
                holder.iconSelect = (ImageView) convertView.findViewById(R.id.iconSelect);
                convertView.setTag(holder);
            }
            PopPresentBean item = datas.get(position);
            holder.iconOral.setText(item.getName());
            holder.stoneNums.setText(String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(item.getWorthAmount()))));
            if (item.isHasSlect()) {
                holder.iconSelect.setVisibility(View.VISIBLE);
            } else {
                holder.iconSelect.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(item.getShowUrl())) {
                imageLoader.loadOrigaImage(item.getShowUrl(), holder.iconImage, true);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iconImage, iconSelect;
        TextView iconOral, stoneNums;
    }


    @Override
    public void jianAction() {
        if (tempPresentBean!=null){
            updatePriceEditor(0);
        }else {
            Toast.makeText(this,"请先选择礼物",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendPresentAction() {
        String nums=giftNums.getText().toString().trim();
        if (!TextUtils.isEmpty(nums)){
            double templeTotalStones=Double.parseDouble(totalStone.getText().toString());
            if (leaveStones>templeTotalStones){
                sendPresent();
            }else{
                showAddMoneyDialog();
            }
        }else{
            Toast.makeText(this,"请添写礼物数量",Toast.LENGTH_SHORT).show();
        }

    }

    private void showAddMoneyDialog() {
        TwoButtonDialog dialog = new TwoButtonDialog(this, "您的余额不够是否现在充值？","以后再说","马上充值", R.style.dineDialog,
						new ButtonTwoListener() {

					@Override
					public void confrimListener() {
					}

					@Override
					public void cancelListener() {
						toPayStonePage();
					}
				});
				dialog.show();
    }

    private void toPayStonePage() {
        Intent intent = new Intent(this, PayStoneActivity.class);
        intent.putExtra("stoneValue", String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(leaveStones))));
        startActivityForResult(intent, 0);
    }

    @Override
    public void addAction() {
        if (tempPresentBean!=null){
            updatePriceEditor(1);
        }else {
            Toast.makeText(this,"请先选择礼物",Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePriceEditor(int falg) {
        String currPrice = giftNums.getText().toString().trim();
        int currPriceValue;
        if (!TextUtils.isEmpty(currPrice)) {
            currPriceValue = Integer.parseInt(currPrice);
            ;
        } else {
            currPriceValue = 0;
        }

        switch (falg) {
            case 0:
                if (currPriceValue > 1) {
                    currPriceValue = currPriceValue - 1;
                    giftNums.setText(String.valueOf(currPriceValue));
                }
                break;
            case 1:
                currPriceValue = currPriceValue + 1;
                giftNums.setText(String.valueOf(currPriceValue));
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (tempPresentBean!=null){
            String currPrice = s.toString();
            int currPriceValue;
            if (!TextUtils.isEmpty(currPrice)) {
                currPriceValue = Integer.parseInt(currPrice);
                ;
            } else {
                currPriceValue = 0;
            }
            totalStone.setText(String.valueOf(ConstTaskTag.getDoublePrice(String.valueOf(tempPresentBean.getWorthAmount()* currPriceValue)) ));
        }else {
            Toast.makeText(this,"请先选择礼物",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
