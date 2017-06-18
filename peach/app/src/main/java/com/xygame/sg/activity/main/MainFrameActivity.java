/*
 * 文 件 名:  MainFrameActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月2日
 */
package com.xygame.sg.activity.main;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xy.im.util.ExitAppAction;
import com.xy.im.util.LeaveDiscGroup;
import com.xygame.second.sg.Group.GroupNewNoticeEngeer;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.Group.bean.GroupNoticeMessageBean;
import com.xygame.second.sg.comm.inteface.TransActMain;
import com.xygame.second.sg.personal.activity.MeFragment;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.utils.AuthTest;
import com.xygame.second.sg.utils.Contant;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.second.sg.utils.NotificationManagerHelper;
import com.xygame.second.sg.xiadan.XiaDanFragment;
import com.xygame.second.sg.xiadan.XiaDanQiangFragment;
import com.xygame.second.sg.xiadan.activity.WaitGodActivity;
import com.xygame.second.sg.xiadan.activity.WaitGodTwoActivity;
import com.xygame.second.sg.xiadan.activity.godlist.GodListChoice;
import com.xygame.second.sg.xiadan.bean.GodQiangDanRebackBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGMainBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ExitAppActivity;
import com.xygame.sg.activity.commen.IndexActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.adapter.main.FragmentAdapter;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.NewsGodEngine;
import com.xygame.sg.im.OfflineMsgManager;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.im.TimerEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.service.MyPushIntentService;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.IndexViewPager;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月2日
 * @action [应用主界面]
 */
public class MainFrameActivity extends SGMainBaseActivity implements OnClickListener, OnPageChangeListener,ConnectionListener,/* UmengUpdateListener ,UmengDownloadListener,*/TransActMain,PacketListener {
    private IndexViewPager mPager;
    private FragmentAdapter<Fragment> viewPageAdapter;
    private ArrayList<Fragment> mFragments;

//    private FanErFragment newModelFragment;
    private NewsFragment groupFrament;
    private XiaDanQiangFragment noticeFragment;
    private XiaDanFragment circleFragment;
    private MeFragment personalFragment;
    private Animation mFadeInScale;
    private PushAgent mPushAgent;

    private ImageView modelIcon, noticeIcon, newsIcon, personalIcon,gropIcon;
    private TextView modelText, noticeText, newsText, personalText,gropText,nuReadNews;
    private View modelView, noticeView, sheyishi, personalView,gropView;
    private String lastLoginTime;
    private ImageView imageView_pic;
    private boolean ifJump,isRunningActivity=false;
    private long mExitTime;
    /**
     * 进度条
     */
    private ProgressBar mLeft = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sg_mainframe_activity);
        jump();
        initViews();
        initAnim();
        initListeners();
        Intent pushService = new Intent(this, MyPushIntentService.class);
        startService(pushService);
        initDatas();
        initTalkingData();
    }

    private void jump() {
        final SharedPreferences preferences = getSharedPreferences("startcount", Context.MODE_MULTI_PROCESS);
        ifJump = preferences.getBoolean("firstRun", true);
        if (ifJump) {// 判断程序第几次运行，如果是第一次运行则跳转到引导页面
            ifJump = false;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", ifJump);// 存入数据
            editor.apply();// 提交修改
            Intent intent = new Intent(MainFrameActivity.this, IndexActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateAllData();
    }


    private void initUmemSDK() {
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        mPushAgent.enable();
        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
    }

    public String getRegistrationId() {
        return mPushAgent.getRegistrationId();
    }

    private void initTalkingData() {
        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(true);
    }


    private void updateAllData() {
        XMPPUtils.reConnect(this);
        UmengUpdateAgent.setDeltaUpdate(false);//true(默认)使用增量更新，false使用全量更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        //+++++++++++++++++++++++++++++++++++++++++
        //强更部分
        UmengUpdateAgent.setUpdateAutoPopup(true);
//        UmengUpdateAgent.setUpdateListener(this);
//        UmengUpdateAgent.setDownloadListener(this);
        //+++++++++++++++++++++++++++++++++++++++++
        UmengUpdateAgent.update(this);
        if (UserPreferencesUtil.isRefreshRequest(this)) {
            SGApplication.getInstance().initBaiduLaction(new MyLocationListenner());
            if (ifJump) {
                imageView_pic.setVisibility(View.GONE);
            } else {
                imageView_pic.setVisibility(View.VISIBLE);
                imageView_pic.startAnimation(mFadeInScale);
            }
            UserPreferencesUtil.setIsRefreshRequest(MainFrameActivity.this, false);
            if (UserPreferencesUtil.isOnline(this)){
                getUnreadMessage();
//                upLoadDeviceToken();
            }else{
                if (!TextUtils.isEmpty(UserPreferencesUtil.getLoginName(this))){
                    autoLogin(0);
                }
            }
        }
    }

    private void quaryUnReadNews() {
        int totalCount = 0;
        int chatNewsCount = NewsEngine.quaryUnReadChatNews(this, UserPreferencesUtil.getUserId(this));
        int daymicNewsCount = NewsEngine.quaryUnReadDaymicNews(this, UserPreferencesUtil.getUserId(this));
        int discNewsCount = TempGroupNewsEngine.quaryUnReadChatNews(this, UserPreferencesUtil.getUserId(this));
        int noticeNewsCount=NewsEngine.quaryUnReadNoticeChatNews(this, UserPreferencesUtil.getUserId(this));
        int daymicNewsCount2 = NewsGodEngine.quaryUnReadDaymicNews(this, UserPreferencesUtil.getUserId(this));
        totalCount = chatNewsCount + daymicNewsCount+discNewsCount+noticeNewsCount+daymicNewsCount2;
        if (totalCount != 0) {
            nuReadNews.setVisibility(View.VISIBLE);
            nuReadNews.setText(String.valueOf(totalCount));
        } else {
            nuReadNews.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUmemSDK();
        TCAgent.onResume(this);
        if (UserPreferencesUtil.isOnline(this)){
            quaryUnReadNews();
            if (CacheService.getInstance().getCacheAccessToken(ConstTaskTag.CACHE_QUPAI_TOKEN)==null){
                AuthTest.getInstance().initAuth(this,Contant.APP_KEY,Contant.APP_SECRET,Contant.space);
            }
            List<BlackMemberBean> blackListDatasDatas=CacheService.getInstance().getCacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this));
            if (blackListDatasDatas==null){
                requestBlackList();
            }
        }else{
            nuReadNews.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        XMPPUtils.exitAppUpdateBean(this);
        super.onDestroy();
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPause(this);
    }

    /**
     * @action [初始化组件]
     */
    private void initViews() {
        nuReadNews=(TextView)findViewById(R.id.nuReadNews);
        imageView_pic = (ImageView) findViewById(R.id.imageView_pic);
        mPager = (IndexViewPager) findViewById(R.id.mPager);
        modelIcon = (ImageView) findViewById(R.id.modelIcon);
        noticeIcon = (ImageView) findViewById(R.id.noticeIcon);
        newsIcon = (ImageView) findViewById(R.id.newsIcon);
        personalIcon = (ImageView) findViewById(R.id.personalIcon);
        gropIcon=(ImageView)findViewById(R.id.gropIcon);
        gropText=(TextView)findViewById(R.id.gropText);
        modelText = (TextView) findViewById(R.id.modelText);
        noticeText = (TextView) findViewById(R.id.noticeText);
        newsText = (TextView) findViewById(R.id.newsText);
        personalText = (TextView) findViewById(R.id.personalText);
        modelView = findViewById(R.id.modelView);
        gropView=findViewById(R.id.gropView);
        noticeView = findViewById(R.id.noticeView);
        sheyishi = findViewById(R.id.sheyishi);
        personalView = findViewById(R.id.personalView);
    }

    /**
     * @action [初始化监听]
     */
    private void initListeners() {
        mPager.setOnPageChangeListener(this);
        modelView.setOnClickListener(this);
        gropView.setOnClickListener(this);
        noticeView.setOnClickListener(this);
        sheyishi.setOnClickListener(this);
        personalView.setOnClickListener(this);
        setListener();
    }

    /**
     * 监听事件
     */
    public void setListener() {
        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                imageView_pic.setVisibility(View.GONE);
            }
        });
    }

    /**
     * @action [初始化数据]
     */
    private void initDatas() {
        UserPreferencesUtil.setIsRefreshRequest(MainFrameActivity.this, true);
        SGApplication.getInstance().initBaiduLaction(new MyLocationListenner());
        UserPreferencesUtil.judgMentFristRun(this);
        registerBoradcastReceiver();// 监听登录
        if (UserPreferencesUtil.isOnline(this)) {
            if (SGApplication.getInstance().getConnection() == null) {
                SGApplication.getInstance().openConnection();
            }
            SGApplication.getInstance().getConnection().addConnectionListener(MainFrameActivity.this);
            PacketFilter orFilter = new PacketTypeFilter(
                    org.jivesoftware.smack.packet.Message.class);
            SGApplication.getInstance().getConnection().addPacketListener(this,orFilter);
        }
        initFragmentsDatas();
        viewPageAdapter = new FragmentAdapter<Fragment>(getSupportFragmentManager(), mFragments);
        mPager.setOffscreenPageLimit(mFragments.size());
        mPager.setAdapter(viewPageAdapter);
        mPager.setScanScroll(false);
        mPager.setCurrentItem(0, false);
        updateBottomViews(1);
    }

    private void initAnim() {
        mFadeInScale = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(2000);
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        myIntentFilter.addAction(Constants.ACTION_LOGIN_FAILTH);
        myIntentFilter.addAction(XMPPUtils.ACTION_RECONNECT_STATE);
        myIntentFilter.addAction("com.xygame.push.dynamic.message.action");
        myIntentFilter.addAction("com.xygame.exit.app.action");
        myIntentFilter.addAction("com.xygame.push.group.message.action");
        myIntentFilter.addAction("com.xygame.push.disc.group.kickout.action");
        myIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        myIntentFilter.addAction(Constants.XMPP_LOGIN_ACTION);
        myIntentFilter.addAction(Constants.NOTICE_REQUEST_GROUP);
        myIntentFilter.addAction(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
        myIntentFilter.addAction(XMPPUtils.ACTION_TO_MAINPAGE);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {
                upLoadDeviceToken();
                getUnreadMessage();
//                mPager.setCurrentItem(0,false);
            }else if (Constants.NOTICE_REQUEST_GROUP.equals(intent.getAction())){
                if (UserPreferencesUtil.isOnline(MainFrameActivity.this)){
                    if (SGApplication.getInstance().getConnection()!=null){
                        if (!SGApplication.getInstance().getConnection().isAuthenticated()){
                            ThreadPool.getInstance().excuseThread(new ReconnectXmppAct(MainFrameActivity.this));
                        }
                    }
                }
            }else if(Constants.XMPP_LOGIN_ACTION.equals(intent.getAction())){
                if (SGApplication.getInstance().getConnection() == null) {
                    SGApplication.getInstance().openConnection();
                }
                SGApplication.getInstance().getConnection().addConnectionListener(MainFrameActivity.this);
                PacketFilter orFilter = new PacketTypeFilter(
                        org.jivesoftware.smack.packet.Message.class);
                SGApplication.getInstance().getConnection().addPacketListener(MainFrameActivity.this, orFilter);
                queryUserGroups();
            } else if (Constants.ACTION_LOGIN_FAILTH.equals(intent.getAction())) {
                // 登录失败或取消登录
                mPager.setCurrentItem(0, false);
            }else if ("com.xygame.push.dynamic.message.action".equals(intent.getAction())) {
                if (UserPreferencesUtil.isOnline(MainFrameActivity.this)) {
                    getUnreadMessage();
                }
            }else if ("com.xygame.push.group.message.action".equals(intent.getAction())){
                if (UserPreferencesUtil.isOnline(MainFrameActivity.this)) {
                    String body=intent.getStringExtra("body");
                    joinGroupAction(body);
                }
            }else if ("com.xygame.exit.app.action".equals(intent.getAction())) {
                mPager.setCurrentItem(0,false);
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    UserPreferencesUtil.setIsRefreshRequest(MainFrameActivity.this, true);
                    UserPreferencesUtil.setRefract(MainFrameActivity.this, false);
                    mPager.setCurrentItem(0, false);
                    ThreadPool.getInstance().excuseThread(new ExitAppAction(MainFrameActivity.this));
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    XMPPUtils.reConnect(MainFrameActivity.this);
                }
            }else if ("com.xygame.push.disc.group.kickout.action".equals(intent.getAction())){
                if (UserPreferencesUtil.isOnline(MainFrameActivity.this)) {
                    String body=intent.getStringExtra("body");
                    disGroupKickOutAction(body);
                }
            }else if (XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION.equals(intent.getAction())){
                if (UserPreferencesUtil.isOnline(MainFrameActivity.this)) {
                    String groupName=intent.getStringExtra("groupName");
                    String groupId=intent.getStringExtra("groupId");
                    TempGroupNewsEngine.updateGroupName(MainFrameActivity.this, groupName, groupId, UserPreferencesUtil.getUserId(MainFrameActivity.this));
                }
            }else if (XMPPUtils.ACTION_RECONNECT_STATE.equals(intent.getAction())){
                if (UserPreferencesUtil.isOnline(MainFrameActivity.this)) {
                    String custom=intent.getStringExtra("custom");
                    if (!TextUtils.isEmpty(custom)){
                        parseGodParse(custom);
                    }
                }
            }else if (XMPPUtils.ACTION_TO_MAINPAGE.equals(intent.getAction())){
                mPager.setCurrentItem(0, false);
            }
        }
    };

    private void disGroupKickOutAction(String body) {
        XMPPUtils.kickOutDiscRoom(this, body);
    }

    private void joinGroupAction(String body) {
       XMPPUtils.addInDiscRoom(this, body);
    }

    private void showConfirmDiloag() {
        XMPPUtils.exitAppUpdateBean(this);
        Intent intent1 = new Intent(Constants.ACTION_LOGIN_FAILTH);
        sendBroadcast(intent1);
        Intent intent = new Intent(MainFrameActivity.this, ExitAppActivity.class);
        intent.putExtra("lastLoginTime", lastLoginTime);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.modelView) {
            mPager.setCurrentItem(0,false);
        }else if (v.getId()==R.id.gropView){
            NotificationManagerHelper.getInstance(this).getNotificationManager().cancel(0);
            mPager.setCurrentItem(2,false);
        }else if (v.getId() == R.id.noticeView) {
            mPager.setCurrentItem(0,false);
            Map kv = new HashMap();
            kv.put("统计类型", "约会");
            TCAgent.onEvent(this, "首页活动", "约会位", kv);
        } else if (v.getId() == R.id.sheyishi) {
            mPager.setCurrentItem(1, false);
        } else if (v.getId() == R.id.personalView) {
            mPager.setCurrentItem(3,false);
        }
    }

    protected void upLoadDeviceToken() {
        if (!TextUtils.isEmpty(getRegistrationId())){
            try {
                RequestBean item = new RequestBean();
                item.setIsPublic(true);
                item.setData(new JSONObject().put("deviceType", "1").put("userId", UserPreferencesUtil.getUserId(this)).put("deviceToken", getRegistrationId()));
                item.setServiceURL(ConstTaskTag.QUEST_DEVICE_TOKEN);
                ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_DEVICE_TOKEN);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void queryUserGroups(){
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            item.setData(new JSONObject());
            item.setServiceURL(ConstTaskTag.QUEST_GROUP_ALL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GROUP_ALL);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }


    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        onPageSelectedListensers(arg0);
    }

    private void initFragmentsDatas() {
        if (null == mFragments) {
            mFragments = new ArrayList<Fragment>();
        }
//        if (null == newModelFragment) {
//            newModelFragment = new FanErFragment();
//        }
        if (null==groupFrament){
            groupFrament=new NewsFragment();
        }
        if (null == noticeFragment) {
            noticeFragment = new XiaDanQiangFragment();
        }
        if (null == circleFragment) {
            circleFragment = new XiaDanFragment();
        }
        if (null == personalFragment) {
            personalFragment = new MeFragment();
        }
//        mFragments.add(newModelFragment);
        mFragments.add(circleFragment);
        mFragments.add(noticeFragment);
        mFragments.add(groupFrament);
        mFragments.add(personalFragment);
        noticeFragment.setTransToPersonalListener(this);
    }

    public void onPageSelectedListensers(int arg0) {
        switch (arg0) {
//            case 0:
//                updateBottomViews(0);
//                break;
            case 0: {
                updateBottomViews(1);
            }
            break;
            case 1: {
                boolean islogin = UserPreferencesUtil.isOnline(this);
                if (!islogin) {
                    updateBottomViews(2);
                    noticeFragment.loadActType();
                    noticeFragment.startLocation();
                }else{
                    judgXiaDanStauts();
                }
            }
            break;
            case 2:
                judgmentLoginForNews(2);
                updateBottomViews(3);
                break;
            case 3: {
                judgmentLogin(3);
                updateBottomViews(4);
            }
            break;
        }
    }

    private void judgXiaDanStauts() {
        TimerCountBean timerCountBean= TimerEngine.quaryTimerBeansByDuringLength(this, UserPreferencesUtil.getUserId(this), Constants.QIANGDAN_TIMER);
        TimerCountBean userWithDeviceRelative= TimerEngine.quaryTimerBeansByDuringLength(this, UserPreferencesUtil.getUserId(this), Constants.IS_REQUEST_QIANG_FLAG);
        if (timerCountBean!=null){
            String  createTime=timerCountBean.getStartTime();
            if (!"0".equals(createTime)){
                String holdTime=UserPreferencesUtil.getorderExpireTime(this);
                if (!CalendarUtils.isPassed10Min(holdTime,createTime)){
                    List<GodQiangDanRebackBean> qiangDanRebackBeans= CacheService.getInstance().getGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this));
                    if (qiangDanRebackBeans!=null){
                        Intent intent = new Intent(this, WaitGodTwoActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(this, WaitGodActivity.class);
                        startActivity(intent);
                    }
                    isRunningActivity=true;
                    if (userWithDeviceRelative!=null){
                        if ("1".equals(userWithDeviceRelative.getGroupId())){
                            quaryQinagDanGod();
                        }
                    }else{
                        quaryQinagDanGod();
                    }
                }else{
                    CacheService.getInstance().clearGodQiangDanRebackBeanCache();
                    updateBottomViews(2);
                    noticeFragment.loadActType();
                    noticeFragment.startLocation();
                }
            }else{
                CacheService.getInstance().clearGodQiangDanRebackBeanCache();
                updateBottomViews(2);
                noticeFragment.loadActType();
                noticeFragment.startLocation();
            }
        }else{
            isRunningActivity=false;
            if (userWithDeviceRelative!=null){
                if ("1".equals(userWithDeviceRelative.getGroupId())){
                    quaryQinagDanGod();
                }
            }else{
                quaryQinagDanGod();
            }
            updateBottomViews(2);
            noticeFragment.loadActType();
            noticeFragment.startLocation();
        }
    }

    private void judgmentLoginForNews(int index) {
        boolean islogin = UserPreferencesUtil.isOnline(this);
        if (!islogin) {
            groupFrament.clearData();
            if (!TextUtils.isEmpty(UserPreferencesUtil.getLoginName(this))){
                autoLogin(index);
            }else{
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                intent.putExtra("whereFlag", "mainMe");
                startActivity(intent);
            }
        }else{
            groupFrament.quaryData();
            groupFrament.isRquestServiceType();
        }
    }

    private void judgmentLogin(int index) {
        boolean islogin = UserPreferencesUtil.isOnline(this);
        if (!islogin) {
            if (!TextUtils.isEmpty(UserPreferencesUtil.getLoginName(this))){
                autoLogin(index);
            }else{
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                intent.putExtra("whereFlag","mainMe");
                startActivity(intent);
            }
        }else{
            personalFragment.requestData();
        }
    }

    private void updateBottomViews(int index) {
        switch (index) {
            case 0:
                gropText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                gropIcon.setBackgroundResource(R.drawable.xiaoxida);

                modelText.setTextColor(getResources().getColor(R.color.main_page_selecct_color));
                noticeText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                newsText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                personalText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));

                modelIcon.setBackgroundResource(R.drawable.sg_main_mote1);
                noticeIcon.setBackgroundResource(R.drawable.im2);
                newsIcon.setBackgroundResource(R.drawable.sg_main_new2);
                personalIcon.setBackgroundResource(R.drawable.sg_main_geren2);
                break;
            case 1:
                gropText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                gropIcon.setBackgroundResource(R.drawable.xiaoxida);

                modelText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                noticeText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                newsText.setTextColor(getResources().getColor(R.color.main_page_selecct_color));
                personalText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));

                modelIcon.setBackgroundResource(R.drawable.sg_main_mote2);
                noticeIcon.setBackgroundResource(R.drawable.im2);
                newsIcon.setBackgroundResource(R.drawable.sg_main_new1);
                personalIcon.setBackgroundResource(R.drawable.sg_main_geren2);

                break;
            case 2:
                gropText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                gropIcon.setBackgroundResource(R.drawable.xiaoxida);

                modelText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                noticeText.setTextColor(getResources().getColor(R.color.main_page_selecct_color));
                newsText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                personalText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));

                modelIcon.setBackgroundResource(R.drawable.sg_main_mote2);
                noticeIcon.setBackgroundResource(R.drawable.im1);
                newsIcon.setBackgroundResource(R.drawable.sg_main_new2);
                personalIcon.setBackgroundResource(R.drawable.sg_main_geren2);
                break;
            case 3:
                gropText.setTextColor(getResources().getColor(R.color.main_page_selecct_color));
                gropIcon.setBackgroundResource(R.drawable.xiaoxidas);

                modelText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                noticeText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                newsText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                personalText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));

                modelIcon.setBackgroundResource(R.drawable.sg_main_mote2);
                noticeIcon.setBackgroundResource(R.drawable.im2);
                newsIcon.setBackgroundResource(R.drawable.sg_main_new2);
                personalIcon.setBackgroundResource(R.drawable.sg_main_geren2);
                break;
            case 4:
                gropText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                gropIcon.setBackgroundResource(R.drawable.xiaoxida);

                modelText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                noticeText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                newsText.setTextColor(getResources().getColor(R.color.main_page_unselect_color));
                personalText.setTextColor(getResources().getColor(R.color.main_page_selecct_color));

                modelIcon.setBackgroundResource(R.drawable.sg_main_mote2);
                noticeIcon.setBackgroundResource(R.drawable.im2);
                newsIcon.setBackgroundResource(R.drawable.sg_main_new2);
                personalIcon.setBackgroundResource(R.drawable.sg_main_geren1);
                break;
            default:
                break;
        }
    }

    @Override
    public void connectionClosed() {
//        if (!UserPreferencesUtil.isRefreshRequest(this)) {
//            Toast.makeText(this,"服务器连接失败",Toast.LENGTH_SHORT).show();
//            ThreadPool.getInstance().excuseThread(new DelayTime());
//        }
    }

//    @Override
//    public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
//        switch (updateStatus) {
//            case UpdateStatus.Yes: // has update
////                Forcedialog(updateResponse);
//                break;
//            case UpdateStatus.No: // has no update
////                Toast.makeText(this, "没有更新", Toast.LENGTH_SHORT).show();
//                break;
//            case UpdateStatus.NoneWifi: // none wifi
////                Toast.makeText(this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
//                break;
//            case UpdateStatus.Timeout: // time out
////                Toast.makeText(this, "超时", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//
//    @Override
//    public void OnDownloadStart() {
//        android.os.Message message=new android.os.Message();
//        message.what=6;
//        message.arg1=0;
//        handler.sendMessage(message);
//    }
//
//    @Override
//    public void OnDownloadUpdate(int progress) {
//        android.os.Message message=new android.os.Message();
//        message.what=5;
//        message.arg1=progress;
//        handler.sendMessage(message);
//    }
//
//    @Override
//    public void OnDownloadEnd(int result, String file) {
//        handler.sendEmptyMessage(4);
//        UmengUpdateAgent.startInstall(this, new File(file));
//    }

//    protected void Forcedialog(final UpdateResponse updateResponse) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View mView=LayoutInflater.from(this).inflate(R.layout.activity_custom_update_dialog, null);
//        View comfirmButton=mView.findViewById(R.id.comfirmButton);
//        comfirmButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UmengUpdateAgent.startDownload(MainFrameActivity.this, updateResponse);
//                showIndeterDialog();
//            }
//        });
//        builder.setView(mView);
//        builder.setCancelable(false);
//        builder.create().show();
//    }

//    private void showIndeterDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View dialogView= LayoutInflater.from(this).inflate(R.layout.activity_custom_progress_mypage_left,null);
//        mLeft = (ProgressBar) dialogView.findViewById(R.id.progress_horizontal_left);
//        mLeft.setMax(100);
//        mLeft.setIndeterminate(false);
//        builder.setView(dialogView);
//        builder.setCancelable(false);
//        builder.create().show();
//    }

    class DelayTime implements Runnable {

        @Override
        public void run() {
            loadData();
            handler.sendEmptyMessage(2);
        }

        public void loadData() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectionClosedOnError(Exception arg0) {
        //这里就是网络不正常或者被挤掉断线激发的事件
        String errorMsg = arg0.getMessage();
        if (errorMsg.contains("conflict")) {
            SGApplication.getInstance().exit();
            UserPreferencesUtil.setIsOnline(this, false);
            UserPreferencesUtil.setRefract(this, true);
            UserPreferencesUtil.setIsRefreshRequest(MainFrameActivity.this, true);
            String[] arry = errorMsg.split(":");
            lastLoginTime = arry[arry.length - 1];
            lastLoginTime = lastLoginTime.replace(" ", "");
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    public void reconnectingIn(int arg0) {
        //重新连接的动作正在进行的动作，里面的参数arg0是一个倒计时的数字，如果连接失败的次数增多，数字会越来越大，开始的时候是14
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        //重新连接失败
        ThreadPool.getInstance().excuseThread(new DelayTime());
    }

    @Override
    public void reconnectionSuccessful() {
        //当网络断线了，重新连接上服务器触发的事件
        Toast.makeText(this,"网络连接成功",Toast.LENGTH_SHORT).show();
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    showConfirmDiloag();
                    break;
                case 1:
                    quaryUnReadNews();
                    SGNewsBean message = (SGNewsBean) msg.obj;
                    Intent intent = new Intent(XMPPUtils.NEW_MESSAGE_ACTION);
                    intent.putExtra("newBean", message);
                    intent.putExtra("newsMessage", true);
                    sendBroadcast(intent);
                    break;
                case 2:
                    XMPPUtils.reConnect(MainFrameActivity.this);
                    break;
                case 3:
                    GroupNoticeMessageBean sendMsgBean=(GroupNoticeMessageBean) msg.obj;
                    Intent intent1 = new Intent(XMPPUtils.NEW_MESSAGE_GROUP_NOTICE);
                    intent1.putExtra("newBean", sendMsgBean);
                    intent1.putExtra("newsMessage", true);
                    sendBroadcast(intent1);
                    break;
                case 4:
                    Toast.makeText(MainFrameActivity.this, "准备安装..." , Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    int proInt=msg.arg1;
                    mLeft.setProgress(proInt);
                    break;
                case 6:
                    mLeft.setProgress(0);
                    break;
                case 7:
                    queryUserGroups();
                    break;
                case 8:
                    SGNewsBean sendMsgBean1=(SGNewsBean) msg.obj;
                    int flag=msg.arg1;
                    Intent intent2 = new Intent(XMPPUtils.NEW_MESSAGE_DISC_GROUP_NOTICE);
                    intent2.putExtra("newBean", sendMsgBean1);
                    intent2.putExtra("newsMessage", true);
                    if (flag==1){
                        intent2.putExtra("takedOut",true);
                    }else{
                        intent2.putExtra("takedOut",false);
                    }
                    sendBroadcast(intent2);
                    quaryUnReadNews();
                    Intent intent3 = new Intent(XMPPUtils.NEW_MESSAGE_ACTION);
                    intent3.putExtra("newBean", sendMsgBean1);
                    intent3.putExtra("newsMessage", true);
                    sendBroadcast(intent3);
                    break;
                default:
                    break;
            }

        }

    };

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            BaiduPreferencesUtil.setAddress(SGApplication.getInstance().getBaseContext(), location.getAddrStr());
            BaiduPreferencesUtil.setCity(SGApplication.getInstance().getBaseContext(), location.getCity());
            BaiduPreferencesUtil.setEara(SGApplication.getInstance().getBaseContext(), location.getDistrict());
            BaiduPreferencesUtil.setLat(SGApplication.getInstance().getBaseContext(), String.valueOf(lat));
            BaiduPreferencesUtil.setLon(SGApplication.getInstance().getBaseContext(), String.valueOf(lon));
            BaiduPreferencesUtil.setProvice(SGApplication.getInstance().getBaseContext(), location.getProvince());
            BaiduPreferencesUtil.setStreet(SGApplication.getInstance().getBaseContext(), location.getStreet());
            if (location.getCity() != null) {
                SGApplication.getInstance().stopLocClient();
                Constants.setupLocation(MainFrameActivity.this);
            }
        }

    }

    @Override
    public void onBackPressed() {
        //判断两次返回时间间隔,小于两秒则退出程序
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次就真的离开蜜桃社区了哦!", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            UserPreferencesUtil.setIsRefreshRequest(MainFrameActivity.this, true);
            UserPreferencesUtil.setRefract(this, false);
            moveTaskToBack(true);
            ThreadPool.getInstance().excuseThread(new ExitAppAction(this));
//            finish();
        }
    }

    public void getUnreadMessage() {
        RequestBean item = new RequestBean();
        try {
            String userId = UserPreferencesUtil.getUserId(this);
            item.setData(new JSONObject().put("curUserId", userId));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_UNREAD_MSG);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_UNREAD_MSG_COD_);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.RESPOSE_UNREAD_MSG_COD_:
                if ("0000".equals(data.getCode())) {
                    newsDatas(data.getRecord());
                }
                break;
            case ConstTaskTag.QUERY_GROUP_ALL:
                if ("0000".equals(data.getCode())) {
                    if (!TextUtils.isEmpty(data.getRecord())){
                        CacheService.getInstance().cacheGroupDatas(UserPreferencesUtil.getUserId(this),null);
                        CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(this),null);
                        parseGroupDatas(data.getRecord());
                    }
                }
                break;
            case ConstTaskTag.QUERY_USER_LOGIN:
                if ("0000".equals(data.getCode())){
                    paerseDatas(data.getRecord());
                }else {
                    Intent intent = new Intent(this, LoginWelcomActivity.class);
                    intent.putExtra("whereFlag","mainMe");
                    startActivity(intent);
                }
                break;
            case ConstTaskTag.QUERY_QIANG_RESULT_GOD:
                if ("0000".equals(data.getCode())){
                    XMPPUtils.intoAppUpdateBean(this);
                    if (!TextUtils.isEmpty(data.getRecord())){
                        parseQiangGodInfo(data.getRecord());
                    }else{
                        TimerCountBean timerCountBean=new TimerCountBean();
                        timerCountBean.setPayExpireTime("");
                        timerCountBean.setOrderExpireTime("");
                        timerCountBean.setStartTime("0");
                        timerCountBean.setUserId(UserPreferencesUtil.getUserId(this));
                        timerCountBean.setGroupId("");
                        timerCountBean.setDuringLength(Constants.QIANGDAN_TIMER);
                        TimerEngine.inserTimerBean(this, timerCountBean);
                    }
                }
                break;
        }
    }

    public void newsDatas(String data) {
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                JSONObject subMap = array.getJSONObject(i);
                SGNewsBean msg = new SGNewsBean();
                msg.setRecruitId(StringUtils.getJsonValue(subMap, "userIcon"));//暂时保存用户头像
                msg.setNoticeId(StringUtils.getJsonValue(subMap, "userNick"));//暂时保存为用户昵称
                msg.setNoticeSubject(StringUtils.getJsonValue(subMap, "extra"));//扩展消息，JSON字符串，与msgType协助控制调整
                msg.setMsgContent(StringUtils.getJsonValue(subMap, "msgContent"));
                msg.setNewType(Constants.NEWS_DYNAMIC);
                msg.setUserId(UserPreferencesUtil.getUserId(this));
                msg.setMessageStatus(Constants.NEWS_UNREAD);
                msg.setTimestamp(StringUtils.getJsonValue(subMap, "createTime"));
                msg.setFromUser(StringUtils.getJsonValue(subMap, "relateUserId"));
                msg.setToUser(StringUtils.getJsonValue(subMap, "toUserId"));
                msg.setFriendUserId(StringUtils.getJsonValue(subMap, "relateUserId"));
                String msgType = StringUtils.getJsonValue(subMap, "msgType");
                if (Constants.TARGET_MEMBER.equals(msgType)) {
                    msg.setType(Constants.TARGET_MEMBER);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_REMOVE_APPLY.equals(msgType)) {
                    msg.setType(Constants.TARGET_REMOVE_APPLY);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_REMOVE_SUCCESS.equals(msgType)) {
                    msg.setType(Constants.TARGET_REMOVE_SUCCESS);
                    msg.setInout("0");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_BALANCE.equals(msgType)) {
                    msg.setType(Constants.TARGET_BALANCE);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_SHOW_EVALUATE.equals(msgType)) {
                    msg.setType(Constants.TARGET_SHOW_EVALUATE);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_NOTICE_DETAIL.equals(msgType)) {
                    msg.setType(Constants.TARGET_NOTICE_DETAIL);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_HIRED.equals(msgType)) {
                    msg.setType(Constants.TARGET_HIRED);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_INCOME.equals(msgType)) {
                    msg.setType(Constants.TARGET_INCOME);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_CAMERA_ENMPLOM_MODEL.equals(msgType)) {
                    msg.setType(Constants.TARGET_CAMERA_ENMPLOM_MODEL);
                    msg.setInout("-1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_MODEL_REFUSE_ENMPLOM_MODEL.equals(msgType)) {
                    msg.setType(Constants.TARGET_CAMERA_ENMPLOM_MODEL);
                    msg.setInout("1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                } else if (Constants.TARGET_JINPAI_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_JINPAI_ACT);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else if (Constants.TARGET_FREE_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_FREE_ACT);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else if (Constants.TARGET_FUFEI_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_FUFEI_ACT);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else  if (Constants.TARGET_TAKOUT_MEMBER.equals(msgType)){
                    msg.setType(Constants.TARGET_TAKOUT_MEMBER);
                    JSONObject object=new JSONObject(msg.getNoticeSubject());
                    String groupId=StringUtils.getJsonValue(object,"groupId");
                    XMPPUtils.takoutFriend(MainFrameActivity.this,groupId);
                    GroupNewNoticeEngeer.inserBlackGroup(this, UserPreferencesUtil.getUserId(this), groupId);
                    NewsEngine.inserChatNew(this, msg);
                    Intent intent2 = new Intent("com.xygame.group.takeout.action");
                    intent2.putExtra("groupId",groupId);
                    sendBroadcast(intent2);
                    quaryUnReadNews();
                    Intent intent3 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent3);
                }else if (Constants.TARGET_CLOSE_NO.equals(msgType)){
                    msg.setType(Constants.TARGET_CLOSE_NO);
                    NewsEngine.inserChatNew(this, msg);
                    UserPreferencesUtil.setIsOnline(this, false);
                    UserPreferencesUtil.setUserId(this, null);
                    UserPreferencesUtil.setCellPhone(this, null);
                    SGApplication.getInstance().exit();
                    mPager.setCurrentItem(0, false);
                    showCloseAccountDioag("您的账号被冻结，如有疑问请联系客服");
                    ThreadPool.getInstance().excuseThread(new ExitAppAction(this));
                }else if (Constants.TARGET_XIADAN_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_XIADAN_ACT);
                    msg.setInout("-1");//暂时保存消息保存状态-1：表示未操作，0表示同意，1表示取消
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                    XMPPUtils.sendMessageFromGodToCustom(this,msg);
                }else if (Constants.TARGET_XIADAN_FEEDBACK_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_XIADAN_FEEDBACK_ACT);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else if (Constants.TARGET_XIADAN_JIESUAN_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_XIADAN_JIESUAN_ACT);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else if (Constants.TARGET_XIADAN_CANCEL_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_XIADAN_CANCEL_ACT);
                    msg.setInout("4");
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else if (Constants.TARGET_XIADAN_APPLY_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_XIADAN_APPLY_ACT);
                    msg.setInout("5");
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else if (Constants.TARGET_XIADAN_APPLY_REPORT_ACT.equals(msgType)){
                    msg.setType(Constants.TARGET_XIADAN_APPLY_REPORT_ACT);
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else if (Constants.TARGET_QIANG_PUSH.equals(msgType)){
                    msg.setType(Constants.TARGET_QIANG_PUSH);
                    msg.setInout("5");
                    NewsGodEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }else{
                    msg.setType("");
                    NewsEngine.inserChatNew(this, msg);
                    quaryUnReadNews();
                    Intent intent2 = new Intent("com.xygame.push.dynamic.message.list.action");
                    sendBroadcast(intent2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCloseAccountDioag(String tip){
        OneButtonDialog dialog = new OneButtonDialog(this, tip, R.style.dineDialog,

						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
                        Intent intent = new Intent(Constants.ACTION_LOGIN_FAILTH);
                        sendBroadcast(intent);
					}
				});
        dialog.show();
    }

    @Override
    public void toPersonPage() {
        mPager.setCurrentItem(0, false);
    }

    @Override
    public void processPacket(Packet packet) {
        if (packet instanceof org.jivesoftware.smack.packet.Message) {
            // Do something with the incoming packet here.
            final org.jivesoftware.smack.packet.Message message = (org.jivesoftware.smack.packet.Message) packet;
            if (message.getError() != null) {
                Log.e("receive msg error", message.getError().toString());
                if (message.getError().getCode() == 404) {
                    // Check to see if the user is online to recieve this
                    // message.
                }
                return;
            }
            if (org.jivesoftware.smack.packet.Message.Type.chat == message
                    .getType()) {
                if (message.getBody() != null) {
                    processChatMessage(message);
                }
            }
            if (org.jivesoftware.smack.packet.Message.Type.groupchat == message
                    .getType()) {
                if (UserPreferencesUtil.isOnline(this)) {
                    String packetId=message.getPacketID();
                    parseGroupNews(message,packetId);
                }
            }
        }
    }

    private void processChatMessage(org.jivesoftware.smack.packet.Message message){
        if (message != null && message.getBody() != null && !message.getBody().equals("null")) {
            String mssageBody = message.getBody();
            try {
                if (UserPreferencesUtil.isOnline(this)) {
                    SGNewsBean msg = new SGNewsBean();
                    JSONObject obj = new JSONObject(mssageBody);
                    String ext = obj.getString("ext");
                    JSONObject subObj = new JSONObject(ext);
                    String fromUser=StringUtils.getJsonValue(subObj, "fromUser");
                    JSONObject fromUserObj=new JSONObject(fromUser);
                    String fromUserId=StringUtils.getJsonValue(fromUserObj,"userId");
                    List<BlackMemberBean> blackListDatasDatas= CacheService.getInstance().getCacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this));
                    boolean flag=false;
                    if (blackListDatasDatas!=null){
                        for (BlackMemberBean it:blackListDatasDatas){
                            if (fromUserId.equals(it.getUserId())){
                                flag=true;
                                break;
                            }
                        }
                    }
                    if (!flag){
                        msg.setFromUser(fromUser);
                        msg.setToUser(StringUtils.getJsonValue(subObj, "toUser"));
                        msg.setNoticeSubject(StringUtils.getJsonValue(subObj, "noticeSubject"));
                        msg.setRecruitLocIndex(StringUtils.getJsonValue(subObj, "recruitLocIndex"));
                        msg.setMsgContent(StringUtils.getJsonValue(obj, "msgContent"));
                        msg.setNewType(Constants.NEWS_CHAT);
                        msg.setNoticeId(StringUtils.getJsonValue(obj, "noticeId"));
                        msg.setRecruitId(StringUtils.getJsonValue(obj, "recruitId"));
                        msg.setTimestamp(StringUtils.getJsonValue(obj, "timestamp"));
                        msg.setType(StringUtils.getJsonValue(obj, "type"));
                        msg.setUserId(UserPreferencesUtil.getUserId(this));
                        msg.setMessageStatus(Constants.NEWS_UNREAD);
                        msg.setInout(Constants.NEWS_RECIEVE);
                        msg.setIsShow(Constants.NEWS_SHOW);
                        msg.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                        if (!UserPreferencesUtil.getUserId(this).equals(SenderUser.getFromUserId(msg, this))) {
                            msg.setFriendUserId(SenderUser.getFromUserId(msg, this));
                        } else if (!UserPreferencesUtil.getUserId(this)
                                .equals(SenderUser.getToUserId(msg, this))) {
                            msg.setFriendUserId(SenderUser.getToUserId(msg, this));
                        }

                        if (message.getSubject()!=null){
                            if (message.getSubject().contains("5")){
                                NewsEngine.inserNoticeChatNew(this, msg);
                            }else{
                                NewsEngine.inserChatNew(this, msg);
                            }
                        }else if (message.getSubjects()!=null){
                            Collection<Message.Subject> subjects=message.getSubjects();
                            Iterator iter = subjects.iterator();
                            boolean tempBoolean=false;
                            while(iter.hasNext()){
                                Message.Subject str = (Message.Subject) iter.next();
                                if (str.getSubject().contains("5")){
                                    tempBoolean=true;
                                }
                            }
                            if (tempBoolean){
                                NewsEngine.inserNoticeChatNew(this, msg);
                            }else{
                                NewsEngine.inserChatNew(this, msg);
                            }
                        }else{
                            NewsEngine.inserChatNew(this, msg);
                        }
                        android.os.Message m = handler.obtainMessage();
                        m.what = 1;
                        m.obj = msg;
                        m.sendToTarget();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void parseGroupNews(org.jivesoftware.smack.packet.Message message,String packetId) {
        String sss = message.getBody();
        if (!TextUtils.isEmpty(sss)){
            if (message.getSubject()!=null){
                if (message.getSubject().startsWith("1")){
                    processNoticeGroupMsg(sss, packetId);
                } else{
                    processDiscGroupMsg(sss,packetId);
                }
            }else if (message.getSubjects()!=null){
                Collection<Message.Subject> subjects=message.getSubjects();
                Iterator iter = subjects.iterator();
                boolean tempBoolean=false;
                while(iter.hasNext()){
                    Message.Subject str = (Message.Subject) iter.next();
                    if (str.getSubject().startsWith("1")){
                        tempBoolean=true;
                    }
                }
                if (tempBoolean){
                    processNoticeGroupMsg(sss, packetId);
                }else{
                    processDiscGroupMsg(sss,packetId);
                }
            }
        }
    }


    private void processDiscGroupMsg(String mssageBody,String packetId){
        SGNewsBean existBean=TempGroupNewsEngine.getSGNewBeanByFriendUserId(this, UserPreferencesUtil.getUserId(this), packetId);
        if (existBean==null){
            try {
                JSONObject obj = new JSONObject(mssageBody);
                String ext = StringUtils.getJsonValue(obj, "ext");
                String sendUserId="",fromUser="";
                JSONObject subObj=null,fromUserJson=null;
                if (!TextUtils.isEmpty(ext)){
                    subObj = new JSONObject(ext);
                    fromUser=StringUtils.getJsonValue(subObj, "fromUser");
                    fromUserJson=new JSONObject(fromUser);
                    sendUserId=StringUtils.getJsonValue(fromUserJson,"userId");
                }
                if (!sendUserId.equals(UserPreferencesUtil.getUserId(this))){
                    if (ConstTaskTag.GROUP_DISC_LOSE.equals(StringUtils.getJsonValue(obj, "type"))){
                        SGNewsBean sendBean = new SGNewsBean();
                        // 创建消息实体
                        sendBean.setFromUser("");
                        sendBean.setNoticeSubject(StringUtils.getJsonValue(obj, "groupName"));
                        sendBean.setRecruitLocIndex("");
                        sendBean.setNoticeId(StringUtils.getJsonValue(obj, "groupId"));
                        sendBean.setFriendNickName("");
                        sendBean.setFriendUserIcon("");
                        sendBean.setFriendUserId(packetId);
                        // +++++++++++++++++++++++++++++++++++++++++++++++
                        sendBean.setMsgContent(StringUtils.getJsonValue(obj, "msgContent"));
                        sendBean.setNewType(Constants.GROUP_CHAT);
                        sendBean.setRecruitId("");
                        sendBean.setTimestamp(StringUtils.getJsonValue(obj, "timestamp"));
                        sendBean.setType(Constants.SEND_TEXT_TIP);
                        sendBean.setUserId(UserPreferencesUtil.getUserId(this));
                        sendBean.setMessageStatus(Constants.NEWS_UNREAD);
                        sendBean.setInout(Constants.NEWS_RECIEVE);
                        sendBean.setIsShow(Constants.NEWS_SHOW);
                        sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                        ThreadPool.getInstance().excuseThread(new LeaveDiscGroup(this, sendBean.getNoticeId()));
                        // 新增本地数据
                        TempGroupNewsEngine.inserChatNew(this, sendBean);
                        android.os.Message m = handler.obtainMessage();
                        m.what = 8;
                        m.arg1=1;
                        m.obj = sendBean;
                        m.sendToTarget();
                    }else if (ConstTaskTag.GROUP_DISC_FRIENDJOIN.equals(StringUtils.getJsonValue(obj, "type"))){
                        SGNewsBean sendBean = new SGNewsBean();
                        // 创建消息实体
                        sendBean.setFromUser("");
                        sendBean.setNoticeSubject(StringUtils.getJsonValue(obj, "groupName"));
                        sendBean.setRecruitLocIndex("");
                        sendBean.setNoticeId(StringUtils.getJsonValue(obj, "groupId"));
                        sendBean.setFriendNickName("");
                        sendBean.setFriendUserIcon("");
                        sendBean.setFriendUserId(packetId);
                        // +++++++++++++++++++++++++++++++++++++++++++++++
                        sendBean.setMsgContent(StringUtils.getJsonValue(obj, "msgContent").concat("加入了讨论组"));
                        sendBean.setNewType(Constants.GROUP_CHAT);
                        sendBean.setRecruitId("");
                        sendBean.setTimestamp(StringUtils.getJsonValue(obj, "timestamp"));
                        sendBean.setType(Constants.SEND_TEXT_TIP);
                        sendBean.setUserId(UserPreferencesUtil.getUserId(this));
                        sendBean.setMessageStatus(Constants.NEWS_UNREAD);
                        sendBean.setInout(Constants.NEWS_RECIEVE);
                        sendBean.setIsShow(Constants.NEWS_SHOW);
                        sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);

                        // 新增本地数据
                        TempGroupNewsEngine.inserChatNew(this, sendBean);
                        android.os.Message m = handler.obtainMessage();
                        m.what = 8;
                        m.arg1=0;
                        m.obj = sendBean;
                        m.sendToTarget();
                    }else if (ConstTaskTag.GROUP_DISC_NAME.equals(StringUtils.getJsonValue(obj, "type"))){
                        SGNewsBean sendBean = new SGNewsBean();
                        // 创建消息实体
                        sendBean.setFromUser("");
                        sendBean.setNoticeSubject(StringUtils.getJsonValue(obj, "msgContent"));
                        sendBean.setRecruitLocIndex("");
                        sendBean.setNoticeId(StringUtils.getJsonValue(obj, "groupId"));
                        sendBean.setFriendNickName("");
                        sendBean.setFriendUserIcon("");
                        sendBean.setFriendUserId(packetId);
                        // +++++++++++++++++++++++++++++++++++++++++++++++
                        sendBean.setMsgContent(StringUtils.getJsonValue(obj, "modfyUserName").concat("修改了讨论组名称“" + sendBean.getNoticeSubject() + "”"));
                        sendBean.setNewType(Constants.GROUP_CHAT);
                        sendBean.setRecruitId("");
                        sendBean.setTimestamp(StringUtils.getJsonValue(obj, "timestamp"));
                        sendBean.setType(Constants.SEND_TEXT_TIP);
                        sendBean.setUserId(UserPreferencesUtil.getUserId(this));
                        sendBean.setMessageStatus(Constants.NEWS_UNREAD);
                        sendBean.setInout(Constants.NEWS_RECIEVE);
                        sendBean.setIsShow(Constants.NEWS_SHOW);
                        sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                        // 新增本地数据
                        TempGroupNewsEngine.inserChatNew(this, sendBean);
                        android.os.Message m = handler.obtainMessage();
                        Intent intent=new Intent(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
                        intent.putExtra("flag", Constants.EDITOR_DISC_GROUP_NAME);
                        intent.putExtra(Constants.EDITOR_DISC_GROUP_NAME, sendBean.getNoticeSubject());
                        intent.putExtra("groupName", sendBean.getNoticeSubject());
                        intent.putExtra("groupId", sendBean.getNoticeId());
                        sendBroadcast(intent);
                        m.what = 8;
                        m.arg1=0;
                        m.obj = sendBean;
                        m.sendToTarget();
                    }else{
                        SGNewsBean msg = new SGNewsBean();
                        msg.setFromUser(fromUser);
                        msg.setFriendUserId(packetId);
                        msg.setToUser(StringUtils.getJsonValue(subObj, "toUser"));
                        msg.setNoticeSubject(StringUtils.getJsonValue(obj, "groupName"));
                        msg.setRecruitLocIndex(StringUtils.getJsonValue(subObj, "recruitLocIndex"));
                        msg.setMsgContent(StringUtils.getJsonValue(obj, "msgContent"));
                        msg.setNewType(Constants.GROUP_CHAT);
                        msg.setNoticeId(StringUtils.getJsonValue(obj, "groupId"));
                        msg.setRecruitId(StringUtils.getJsonValue(obj, "recruitId"));
                        msg.setTimestamp(StringUtils.getJsonValue(obj, "timestamp"));
                        msg.setType(StringUtils.getJsonValue(obj, "type"));
                        msg.setUserId(UserPreferencesUtil.getUserId(this));
                        msg.setMessageStatus(Constants.NEWS_UNREAD);
                        msg.setInout(Constants.NEWS_RECIEVE);
                        msg.setIsShow(Constants.NEWS_SHOW);
                        msg.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                        TempGroupNewsEngine.inserChatNew(this, msg);
                        android.os.Message m = handler.obtainMessage();
                        m.what = 8;
                        m.obj = msg;
                        m.arg1=0;
                        m.sendToTarget();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void processNoticeGroupMsg(String sss,String packetId){
        GroupNoticeMessageBean gMsg=GroupNewNoticeEngeer.quaryGroupMsgItem(this,UserPreferencesUtil.getUserId(this),packetId);
        if (gMsg==null){
            try {
                if (!TextUtils.isEmpty(sss)){
                    JSONObject object=new JSONObject(sss);
                    String timestamp=StringUtils.getJsonValue(object,"timestamp");
                    String type=StringUtils.getJsonValue(object,"type");
                    String msgContent=StringUtils.getJsonValue(object,"msgContent");
                    String ext=StringUtils.getJsonValue(object,"ext");
                    JSONObject extJson=new JSONObject(ext);
                    String fromUser=StringUtils.getJsonValue(extJson,"fromUser");
                    String toUser=StringUtils.getJsonValue(extJson,"toUser");
                    JSONObject fromUserJson=new JSONObject(fromUser);
                    String sendUserId=StringUtils.getJsonValue(fromUserJson,"userId");
                    if (!sendUserId.equals(UserPreferencesUtil.getUserId(this))){
                        GroupNoticeMessageBean sendMsgBean=new GroupNoticeMessageBean();
                        sendMsgBean.setMsgStatus("");
                        sendMsgBean.setPacketId(packetId);
                        sendMsgBean.setUserId(UserPreferencesUtil.getUserId(this));
                        sendMsgBean.setSendUserId(fromUser);
                        sendMsgBean.setNoticeJson("");
                        sendMsgBean.setGroupJid(StringUtils.getJsonValue(new JSONObject(toUser), "userId"));
                        sendMsgBean.setMsgContent(msgContent);
                        sendMsgBean.setMsgTimer(timestamp);
                        sendMsgBean.setMsgType(type);
                        GroupNewNoticeEngeer.inserGroupNoticeMsg(this, sendMsgBean);
                        android.os.Message m = handler.obtainMessage();
                        m.what = 3;
                        m.obj = sendMsgBean;
                        m.sendToTarget();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void parseGroupDatas(String record) {
        try {
            JSONObject jsonObject =new JSONObject(record);
            String noticeGroups=StringUtils.getJsonValue(jsonObject,"noticeGroups");
            if (ConstTaskTag.isTrueForArrayObj(noticeGroups)) {
                List<GroupBean> value = new ArrayList<>();
                JSONArray array = new JSONArray(noticeGroups);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    GroupBean item = new GroupBean();
                    item.setGroupId(StringUtils.getJsonValue(object1, "groupId"));
                    item.setGroupoType(StringUtils.getJsonValue(object1, "groupType"));
                    item.setUserId(UserPreferencesUtil.getUserId(this));
                    GroupBean isDeleBean= GroupEngine.quaryDeleGroupBeanByGroupId(this, item.getGroupId(), UserPreferencesUtil.getUserId(this));
                    if (isDeleBean==null){
                        GroupBean temp= GroupEngine.quaryGroupBean(this,item,UserPreferencesUtil.getUserId(this));
                        if (temp==null){
                            GroupEngine.inserGroup(this,item);
                        }
                        value.add(item);
                    }
                }
               if (value.size()>0){
                   CacheService.getInstance().cacheGroupDatas(UserPreferencesUtil.getUserId(this),value);
                   initGroupForMe();
               }
            }

            String discussGroups=StringUtils.getJsonValue(jsonObject,"discussGroups");
            if (ConstTaskTag.isTrueForArrayObj(discussGroups)) {
                List<GroupBean> value = new ArrayList<>();
                JSONArray array = new JSONArray(discussGroups);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    GroupBean item = new GroupBean();
                    item.setCreateUserId(StringUtils.getJsonValue(object1,"createUserId"));
                    item.setGroupId(StringUtils.getJsonValue(object1, "groupId"));
                    item.setGroupoType(StringUtils.getJsonValue(object1, "groupType"));
                    item.setGroupName(StringUtils.getJsonValue(object1, "groupName"));
                    item.setUserId(UserPreferencesUtil.getUserId(this));
                    GroupBean isDeleBean= GroupEngine.quaryDeleGroupBeanByGroupId(this, item.getGroupId(), UserPreferencesUtil.getUserId(this));
                    if (isDeleBean==null){
                        GroupBean temp= GroupEngine.quaryGroupBean(this,item,UserPreferencesUtil.getUserId(this));
                        if (temp==null){
                            GroupEngine.inserGroup(this,item);
                        }
                        value.add(item);
                    }
                }
                if (value.size()>0){
                    CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(this),value);
                    joinDiscGroupAction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void joinDiscGroupAction() {
        XMPPUtils.initDiscGroupAction(this);
    }

    private void initGroupForMe() {
       XMPPUtils.initGroupAction(this);
    }



    private class ReconnectXmppAct implements Runnable{
        private Context context;
        public ReconnectXmppAct(Context context){
            this.context=context;
        }
        @Override
        public void run() {
            try {
                SGApplication.getInstance().getConnection().connect();
                if (SGApplication.getInstance().getConnection().isConnected()){
                    if (!SGApplication.getInstance().getConnection().isAuthenticated()){
                        SGApplication.getInstance().getConnection().login(UserPreferencesUtil.getUserId(context), UserPreferencesUtil.getPwd(context), "sgapp"); // 登录
                        OfflineMsgManager.getInstance(context).dealOfflineMsg(SGApplication.getInstance().getConnection());//处理离线消息
                        SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.available));
                        DeliveryReceiptManager.getInstanceFor(SGApplication.getInstance().getConnection()).enableAutoReceipts();
                        handler.sendEmptyMessage(7);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void quaryQinagDanGod() {
        try {
            RequestBean item = new RequestBean();
            item.setIsPublic(false);
            JSONObject obj = new JSONObject();
            List<GodQiangDanRebackBean> qiangDanRebackBeans= CacheService.getInstance().getGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this));
            if (qiangDanRebackBeans!=null&&qiangDanRebackBeans.size()>0){
                GodQiangDanRebackBean bean=qiangDanRebackBeans.get(0);
                obj.put("lastOrderTime", bean.getOrderTime());
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_QIANG_RESULT_GOD);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_QIANG_RESULT_GOD);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void autoLogin(int flag){
        try {
            RequestBean item = new RequestBean();
            item.setIsPublic(false);
            JSONObject obj = new JSONObject();
            obj.put("registerType",UserPreferencesUtil.getUserLoginType(this));
            obj.put("loginName", UserPreferencesUtil.getLoginName(this));
            obj.put("loginPwd", UserPreferencesUtil.getLoginPwd(this));
            ProvinceBean areaBean= CacheService.getInstance().getCacheCommenAreaBean(Constants.COMMEN_AREA_FLAG);
            if (areaBean!=null){
                obj.put("province", areaBean.getProvinceCode());
                CityBean cityBean=areaBean.getCityBean();
                if (cityBean!=null){
                    obj.put("city", cityBean.getCityCode());
                }
            }
            item.setData(obj);
            if (flag!=0){
                ShowMsgDialog.showNoMsg(this, false);
            }
            item.setServiceURL(ConstTaskTag.QUEST_USER_LOGIN);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USER_LOGIN);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void paerseDatas(String record) {
        try {
            ShowMsgDialog.cancel();
            JSONObject obj=new JSONObject(record);
            String orderExpireTime= StringUtils.getJsonValue(obj,"orderExpireTime");
            String payExpireTime=StringUtils.getJsonValue(obj, "payExpireTime");
            UserPreferencesUtil.setorderExpireTime(this,orderExpireTime);
            UserPreferencesUtil.setpayExpireTime(this, payExpireTime);
            String userId=obj.getString("userId");
            UserPreferencesUtil.setUserId(this, userId);
            String usernick=obj.getString("usernick");
            UserPreferencesUtil.setUserNickName(this, usernick);
            String userIcon=obj.getString("userIcon");
            UserPreferencesUtil.setHeadPic(this, userIcon);
            String gender=obj.getString("gender");
            UserPreferencesUtil.setSex(this, gender);
            String checkOff= StringUtils.getJsonValue(obj,"chatOff");
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
            UserPreferencesUtil.setIsOnline(this, true);
            if (SGApplication.getInstance().getConnection()==null||UserPreferencesUtil.isRefract(this)){
                XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
                Intent intent = new Intent(Constants.ACTION_LOGIN_SUCCESS);
                sendBroadcast(intent);
            }else{
                UserPreferencesUtil.setIsNewConnect(this, false);
                Intent intent1 = new Intent(Constants.NOTICE_REQUEST_GROUP);
                sendBroadcast(intent1);
                Intent intent = new Intent(Constants.ACTION_LOGIN_SUCCESS);
                sendBroadcast(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parseQiangGodInfo(String record) {
        try {
            JSONObject jsonObject =new JSONObject(record);
            TimerCountBean timerCountBean=TimerEngine.quaryTimerBeansByDuringLength(this,UserPreferencesUtil.getUserId(this),Constants.QIANGDAN_TIMER);
            String orderExpireTime= StringUtils.getJsonValue(jsonObject,"orderExpireTime");
            String payExpireTime=StringUtils.getJsonValue(jsonObject, "payExpireTime");
            if (timerCountBean==null){
                timerCountBean=new TimerCountBean();
                timerCountBean.setPayExpireTime(payExpireTime);
                timerCountBean.setOrderExpireTime(orderExpireTime);
                timerCountBean.setStartTime(StringUtils.getJsonValue(jsonObject,"createTime"));
                timerCountBean.setUserId(UserPreferencesUtil.getUserId(this));
                timerCountBean.setGroupId(StringUtils.getJsonValue(jsonObject, "orderId"));
                timerCountBean.setDuringLength(Constants.QIANGDAN_TIMER);
                TimerEngine.inserTimerBean(this, timerCountBean);
            }else{
                timerCountBean.setPayExpireTime(payExpireTime);
                timerCountBean.setOrderExpireTime(orderExpireTime);
                timerCountBean.setStartTime(StringUtils.getJsonValue(jsonObject, "createTime"));
                timerCountBean.setGroupId(StringUtils.getJsonValue(jsonObject, "orderId"));
                TimerEngine.updateTimerBeanInfo(this, timerCountBean);
            }
            String members=StringUtils.getJsonValue(jsonObject,"members");
            String startTime=StringUtils.getJsonValue(jsonObject,"startTime");
            String holdTime=StringUtils.getJsonValue(jsonObject,"holdTime");
            String orderId=StringUtils.getJsonValue(jsonObject, "orderId");
            String address=StringUtils.getJsonValue(jsonObject,"address");
            String oral=StringUtils.getJsonValue(jsonObject,"orderDesc");
            if (ConstTaskTag.isTrueForArrayObj(members)){
                JSONArray jsonArray=new JSONArray(members);
                List<GodQiangDanRebackBean> qiangDanRebackBeans=new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject memberObj=jsonArray.getJSONObject(i);
                    GodQiangDanRebackBean godQiangDanRebackBean=new GodQiangDanRebackBean();
                    godQiangDanRebackBean.setUserId(StringUtils.getJsonValue(memberObj, "userId"));
                    godQiangDanRebackBean.setAge(StringUtils.getJsonValue(memberObj, "age"));
                    godQiangDanRebackBean.setGender(StringUtils.getJsonValue(memberObj, "gender"));
                    godQiangDanRebackBean.setPriceId(StringUtils.getJsonValue(memberObj, "priceId"));
                    godQiangDanRebackBean.setPriceRate(StringUtils.getJsonValue(memberObj, "priceRate"));
                    godQiangDanRebackBean.setSkillTitle(StringUtils.getJsonValue(memberObj, "skillTitle"));
                    godQiangDanRebackBean.setUserIcon(StringUtils.getJsonValue(memberObj, "userIcon"));
                    godQiangDanRebackBean.setUsernick(StringUtils.getJsonValue(memberObj, "usernick"));
                    godQiangDanRebackBean.setVideoUrl(StringUtils.getJsonValue(memberObj, "videoUrl"));
                    godQiangDanRebackBean.setVoiceUrl(StringUtils.getJsonValue(memberObj, "voiceUrl"));
                    godQiangDanRebackBean.setOrderCount(StringUtils.getJsonValue(memberObj, "orderCount"));
                    godQiangDanRebackBean.setSkillCode(StringUtils.getJsonValue(memberObj, "skillCode"));
                    godQiangDanRebackBean.setOrderTime(StringUtils.getJsonValue(memberObj, "orderTime"));
                    godQiangDanRebackBean.setAddress(address);
                    godQiangDanRebackBean.setOral(oral);
                    godQiangDanRebackBean.setOrderId(orderId);
                    godQiangDanRebackBean.setStartTime(startTime);
                    godQiangDanRebackBean.setHoldTime(holdTime);
                    qiangDanRebackBeans.add(godQiangDanRebackBean);
                }
                if (qiangDanRebackBeans.size()>0){
                    List<GodQiangDanRebackBean> tempDatas= CacheService.getInstance().getGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this));
                    if (tempDatas!=null){
                        for (GodQiangDanRebackBean newsBean:qiangDanRebackBeans){
                            boolean isHave=false;
                            for (GodQiangDanRebackBean oldBean:tempDatas){
                                if (newsBean.getUserId().equals(oldBean.getUserId())){
                                    isHave=true;
                                    break;
                                }
                            }
                            if (!isHave){
                                tempDatas.add(newsBean);
                            }
                        }
                        CacheService.getInstance().cacheGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this), tempDatas);
                    }else{
                        CacheService.getInstance().cacheGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this), qiangDanRebackBeans);
                    }
                    if (isRunningActivity){
                        Intent noticePage = new Intent();
                        noticePage.setAction(XMPPUtils.ACTION_SYS_MSG);
                        noticePage.putExtra("flagTag", "reSet");
                        sendBroadcast(noticePage);
                    }else{
                        Intent intent = new Intent(this, WaitGodTwoActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseGodParse(String custom) {
        try {
            JSONObject godObject=new JSONObject(custom);
            GodQiangDanRebackBean godQiangDanRebackBean=new GodQiangDanRebackBean();

            String member=StringUtils.getJsonValue(godObject,"member");
            JSONObject memberObj=new JSONObject(member);
            godQiangDanRebackBean.setUserId(StringUtils.getJsonValue(memberObj, "userId"));
            godQiangDanRebackBean.setAge(StringUtils.getJsonValue(memberObj, "age"));
            godQiangDanRebackBean.setGender(StringUtils.getJsonValue(memberObj, "gender"));
            godQiangDanRebackBean.setPriceId(StringUtils.getJsonValue(memberObj, "priceId"));
            godQiangDanRebackBean.setPriceRate(StringUtils.getJsonValue(memberObj, "priceRate"));
            godQiangDanRebackBean.setSkillTitle(StringUtils.getJsonValue(memberObj, "skillTitle"));
            godQiangDanRebackBean.setUserIcon(StringUtils.getJsonValue(memberObj, "userIcon"));
            godQiangDanRebackBean.setUsernick(StringUtils.getJsonValue(memberObj, "usernick"));
            godQiangDanRebackBean.setVideoUrl(StringUtils.getJsonValue(memberObj, "videoUrl"));
            godQiangDanRebackBean.setVoiceUrl(StringUtils.getJsonValue(memberObj, "voiceUrl"));
            godQiangDanRebackBean.setOrderCount(StringUtils.getJsonValue(memberObj, "orderCount"));
            godQiangDanRebackBean.setSkillCode(StringUtils.getJsonValue(memberObj, "skillCode"));
            godQiangDanRebackBean.setOrderTime(StringUtils.getJsonValue(memberObj, "orderTime"));
            String order=StringUtils.getJsonValue(godObject,"order");
            JSONObject jsonObject=new JSONObject(order);
            godQiangDanRebackBean.setStartTime(StringUtils.getJsonValue(jsonObject,"startTime"));
            godQiangDanRebackBean.setHoldTime(StringUtils.getJsonValue(jsonObject,"holdTime"));
            String address=StringUtils.getJsonValue(jsonObject,"address");
            String oral=StringUtils.getJsonValue(jsonObject,"orderDesc");
            godQiangDanRebackBean.setAddress(address);
            godQiangDanRebackBean.setOral(oral);
            String orderId1=StringUtils.getJsonValue(jsonObject, "orderId");
            if (!TextUtils.isEmpty(orderId1)){
                godQiangDanRebackBean.setOrderId(orderId1);
            }
            List<GodQiangDanRebackBean> qiangDanRebackBeans=CacheService.getInstance().getGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this));
            if (qiangDanRebackBeans==null){
                qiangDanRebackBeans=new ArrayList<>();
                qiangDanRebackBeans.add(godQiangDanRebackBean);
                CacheService.getInstance().cacheGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this), qiangDanRebackBeans);
            }else{
                boolean isHave=false;
                for (GodQiangDanRebackBean oldBean:qiangDanRebackBeans){
                    if (oldBean.getUserId().equals(godQiangDanRebackBean.getUserId())){
                        isHave=true;
                        break;
                    }
                }
                if (!isHave) {
                    qiangDanRebackBeans.add(godQiangDanRebackBean);
                    CacheService.getInstance().cacheGodQiangDanRebackBean(UserPreferencesUtil.getUserId(this), qiangDanRebackBeans);
                }
            }
            Intent noticePage = new Intent();
            noticePage.setAction(XMPPUtils.ACTION_SYS_MSG);
            noticePage.putExtra("godQiangDanRebackBean", godQiangDanRebackBean);
            noticePage.putExtra("flagTag", "reSet");
            sendBroadcast(noticePage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0: {
                mPager.setCurrentItem(0, false);
                break;
            }
            default:
                break;
        }
    }
}