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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.xygame.second.sg.comm.bean.TransferGift;
import com.xygame.second.sg.comm.inteface.ScrollViewListener;
import com.xygame.second.sg.defineview.CustomScrollView;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.jinpai.adapter.AreaShowAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.jinpai.bean.JinPaiCanYuBean;
import com.xygame.second.sg.jinpai.bean.JinPaiSmallTypeBean;
import com.xygame.second.sg.jinpai.bean.PriceStepBean;
import com.xygame.second.sg.personal.activity.PayStoneActivity;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.sendgift.activity.JinPaiMembersActivity;
import com.xygame.second.sg.sendgift.activity.YuePaiDetailActivity;
import com.xygame.second.sg.sendgift.bean.OtherActBean;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
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
import java.util.Date;
import java.util.List;

import lib.BaseViewPager;
import lib.CycleViewPagerHandler;


public class JinPaiDetailActivity extends SGBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, ScrollViewListener {

    private View backButton, rightButton, playButton, intoPersonal, sex_age_bg, bottomView, actButton, jianButton, addButton, cyActTip, otherActTip, moveToView,closeImage,locationView;
    private TextView titleName, bigTypeTex, userName, sexAge, loactionTex, actOral, timeDuringText, actTitleText, rightButtonText, stoneNums, totalPersons, leftTimer, fuDuText;
    private String actId, leftTimerStr, currentHightestUserIcon, currentHightestUserId, currentTotoalPersons="0", currHightestPrice, startPrice;
    private ImageView sexIcon;
    private CustomScrollView scrollView;
    private CircularImage avatar_iv, topMan;
    private String mp4Url, publihserId;
    private ImageLoader imageLoader;
    private Button showPriceButton;
    private EditText jpPriceText;
    private LinearLayout dangDateLayout, cyXiangQing, actView;
    private GridView areaGrid;
    private List<OtherActBean> actBeans = new ArrayList<>();
    private long hours;
    private long minutes;
    private long seconds;
    private long diff;
    private long days;
    private boolean isJinpai = true;
    private List<PriceStepBean> priceStepDatas;
    private PriceStepBean currPriceStepBean;
    private List<JinPaiCanYuBean> giftPresenterTotal;
    private static Animation alphaIn, alphaOut;
    private String actStatus;//5和6关闭
    private int movingPostion=0;

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
            if (msg.what == 1) {
                diff = diff - 1000;
                getShowTime();
                if (diff > 0) {
                    Message message = mHandler.obtainMessage(1);
                    mHandler.sendMessageDelayed(message, 1000);
                } else {
                    isJinpai = false;
                    leftTimer.setText("竞拍已截止");
                }
            }
            if (msg.what==2){
                movingPostion=moveToView.getTop();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jing_pai_detail_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        locationView=findViewById(R.id.locationView);
        closeImage=findViewById(R.id.closeImage);
        moveToView = findViewById(R.id.moveToView);
        scrollView = (CustomScrollView) findViewById(R.id.scrollView);
        otherActTip = findViewById(R.id.otherActTip);
        cyActTip = findViewById(R.id.cyActTip);
        addButton = findViewById(R.id.addButton);
        jianButton = findViewById(R.id.jianButton);
        jpPriceText = (EditText) findViewById(R.id.jpPriceText);
        bottomView = findViewById(R.id.bottomView);
        actButton = findViewById(R.id.actButton);
        actTitleText = (TextView) findViewById(R.id.actTitleText);
        stoneNums = (TextView) findViewById(R.id.stoneNums);
        timeDuringText = (TextView) findViewById(R.id.timeDuringText);
        dangDateLayout = (LinearLayout) findViewById(R.id.dangDateLayout);
        actOral = (TextView) findViewById(R.id.actOral);
        loactionTex = (TextView) findViewById(R.id.loactionTex);
        sexAge = (TextView) findViewById(R.id.sexAge);
        sexIcon = (ImageView) findViewById(R.id.sexIcon);
        sex_age_bg = findViewById(R.id.sex_age_bg);
        userName = (TextView) findViewById(R.id.userName);
        avatar_iv = (CircularImage) findViewById(R.id.avatar_iv);
        topMan = (CircularImage) findViewById(R.id.topMan);
        intoPersonal = findViewById(R.id.intoPersonal);
        showPriceButton = (Button) findViewById(R.id.comfimButton);
        totalPersons = (TextView) findViewById(R.id.totalPersons);
        titleName = (TextView) findViewById(R.id.titleName);
        leftTimer = (TextView) findViewById(R.id.leftTimer);
        fuDuText = (TextView) findViewById(R.id.fuDuText);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        playButton = findViewById(R.id.playButton);
        bigTypeTex = (TextView) findViewById(R.id.bigTypeTex);
        viewPager = (BaseViewPager) findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) findViewById(R.id.layout_viewpager_indicator);
        viewPagerFragmentLayout = (FrameLayout) findViewById(R.id.layout_viewager_content);
        areaGrid = (GridView) findViewById(R.id.areaGrid);
        cyXiangQing = (LinearLayout) findViewById(R.id.cyXiangQing);
        actView = (LinearLayout) findViewById(R.id.actView);
    }

    private void initDatas() {
        alphaIn = AnimationUtils.loadAnimation(this,
                R.anim.alpha_in);
        alphaOut = AnimationUtils.loadAnimation(this,
                R.anim.alpha_out);
        giftPresenterTotal = new ArrayList<>();
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("活动详情");
        actId = getIntent().getStringExtra("actId");
        publihserId = getIntent().getStringExtra("userId");
        bottomView.setVisibility(View.GONE);
        if (publihserId.equals(UserPreferencesUtil.getUserId(this))) {
            titleName.setText("活动管理");
        } else {
            titleName.setText("活动详情");
        }
        priceStepDatas = CacheService.getInstance().getCachePriceStep(ConstTaskTag.CACHE_PRICE_STEP);
        List<JinPaiBigTypeBean> vector = CacheService.getInstance().getCacheActDatas(ConstTaskTag.CACHE_ACT_TYPE);
        if (vector != null) {
            if (vector.size() > 0) {
                if (priceStepDatas == null) {
                    getPriceStep();
                } else {
                    requestActData();
                }
            } else {
                requestActType();
            }
        } else {
            requestActType();
        }

    }

    @Override
    protected void onStart() {
        ThreadPool.getInstance().excuseThread(new CaculaMoving());
        super.onStart();
    }

    private class CaculaMoving implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                mHandler.sendEmptyMessage(2);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void getPriceStep() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            item.setData(object);
            item.setServiceURL(ConstTaskTag.QUEST_PRICE_STEP);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_PRICE_STEP);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void requestActType() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("ownerType", 1);
            item.setData(object);
            item.setServiceURL(ConstTaskTag.QUEST_ACT_TYPE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_TYPE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        intoPersonal.setOnClickListener(this);
        actButton.setOnClickListener(this);
        jianButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        scrollView.setScrollViewListener(this);
        topMan.setOnClickListener(this);
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
            case R.id.comfimButton:
                if (isJinpai) {
                    boolean islogin = UserPreferencesUtil.isOnline(this);
                    if (islogin) {
                        if (!publihserId.equals(UserPreferencesUtil.getUserId(this))) {
                            if (isGo()) {
                                commitJinPaiAction();
                            }
                        } else {
                            Toast.makeText(this, "本人不能参与竞拍", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "竞拍已截止", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.playButton:
                if (mp4Url != null) {
                    Intent intent = new Intent(JinPaiDetailActivity.this, VideoPlayer.class);
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
            case R.id.topMan:
                Intent intent2 = new Intent(this, PersonalDetailActivity.class);
                intent2.putExtra("userNick","");
                intent2.putExtra("userId", currentHightestUserId);
                startActivity(intent2);
                break;
            case R.id.actButton:
                scrollView.roreydiuAotuScroll(movingPostion);
//                bottomView.startAnimation(alphaOut);
//                bottomView .setVisibility(View.GONE);
                break;
            case R.id.jianButton:
                updatePriceEditor(0);
                break;
            case R.id.addButton:
                updatePriceEditor(1);
                break;
        }
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

    private void updatePriceEditor(int falg) {
        String currPrice = jpPriceText.getText().toString().trim();
        int currPriceValue;
        if (!TextUtils.isEmpty(currPrice)) {
            currPriceValue = Integer.parseInt(currPrice);
            ;
        } else {
            currPriceValue = 0;
        }

        switch (falg) {
            case 0:
                if (currPriceValue > currPriceStepBean.getStep()) {
                    currPriceValue = currPriceValue - currPriceStepBean.getStep();
                    jpPriceText.setText(String.valueOf(currPriceValue));
                }
                break;
            case 1:
                currPriceValue = currPriceValue + currPriceStepBean.getStep();
                jpPriceText.setText(String.valueOf(currPriceValue));
                break;
        }
    }

    private Boolean isGo() {
        String currNewPrice = jpPriceText.getText().toString().trim();
        int tempCount = Integer.parseInt(currHightestPrice);
        int currNewPriceValue = Integer.parseInt(currNewPrice);
        if ("0".equals(currentTotoalPersons)){
            if (currNewPriceValue < tempCount) {
                Toast.makeText(this, "出价不能小于当前价格", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if (currNewPriceValue <= tempCount) {
            Toast.makeText(this, "出价必须大于当前价格", Toast.LENGTH_SHORT).show();
            return false;
        }
        int addPrice = currNewPriceValue - tempCount;
        int stepInt = currPriceStepBean.getStep();
        if (addPrice % stepInt != 0) {
            Toast.makeText(this, "出价金额加价幅度必须是当前加价幅度的倍数", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtils.isEmpty(currentHightestUserId)) {
            if (currentHightestUserId.equals(UserPreferencesUtil.getUserId(this))) {
                Toast.makeText(this, "您不能连续竞拍", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void commitJinPaiAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("actId", actId);

            object.put("actTitle", actTitleText.getText().toString());
            object.put("userId", publihserId);
            object.put("bidPrice", Integer.parseInt(jpPriceText.getText().toString().trim())*100);

            item.setData(object);
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_ACT_CURR_BIDER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_CURR_BIDER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_JINPAI_CLOSE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JINPAI_CLOSE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
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
        item.setServiceURL(ConstTaskTag.QUEST_ACT_DETAIL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_DETAIL);
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
            case ConstTaskTag.QUERY_PRICE_STEP:
                if ("0000".equals(data.getCode())) {
                    parseStepValues(data.getRecord());
                    requestActData();
                }
                break;
            case ConstTaskTag.QUERY_ACT_TYPE:
                if ("0000".equals(data.getCode())) {
                    if (priceStepDatas == null) {
                        getPriceStep();
                    } else {
                        requestActData();
                    }
                    parseTypeDatas(data.getRecord());
                }
                break;
            case ConstTaskTag.QUERY_ACT_DETAIL:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else if ("7010".equals(data.getCode())){
                    showWaringTip("抱歉，该活动违规已被平台关闭");
                }else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_ACT_CURR_BIDER:
                if ("0000".equals(data.getCode())) {
                    currHightestPrice = jpPriceText.getText().toString().trim();
                    updateJinPaiSubView();
                    Toast.makeText(this, "竞拍成功", Toast.LENGTH_SHORT).show();
                }else if ("7004".equals(data.getCode())){
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    jinPai7004Act(data.getRecord());
                }else if ("6012".equals(data.getCode())) {
                    jinPai6012Act("余额不足", data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_JINPAI_CLOSE:
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

    private void updateJinPaiSubView() {
        if (TextUtils.isEmpty(currentTotoalPersons)) {
            currentTotoalPersons = "0";
        }
        currentTotoalPersons = String.valueOf(Integer.parseInt(currentTotoalPersons) + 1);
        currentHightestUserId = UserPreferencesUtil.getUserId(this);
        currentHightestUserIcon = UserPreferencesUtil.getHeadPic(this);
        stoneNums.setText(currHightestPrice);
        totalPersons.setText(currentTotoalPersons);
        imageLoader.loadImage(currentHightestUserIcon, topMan, false);

        int tempCount = Integer.parseInt(currHightestPrice);

        for (PriceStepBean priceStepBean : priceStepDatas) {
            if (priceStepBean.getRangeMax() == -1) {
                if (tempCount >= priceStepBean.getRangeMin()) {
                    currPriceStepBean = priceStepBean;
                    fuDuText.setText(String.valueOf(priceStepBean.getStep()));
                    break;
                }
            } else {
                if (tempCount >= priceStepBean.getRangeMin() && tempCount < priceStepBean.getRangeMax()) {
                    currPriceStepBean = priceStepBean;
                    fuDuText.setText(String.valueOf(priceStepBean.getStep()));
                    break;
                }
            }
        }

        JinPaiCanYuBean item = new JinPaiCanYuBean();
        item.setBidTime(String.valueOf(System.currentTimeMillis()));
        item.setBidPrice(String.valueOf(Integer.parseInt(currHightestPrice)*100));
        Presenter item1 = new Presenter();
        item1.setAge(UserPreferencesUtil.getUserAge(this));
        item1.setGender(UserPreferencesUtil.getSex(this));
        item1.setUserIcon(UserPreferencesUtil.getHeadPic(this));
        item1.setUserId(UserPreferencesUtil.getUserId(this));
        item1.setUserNick(UserPreferencesUtil.getUserNickName(this));
        item.setPresenter(item1);
        giftPresenterTotal.add(0, item);
        List<JinPaiCanYuBean> tempDatas = new ArrayList<>();
        if (giftPresenterTotal.size()<3){
            for (int i = 0; i <giftPresenterTotal.size(); i++) {
                tempDatas.add(giftPresenterTotal.get(i));
            }
        }else{
            for (int i = 0; i < 3; i++) {
                tempDatas.add(giftPresenterTotal.get(i));
            }
        }
        updateGiftViews(tempDatas);
    }

    private void jinPai6012Act(String tip, final String record) {
        TwoButtonDialog dialog = new TwoButtonDialog(this, tip, "以后再说", "前往充值", R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                    }

                    @Override
                    public void cancelListener() {
                        Intent intent = new Intent(JinPaiDetailActivity.this, PayStoneActivity.class);
                        intent.putExtra("stoneValue", String.valueOf(ConstTaskTag.getDoublePrice(record)));
                        startActivityForResult(intent, 0);
                    }
                });
        dialog.show();
    }

    private void parseStepValues(String record) {
        try {
            if (!TextUtils.isEmpty(record) && !"[]".equals(record) && !"[null]".equals(record) && !"null".equals(record)) {
                List<PriceStepBean> datas = new ArrayList<>();
                JSONArray array1 = new JSONArray(record);
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject object = array1.getJSONObject(i);
                    PriceStepBean psBean = new PriceStepBean();
                    psBean.setRangeMax(StringUtils.getJsonIntValue(object, "rangeMax"));
                    psBean.setRangeMin(StringUtils.getJsonIntValue(object, "rangeMin"));
                    psBean.setStep(StringUtils.getJsonIntValue(object, "step"));
                    datas.add(psBean);
                }
                if (datas.size() > 0) {
                    priceStepDatas = datas;
                    CacheService.getInstance().cachePriceStep(ConstTaskTag.CACHE_PRICE_STEP, datas);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject object = new JSONObject(record);

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

                //base部分解析
                String base = StringUtils.getJsonValue(object, "base");
                JSONObject object2 = new JSONObject(base);
                actOral.setText(StringUtils.getJsonValue(object2, "actDesc"));
                startPrice = ConstTaskTag.getIntPrice(StringUtils.getJsonValue(object2, "startPrice"));
                actTitleText.setText(StringUtils.getJsonValue(object2, "actTitle"));
                actStatus=StringUtils.getJsonValue(object2, "actStatus");
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
                if ("6".equals(actStatus)||"5".equals(actStatus)){//2待审核，3审核通过，4审核拒绝，5平台取消，6关闭
                    leftTimer.setText("竞拍已关闭");
                    showPriceButton.setText("竞拍已关闭");
                    showPriceButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                    closeImage.setVisibility(View.VISIBLE);
                    rightButton.setVisibility(View.GONE);
                    bottomView.setVisibility(View.GONE);
                }else{
                    if ("3".equals(actStatus)&&!CalendarUtils.isTimeGone(Long.parseLong(leftTimerStr), 0)) {
                        if (publihserId.equals(UserPreferencesUtil.getUserId(this))) {
                            rightButton.setVisibility(View.VISIBLE);
                            rightButtonText.setVisibility(View.VISIBLE);
                            rightButtonText.setText("关闭");
                            rightButton.setOnClickListener(this);
                        }else{
                            bottomView.setVisibility(View.VISIBLE);
                            bottomView.getBackground().setAlpha(50);
                        }
                        startWorkTimer();
                        showPriceButton.setOnClickListener(this);
                    } else {
                        leftTimer.setText("竞拍已截止");
                        showPriceButton.setText("竞拍已截止");
                        showPriceButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
                        bottomView.setVisibility(View.GONE);
                        rightButton.setVisibility(View.GONE);
                    }
                }
                String holdTime = StringUtils.getJsonValue(object2, "holdTime");
                switch (Integer.parseInt(holdTime)) {
                    case 1:
                        timeDuringText.setText("1小时");
                        break;
                    case 2:
                        timeDuringText.setText("2小时");
                        break;
                    case 4:
                        timeDuringText.setText("半天（4小时）");
                        break;
                    case 8:
                        timeDuringText.setText("全天（8小时）");
                        break;
                }

                //数量和拍价的解析
                String bid = StringUtils.getJsonValue(object, "bid");
                if (!TextUtils.isEmpty(bid)) {
                    JSONObject object1 = new JSONObject(bid);
                    currHightestPrice =ConstTaskTag.getIntPrice( StringUtils.getJsonValue(object1, "bidPrice"));
                    currentTotoalPersons = StringUtils.getJsonValue(object1, "totalBidCount");
                    currentHightestUserId = StringUtils.getJsonValue(object1, "userId");
                    currentHightestUserIcon = StringUtils.getJsonValue(object1, "userIcon");
                    stoneNums.setText(currHightestPrice);
                    totalPersons.setText(currentTotoalPersons);
                    imageLoader.loadImage(currentHightestUserIcon, topMan, false);
                } else {
                    currHightestPrice = startPrice;
                    stoneNums.setText(currHightestPrice);
                }

                jpPriceText.setText(currHightestPrice);
                int tempCount = Integer.parseInt(currHightestPrice);

                if (priceStepDatas != null) {
                    for (PriceStepBean priceStepBean : priceStepDatas) {
                        if (priceStepBean.getRangeMax() == -1) {
                            if (tempCount >= priceStepBean.getRangeMin()) {
                                currPriceStepBean = priceStepBean;
                                fuDuText.setText(String.valueOf(priceStepBean.getStep()));
                                break;
                            }
                        } else {
                            if (tempCount >= priceStepBean.getRangeMin() && tempCount < priceStepBean.getRangeMax()) {
                                currPriceStepBean = priceStepBean;
                                fuDuText.setText(String.valueOf(priceStepBean.getStep()));
                                break;
                            }
                        }
                    }
                }

                //档期
                String schedules = StringUtils.getJsonValue(object, "schedules");
                if (ConstTaskTag.isTrueForArrayObj(schedules)) {
                    JSONArray array1 = new JSONArray(schedules);
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject jsonObject=array1.getJSONObject(i);
                        if (i==0){
                            switch (StringUtils.getJsonIntValue(jsonObject,"timeIntervalType")) {
                                case 1:
                                    View view = LayoutInflater.from(this).inflate(R.layout.jing_pai_schedle_item, null);
                                    TextView schedleName = (TextView) view.findViewById(R.id.schedleName);
                                    schedleName.setText("未来三天");
                                    dangDateLayout.addView(view);
                                    break;
                                case 2:
                                    View view1 = LayoutInflater.from(this).inflate(R.layout.jing_pai_schedle_item, null);
                                    TextView schedleName1 = (TextView) view1.findViewById(R.id.schedleName);
                                    schedleName1.setText("未来一周");
                                    dangDateLayout.addView(view1);
                                    break;
                                case 3:
                                    View view2 = LayoutInflater.from(this).inflate(R.layout.jing_pai_schedle_item, null);
                                    TextView schedleName2 = (TextView) view2.findViewById(R.id.schedleName);
                                    schedleName2.setText("未来一个月");
                                    dangDateLayout.addView(view2);
                                    break;
                            }
                        }

                        switch (StringUtils.getJsonIntValue(jsonObject,"timeType")) {
                            case 1:
                                View view = LayoutInflater.from(this).inflate(R.layout.jing_pai_schedle_item, null);
                                TextView schedleName = (TextView) view.findViewById(R.id.schedleName);
                                schedleName.setText("上午（9：00-12:00）");
                                dangDateLayout.addView(view);
                                break;
                            case 2:
                                View view1 = LayoutInflater.from(this).inflate(R.layout.jing_pai_schedle_item, null);
                                TextView schedleName1 = (TextView) view1.findViewById(R.id.schedleName);
                                schedleName1.setText("下午（12：00-18:00）");
                                dangDateLayout.addView(view1);
                                break;
                            case 3:
                                View view2 = LayoutInflater.from(this).inflate(R.layout.jing_pai_schedle_item, null);
                                TextView schedleName2 = (TextView) view2.findViewById(R.id.schedleName);
                                schedleName2.setText("晚上（18：00-22:00）");
                                dangDateLayout.addView(view2);
                                break;
                        }
                    }
                } else {
                    dangDateLayout.setVisibility(View.GONE);
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
                    areaGrid.setVisibility(View.GONE);
                }

                //参与详情
                String bidFlows = StringUtils.getJsonValue(object, "bidFlows");
                if (!TextUtils.isEmpty(bidFlows) && !"[]".equals(bidFlows) && !"null".equals(bidFlows) && !"[null]".equals(bidFlows)) {
                    List<JinPaiCanYuBean> giftPresenters = new ArrayList<>();
                    JSONArray array2 = new JSONArray(bidFlows);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        JinPaiCanYuBean item = new JinPaiCanYuBean();
                        item.setBidPrice(StringUtils.getJsonValue(object1, "bidPrice"));
                        item.setBidTime(StringUtils.getJsonValue(object1, "bidTime"));
                        String participator = StringUtils.getJsonValue(object1, "bidUser");
                        JSONObject object4 = new JSONObject(participator);
                        Presenter item1 = new Presenter();
                        item1.setAge(StringUtils.getJsonValue(object4, "age"));
                        item1.setGender(StringUtils.getJsonValue(object4, "gender"));
                        item1.setUserIcon(StringUtils.getJsonValue(object4, "userIcon"));
                        item1.setUserId(StringUtils.getJsonValue(object4, "userId"));
                        item1.setUserNick(StringUtils.getJsonValue(object4, "usernick"));
                        item.setPresenter(item1);
                        giftPresenters.add(item);
                    }
                    if (giftPresenters.size() > 0) {
                        giftPresenterTotal.addAll(giftPresenters);
                        updateGiftViews(giftPresenters);
                    } else {
                        cyActTip.setVisibility(View.GONE);
                        cyXiangQing.setVisibility(View.GONE);
                    }
                } else {
                    cyActTip.setVisibility(View.GONE);
                    cyXiangQing.setVisibility(View.GONE);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        delayScorll();
    }

    private void jinPai7004Act(String record) {
        if (!TextUtils.isEmpty(record)){
            try {
                JSONObject object1=new JSONObject(record);
                currHightestPrice = ConstTaskTag.getIntPrice(StringUtils.getJsonValue(object1, "bidPrice"));
                currentTotoalPersons = StringUtils.getJsonValue(object1, "totalBidCount");
                currentHightestUserId = StringUtils.getJsonValue(object1, "userId");
                currentHightestUserIcon = StringUtils.getJsonValue(object1, "userIcon");
                stoneNums.setText(currHightestPrice);
                totalPersons.setText(currentTotoalPersons);
                imageLoader.loadImage(currentHightestUserIcon, topMan, false);

                jpPriceText.setText(currHightestPrice);
                int tempCount = Integer.parseInt(currHightestPrice);

                if (priceStepDatas != null) {
                    for (PriceStepBean priceStepBean : priceStepDatas) {
                        if (priceStepBean.getRangeMax() == -1) {
                            if (tempCount >= priceStepBean.getRangeMin()) {
                                currPriceStepBean = priceStepBean;
                                fuDuText.setText(String.valueOf(priceStepBean.getStep()));
                                break;
                            }
                        } else {
                            if (tempCount >= priceStepBean.getRangeMin() && tempCount < priceStepBean.getRangeMax()) {
                                currPriceStepBean = priceStepBean;
                                fuDuText.setText(String.valueOf(priceStepBean.getStep()));
                                break;
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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


    private void updateGiftViews(List<JinPaiCanYuBean> giftPresenters) {
        cyActTip.setVisibility(View.VISIBLE);
        cyXiangQing.setVisibility(View.VISIBLE);
        cyXiangQing.removeAllViews();
        for (JinPaiCanYuBean item : giftPresenters) {
            View convertView = LayoutInflater.from(this).inflate(
                    R.layout.jing_pai_members_item, null);
            CircularImage avatar_iv = (CircularImage) convertView.findViewById(R.id.avatar_iv);
            TextView fpValueText = (TextView) convertView.findViewById(R.id.fpValueText);
            TextView giftTimer = (TextView) convertView.findViewById(R.id.giftTimer);
            View bottomLineView = convertView.findViewById(R.id.bottomLineView);
            if (giftPresenters.size() == 1) {
                bottomLineView.setVisibility(View.GONE);
            }
            Presenter presenter = item.getPresenter();
            imageLoader.loadImage(presenter.getUserIcon(), avatar_iv, true);
            avatar_iv.setOnClickListener(new IntoPersonalDeltail(presenter));
            giftTimer.setText(CalendarUtils.getHenGongDateDis(Long.parseLong(item.getBidTime())));
            fpValueText.setText(ConstTaskTag.getIntPrice(item.getBidPrice()));
            cyXiangQing.addView(convertView);
        }
        if (cyXiangQing.getChildCount() >= 3) {
            View convertView = LayoutInflater.from(this).inflate(
                    R.layout.gift_more_item, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(JinPaiDetailActivity.this, JinPaiMembersActivity.class);
                    intent.putExtra("actId", actId);
                    startActivityForResult(intent, 1);
                }
            });
            cyXiangQing.addView(convertView);
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
        intent.putExtra("userNick",presenter.getUserNick()==null?"":presenter.getUserNick());
        intent.putExtra("userId", presenter.getUserId());
        startActivity(intent);
    }

    private void updateOtherActionViews() {
        actView.removeAllViews();
        for (int i=0;i<actBeans.size();i++) {
            OtherActBean item=actBeans.get(i);
            View convertView = LayoutInflater.from(this).inflate(
                    R.layout.other_act_item, null);
            TextView titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
            TextView subTitle = (TextView) convertView.findViewById(R.id.subTitle);
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
                giftPresenterTotal.clear();
                TransferGift item = (TransferGift) data.getSerializableExtra(Constants.COMEBACK);
                if (item != null) {
                    giftPresenterTotal.addAll(item.getJinPaiCanYuBeans());
                    List<JinPaiCanYuBean> tempDatas = new ArrayList<>();
                    if (giftPresenterTotal.size()<3){
                        for (int i = 0; i <giftPresenterTotal.size(); i++) {
                            tempDatas.add(giftPresenterTotal.get(i));
                        }
                    }else{
                        for (int i = 0; i < 3; i++) {
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
//            isCycle();
            if (position==1){
                Intent intent = new Intent(JinPaiDetailActivity.this, VideoPlayer.class);
                intent.putExtra("vidoUrl", mp4Url);
                startActivity(intent);
            }else{
                String[] tempImages=new String[infos.size()-1];
                for (int i=1;i<infos.size();i++){
                    tempImages[i-1]=infos.get(i).getUrl();
                }
                Constants.imageBrower(JinPaiDetailActivity.this, position - 2, tempImages, false);
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

    private void startWorkTimer() {
        getTime();
        Message message = mHandler.obtainMessage(1);
        mHandler.sendMessageDelayed(message, 1000);
    }

    private void getShowTime() {
        days = diff / (1000 * 60 * 60 * 24);
        hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60))
                / (1000 * 60);
        seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        updateTimer();
    }

    private void updateTimer() {
        StringBuffer sb = new StringBuffer();
        if (days != 0) {
            String dayStr = String.valueOf(days).concat("天");
            sb.append(dayStr);
        }
        if (hours != 0) {
            String hourStr = String.valueOf(hours).concat("小时");
            sb.append(hourStr);
        }
        if (minutes != 0) {
            String minStr = String.valueOf(minutes).concat("分");
            sb.append(minStr);
        }
        if (seconds != 0) {
            String minStr = String.valueOf(seconds).concat("秒");
            sb.append(minStr);
        }
        leftTimer.setText(sb.toString().replace("-", ""));
    }

    private void getTime() {
        try {
            Date d1 = new Date(Long.parseLong(leftTimerStr));
            Date d2 = new Date();
            diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long limitTime = diff / 1000;
            days = limitTime / (24 * 60 * 60);
            hours = (limitTime % (24 * 60 * 60)) / (60 * 60);
            minutes = ((limitTime % (24 * 60 * 60)) % (60 * 60)) / 60;
            seconds = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
            updateTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy) {
//        bottomView.startAnimation(alphaIn);
//        bottomView .setVisibility(View.VISIBLE);
//        if (scrollView == scrollView1) {
//            scrollView2.scrollTo(x, y);
//        } else if (scrollView == scrollView2) {
//            scrollView1.scrollTo(x, y);
//        }
    }
}
