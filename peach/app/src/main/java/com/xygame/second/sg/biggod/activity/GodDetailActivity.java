package com.xygame.second.sg.biggod.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stevenhu.android.phone.bean.ADInfo;
import com.stevenhu.android.phone.utils.ViewFactory;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.utils.ActionsFilter;
import com.xygame.second.sg.xiadan.activity.XiaDanDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.ChatActivity;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lib.BaseViewPager;
import lib.CycleViewPagerHandler;

public class GodDetailActivity extends SGBaseActivity implements OnClickListener, ViewPager.OnPageChangeListener {
    /**
     * 公用变量部分
     */
    private TextView titleName, userName, sexAge, loactionTex, godAppName, userLable, priceValue, acceptTime, oralText;
    private View backButton, intoPersonal, sex_age_bg, chatView, xiaDanView, bottomView, playButton, locationView,isShowVoiceView,playVoiceButton;
    private ImageView sexIcon,backgroundImage;
    private CircularImage avatar_iv, godAppIcon;
    private ImageLoader mImageLoader;
    private String userId, skillCode, mp4Url, voiceUrl,quaryUserImage,recordStatus ,coverUrl;
    private boolean noMp4=true;
    private MediaPlayer mediaPlayer;
    private  JinPaiBigTypeBean currPinLeiBean;

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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_detail_layout);
        registerLoginListener();//监听登录
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        backgroundImage=(ImageView)findViewById(R.id.backgroundImage);
        playVoiceButton=findViewById(R.id.playVoiceButton);
        isShowVoiceView=findViewById(R.id.isShowVoiceView);
        locationView = findViewById(R.id.locationView);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        intoPersonal = findViewById(R.id.intoPersonal);
        loactionTex = (TextView) findViewById(R.id.loactionTex);
        sexAge = (TextView) findViewById(R.id.sexAge);
        sexIcon = (ImageView) findViewById(R.id.sexIcon);
        sex_age_bg = findViewById(R.id.sex_age_bg);
        userName = (TextView) findViewById(R.id.userName);
        avatar_iv = (CircularImage) findViewById(R.id.avatar_iv);
        godAppIcon = (CircularImage) findViewById(R.id.godAppIcon);
        godAppName = (TextView) findViewById(R.id.godAppName);
        userLable = (TextView) findViewById(R.id.userLable);
        priceValue = (TextView) findViewById(R.id.priceValue);
        acceptTime = (TextView) findViewById(R.id.acceptTime);
        oralText = (TextView) findViewById(R.id.oralText);
        chatView = findViewById(R.id.chatView);
        xiaDanView = findViewById(R.id.xiaDanView);
        bottomView = findViewById(R.id.bottomView);
        viewPager = (BaseViewPager) findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) findViewById(R.id.layout_viewpager_indicator);
        viewPagerFragmentLayout = (FrameLayout) findViewById(R.id.layout_viewager_content);
        playButton = findViewById(R.id.playButton);
    }

    private void initListensers() {
        intoPersonal.setOnClickListener(this);
        backButton.setOnClickListener(this);
        chatView.setOnClickListener(this);
        xiaDanView.setOnClickListener(this);
        playButton.setOnClickListener(this);
        playVoiceButton.setOnClickListener(this);
        backgroundImage.setOnClickListener(this);
    }

    private void initDatas() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("大神资质");
        userId = getIntent().getStringExtra("userId");
        skillCode = getIntent().getStringExtra("skillCode");
        loadProjectDatas();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.intoPersonal) {
            Intent intent = new Intent(this, PersonalDetailActivity.class);
            intent.putExtra("userNick", getIntent().getStringExtra("userName"));
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else if (v.getId() == R.id.chatView) {
            boolean islogin1 = UserPreferencesUtil.isOnline(this);
            if (islogin1){
                ToChatBean toChatBean = new ToChatBean();
                toChatBean.setRecruitLocIndex("");
                toChatBean.setNoticeId("");
                toChatBean.setNoticeSubject(getIntent().getStringExtra("userName"));
                toChatBean.setUserIcon(quaryUserImage);
                toChatBean.setUserId(userId);
                toChatBean.setUsernick(getIntent().getStringExtra("userName"));
                toChatBean.setRecruitId("");
                Intent intent=new Intent(this, ChatActivity.class);
                intent.putExtra("toChatBean", toChatBean);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.xiaDanView) {
            boolean islogin1 = UserPreferencesUtil.isOnline(this);
            if (islogin1){
                Intent intent=new Intent(this,XiaDanDetailActivity.class);
                intent.putExtra("userIcon",quaryUserImage);
                intent.putExtra("userNick",getIntent().getStringExtra("userName"));
                intent.putExtra("userId",userId);
                intent.putExtra("bean",currPinLeiBean);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.playButton) {
            if (!TextUtils.isEmpty(mp4Url)) {
                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra("vidoUrl", mp4Url);
                startActivity(intent);
            }
        }else if (v.getId()==R.id.backgroundImage){
            if (!TextUtils.isEmpty(mp4Url)) {
                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra("vidoUrl", mp4Url);
                startActivity(intent);
            }else {
                String[] tempImages = new String[1];
                tempImages[0]=coverUrl;
                Constants.imageBrower(GodDetailActivity.this,0, tempImages, false);
            }
        }else if (v.getId()==R.id.playVoiceButton){
            if (!TextUtils.isEmpty(voiceUrl)){
                playerWorker(voiceUrl);
            }
        }
    }

    public void loadProjectDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("skillCode", skillCode);
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_GOD_DETAIL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GOD_DETAIL);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_GOD_DETAIL:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(String record) {
        if (!TextUtils.isEmpty(record)) {
            try {
                JSONObject jsonObject = new JSONObject(record);
                String order = StringUtils.getJsonValue(jsonObject, "order");
                String user = StringUtils.getJsonValue(jsonObject, "user");
                String expert = StringUtils.getJsonValue(jsonObject, "expert");
                if (!TextUtils.isEmpty(expert)) {
                    JSONObject expertJson = new JSONObject(expert);
                    String skillDesc = StringUtils.getJsonValue(expertJson, "skillDesc");
                    String skillCode = StringUtils.getJsonValue(expertJson, "skillCode");
                    String skillTitle = StringUtils.getJsonValue(expertJson, "skillTitle");
                    voiceUrl = StringUtils.getJsonValue(expertJson, "voiceUrl");
                    coverUrl = StringUtils.getJsonValue(expertJson, "coverUrl");
                    mp4Url = StringUtils.getJsonValue(expertJson, "videoUrl");
                    recordStatus = StringUtils.getJsonValue(expertJson, "recordStatus");
                    if (TextUtils.isEmpty(voiceUrl)){
                        isShowVoiceView.setVisibility(View.GONE);
                    }else{
                        isShowVoiceView.setVisibility(View.VISIBLE);
                    }
                    List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
                    for (JinPaiBigTypeBean it : typeDatas) {
                        if (it.getId().equals(skillCode)) {
                            currPinLeiBean=it;
                            godAppName.setText(it.getName());
                            List<GodLableBean> typeLable = Constants.getGodLableDatas(it.getSubStr());
                            if (typeLable != null) {
                                for (GodLableBean bean : typeLable) {
                                    if (bean.getTitleId().equals(skillTitle)) {
                                        userLable.setText(bean.getTitleName());
                                        break;
                                    }
                                }
                            }
                            mImageLoader.loadImage(it.getUrl(), godAppIcon, false);
                            break;
                        }
                    }
                    if (userId.equals(UserPreferencesUtil.getUserId(this))){
                        bottomView.setVisibility(View.GONE);
                    }else{
                        bottomView.setVisibility(View.VISIBLE);
                        bottomView.getBackground().setAlpha(50);
                    }
                    if ("1".equals(recordStatus)) {
                        xiaDanView.setVisibility(View.VISIBLE);
                    } else {
                        xiaDanView.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(skillDesc)){
                        oralText.setVisibility(View.VISIBLE);
                        oralText.setText(skillDesc);
                    }else{
                        oralText.setVisibility(View.GONE);
                    }
//                    List<BannerBean> resBannerList = new ArrayList<>();
                    if (!TextUtils.isEmpty(mp4Url)) {
                        noMp4=false;
                        playButton.setVisibility(View.VISIBLE);
//                        BannerBean item1 = new BannerBean();
//                        item1.setPicUrl(mp4Url);
//                        resBannerList.add(item1);
                    } else {
                        noMp4=true;
                        playButton.setVisibility(View.GONE);
                    }
                    mImageLoader.loadImage(coverUrl, backgroundImage, true);
//                    BannerBean item1 = new BannerBean();
//                    item1.setPicUrl(coverUrl);
//                    resBannerList.add(item1);
//                    if (resBannerList.size() > 0) {
//                        initialize(resBannerList);
//                    }
                }
                if (!TextUtils.isEmpty(user)) {
                    JSONObject userJson = new JSONObject(user);
                    String province = StringUtils.getJsonValue(userJson, "province");
                    quaryUserImage = StringUtils.getJsonValue(userJson, "userIcon");
                    String gender = StringUtils.getJsonValue(userJson, "gender");
                    String usernick = StringUtils.getJsonValue(userJson, "usernick");
                    String city = StringUtils.getJsonValue(userJson, "city");
                    String age = StringUtils.getJsonValue(userJson, "age");
                    if (StringUtils.isEmpty(city)) {
                        locationView.setVisibility(View.GONE);
                    } else {
                        locationView.setVisibility(View.VISIBLE);
                        AssetDataBaseManager.CityBean cb2 = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(city));
                        loactionTex.setText(cb2.getName());
                    }
                    userName.setText(usernick);
                    if (Constants.SEX_WOMAN.equals(gender)) {
                        sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
                        sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
                        sexAge.setText(age);
                    } else if (Constants.SEX_MAN.equals(gender)) {
                        sexIcon.setImageResource(R.drawable.sg_man_light_icon);
                        sexAge.setText(age);
                        sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
                    }
                    mImageLoader.loadImage(quaryUserImage, avatar_iv, true);
                }
                if (!TextUtils.isEmpty(order)) {
                    JSONObject orderJson = new JSONObject(order);
                    String priceId = StringUtils.getJsonValue(orderJson, "priceId");
                    String priceRate = StringUtils.getJsonValue(orderJson, "priceRate");
                    String orderCount = StringUtils.getJsonValue(orderJson, "orderCount");
                    List<PriceBean> fuFeiDatas = CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                    if (fuFeiDatas != null) {
                        for (PriceBean t : fuFeiDatas) {
                            if (t.getId().equals(priceId)) {
                                int value = (Integer.parseInt(t.getPrice()) * Integer.parseInt(priceRate)) / 100;
                                priceValue.setText(String.valueOf(value));
                                break;
                            }
                        }
                    }
                    acceptTime.setText("接单".concat(orderCount).concat("次"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playerWorker(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        unRegisterLoginListener();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    public void registerLoginListener() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterLoginListener() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                String loginUserId=UserPreferencesUtil.getUserId(GodDetailActivity.this);
                if (userId.equals(loginUserId)){
                    bottomView.setVisibility(View.GONE);
                }else{
                    bottomView.setVisibility(View.VISIBLE);
                    bottomView.getBackground().setAlpha(50);
                    if ("1".equals(recordStatus)) {
                        xiaDanView.setVisibility(View.VISIBLE);
                    } else {
                        xiaDanView.setVisibility(View.GONE);
                    }
                }
            } else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
                bottomView.setVisibility(View.VISIBLE);
                bottomView.getBackground().setAlpha(50);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (Activity.RESULT_OK != resultCode || null == data) {
                    return;
                }
//              boolean flag=data.getBooleanExtra(Constants.COMEBACK,false);
//                if (flag){
//                    finish();
//                }
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
            if (noMp4){
                String[] tempImages = new String[infos.size()];
                for (int i = 0; i < infos.size(); i++) {
                    tempImages[i] = infos.get(i).getUrl();
                }
                Constants.imageBrower(GodDetailActivity.this, position - 1, tempImages, false);
            }else {
                if (position == 1) {
                    Intent intent = new Intent(GodDetailActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("vidoUrl", mp4Url);
                    startActivity(intent);
                } else {
                    String[] tempImages = new String[infos.size() - 1];
                    for (int i = 1; i < infos.size(); i++) {
                        tempImages[i - 1] = infos.get(i).getUrl();
                    }
                    Constants.imageBrower(GodDetailActivity.this, position - 2, tempImages, false);
                }
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
        if (noMp4){
            playButton.setVisibility(View.GONE);
        }else{
            if (currentPosition == 1) {
                playButton.setVisibility(View.VISIBLE);
            } else {
                playButton.setVisibility(View.GONE);
            }
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
