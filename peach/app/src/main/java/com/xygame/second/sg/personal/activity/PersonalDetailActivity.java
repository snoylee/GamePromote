package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xygame.second.sg.biggod.activity.GodDetailActivity;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.comm.bean.ActManagerBean;
import com.xygame.second.sg.comm.inteface.ScrollViewListener;
import com.xygame.second.sg.defineview.CustomScrollView;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.jinpai.activity.JinPaiDetailActivity;
import com.xygame.second.sg.jinpai.activity.JinPaiFuFeiDetailActivity;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.personal.adapter.PersonDetailPhotoAdapter;
import com.xygame.second.sg.personal.adapter.PersonDetailPresenterAdapter;
import com.xygame.second.sg.personal.bean.PersonDetailPersenterBean;
import com.xygame.second.sg.personal.bean.PhotoBean;
import com.xygame.second.sg.personal.blacklist.BlackMemberBean;
import com.xygame.second.sg.sendgift.activity.YuePaiDetailActivity;
import com.xygame.second.sg.xiadan.activity.XiaDanDetailActivity;
import com.xygame.second.sg.xiadan.adapter.GodBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.TabEntity;
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
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalDetailActivity extends SGBaseActivity implements OnClickListener ,ScrollViewListener{
    /**
     * 公用变量部分
     */
    private TextView titleName,guanZhuTipText,userNickText,cityText,fansNumText;
    private View backButton,bottomView,guanZhuButtonView,sendNewsButton,baseInfoView,actInfoView,rightButton,intoPhotoMoreView;
    private CustomScrollView scrollView;
    private CircularImage userHeadImage;
    private String quaryUserId,video,quaryUserImage,pulicSexTextStr=null,pulicUserAgeStr=null,pulicUsernick;
    private ImageView rightbuttonIcon;
    private ImageLoader mImageLoader;
    private VideoImageLoader videoImageLoader;
    private boolean isGuanZhu=false,isLoadBase=true;
    private int tempH;
    private  PersonDetailPhotoAdapter photoAdapter;
    /**
     * Tab部分
     */
    private CommonTabLayout tab_l,tab_l1;
    private String[] titles = {"资料","活动"};
    private int[] iconUnselectIds = {R.drawable.tab_data_unselect,  R.drawable.tab_works_unselect};
    private int[] iconSelectIds = { R.drawable.tab_data_select,  R.drawable.tab_works_select};
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>();
    /**
     * viewPage部分
     */
    private LayoutInflater mInflater;
    private LinearLayout mPager;// 页内容
    private int pageSize = 500;//每页显示的数量
    private int hotPage = 1;//当前显示页数
    private String hotReqTime;
    private boolean hotIsLoading = true;

    /**
     * 资料部分
     */
    private View dynimcView,middleBlockLineView,xcView,phView,intoPhMoreView,idCertify,id_text,id_gou,videoCertify,vido_text,vido_gou,nullBottomView,intoFansZhuView,serviceView,xiaDanView;
    private LinearLayout serviceSubView;
    private GridView gridview,phLearLayout;
    private ImageView idCertifyFlagImage;
    private CircularImage  videoCertifyImage;
    private View playIcon;
    private TextView miTaoId,sexText,userAge,userGrade,userSign,loveStatus,userCarrier,userIncoming,userHeight,userWeight,loveFeeling,sexFeeling,anotherRequest;

    /**
     * 活动部分
     */
    private LinearLayout personActView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_detail_layout);
        initViews();
        initListensers();
        initDatas();
    }

    @Override
    protected void onResume() {
        ThreadPool.getInstance().excuseThread(new DelayTime());
        super.onResume();
    }

    private void initViews() {
        xiaDanView=findViewById(R.id.xiaDanView);
        intoFansZhuView=findViewById(R.id.intoFansZhuView);
        rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
        rightButton=findViewById(R.id.rightButton);
        sendNewsButton=findViewById(R.id.sendNewsButton);
        fansNumText=(TextView)findViewById(R.id.fansNumText);
        cityText=(TextView)findViewById(R.id.cityText);
        userNickText=(TextView)findViewById(R.id.userNickText);
        guanZhuTipText=(TextView)findViewById(R.id.guanZhuTipText);
        guanZhuButtonView=findViewById(R.id.guanZhuButtonView);
        userHeadImage=(CircularImage)findViewById(R.id.userHeadImage);
        bottomView=findViewById(R.id.bottomView);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        scrollView = (CustomScrollView) findViewById(R.id.scrollView);
        tab_l = (CommonTabLayout) findViewById(R.id.tab_l);
        tab_l1 = (CommonTabLayout) findViewById(R.id.tab_l1);
        mPager = (LinearLayout) findViewById(R.id.pager);
        mInflater = getLayoutInflater();

        baseInfoView=mInflater.inflate(R.layout.personal_detail_info_layout, null);
        intoPhotoMoreView=baseInfoView.findViewById(R.id.intoPhotoMoreView);
        middleBlockLineView=baseInfoView.findViewById(R.id.middleBlockLineView);
        xcView=baseInfoView.findViewById(R.id.xcView);
        idCertify=baseInfoView.findViewById(R.id.idCertify);
        intoPhMoreView=baseInfoView.findViewById(R.id.intoPhMoreView);
        dynimcView=baseInfoView.findViewById(R.id.dynimcView);
        serviceSubView=(LinearLayout)baseInfoView.findViewById(R.id.serviceSubView);
        gridview=(GridView)baseInfoView.findViewById(R.id.gridview);
        phLearLayout=(GridView)baseInfoView.findViewById(R.id.phLearLayout);
        idCertifyFlagImage=(ImageView)baseInfoView.findViewById(R.id.idCertifyFlagImage);
        id_text=baseInfoView.findViewById(R.id.id_text);
        id_gou=baseInfoView.findViewById(R.id.id_gou);
        videoCertify=baseInfoView.findViewById(R.id.videoCertify);
        videoCertifyImage=(CircularImage)baseInfoView.findViewById(R.id.videoCertifyImage);
        playIcon=baseInfoView.findViewById(R.id.playIcon);
        vido_text=baseInfoView.findViewById(R.id.vido_text);
        vido_gou=baseInfoView.findViewById(R.id.vido_gou);
        miTaoId=(TextView)baseInfoView.findViewById(R.id.miTaoId);
        phView=baseInfoView.findViewById(R.id.phView);
        serviceView=baseInfoView.findViewById(R.id.serviceView);
        nullBottomView=baseInfoView.findViewById(R.id.nullBottomView);

        sexText=(TextView)baseInfoView.findViewById(R.id.sexText);
        userAge=(TextView)baseInfoView.findViewById(R.id.userAge);
        userGrade=(TextView)baseInfoView.findViewById(R.id.userGrade);
        userSign=(TextView)baseInfoView.findViewById(R.id.userSign);
        loveStatus=(TextView)baseInfoView.findViewById(R.id.loveStatus);
        userCarrier=(TextView)baseInfoView.findViewById(R.id.userCarrier);
        userIncoming=(TextView)baseInfoView.findViewById(R.id.userIncoming);
        userHeight=(TextView)baseInfoView.findViewById(R.id.userHeight);
        userWeight=(TextView)baseInfoView.findViewById(R.id.userWeight);
        loveFeeling=(TextView)baseInfoView.findViewById(R.id.loveFeeling);
        sexFeeling=(TextView)baseInfoView.findViewById(R.id.sexFeeling);
        anotherRequest=(TextView)baseInfoView.findViewById(R.id.anotherRequest);


        actInfoView=mInflater.inflate(R.layout.personal_detail_act_layout, null);
        personActView=(LinearLayout)actInfoView.findViewById(R.id.personActView);
        mPager.addView(baseInfoView);
    }

    private void initListensers() {
        userHeadImage.setOnClickListener(this);
        backButton.setOnClickListener(this);
        scrollView.setScrollViewListener(this);
        guanZhuButtonView.setOnClickListener(this);
        sendNewsButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        videoCertify.setOnClickListener(this);
        idCertify.setOnClickListener(this);
        intoFansZhuView.setOnClickListener(this);
        intoPhMoreView.setOnClickListener(this);
        xiaDanView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        }else if (v.getId()==R.id.guanZhuButtonView){
            boolean islogin = UserPreferencesUtil.isOnline(this);
            if (!islogin) {
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                startActivity(intent);
            }else{
                guanZhuAction();
            }
        }else if (v.getId()==R.id.sendNewsButton){
            boolean islogin = UserPreferencesUtil.isOnline(this);
            if (!islogin) {
                Intent intent = new Intent(this, LoginWelcomActivity.class);
                startActivity(intent);
            }else{
                if (quaryUserImage!=null){
                    if (!TextUtils.isEmpty(pulicUsernick)){
                        ToChatBean toChatBean = new ToChatBean();
                        toChatBean.setRecruitLocIndex("");
                        toChatBean.setNoticeId("");
                        toChatBean.setNoticeSubject(pulicUsernick);
                        toChatBean.setUserIcon(quaryUserImage);
                        toChatBean.setUserId(quaryUserId);
                        toChatBean.setUsernick(pulicUsernick);
                        toChatBean.setRecruitId("");
                        Intent intent=new Intent(this, ChatActivity.class);
                        intent.putExtra("toChatBean", toChatBean);
                        startActivity(intent);
                    }
                }
            }
        }else if (v.getId()==R.id.rightButton){
            Intent intent=new Intent(this, ShareBoardView.class);
            intent.putExtra(Constants.SHARE_ICONURL_KEY,quaryUserImage);
            intent.putExtra("userNickName",pulicUsernick);
            startActivityForResult(intent, 0);
        }else if (v.getId()==R.id.idCertify){
            if (quaryUserId.equals(UserPreferencesUtil.getUserId(this))){
                String idStatus = UserPreferencesUtil.getUserIDAuth(this);
                if (idStatus != null) {
                    if (!"2".equals(idStatus)) {
                       startActivity(new Intent(this, ModelIdentyFirstActivity.class));
                    }
                } else {
                    startActivity(new Intent(this, ModelIdentyFirstActivity.class));
                }
            }
        }else if (v.getId()==R.id.videoCertify){
            if (quaryUserId.equals(UserPreferencesUtil.getUserId(this))){
                String videoStatus = UserPreferencesUtil.getUserVideoAuth(this);
                if (videoStatus != null) {
                    if (!"2".equals(videoStatus)){
                        Intent intent11 = new Intent(this, VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }else{
                        Intent intent = new Intent(this, VideoPlayerActivity.class);
                        intent.putExtra("vidoUrl", video);
                        startActivity(intent);
                    }
                } else {
                    Intent intent13 = new Intent(this, VideoCertifyActivity.class);
                    startActivity(intent13);
                }
            }else{
                if (!TextUtils.isEmpty(video)){
                    Intent intent = new Intent(this, VideoPlayerActivity.class);
                    intent.putExtra("vidoUrl", video);
                    startActivity(intent);
                }
            }
        }else if (v.getId()==R.id.intoPhMoreView){
            Intent intent1 = new Intent(this, PersonalDetailRankActivity.class);
            intent1.putExtra("fromFlag", "browers");
            intent1.putExtra("publihserId", quaryUserId);
            startActivity(intent1);
        }else if (v.getId()==R.id.userHeadImage){
            if (!TextUtils.isEmpty(quaryUserImage)){
                String[] tempImages=new String[1];
                tempImages[0]=quaryUserImage;
                Constants.imageBrower(this,0,tempImages,false);
            }
        }else if (v.getId()==R.id.intoFansZhuView){
            if (!TextUtils.isEmpty(quaryUserId)){
                Intent intent13 = new Intent(this, MyFansActivity.class);
                intent13.putExtra("userId",quaryUserId);
                startActivity(intent13);
            }
        }else if (v.getId()==R.id.xiaDanView){
            Intent intent=new Intent(this,XiaDanDetailActivity.class);
            intent.putExtra("userIcon",quaryUserImage);
            intent.putExtra("userNick",pulicUsernick);
            intent.putExtra("userId",quaryUserId);
            startActivity(intent);
        }
    }

    private void initDatas() {
        serviceView.setVisibility(View.GONE);
        xiaDanView.setVisibility(View.GONE);
        phView.setVisibility(View.GONE);
        xcView.setVisibility(View.GONE);
        dynimcView.setVisibility(View.GONE);
        videoImageLoader  = VideoImageLoader.getInstance();
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        quaryUserId=getIntent().getStringExtra("userId");
        pulicUsernick=getIntent().getStringExtra("userNick");
        if (!TextUtils.isEmpty(pulicUsernick)){
            titleName.setText(pulicUsernick);
        }
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.more_icon);

        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabEntity(titles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        tab_l.setTabData(tabEntities);
        tab_l.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                tab_l1.setCurrentTab(position);
                switchView(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        tab_l1.setTabData(tabEntities);
        tab_l1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                tab_l.setCurrentTab(position);
                switchView(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        tab_l1.setCurrentTab(0);
        tab_l.setCurrentTab(0);
        List<PriceBean> fuFeiDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
        if (fuFeiDatas!=null){
            loadUserInfo();
        }else{
            loadPriceDatas();
        }
    }

    private void updateBottomViews() {
        bottomView.setVisibility(View.VISIBLE);
        bottomView.getBackground().setAlpha(50);
        if (quaryUserId.equals(UserPreferencesUtil.getUserId(this))){
            rightButton.setVisibility(View.INVISIBLE);
            guanZhuButtonView.setVisibility(View.GONE);
            bottomView.setVisibility(View.GONE);
            nullBottomView.setVisibility(View.GONE);
        }else{
            if ("0".equals(UserPreferencesUtil.getCheckOff(this))){
                bottomView.setVisibility(View.VISIBLE);
            }else{
                bottomView.setVisibility(View.GONE);
            }
        }
    }

    private void switchView(int index){
        mPager.removeAllViews();
        switch (index){
            case 0:
                if (isLoadBase){
                    loadUserInfo();
                }
                mPager.addView(baseInfoView);
                break;
            case 1:
                if (personActView.getChildCount()==0){
                    loadUserActions();
                }
                mPager.addView(actInfoView);
                break;
        }
    }

    private class DelayTime implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                mHandler.sendEmptyMessage(0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    tempH=tab_l.getTop();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y>tempH) {
            tab_l1.setVisibility(View.VISIBLE);
        }else{
            tab_l1.setVisibility(View.GONE);
        }
    }

    public void loadPriceDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            ShowMsgDialog.showNoMsg(this, true);
            item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE_PRICE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE_PRICE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void guanZhuAction() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("userId", quaryUserId);
            if (isGuanZhu){//1为关注，2为取消关注
                object.put("status", "2");
            }else{
                object.put("status", "1");
            }
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_USER_ATTEN);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USER_ATTEN);
    }

    private void loadUserInfo() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("userId",quaryUserId);
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_USER_INFO);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USER_INFO);
    }

    private void loadUserActions() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("userId",quaryUserId);
            object.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
            if (hotPage > 1) {
                object.put("reqTime", hotReqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(object);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
//        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_USER_ACTIONS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USER_ACTIONS);
    }


    private void commitJuBaoAction() {
        if (!TextUtils.isEmpty(pulicUsernick)){
            RequestBean item = new RequestBean();
            item.setIsPublic(false);
            try {
                JSONObject object = new JSONObject();
                object.put("freezeUserId",quaryUserId);
                item.setData(object);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.QUEST_ADD_BLACK_LIST);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ADD_BLACK_LIST);
        }else{
            Toast.makeText(this,"网络异常，刷新界面重试",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ADD_BLACK_LIST:
                if ("0000".equals(data.getCode())) {
                    Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
                    addToBlackList();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_USER_ACTIONS:
//                ShowMsgDialog.cancel();
                if ("0000".equals(data.getCode())) {
                    parseUserActDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_USER_INFO:
                ThreadPool.getInstance().excuseThread(new DelayTime());
                if ("0000".equals(data.getCode())) {
                    isLoadBase=false;
                    parseUserInfoDatas(data.getRecord());
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_USER_ATTEN:
                if ("0000".equals(data.getCode())) {
                    if (!isGuanZhu){
                        isGuanZhu=true;
                        guanZhuTipText.setText("已关注");
                        String temCount=fansNumText.getText().toString();
                        fansNumText.setText(String.valueOf(Integer.parseInt(temCount)+1));
                    }else{
                        isGuanZhu=false;
                        guanZhuTipText.setText("关注");
                        String temCount=fansNumText.getText().toString();
                        fansNumText.setText(String.valueOf(Integer.parseInt(temCount)-1));
                    }
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SERVER_TYPE_PRICE:
                if ("0000".equals(data.getCode())) {
                    parsePriceDatas(data.getRecord());
                    loadUserInfo();
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parsePriceDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                List<PriceBean> fuFeiDatas = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    PriceBean item = new PriceBean();
                    item.setPrice(StringUtils.getJsonValue(object, "basePrice"));
                    item.setId(StringUtils.getJsonValue(object, "priceId"));
                    item.setTitleId(StringUtils.getJsonValue(object, "titleId"));
                    item.setTypeId(StringUtils.getJsonValue(object, "typeId"));
                    fuFeiDatas.add(item);
                }
                CacheService.getInstance().cachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS, fuFeiDatas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addToBlackList() {
        boolean isExist=false;
        List<BlackMemberBean> blackListDatasDatas= CacheService.getInstance().getCacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this));
        for (int i=0;i<blackListDatasDatas.size();i++){
            if (quaryUserId.equals(blackListDatasDatas.get(i).getUserId())){
                isExist=true;
                break;
            }
        }
        if (blackListDatasDatas!=null){
            if (!isExist){
                BlackMemberBean item = new BlackMemberBean();
                item.setUsernick(pulicUsernick);
                item.setUserIcon(quaryUserImage);
                item.setUserId(quaryUserId);
                item.setAge(pulicUserAgeStr);
                item.setGender(pulicSexTextStr);
                blackListDatasDatas.add(item);
                CacheService.getInstance().cacheBlackListDatasDatas(UserPreferencesUtil.getUserId(this),blackListDatasDatas);
            }
        }
    }

    private void parseUserActDatas(String record) {
        if (!TextUtils.isEmpty(record) && !"null".equals(record)) {
            List<ActManagerBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(record);
                hotReqTime = object.getString("reqTime");
                String actions = object.getString("actions");
                if (ConstTaskTag.isTrueForArrayObj(actions)) {
                    JSONArray array2 = new JSONArray(actions);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        ActManagerBean item = new ActManagerBean();
                        item.setActId(StringUtils.getJsonValue(object1, "actId"));
                        item.setActNature(StringUtils.getJsonValue(object1, "actNature"));
                        item.setActTitle(StringUtils.getJsonValue(object1, "actTitle"));
                        item.setPartType(StringUtils.getJsonValue(object1, "partType"));
                        item.setPrice(StringUtils.getJsonValue(object1, "price"));
                        item.setShowCoverUrl(StringUtils.getJsonValue(object1, "showCoverUrl"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                hotIsLoading = false;
            }
            updateActionViews(datas);
//            paiSAdapter.addDatas(datas, hotPage);
        } else {
            hotIsLoading = false;
        }
    }

    private void updateActionViews(List<ActManagerBean> datas) {
        personActView.removeAllViews();
        for (ActManagerBean item:datas){
           View convertView = mInflater.inflate(
                   R.layout.act_item_detail, null);
            View bottomLine = mInflater.inflate(
                    R.layout.line_view, null);
            TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
            TextView priceTip=(TextView)convertView.findViewById(R.id.priceTip);
            TextView cherryNums = (TextView) convertView.findViewById(R.id.cherryNums);
            ImageView iconImage = (ImageView) convertView
                    .findViewById(R.id.iconImage);
            ImageView actType = (ImageView) convertView.findViewById(R.id.actType);
            View priceView=convertView.findViewById(R.id.priceView);
            View playIcon=convertView.findViewById(R.id.playIcon);
            playIcon.setVisibility(View.VISIBLE);
            nameText.setText(item.getActTitle());
            cherryNums.setText(ConstTaskTag.getIntPrice(item.getPrice()));
            if ("1".equals(item.getActNature())){//竞拍约
                priceTip.setText("目前");
                actType.setImageResource(R.drawable.act_flag_jinpai);
            }else if ("2".equals(item.getActNature())){//付费约
                priceTip.setText("每小时");
                actType.setImageResource(R.drawable.act_flag_fufei);
            }else if ("3".equals(item.getActNature())){//送礼约
                actType.setImageResource(R.drawable.act_flag_gift);
                priceView.setVisibility(View.GONE);
            }
            mImageLoader.loadImage(item.getShowCoverUrl(), iconImage, true);
            convertView.setOnClickListener(new IntoDetial(item));
            personActView.addView(convertView);
            personActView.addView(bottomLine);
        }
    }

    private class IntoDetial implements OnClickListener{
        private ActManagerBean item;
        public IntoDetial(ActManagerBean item){
            this.item=item;
        }

        @Override
        public void onClick(View v) {
            intoAction(item);
        }
    }

    private void intoAction(ActManagerBean item) {
        Intent intent=new Intent();
        if ("1".equals(item.getActNature())){
            intent.setClass(this, JinPaiDetailActivity.class);
        }else if ("2".equals(item.getActNature())){
            intent.setClass(this, JinPaiFuFeiDetailActivity.class);
        }else if ("3".equals(item.getActNature())){
            intent.setClass(this, YuePaiDetailActivity.class);
            intent.putExtra("fromFlag", "browers");
        }
        intent.putExtra("actId", item.getActId());
        intent.putExtra("userId",item.getUserId());
        startActivity(intent);
    }

    private void parseUserInfoDatas(String record) {
        try {
            if (!TextUtils.isEmpty(record)&&!"".equals(record)){
                JSONObject object=new JSONObject(record);

                String fansCount=StringUtils.getJsonValue(object,"fansCount");
                fansNumText.setText(fansCount);
                String hasAttent=StringUtils.getJsonValue(object,"hasAttent");
                if ("1".equals(hasAttent)){
                    isGuanZhu=true;
                    guanZhuTipText.setText("已关注");
                }else{
                    isGuanZhu=false;
                    guanZhuTipText.setText("关注");
                }

                //关于我的部分
                String miTaoIdStr=null,userGradeStr=null,userSignStr=null,loveStatusStr=null,userCarrierStr=null,userIncomingStr=null,userHeightStr=null,userWeightStr=null,loveFeelingStr=null,sexFeelingStr=null,anotherRequestStr=null;

                //Base部分
                String base= StringUtils.getJsonValue(object,"base");
                if (!TextUtils.isEmpty(base)&&!"".equals(base)){
                    JSONObject baseJson=new JSONObject(base);
                    miTaoIdStr=StringUtils.getJsonValue(baseJson,"userPin");
                    pulicSexTextStr=StringUtils.getJsonValue(baseJson,"gender");
                    pulicUserAgeStr=StringUtils.getJsonValue(baseJson,"age");
                    userSignStr=StringUtils.getJsonValue(baseJson,"introDesc");

                    pulicUsernick=StringUtils.getJsonValue(baseJson,"usernick");
                    userNickText.setText(pulicUsernick);
                    titleName.setText(pulicUsernick);
                    String province=StringUtils.getJsonValue(baseJson,"province");
                    String city=StringUtils.getJsonValue(baseJson,"city");
                    if (!TextUtils.isEmpty(province)){
                        AssetDataBaseManager.CityBean provinceBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(province));
                        if (!TextUtils.isEmpty(city)){
                            AssetDataBaseManager.CityBean cityBean = AssetDataBaseManager.getManager().queryCityById(Integer.parseInt(city));
                            cityText.setText(provinceBean.getName().concat(" ").concat(cityBean.getName()));
                        }else{
                            cityText.setText(provinceBean.getName());
                        }
                    }else{
                        cityText.setVisibility(View.GONE);
                    }

                    String userIcon=StringUtils.getJsonValue(baseJson,"userIcon");
                    if (!TextUtils.isEmpty(userIcon)){
                        quaryUserImage=userIcon;
                        mImageLoader.loadImage(userIcon,userHeadImage, true);
                    }
                    video=StringUtils.getJsonValue(baseJson,"video");
                }

                //关于我的部分数据
                String body=StringUtils.getJsonValue(object,"body");
                if (!TextUtils.isEmpty(body)){
                    JSONObject bodyJson=new JSONObject(body);
                    userHeightStr=StringUtils.getJsonValue(bodyJson,"height");
                    userWeightStr=StringUtils.getJsonValue(bodyJson,"weight");
                }
                String other=StringUtils.getJsonValue(object,"other");
                if (!TextUtils.isEmpty(other)){
                    JSONObject otherJson=new JSONObject(other);
                    loveStatusStr=StringUtils.getJsonValue(otherJson,"loveStatus");
                    userCarrierStr=StringUtils.getJsonValue(otherJson,"job");
                    userIncomingStr=StringUtils.getJsonValue(otherJson,"salary");
                    loveFeelingStr=StringUtils.getJsonValue(otherJson,"loveOpinion");
                    sexFeelingStr=StringUtils.getJsonValue(otherJson,"sexOpinion");
                    anotherRequestStr=StringUtils.getJsonValue(otherJson,"mateOpinion");
                }

                //关于我的显示
                if (!TextUtils.isEmpty(miTaoIdStr)){
                    miTaoId.setText(miTaoIdStr);
                }
                if (!TextUtils.isEmpty(pulicSexTextStr)){
                    if (Constants.SEX_WOMAN.equals(pulicSexTextStr)) {
                        sexText.setText("女");
                    } else if (Constants.SEX_MAN.equals(pulicSexTextStr)) {
                        sexText.setText("男");
                    }
                }
                if (!TextUtils.isEmpty(pulicUserAgeStr)){
                    userAge.setText(pulicUserAgeStr);
                }
                if (!TextUtils.isEmpty(userSignStr)){
                    userSign.setText(userSignStr);
                }
                if (!TextUtils.isEmpty(userHeightStr)){
                    userHeight.setText(userHeightStr.concat("CM"));
                }
                if (!TextUtils.isEmpty(userWeightStr)){
                    userWeight.setText(userWeightStr.concat("KG"));
                }
                if (!TextUtils.isEmpty(loveStatusStr)){
                    String[] loveStas=ConstTaskTag.LOVE_OPTION;
                    loveStatus.setText(loveStas[Integer.parseInt(loveStatusStr)-1]);
                }
                if (!TextUtils.isEmpty(userCarrierStr)){
                    userCarrier.setText(userCarrierStr);
                }
                if (!TextUtils.isEmpty(userIncomingStr)){
                    String[] salary=ConstTaskTag.SALARY_ARRAY;
                    userIncoming.setText(salary[Integer.parseInt(userIncomingStr)-1]);
                }
                if (!TextUtils.isEmpty(loveFeelingStr)){
                    loveFeeling.setText(loveFeelingStr);
                }
                if (!TextUtils.isEmpty(sexFeelingStr)){
                    sexFeeling.setText(sexFeelingStr);
                }
                if (!TextUtils.isEmpty(anotherRequestStr)){
                    anotherRequest.setText(anotherRequestStr);
                }

                //相册部分
                String photos=StringUtils.getJsonValue(object,"photos");
                if (ConstTaskTag.isTrueForArrayObj(photos)){
                    JSONArray array=new JSONArray(photos);
                    List<PhotoBean> vector=new ArrayList<>();
                    for (int i=0;i<array.length();i++){
                        JSONObject photoObject=array.getJSONObject(i);
                        PhotoBean item=new PhotoBean();
                        item.setCreateTime(StringUtils.getJsonValue(photoObject, "createTime"));
                        item.setResUrl(StringUtils.getJsonValue(photoObject, "resUrl"));
                        vector.add(item);
                    }
                    if (vector.size()>0){
                        xcView.setVisibility(View.VISIBLE);
                        photoAdapter=new PersonDetailPhotoAdapter(this,vector);
                        gridview.setAdapter(photoAdapter);
                        gridview.setOnItemClickListener(new BrowsPhotoes());
                        if (vector.size()==6){
                            intoPhotoMoreView.setVisibility(View.VISIBLE);
                            intoPhotoMoreView.setOnClickListener(new MorePhotoes());
                        }else{
                            intoPhotoMoreView.setVisibility(View.GONE);
                        }
                    }
                }

                //礼物排行
                String ranks=StringUtils.getJsonValue(object,"ranks");
                if (ConstTaskTag.isTrueForArrayObj(ranks)){
                    JSONArray array=new JSONArray(ranks);
                    List<PersonDetailPersenterBean> vector=new ArrayList<>();
                    for (int i=0;i<array.length();i++){
                        JSONObject photoObject=array.getJSONObject(i);
                        String participator=StringUtils.getJsonValue(photoObject, "user");
                        JSONObject object4=new JSONObject(participator);
                        PersonDetailPersenterBean item=new PersonDetailPersenterBean();
                        item.setUserId(StringUtils.getJsonValue(object4, "userId"));
                        item.setUserIcon(StringUtils.getJsonValue(object4, "userIcon"));
                        item.setUsernick(StringUtils.getJsonValue(object4, "usernick"));
                        vector.add(item);
                    }
                    if (vector.size()>0){
                        phView.setVisibility(View.VISIBLE);
                        PersonDetailPresenterAdapter photoAdapter=new PersonDetailPresenterAdapter(this,vector);
                        phLearLayout.setAdapter(photoAdapter);
                        if (vector.size()==5){
                            intoPhMoreView.setVisibility(View.VISIBLE);
                        }else{
                            intoPhMoreView.setVisibility(View.GONE);
                        }
                    }
                }
                updateBottomViews();
                //大神认证相关
                String experts=StringUtils.getJsonValue(object,"experts");
                if (ConstTaskTag.isTrueForArrayObj(experts)){
                    JSONArray array=new JSONArray(experts);
                    List<GodBean> vector=new ArrayList<>();
                    for (int i=0;i<array.length();i++){
                        JSONObject object4=array.getJSONObject(i);
                        GodBean item=new GodBean();
                        item.setSkillCode(StringUtils.getJsonValue(object4, "skillCode"));
                        item.setCoverUrl(StringUtils.getJsonValue(object4, "coverUrl"));
                        item.setOrderCount(StringUtils.getJsonValue(object4, "orderCount"));
                        item.setPriceId(StringUtils.getJsonValue(object4, "priceId"));
                        item.setPriceRate(StringUtils.getJsonValue(object4, "priceRate"));
                        item.setSkillTitle(StringUtils.getJsonValue(object4, "skillTitle"));
                        vector.add(item);
                    }
                    if (vector.size()>0){
                        initGodViews(vector);
                    }else{
                        serviceView.setVisibility(View.GONE);
                        xiaDanView.setVisibility(View.GONE);
                    }
                }else{
                    serviceView.setVisibility(View.GONE);
                    xiaDanView.setVisibility(View.GONE);
                }

                if (gridview.getVisibility()==View.GONE&&phLearLayout.getVisibility()==View.GONE){
                    middleBlockLineView.setVisibility(View.GONE);
                }

                //认证部分
                String auth=StringUtils.getJsonValue(object, "auth");
                String idStatus=null,videoStatus=null;

                if (!TextUtils.isEmpty(auth)){
                    JSONObject object1=new JSONObject(auth);
                    idStatus=StringUtils.getJsonValue(object1,"idcardAuth");
                    videoStatus=StringUtils.getJsonValue(object1,"videoAuth");
                }

                if (!TextUtils.isEmpty(idStatus)) {
                    if ("2".equals(idStatus)) {
                        idCertifyFlagImage.setImageResource(R.drawable.second_user_id_certify);
                        id_text.setVisibility(View.GONE);
                        id_gou.setVisibility(View.VISIBLE);
                    } else {
                        idCertifyFlagImage.setImageResource(R.drawable.second_user_id_certify1);
                        id_text.setVisibility(View.VISIBLE);
                        id_gou.setVisibility(View.GONE);
                    }
                } else {
                    idCertifyFlagImage.setImageResource(R.drawable.second_user_id_certify1);
                    id_text.setVisibility(View.VISIBLE);
                    id_gou.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(videoStatus)) {
                    if ("2".equals(videoStatus)) {
                        playIcon.setVisibility(View.VISIBLE);
                        videoCertifyImage.setImageResource(R.drawable.second_user_viod_certify);
                        videoImageLoader.DisplayImage(video, videoCertifyImage);
                        vido_text.setVisibility(View.GONE);
                        vido_gou.setVisibility(View.VISIBLE);
                    } else {
                        playIcon.setVisibility(View.GONE);
                        videoCertifyImage.setImageResource(R.drawable.second_user_viod_certify);
                        vido_text.setVisibility(View.VISIBLE);
                        vido_gou.setVisibility(View.GONE);
                    }
                } else {
                    playIcon.setVisibility(View.GONE);
                    videoCertifyImage.setImageResource(R.drawable.second_user_viod_certify);
                    vido_text.setVisibility(View.VISIBLE);
                    vido_gou.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initGodViews( List<GodBean> vector) {
        xiaDanView.setVisibility(View.VISIBLE);
        serviceView.setVisibility(View.VISIBLE);
        for (GodBean item:vector){
            View godView=LayoutInflater.from(this).inflate(R.layout.personal_info_god_item,null);
            ImageView userImage=(ImageView)godView.findViewById(R.id.userImage);
            CircularImage godAppIcon=(CircularImage)godView.findViewById(R.id.godAppIcon);
            TextView userName=(TextView)godView.findViewById(R.id.userName);
            TextView userLable=(TextView)godView.findViewById(R.id.userLable);
            TextView priceValue=(TextView)godView.findViewById(R.id.priceValue);
            TextView twiceText=(TextView)godView.findViewById(R.id.twiceText);
            twiceText.setText("接单".concat(item.getOrderCount()).concat("次"));
            List<PriceBean> fuFeiDatas= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
            if (fuFeiDatas!=null){
                for (PriceBean t:fuFeiDatas){
                    if (t.getId().equals(item.getPriceId())){
                        int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(item.getPriceRate()))/100;
                        priceValue.setText(String.valueOf(value));
                        break;
                    }
                }
            }
            List<JinPaiBigTypeBean> typeDatas = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
            for (JinPaiBigTypeBean typeBean:typeDatas){
                if (typeBean.getId().equals(item.getSkillCode())){
                    List<GodLableBean> typeLable=Constants.getGodLableDatas(typeBean.getSubStr());
                    if (typeLable!=null){
                        for (GodLableBean it:typeLable){
                            if (it.getTitleId().equals(item.getSkillTitle())){
                                userLable.setText(it.getTitleName());
                                userName.setText(typeBean.getName());
                                mImageLoader.loadImage(typeBean.getUrl(), godAppIcon, true);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            mImageLoader.loadImage(item.getCoverUrl(), userImage, true);
            godView.setOnClickListener(new IntoGodDetail(item));
            serviceSubView.addView(godView);
        }
    }

    private class IntoGodDetail implements OnClickListener{
        private GodBean item;
        public IntoGodDetail(GodBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            intoGodDetailAct(item);
        }
    }

    private void intoGodDetailAct(GodBean item) {
        Intent intent=new Intent(this,GodDetailActivity.class);
        intent.putExtra("userId",quaryUserId);
        intent.putExtra("userName", pulicUsernick);
        intent.putExtra("skillCode",item.getSkillCode());
        startActivity(intent);
    }

    private class BrowsPhotoes implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<PhotoBean> photoes= photoAdapter.getDatas();
            String[] urls = new String[photoes.size()];
            for (int j = 0; j < photoes.size(); j++) {
                urls[j] = photoes.get(j).getResUrl();
            }
            Constants.imageBrower(PersonalDetailActivity.this, position, urls, false);
        }
    }

    private class MorePhotoes implements OnClickListener{
        @Override
        public void onClick(View v) {
            intoPhMoreViewAction();
        }
    }

    private void intoPhMoreViewAction(){
        Intent intent=new Intent(this,PohoesBrowserActivity.class);
        intent.putExtra("quaryUserId",quaryUserId);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                String flag=data.getStringExtra(Constants.COMEBACK);
                if ("jubao".equals(flag)){
                    boolean islogin = UserPreferencesUtil.isOnline(this);
                    if (!islogin) {
                        Intent intent = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(this, ReportFristActivity.class);
                        intent.putExtra("resType", Constants.JUBAO_TYPE_YUNHU);
                        intent.putExtra("userId", quaryUserId);
                        intent.putExtra("resourceId", quaryUserId);
                        startActivity(intent);
                    }
                }else if ("blackList".equals(flag)){
                    boolean islogin = UserPreferencesUtil.isOnline(this);
                    if (!islogin) {
                        Intent intent = new Intent(this, LoginWelcomActivity.class);
                        startActivity(intent);
                    }else{
                        commitJuBaoAction();
                    }
                }
                break;
            }
            default:
                break;
        }
    }
}
