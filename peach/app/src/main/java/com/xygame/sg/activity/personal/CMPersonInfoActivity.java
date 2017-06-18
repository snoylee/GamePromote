package com.xygame.sg.activity.personal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.nineoldandroids.view.ViewHelper;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.personal.adapter.CMSlidingPagerAdapter;
import com.xygame.sg.activity.personal.bean.MeOverview;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.activity.personal.bean.UserLogo;
import com.xygame.sg.activity.personal.bean.UserType;
import com.xygame.sg.activity.personal.bean.UserVideo;
import com.xygame.sg.activity.personal.bean.VideoBean;
import com.xygame.sg.activity.personal.fragment.ScrollTabHolder;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.ChoiceShareReportActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.imageloader.MemoryCache;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.RecycleBitmap;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.broadcast.EditorUserInfoBroadcastListener;
import com.xygame.sg.vido.VideoPlayerActivity;
import com.xygame.sg.widget.banner.SimpleImageBanner;
import com.xygame.sg.widget.banner.entity.BannerItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 摄影师页面
 */
public class CMPersonInfoActivity extends SGBaseActivity implements ScrollTabHolder, OnPageChangeListener, View.OnClickListener {
    private String editOrQueryFlag;//是查看还是编辑
    private String queryedUserId;//要查看的用户id
    private String queryUserNick;//要查看的用户昵称
    private String queryUserType = Constants.CARRE_MODEL;//要查看的用户昵称
    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;

    private View backButton, rightButton,sysView,jjrView;
    private TextView titleName, rightButtonText;
    private ImageView rightbuttonIcon;

    private LinearLayout query_type_ll;
    private TextView type_icon_tv;//用户类型icon（模特or摄影师）
    private TextView type_tv;//用户类型text（模特or摄影师）
    private TextView fans_num_tv;//粉丝数
    private TextView to_attemtion_tv;//关注
    private TextView cancel_attemtion_tv;//取消关注

    private TextView edit_avtar_tv;//点击跳转到编辑头像页面

    private RelativeLayout banner_rl;

    private LinearLayout header;
    private int headerHeight;
    private int headerTranslationDis;
    private ViewPager viewPager;
    private CMSlidingPagerAdapter adapter;
    private List<String> userHeads = new ArrayList<String>();
    private VideoBean videoBean = new VideoBean();
    private String typeFlag;

    private CommonTabLayout tab_l;
    private String[] titles = {"作品", "资料"};
    private int[] iconUnselectIds = { R.drawable.tab_works_unselect,  R.drawable.tab_data_unselect};
    private int[] iconSelectIds = { R.drawable.tab_works_select,R.drawable.tab_data_select};
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<CustomTabEntity>();

//    VisitUnit visitUnit = new VisitUnit();
    private String shareIconUrl = "";//分享用头像图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cm_person_info);
        EditorUserInfoBroadcastListener.registerEditorUserInfoListener(this, mBroadcastReceiver);
        editOrQueryFlag = getIntent().getStringExtra(Constants.EDIT_OR_QUERY_FLAG);
        if (editOrQueryFlag.equals(Constants.QUERY_INFO_FLAG)) {
            queryedUserId = getIntent().getStringExtra("userId");//要查看的用户id
            if (getIntent().hasExtra("userNick")){
                queryUserNick = getIntent().getStringExtra("userNick");//要查看的用户昵称
            }
            isQuery = true;
        }
        initViews();
        initDatas();
        addListener();
        requestData();
    }

    private void initViews() {
        rightButton = findViewById(R.id.rightButton);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        edit_avtar_tv = (TextView) findViewById(R.id.edit_avtar_tv);
        type_icon_tv = (TextView) findViewById(R.id.type_icon_tv);
        type_tv = (TextView) findViewById(R.id.type_tv);
        sysView=findViewById(R.id.sysView);
        jjrView=findViewById(R.id.jjrView);
        fans_num_tv = (TextView) findViewById(R.id.fans_num_tv);
        to_attemtion_tv = (TextView) findViewById(R.id.to_attemtion_tv);
        cancel_attemtion_tv = (TextView) findViewById(R.id.cancel_attemtion_tv);
        tab_l = (CommonTabLayout) findViewById(R.id.tab_l);
        viewPager = (ViewPager) findViewById(R.id.pager);
        header = (LinearLayout) findViewById(R.id.header);


        banner_rl = (RelativeLayout) findViewById(R.id.banner_rl);

        query_type_ll = (LinearLayout) findViewById(R.id.query_type_ll);

        if (editOrQueryFlag.equals(Constants.EDIT_INFO_FLAG)) {
            titleName.setText(UserPreferencesUtil.getUserNickName(this));
            query_type_ll.setVisibility(View.GONE);
            edit_avtar_tv.setVisibility(View.VISIBLE);
            to_attemtion_tv.setVisibility(View.GONE);
            cancel_attemtion_tv.setVisibility(View.GONE);
        } else if (editOrQueryFlag.equals(Constants.QUERY_INFO_FLAG)) {
            titleName.setText(queryUserNick);
            query_type_ll.setVisibility(View.VISIBLE);
            edit_avtar_tv.setVisibility(View.GONE);
            to_attemtion_tv.setVisibility(View.GONE);
            cancel_attemtion_tv.setVisibility(View.GONE);
        }
    }

    private void initDatas() {
        if (isQuery) {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setVisibility(View.VISIBLE);
            rightbuttonIcon.setVisibility(View.VISIBLE);
            rightButtonText.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }

        headerHeight = getResources().getDimensionPixelSize(R.dimen.max_header_height);
        headerTranslationDis = -getResources().getDimensionPixelSize(R.dimen.header_offset_dis);

        if (editOrQueryFlag.equals(Constants.QUERY_INFO_FLAG)) {
            adapter = new CMSlidingPagerAdapter(getSupportFragmentManager(), this, viewPager, true);
        } else {
            adapter = new CMSlidingPagerAdapter(getSupportFragmentManager(), this, viewPager, false);
        }
        adapter.setTabHolderScrollingListener(this);//控制页面上滑
        viewPager.setOffscreenPageLimit(adapter.getCacheCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabEntity(titles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        tab_l.setTabData(tabEntities);
        tab_l.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        edit_avtar_tv.setOnClickListener(this);
        to_attemtion_tv.setOnClickListener(this);
        cancel_attemtion_tv.setOnClickListener(this);
        rightbuttonIcon.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
    }


    /**
     * 主要用于获取头像
     */
    private void requestData() {


        if (editOrQueryFlag.equals(Constants.EDIT_INFO_FLAG)) {
            RequestBean item = new RequestBean();
            try {
                JSONObject obj = new JSONObject();
                obj.put("userId",UserPreferencesUtil.getUserId(this));
                item.setData(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            item.setServiceURL(ConstTaskTag.QUERY_MYOVERVIEW_URL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MYOVERVIEW_INT);
        } else if (editOrQueryFlag.equals(Constants.QUERY_INFO_FLAG)) {

            RequestBean item = new RequestBean();
            try {
                JSONObject obj = new JSONObject();
                obj.put("seeUserId",queryedUserId);
                if (UserPreferencesUtil.isOnline(this)){
                    obj.put("userId", UserPreferencesUtil.getUserId(this));
                }
//                else {
//                    obj.put("userId", queryedUserId);
//                }
                item.setData(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            item.setServiceURL(ConstTaskTag.QUEST_MODEL_INFO);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_MODEL_INFO);
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_MYOVERVIEW_INT:
                responseOverView(data);
                break;
            case ConstTaskTag.RESPOSE_MODEL_INFO:
                responseModelData(data);
                break;
            case ConstTaskTag.EDIT_ATTENTION_INT:
                setAttention(data);
                break;
        }
    }
    private void responseOverView(ResponseBean data){
//        MeOverview meOverview = JSON.parseObject(data.getRecord(), MeOverview.class);
        SimpleImageBanner sib_simple_usage = (SimpleImageBanner) getLayoutInflater().inflate(R.layout.banner_view, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sib_simple_usage.setLayoutParams(params);
        banner_rl.removeAllViews();
        banner_rl.addView(sib_simple_usage);
        userHeads.clear();
        ArrayList<BannerItem> bannerItems = new ArrayList<BannerItem>();
//        if (editOrQueryFlag.equals(Constants.EDIT_INFO_FLAG)) {
//            bannerItems = getList(meOverview);
//            sib_simple_usage.setSource(bannerItems).startScroll();
//        }
//        sib_simple_usage.setOnItemClickL(new BannerClickListenner(bannerItems));
        String userType = UserPreferencesUtil.getUserType(this);
        Intent intent=new Intent("com.xygame.pinfo.action");
        intent.putExtra("userType",userType);
        intent.putExtra("queryedUserId", queryedUserId);
        intent.putExtra("editOrQueryFlag",editOrQueryFlag);
        sendBroadcast(intent);
    }

    public ArrayList<BannerItem> getList(MeOverview meOverview) {
        List<UserLogo> userIconsTemp = meOverview.getExtLogos();
        UserLogo defaultIconMap = new UserLogo();
        defaultIconMap.setLogoUrl(meOverview.getUserIcon());
        defaultIconMap.setLocIndex(1);
        List<UserLogo> userIcons = new ArrayList<>();
        userIcons.add(defaultIconMap);
        userIcons.addAll(userIconsTemp);
        ArrayList<BannerItem> list = new ArrayList<BannerItem>();
        if (meOverview.getVideo() != null) {
            UserVideo video = meOverview.getVideo();
            BannerItem item = new BannerItem();
            item.setType(1);
            item.setVideoUrl(video.getVideoUrl());
            item.setVideoSize(video.getVideoSize()+0l);
            item.setVideoTime(video.getVideoTime()+0l);
            list.add(item);
            videoBean = new VideoBean();
            videoBean.setId(video.getId()+"");
            videoBean.setVideoUrl(item.getVideoUrl());
            videoBean.setVideoSize(item.getVideoSize());
            videoBean.setVideoTime(item.getVideoTime());
        }
        for (UserLogo map : userIcons) {
            String userIconUrl = map.getLogoUrl();
            String locIndex = map.getLocIndex()+"";
            userHeads.add(userIconUrl);

            BannerItem item = new BannerItem();
            item.imgUrl = userIconUrl;
            item.title = locIndex;
            item.setType(0);
            list.add(item);
        }

        return list;
    }

    private void responseModelData(ResponseBean data){
//        ModelDataVo modelDataVo = JSON.parseObject(data.getRecord(), ModelDataVo.class);
        SimpleImageBanner sib_simple_usage = (SimpleImageBanner) getLayoutInflater().inflate(R.layout.banner_view, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sib_simple_usage.setLayoutParams(params);
        banner_rl.removeAllViews();
        banner_rl.addView(sib_simple_usage);
        userHeads.clear();
        ArrayList<BannerItem> bannerItems = new ArrayList<BannerItem>();
//        if (editOrQueryFlag.equals(Constants.QUERY_INFO_FLAG)) {
//            String userNick = modelDataVo.getUsernick();
//            titleName.setText(userNick);
//            bannerItems = getListFromQuery(modelDataVo);
//            sib_simple_usage.setSource(bannerItems).startScroll();
//            boolean isFans = modelDataVo.getIsFans();
//            if (isFans) {
//                to_attemtion_tv.setVisibility(View.GONE);
//                cancel_attemtion_tv.setVisibility(View.VISIBLE);
//            } else {
//                if (queryedUserId.equals(UserPreferencesUtil.getUserId(this))) {
//                    to_attemtion_tv.setVisibility(View.GONE);
//                } else {
//                    to_attemtion_tv.setVisibility(View.VISIBLE);
//                }
//                cancel_attemtion_tv.setVisibility(View.GONE);
//            }
//
//            String fansCount = modelDataVo.getFansCount()+"";
//            fans_num_tv.setText(fansCount);
//            List<UserType> utypesList = modelDataVo.getUserTypes();
//            UserType utypes = new UserType();
//            if (utypesList.size() > 1) {
//                utypes = utypesList.get(1);
//            } else {
//                utypes = utypesList.get(0);
//            }
//            String userType = utypes.getUtype()+"";
//            if (Constants.CARRE_PHOTOR.equals(userType)) {
//                sysView.setVisibility(View.VISIBLE);
//                jjrView.setVisibility(View.GONE);
//                typeFlag=Constants.CARRE_PHOTOR;
//                Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
//                type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
//                type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
//                type_tv.setText("摄影师");
//                type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
//            }else if (Constants.CARRE_MERCHANT.equals(userType)){
//                typeFlag=Constants.CARRE_MERCHANT;
//                sysView.setVisibility(View.GONE);
//                jjrView.setVisibility(View.VISIBLE);
////                Drawable cameramanDrawable = getResources().getDrawable(R.drawable.cameraman_identy_icon);
////                type_icon_tv.setCompoundDrawablesWithIntrinsicBounds(cameramanDrawable, null, null, null);
////                type_icon_tv.setBackgroundResource(R.drawable.identy_type_cm_icon_bg);
////                type_tv.setText("经纪人");
////                type_tv.setBackgroundResource(R.drawable.identy_cm_type_bg);
//            }
//
//            Intent intent=new Intent("com.xygame.pinfo.action");
//            intent.putExtra("userType",userType);
//            intent.putExtra("queryedUserId",queryedUserId);
//            intent.putExtra("editOrQueryFlag",editOrQueryFlag);
//            sendBroadcast(intent);
//
//        }
        sib_simple_usage.setOnItemClickL(new BannerClickListenner(bannerItems));
    }
    public ArrayList<BannerItem> getListFromQuery(ModelDataVo modelDataVo) {
        ArrayList<BannerItem> list = new ArrayList<BannerItem>();
        List<String> userIcons = modelDataVo.getUserIcons();
        queryUserType = modelDataVo.getUserTypes().get(0).getUtype()+"";
        if (!StringUtils.isEmpty(modelDataVo.getVideoUrl())) {
            BannerItem item = new BannerItem();
            item.setType(1);
            item.setVideoUrl(modelDataVo.getVideoUrl());
            list.add(item);
            videoBean = new VideoBean();
            videoBean.setVideoUrl(item.getVideoUrl());
        }
        for (int i = 0; i < userIcons.size(); i++) {
            String userIconUrl = userIcons.get(i);
            userHeads.add(userIconUrl);
            BannerItem item = new BannerItem();
            item.imgUrl = userIconUrl;
            item.title = userIconUrl;
            list.add(item);
            if (i == 0) {
                shareIconUrl = userIconUrl;
            }
        }

        return list;
    }

    public void setAttention(ResponseBean data) {

        String status = null;
        try {
            JSONObject pMap=new JSONObject(data.getRecord());
            status = pMap.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status.equals("1")) {
            to_attemtion_tv.setVisibility(View.GONE);
            cancel_attemtion_tv.setVisibility(View.VISIBLE);
            int num = Integer.parseInt(fans_num_tv.getText().toString()) + 1;
            fans_num_tv.setText(num + "");
            Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
        } else if (status.equals("3")) {
            to_attemtion_tv.setVisibility(View.VISIBLE);
            cancel_attemtion_tv.setVisibility(View.GONE);
            Toast.makeText(this, "已取消关注", Toast.LENGTH_SHORT).show();
            int num = Integer.parseInt(fans_num_tv.getText().toString()) - 1;
            fans_num_tv.setText(num + "");
        }
        Intent intent = new Intent(Constants.MODEL_ATTENTION_STATUS_CHANGE);
        sendBroadcast(intent);
    }



    public class BannerClickListenner implements BaseBanner.OnItemClickL {
        List<BannerItem> bannerItems;

        public BannerClickListenner(ArrayList<BannerItem> bannerItems) {
            this.bannerItems = bannerItems;
        }

        @Override
        public void onItemClick(int position) {
            BannerItem bannerItem = bannerItems.get(position);
            if (bannerItem.getType() == 1) {
                Intent intent = new Intent(CMPersonInfoActivity.this, VideoPlayerActivity.class);
                intent.putExtra("vidoUrl", bannerItem.getVideoUrl());
//                Intent intent = new Intent(CMPersonInfoActivity.this, BBVideoPlayer.class);
//                intent.putExtra("url", bannerItem.getVideoUrl());
//                String fileName=Constants.getMP4Name(CMPersonInfoActivity.this);
//                String vidoPath = FileUtil.MP4_ROOT_PATH.concat(fileName);
//                intent.putExtra("cache",vidoPath);
//                startActivity(intent);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tab_l.setCurrentTab(position);
        reLocation = true;
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = adapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        if (NEED_RELAYOUT) {
            currentHolder.adjustScroll(header.getHeight() + headerTop);// 修正滚出去的偏移量
        } else {
            currentHolder.adjustScroll((int) (header.getHeight() + ViewHelper.getTranslationY(header)));// 修正滚出去的偏移量
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    private boolean reLocation = false;

    private int headerScrollSize = 0;

    public static final boolean NEED_RELAYOUT = Integer.valueOf(Build.VERSION.SDK).intValue() < Build.VERSION_CODES.HONEYCOMB;

    private int headerTop = 0;

    // 刷新头部显示时，没有onScroll回调，只有当刷新时会有
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount,
                         int pagePosition) {
        if (viewPager.getCurrentItem() != pagePosition) {
            return;
        }
        if (headerScrollSize == 0 && reLocation) {
            reLocation = false;
            return;
        }
        reLocation = false;
        int scrollY = Math.max(-getScrollY(view), headerTranslationDis);
        if (NEED_RELAYOUT) {
            headerTop = scrollY;
            header.post(new Runnable() {
                @Override
                public void run() {
                    header.layout(0, headerTop, header.getWidth(), headerTop + header.getHeight());
                }
            });
        } else {
            ViewHelper.setTranslationY(header, scrollY);
        }
    }

    /**
     * 主要算这玩意，PullToRefreshListView插入了一个刷新头部，因此要根据不同的情况计算当前的偏移量</br>
     * <p>
     * 当刷新时： 刷新头部显示，因此偏移量要加上刷新头的数值 未刷新时： 偏移量不计算头部。
     * <p>
     * firstVisiblePosition >1时，listview中的项开始显示，姑且认为每一项等高来计算偏移量（其实只要显示一个项，向上偏移
     * 量已经大于头部的最大偏移量，因此不准确也没有关系）
     *
     * @param view
     * @return
     */
    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int top = c.getTop();
        int firstVisiblePosition = view.getFirstVisiblePosition();
        if (firstVisiblePosition == 0) {
            return -top + headerScrollSize;
        } else if (firstVisiblePosition == 1) {
            return -top;
        } else {
            return -top + (firstVisiblePosition - 2) * c.getHeight() + headerHeight;
        }
    }

    // 与onHeadScroll互斥，不能同时执行
    @Override
    public void onHeaderScroll(boolean isRefreashing, int value, int pagePosition) {
        if (viewPager.getCurrentItem() != pagePosition) {
            return;
        }
        headerScrollSize = value;
        if (NEED_RELAYOUT) {
            header.post(new Runnable() {
                @Override
                public void run() {
                    header.layout(0, -headerScrollSize, header.getWidth(), -headerScrollSize + header.getHeight());
                }
            });
        } else {
            ViewHelper.setTranslationY(header, -value);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rightButtonText:
                if (typeFlag!=null){
                    if (Constants.CARRE_PHOTOR.equals(typeFlag)) {
                        Intent intent = new Intent(this,ShareBoardView.class);
                        intent.putExtra(Constants.SHARE_TITLE_KEY,getString(R.string.share_sub_title_model));
                        intent.putExtra(Constants.SHARE_SUBTITLE_KEY,"照片找他拍就对了！网红模特的好伙伴，摄影师"+queryUserNick+"在模范儿等待你的加入！");
                        intent.putExtra(Constants.SHARE_ICONURL_KEY,shareIconUrl);
                        startActivity(intent);
                    }else if (Constants.CARRE_MERCHANT.equals(typeFlag)){
                        Intent intent = new Intent(this,ShareBoardView.class);
                        intent.putExtra(Constants.SHARE_TITLE_KEY,getString(R.string.share_sub_title_model));
                        intent.putExtra(Constants.SHARE_SUBTITLE_KEY,"超多通告找他就对了，网红模特的好伙伴，经纪人"+queryUserNick+"在模范儿等待你的加入！");
                        intent.putExtra(Constants.SHARE_ICONURL_KEY,shareIconUrl);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.rightbuttonIcon:
                if(!UserPreferencesUtil.isOnline(this)){
                    Intent intent8 = new Intent(this, LoginWelcomActivity.class);
                    startActivity(intent8);
                } else {
                    Intent intent1 = new Intent(this, ReportFristActivity.class);
                    intent1.putExtra("resType", Constants.JUBAO_TYPE_YUNHU);
                    intent1.putExtra("userId", queryedUserId);
                    intent1.putExtra("resourceId", queryedUserId);
                    startActivity(intent1);
                }
                break;
            case R.id.rightButton:
                Intent shareintent = new Intent(this, ChoiceShareReportActivity.class);
                startActivityForResult(shareintent, 0);
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.edit_avtar_tv:
                TransferImagesBean dataBean = new TransferImagesBean();
                LinkedList<String> picDatas = new LinkedList<String>();
                for (String str : userHeads) {
                    picDatas.add(str);
                }
                dataBean.setSelectImagePah(picDatas);
                dataBean.setVideoBean(videoBean);
                Intent intent6 = new Intent(this, CreateHeadPicActivity.class);
                intent6.putExtra("images", dataBean);
                intent6.putExtra("isCm",true);
                startActivity(intent6);
                break;
            case R.id.to_attemtion_tv:
                editAttention(1);
                break;
            case R.id.cancel_attemtion_tv:
                editAttention(3);
                break;
        }
    }

    /**
     * 0:关注 1：取消
     *
     * @param i
     */
    private void editAttention(int i) {
        if (UserPreferencesUtil.isOnline(this)) {
            RequestBean item = new RequestBean();
            try {
                JSONObject obj = new JSONObject();
                obj.put("userId", UserPreferencesUtil.getUserId(this));
                obj.put("seeUserId",queryedUserId);
                if (i == 1) {
                    obj.put("status", "1");
                } else if (i == 3) {
                    obj.put("status", "3");
                }
                item.setData(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ShowMsgDialog.showNoMsg(this, false);
            item.setServiceURL(ConstTaskTag.EDIT_ATTENTION_URL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.EDIT_ATTENTION_INT);

        } else {
            Intent intent = new Intent(this, LoginWelcomActivity.class);
            startActivity(intent);
        }
    }


    @Override
	public void onDestroy() {
        super.onDestroy();
        MemoryCache.getInstance().clear();
        EditorUserInfoBroadcastListener.unRegisterEditorUserInfoListener(this, mBroadcastReceiver);
        //回收头像区域
        View videoView = findViewById(R.id.video_iv);
        RecycleBitmap.recycleImageView(videoView);
        int size = userHeads.size();
        for (int i = 0;i<size;i++){
            View avatarView = findViewById(R.id.iv);
            RecycleBitmap.recycleImageView(avatarView);
        }
        System.gc();
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
                        Intent intent = new Intent(this,ShareBoardView.class);
                        intent.putExtra(Constants.SHARE_TITLE_KEY,getString(R.string.share_sub_title_model));
                        intent.putExtra(Constants.SHARE_SUBTITLE_KEY,"照片找他拍就对了！网红模特的好伙伴，摄影师"+queryUserNick+"在模仿儿等待你的加入！");
                        intent.putExtra(Constants.SHARE_ICONURL_KEY,shareIconUrl);
                        startActivity(intent);
                    } else if ("report".equals(flag)) {
                        Intent intent1 = new Intent(this, ReportFristActivity.class);
                        intent1.putExtra("resType", Constants.JUBAO_TYPE_YUNHU);
                        intent1.putExtra("userId", queryedUserId);
                        intent1.putExtra("resourceId", queryedUserId);
                        startActivity(intent1);
                    }
                    break;
            }
        }
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_EDITOR_USER_INFO.equals(intent.getAction())) {
                boolean isChanged = intent.getBooleanExtra("flagStr", false);
                if (isChanged) {
                    if (editOrQueryFlag.equals(Constants.EDIT_INFO_FLAG)) {
                        titleName.setText(UserPreferencesUtil.getUserNickName(CMPersonInfoActivity.this));
                    }
                    //因为videoBean是成员变量  所以在删除成功后初始化 以免把原来的值传人头像编辑页面
                    videoBean = new VideoBean();
                    requestData();
                }
            }

        }
    };


    public String getQueryUserNick() {
        return queryUserNick;
    }

    public void setQueryUserNick(String queryUserNick) {
        this.queryUserNick = queryUserNick;
    }

    public String getQueryedUserId() {
        return queryedUserId;
    }

    public void setQueryedUserId(String queryedUserId) {
        this.queryedUserId = queryedUserId;
    }

    public String getQueryUserType() {
        return queryUserType;
    }

    public void setQueryUserType(String queryUserType) {
        this.queryUserType = queryUserType;
    }

}

