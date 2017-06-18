package com.xygame.second.sg.xiadan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.stevenhu.android.phone.bean.ADInfo;
import com.stevenhu.android.phone.utils.ViewFactory;
import com.txr.codec.digest.DigestUtils;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.comm.activity.PlaceChoiceMainActivity;
import com.xygame.second.sg.jinpai.adapter.FuFeiBigTypeChooseAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.activity.GodTypeListActivity;
import com.xygame.second.sg.xiadan.adapter.UserTypeAdapter;
import com.xygame.second.sg.xiadan.bean.GodTypeBean;
import com.xygame.second.sg.xiadan.bean.GodUserBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.SearchUserActivity;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.activity.webview.CommonWebViewActivity;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lib.BaseViewPager;
import lib.CycleViewPagerHandler;

/**
 * Created by tony on 2016/12/22.
 */
public class XiaDanFragment extends SGBaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener, PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    private PullToRefreshListView3 listView;
    private View rightButton,provinceButton;
    private GridView photoList;
    private FuFeiBigTypeChooseAdapter typeAdapter;
    private UserTypeAdapter listAdapter;
    private TextView cityText;
    private ProvinceBean areaBean;


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
    private CycleViewPagerHandler mHandler = new CycleViewPagerHandler(getActivity()) {

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
            if (msg.what==0){
                cityText.setText(areaBean.getProvinceName());
                CacheService.getInstance().cacheJPProvinceBean("XDProvinceBean", areaBean);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerLoginListener();
        initViews();
        addListener();
        initDatas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xiadan_layout, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        areaBean =
                CacheService.getInstance().getCacheJPProvinceBean("XDProvinceBean");
        if (areaBean != null) {
            cityText.setText(areaBean.getProvinceName());
        } else {
            if (BaiduPreferencesUtil.getProvice(getActivity()) != null) {
                ThreadPool.getInstance().excuseThread(new JugmentUse());
            }
        }
    }

    private class JugmentUse implements Runnable {
        @Override
        public void run() {
            List<ProvinceBean> datas = (List<ProvinceBean>) AssetDataBaseManager.getManager().queryCitiesByParentId(0);
            for (ProvinceBean it : datas) {
                it.get();
                if (BaiduPreferencesUtil.getProvice(getActivity()).contains(it.getProvinceName())) {
                    areaBean = it;
                    break;
                }
            }
            mHandler.sendEmptyMessage(0);
        }
    }


    private void initViews() {
        provinceButton=getView().findViewById(R.id.provinceButton);
        cityText=(TextView)getView().findViewById(R.id.cityText);
        rightButton = getView().findViewById(R.id.rightButton);
        listView = (PullToRefreshListView3) getView().findViewById(R.id.listView);

        View gridViewHead = LayoutInflater.from(getActivity()).inflate(R.layout.jinpai_list_head_view, null);
        photoList = (GridView) gridViewHead.findViewById(R.id.photoList);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.stick_header, null);
        viewPager = (BaseViewPager) header.findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) header
                .findViewById(R.id.layout_viewpager_indicator);

        viewPagerFragmentLayout = (FrameLayout) header
                .findViewById(R.id.layout_viewager_content);

        listView.addHeadViewAction(gridViewHead);//添加头部
        listView.addHeadViewAction(header);//添加头部
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    private void initDatas() {
        typeAdapter = new FuFeiBigTypeChooseAdapter(getActivity(), null);
        photoList.setAdapter(typeAdapter);
        configImageLoader();
        listAdapter=new UserTypeAdapter(getActivity(),null);
        listView.setAdapter(listAdapter);
        loadXiaDanDatas();
    }

    public void loadXiaDanDatas(){
        List<BannerBean> resBannerList=CacheService.getInstance().getCacheBannerBeanDatas(ConstTaskTag.CACHE_BLACK_LIST_DATAS);
        if (resBannerList!=null){
            if (views.size()==0){
                initialize(resBannerList);
            }
            List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
            if (jinPaiBigTypeBeans != null) {
                if (jinPaiBigTypeBeans.size() > 0) {
                    if (typeAdapter.getCount()==0){
                        typeAdapter.updateJinPaiDatas(jinPaiBigTypeBeans);
                    }
                    List<PriceBean> fuFeiDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                    if (fuFeiDatas!=null){
                        requestListDatas();
                    }else{
                        loadPriceDatas();
                    }
                } else {
                    requestActType();
                }
            } else {
                requestActType();
            }
        }else{
            loadBannerDatas();
        }
    }

    private void addListener() {
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        rightButton.setOnClickListener(this);
        provinceButton.setOnClickListener(this);
        photoList.setOnItemClickListener(new ChoiceTypeItem());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rightButton:
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.provinceButton:
                Intent intent1=new Intent(getActivity(), PlaceChoiceMainActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private class ChoiceTypeItem implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            JinPaiBigTypeBean item=typeAdapter.getItem(position);
            Intent intent=new Intent(getActivity(), GodTypeListActivity.class);
            intent.putExtra("bean",item);
            startActivity(intent);
        }
    }

    public void loadPriceDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            ShowMsgDialog.showNoMsg(getActivity(), true);
            item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE_PRICE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE_PRICE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadBannerDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_MODEL_BANNER);
            ShowMsgDialog.showNoMsg(getActivity(), true);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODEL_BANNER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void requestActType() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            item.setData(object);
            ShowMsgDialog.showNoMsg(getActivity(), true);
            item.setServiceURL(ConstTaskTag.QUEST_SERVER_TYPE);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_SERVER_TYPE);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void requestListDatas() {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            if (areaBean!=null){
                if (areaBean.getProvinceCode()!=null){
                    if (!"1".equals(areaBean.getProvinceCode())) {
                        object.put("province", areaBean.getProvinceCode());
                    }
                }
            }
            item.setData(object);
            ShowMsgDialog.showNoMsg(getActivity(), true);
            item.setServiceURL(ConstTaskTag.QUEST_HOME_USERS);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_HOME_USERS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        listView.onRefreshComplete();
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_HOME_USERS:
                if ("0000".equals(data.getCode())) {
                    parseListDatas(data.getRecord());
                }else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SERVER_TYPE:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data.getRecord());
                }else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_MODEL_BANNER:
                if ("0000".equals(data.getCode())) {
                    parseModelBannerDatas(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_SERVER_TYPE_PRICE:
                if ("0000".equals(data.getCode())) {
                    parsePriceDatas(data.getRecord());
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void responseFaith() {
        super.responseFaith();
        listView.onRefreshComplete();
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
                requestListDatas();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseListDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                List<GodTypeBean> resBannerList = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    GodTypeBean item = new GodTypeBean();
                    item.setSkillCode(StringUtils.getJsonValue(object, "skillCode"));
                    String users=StringUtils.getJsonValue(object, "users");
                    if (ConstTaskTag.isTrueForArrayObj(users)) {
                        List<GodUserBean> userList = new ArrayList<>();
                        JSONArray userArray = new JSONArray(users);
                        for (int j = 0; j < userArray.length(); j++) {
                            JSONObject userObject = userArray.getJSONObject(j);
                            GodUserBean userBean=new GodUserBean();
                            userBean.setPriceId(StringUtils.getJsonValue(userObject, "priceId"));
                            userBean.setSillTitle(StringUtils.getJsonValue(userObject, "skillTitle"));
                            userBean.setUserIcon(StringUtils.getJsonValue(userObject, "userIcon"));
                            userBean.setUserId(StringUtils.getJsonValue(userObject, "userId"));
                            userBean.setUsernick(StringUtils.getJsonValue(userObject, "usernick"));
                            userBean.setPriceRate(StringUtils.getJsonValue(userObject, "priceRate"));
                            userList.add(userBean);
                        }
                        item.setUserBeans(userList);
                    }
                    resBannerList.add(item);
                }
                listAdapter.addPhotoes(resBannerList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseModelBannerDatas(ResponseBean data) {
        try {
            String record = data.getRecord();
            if (ConstTaskTag.isTrueForArrayObj(record)) {
                List<BannerBean> resBannerList = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    BannerBean item = new BannerBean();
                    item.setPicUrl(StringUtils.getJsonValue(object, "picUrl"));
                    item.setParam(StringUtils.getJsonValue(object, "param"));
                    item.setTargetUrl(StringUtils.getJsonValue(object, "targetUrl"));
                    item.setTitle(StringUtils.getJsonValue(object, "title"));
                    item.setType(StringUtils.getJsonValue(object, "type"));
                    resBannerList.add(item);
                }
                if (resBannerList.size() > 0) {
                    CacheService.getInstance().cacheBannerBeanDatas(ConstTaskTag.CACHE_BLACK_LIST_DATAS,resBannerList);
                    initialize(resBannerList);
                }
            }
            List<JinPaiBigTypeBean> jinPaiBigTypeBeans = CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
            if (jinPaiBigTypeBeans != null) {
                if (jinPaiBigTypeBeans.size() > 0) {
                    if (typeAdapter.getCount()==0){
                        typeAdapter.updateJinPaiDatas(jinPaiBigTypeBeans);
                    }
                    List<PriceBean> fuFeiDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                    if (fuFeiDatas!=null){
                        requestListDatas();
                    }else{
                        loadPriceDatas();
                    }
                } else {
                    requestActType();
                }
            } else {
                requestActType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDatas(String record) {
        if (ConstTaskTag.isTrueForArrayObj(record)) {
            try {
                List<JinPaiBigTypeBean> fuFeiDatas = new ArrayList<>();
                JSONArray array = new JSONArray(record);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    JinPaiBigTypeBean item = new JinPaiBigTypeBean();
                    item.setIsSelect(false);
                    item.setName(StringUtils.getJsonValue(object, "typeName"));
                    item.setId(StringUtils.getJsonValue(object, "typeId"));
                    item.setSubStr(StringUtils.getJsonValue(object, "titles"));
                    item.setUrl(StringUtils.getJsonValue(object, "typeIconUrl"));
                    item.setCategoryName(StringUtils.getJsonValue(object, "categoryName"));
//                    if ("900".equals(item.getId())){
//                        JinPaiBigTypeBean subItem = new JinPaiBigTypeBean();
//                        subItem.setCategoryName(item.getCategoryName());
//                        subItem.setId(Constants.DEFINE_LOL_ID);
//                        subItem.setSubStr(item.getSubStr());
//                        subItem.setUrl(item.getUrl());
//                        subItem.setName("LOL");
//                        fuFeiDatas.add(subItem);
//                    }
                    fuFeiDatas.add(item);
                }
                CacheService.getInstance().cacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE, fuFeiDatas);
                if (typeAdapter.getCount()==0){
                    typeAdapter.updateJinPaiDatas(fuFeiDatas);
                }

                List<PriceBean> priceDatas=CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
                if (priceDatas!=null){
                    requestListDatas();
                }else{
                    loadPriceDatas();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerLoginListener() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.UPDATE_MAIN_LOCATION);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterLoginListener() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.UPDATE_MAIN_LOCATION.equals(intent.getAction())){
                areaBean=(ProvinceBean)intent.getSerializableExtra(Constants.COMEBACK);
                cityText.setText(areaBean.getProvinceName());
                CacheService.getInstance().cacheJPProvinceBean("XDProvinceBean", areaBean);
                requestListDatas();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterLoginListener();
    }

    /**
     * Bnner位部分++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     *
     * @param resBannerList
     */

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
        views.add(ViewFactory.loadOrigaImage(getActivity(), infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.loadOrigaImage(getActivity(), infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.loadOrigaImage(getActivity(), infos.get(0).getUrl()));

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        loadBannerDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

        public void onImageClick(ADInfo info, int position, View imageView) {
            if (isCycle()) {
                BannerBean bannerBean=info.getBannerBean();
                if ("80".equals(bannerBean.getType())){
                    boolean islogin = UserPreferencesUtil.isOnline(getActivity());
                    if (islogin){
                        String f= DigestUtils.md5Hex(UserPreferencesUtil.getUserId(getActivity()).concat("mit"));
                        String s=DigestUtils.md5Hex("miaobanban".concat(f).concat("8eaf39u8dp2%&23#!i82e98"));
                        String targUrl=bannerBean.getTargetUrl().concat("?f=").concat(f).concat("&s=").concat(s);
                        Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
                        intent.putExtra("webUrl", targUrl);
                        intent.putExtra("title", bannerBean.getTitle());
                        intent.putExtra("bannerBean", bannerBean);
                        startActivity(intent);
                    }else{
                        showComfirmDialog("只有登录后才能领取游戏礼包哦");
                    }
                }else{
                    Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
                    intent.putExtra("webUrl", bannerBean.getTargetUrl());
                    intent.putExtra("title", bannerBean.getTitle());
                    intent.putExtra("bannerBean", bannerBean);
                    startActivity(intent);
                }
            }

        }
    };

    private void showComfirmDialog(String tip){
        TwoButtonDialog dialog = new TwoButtonDialog(getActivity(),tip,"马上注册/登录","以后再说", R.style.dineDialog,
						new ButtonTwoListener() {

					@Override
					public void confrimListener() {
						// TODO Auto-generated method stub
                        Intent intent = new Intent(getActivity(), LoginWelcomActivity.class);
                        intent.putExtra("whereFlag","mainMe");
                        startActivity(intent);
					}

					@Override
					public void cancelListener() {
						// TODO Auto-generated method stub
					}
				});
				dialog.show();
    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
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
        setIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        if (arg0 == 1) { // viewPager在滚动
            isScrolling = true;
            return;
        } else if (arg0 == 0) { // viewPager滚动结束
            releaseTime = System.currentTimeMillis();
            viewPager.setCurrentItem(currentPosition, false);
        }
        isScrolling = false;
    }

    //binner部分
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
            View view = LayoutInflater.from(getActivity()).inflate(
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
            if (getActivity() != null && !getActivity().isFinishing()
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
     * 释放指示器高度，可能由于之前指示器被限制了高度，此处释放
     */
    public void releaseHeight() {
        getView().getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
        refreshData();
    }

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
     * 设置viewpager是否可以滚动
     *
     * @param enable
     */
    public void setScrollable(boolean enable) {
        viewPager.setScrollable(enable);
    }

    /**
     * 返回当前位置,循环时需要注意返回的position包含之前在views最前方与最后方加入的视图，即当前页面试图在views集合的位置
     *
     * @return
     */
    public int getCurrentPostion() {
        return currentPosition;
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
     * 如果当前页面嵌套在另一个viewPager中，为了在进行滚动时阻断父ViewPager滚动，可以 阻止父ViewPager滑动事件
     * 父ViewPager需要实现ParentViewPager中的setScrollable方法
     */
    public void disableParentViewPagerTouchEvent(BaseViewPager parentViewPager) {
        if (parentViewPager != null)
            parentViewPager.setScrollable(false);
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
}