package com.xygame.sg.activity.main;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.xygame.second.sg.descover.activity.ModelAllActivity;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.SearchUserActivity;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.model.bean.BannerBean;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.webview.CommonWebViewActivity;
import com.xygame.sg.adapter.main.FanErAdapter;
import com.xygame.sg.bean.comm.FanErBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.TempGroupNewsEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshGridView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lib.BaseViewPager;
import lib.CycleViewPagerHandler;


public class FanErFragment extends SGBaseFragment implements View.OnClickListener, AbsListView.OnScrollListener, ViewPager.OnPageChangeListener, PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    private LinearLayout invis;
    private PullToRefreshGridView2 listView, SYSlistView;
    private View search_iv, header, stick_action, stick_action1, switch_show_cb;
    private boolean isModelView = true;
    private FrameLayout frameView;
    private ViewGroup.LayoutParams layoutParams;
    private View rightButton;
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
        }
    };

    //悬浮菜单部分

    private View modelHotView, modelFansView, modelRecommendView, modelHotView1, modelFansView1, modelRecommendView1, modelHotLine, modelFansLine, modelRecommendLine, modelHotLine1, modelFansLine1, modelRecommendLine1;
    private ImageView modelHotImage, modelFansImage, modelRecommendImage, modelHotImage1, modelFansImage1, modelRecommendImage1;
    private TextView modelHotText, modelFansText, modelRecommendText, modelHotText1, modelFansText1, modelRecommendText1,nuReadNews;

    //顶部Tab部分
    private View sysTabView, modelTabView, sysTabView1, modelTabView1;

    private FanErAdapter sysAdapter, hotAdapter, fansAdapter, recommendAdapter;

    private int pageSize = 15;//每页显示的数量
    private int sysPage = 1, hotPage = 1, fansPage = 1, recomendPage = 1;//当前显示页数
    private String sysReqTime, hotReqTime, fansReqTime, recommendReqTime;
    private boolean sysIsLoading = true, hotIsLoading = true, fansIsLoading = true, recommendIsLoading = true;
    private int typeFlag = 2;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerLoginListener();
        initViews();
        initDatas();
        addListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.faner_layout, null);
    }

    private void initViews() {
        rightButton=getView().findViewById(R.id.rightButton);
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        modelHotImage = (ImageView) getView().findViewById(R.id.modelHotImage);
        modelFansImage = (ImageView) getView().findViewById(R.id.modelFansImage);
        modelRecommendImage = (ImageView) getView().findViewById(R.id.modelRecommendImage);

        modelHotText = (TextView) getView().findViewById(R.id.modelHotText);
        modelFansText = (TextView) getView().findViewById(R.id.modelFansText);
        modelRecommendText = (TextView) getView().findViewById(R.id.modelRecommendText);
        nuReadNews=(TextView)getView().findViewById(R.id.nuReadNews);

        modelHotLine = getView().findViewById(R.id.modelHotLine);
        modelFansLine = getView().findViewById(R.id.modelFansLine);
        modelRecommendLine = getView().findViewById(R.id.modelRecommendLine);

        modelHotView = getView().findViewById(R.id.modelHotView);
        modelFansView = getView().findViewById(R.id.modelFansView);
        modelRecommendView = getView().findViewById(R.id.modelRecommendView);
        invis = (LinearLayout) getView().findViewById(R.id.invis);
        switch_show_cb = getView().findViewById(R.id.switch_show_cb);
        search_iv = getView().findViewById(R.id.search_iv);
        frameView = (FrameLayout) getView().findViewById(R.id.frameView);
        listView = new PullToRefreshGridView2(getActivity());
        SYSlistView = new PullToRefreshGridView2(getActivity());
        listView.setLayoutParams(layoutParams);
        listView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        SYSlistView.setLayoutParams(layoutParams);
        SYSlistView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        header = LayoutInflater.from(getActivity()).inflate(R.layout.stick_header, null);
        sysTabView = header.findViewById(R.id.sysTabView);
        modelTabView = header.findViewById(R.id.modelTabView);
        viewPager = (BaseViewPager) header.findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) header
                .findViewById(R.id.layout_viewpager_indicator);

        viewPagerFragmentLayout = (FrameLayout) header
                .findViewById(R.id.layout_viewager_content);
        stick_action = LayoutInflater.from(getActivity()).inflate(R.layout.stick_action, null);
        modelHotView1 = stick_action.findViewById(R.id.modelHotView1);
        modelFansView1 = stick_action.findViewById(R.id.modelFansView1);
        modelRecommendView1 = stick_action.findViewById(R.id.modelRecommendView1);
        modelHotImage1 = (ImageView) stick_action.findViewById(R.id.modelHotImage1);
        modelFansImage1 = (ImageView) stick_action.findViewById(R.id.modelFansImage1);
        modelRecommendImage1 = (ImageView) stick_action.findViewById(R.id.modelRecommendImage1);
        modelHotText1 = (TextView) stick_action.findViewById(R.id.modelHotText1);
        modelFansText1 = (TextView) stick_action.findViewById(R.id.modelFansText1);
        modelRecommendText1 = (TextView) stick_action.findViewById(R.id.modelRecommendText1);
        modelHotLine1 = stick_action.findViewById(R.id.modelHotLine1);
        modelFansLine1 = stick_action.findViewById(R.id.modelFansLine1);
        modelRecommendLine1 = stick_action.findViewById(R.id.modelRecommendLine1);
        listView.addHeadViewAction(header);//添加头部
//        listView.addHeadViewAction(stick_action);//ListView条目中的悬浮部分 添加到头部
        listView.setNumColumns(3);
        listView.setVerticalSpacing(9);
        listView.setHorizontalSpacing(9);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        frameView.addView(listView);

        stick_action1 = LayoutInflater.from(getActivity()).inflate(R.layout.stick_action1, null);
        sysTabView1 = stick_action1.findViewById(R.id.sysTabView1);
        modelTabView1 = stick_action1.findViewById(R.id.modelTabView1);
        SYSlistView.addHeadViewAction(stick_action1);//ListView条目中的悬浮部分 添加到头部
        SYSlistView.setNumColumns(3);
        SYSlistView.setVerticalSpacing(9);
        SYSlistView.setHorizontalSpacing(9);
        SYSlistView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initDatas() {
        configImageLoader();
        sysAdapter = new FanErAdapter(getActivity(), null, false);
        hotAdapter = new FanErAdapter(getActivity(), null, true);
        fansAdapter = new FanErAdapter(getActivity(), null, true);
        recommendAdapter = new FanErAdapter(getActivity(), null, true);
        listView.setAdapter(hotAdapter);
        SYSlistView.setAdapter(sysAdapter);
        List<BannerBean> resBannerList=CacheService.getInstance().getCacheBannerBeanDatas(ConstTaskTag.CACHE_BLACK_LIST_DATAS);
        if (resBannerList==null){
            loadBannerDatas();
        }else{
            initialize(resBannerList);
        }
        updateFloatMenu(2);
    }

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
        if (isModelView) {
            if (position >= 3) {
                FanErBean modelBean = null;
                switch (typeFlag) {
                    case 1:
                        modelBean = hotAdapter.getItem(position - 3);
                        break;
                    case 2:
                        modelBean = fansAdapter.getItem(position - 3);
                        break;
                    case 3:
                        modelBean = recommendAdapter.getItem(position - 3);
                        break;
                }

                Intent intent = new Intent(getActivity(), PersonalDetailActivity.class);
                intent.putExtra("userNick", modelBean.getUsernick());
                intent.putExtra("userId", modelBean.getUserId()+"");
                intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
                startActivity(intent);
            }
        } else {
            if (position >= 3) {
                FanErBean modelBean = sysAdapter.getItem(position-3);
                Intent intent = new Intent(getActivity(), CMPersonInfoActivity.class);
                intent.putExtra("userNick", modelBean.getUsernick());
                intent.putExtra("userId", modelBean.getUserId() + "");
                intent.putExtra(Constants.EDIT_OR_QUERY_FLAG, Constants.QUERY_INFO_FLAG);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        loadBannerDatas();
        if (isModelView) {
            switch (typeFlag) {
                case 1:
                    hotIsLoading = true;
                    hotPage = 1;
//                    loadHotModelDatas();
                    break;
                case 2:
                    fansIsLoading = true;
                    fansPage = 1;
                    loadFansModelDatas();
                    break;
                case 3:
                    recommendIsLoading = true;
                    recomendPage = 1;
                    loadRcommendModelDatas();
                    break;
            }
        } else {
            sysIsLoading = true;
            sysPage = 1;
            loadSysDatas();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isModelView) {
            switch (typeFlag) {
                case 1:
                    if (hotIsLoading) {
                        hotPage = hotPage + 1;
//                        loadHotModelDatas();
                    } else {
                        falseDatasModel();
                    }
                    break;
                case 2:
                    if (fansIsLoading) {
                        fansPage = fansPage + 1;
                        loadFansModelDatas();
                    } else {
                        falseDatasModel();
                    }
                    break;
                case 3:
                    if (recommendIsLoading) {
                        recomendPage = recomendPage + 1;
                        loadRcommendModelDatas();
                    } else {
                        falseDatasModel();
                    }
                    break;
            }
        } else {
            if (sysIsLoading) {
                sysPage = sysPage + 1;
                loadSysDatas();
            } else {
                falseDatas();
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    listView.onRefreshComplete();
                    Toast.makeText(getActivity(),"已全部加载",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    SYSlistView.onRefreshComplete();
                    Toast.makeText(getActivity(),"已全部加载",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

    private void falseDatasModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message m = handler.obtainMessage();
                    m.what = 1;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Message m = handler.obtainMessage();
                    m.what = 2;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
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

    private void addListener() {
        search_iv.setOnClickListener(this);
        switch_show_cb.setOnClickListener(this);
        listView.setOnScrollListener(this);
        listView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        SYSlistView.setOnScrollListener(this);
        SYSlistView.setOnRefreshListener(this);
        SYSlistView.setOnItemClickListener(this);
        modelHotView.setOnClickListener(this);
        modelFansView.setOnClickListener(this);
        modelRecommendView.setOnClickListener(this);
        modelHotView1.setOnClickListener(this);
        modelFansView1.setOnClickListener(this);
        modelRecommendView1.setOnClickListener(this);
        sysTabView.setOnClickListener(this);
        modelTabView.setOnClickListener(this);
        sysTabView1.setOnClickListener(this);
        modelTabView1.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        nuReadNews.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_show_cb:
                Intent intent1 = new Intent(getActivity(), ModelAllActivity.class);
                startActivity(intent1);
                break;
            case R.id.search_iv:
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.modelHotView:
                updateFloatMenu(1);
                break;
            case R.id.modelFansView:
                updateFloatMenu(2);
                break;
            case R.id.modelRecommendView:
                updateFloatMenu(3);
                break;
            case R.id.modelHotView1:
                updateFloatMenu(1);
                break;
            case R.id.modelFansView1:
                updateFloatMenu(2);
                break;
            case R.id.modelRecommendView1:
                updateFloatMenu(3);
                break;
            case R.id.sysTabView:
                isModelView = false;
                swithMainView();
                break;
            case R.id.modelTabView:
                isModelView = true;
                swithMainView();
                break;
            case R.id.sysTabView1:
                isModelView = false;
                swithMainView();
                break;
            case R.id.modelTabView1:
                isModelView = true;
                swithMainView();
                break;
            case R.id.rightButton:
                boolean islogin = UserPreferencesUtil.isOnline(getActivity());
                if (!islogin) {
                    Intent intent11 = new Intent(getActivity(), LoginWelcomActivity.class);
                    startActivity(intent11);
                }else{
                    Intent intent7 = new Intent(getActivity(), NewsFragment.class);
                    getActivity().startActivity(intent7);
                }
                break;
            case R.id.nuReadNews:
                boolean islogin1 = UserPreferencesUtil.isOnline(getActivity());
                if (!islogin1) {
                    Intent intent11 = new Intent(getActivity(), LoginWelcomActivity.class);
                    startActivity(intent11);
                }else{
                    Intent intent8 = new Intent(getActivity(), NewsFragment.class);
                    getActivity().startActivity(intent8);
                }
                break;
        }
    }

    private void swithMainView() {
        invis.setVisibility(View.GONE);
        frameView.removeAllViews();
        if (isModelView) {
            frameView.addView(listView);
        } else {
            frameView.addView(SYSlistView);
            if (sysAdapter.getCount() == 0) {
                loadSysDatas();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isModelView) {
            if (firstVisibleItem >= 2) {
                invis.setVisibility(View.GONE);
            } else {

                invis.setVisibility(View.GONE);
            }
        }
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
            if (parentViewPager != null)
                parentViewPager.setScrollable(true);

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

    private void updateFloatMenu(int index) {
        switch (index) {
            case 1:
                typeFlag = 1;
                listView.setAdapter(hotAdapter);
                if (hotAdapter.getCount() == 0) {
//                    loadHotModelDatas();
                }
                modelHotImage.setImageResource(R.drawable.fensibang2a);
                modelFansImage.setImageResource(R.drawable.god2);
                modelRecommendImage.setImageResource(R.drawable.tuhao2);

                modelHotText.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
                modelFansText.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelRecommendText.setTextColor(getActivity().getResources().getColor(R.color.gray));

                modelHotLine.setVisibility(View.VISIBLE);
                modelFansLine.setVisibility(View.INVISIBLE);
                modelRecommendLine.setVisibility(View.INVISIBLE);

                modelHotImage1.setImageResource(R.drawable.fensibang2a);
                modelFansImage1.setImageResource(R.drawable.god2);
                modelRecommendImage1.setImageResource(R.drawable.tuhao2);

                modelHotText1.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
                modelFansText1.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelRecommendText1.setTextColor(getActivity().getResources().getColor(R.color.gray));

                modelHotLine1.setVisibility(View.VISIBLE);
                modelFansLine1.setVisibility(View.INVISIBLE);
                modelRecommendLine1.setVisibility(View.INVISIBLE);
                break;
            case 2:
                typeFlag = 2;
                listView.setAdapter(fansAdapter);
                if (fansAdapter.getCount() == 0) {
                    loadFansModelDatas();
                }
                modelHotImage.setImageResource(R.drawable.fensibanga);
                modelFansImage.setImageResource(R.drawable.god1);
                modelRecommendImage.setImageResource(R.drawable.tuhao2);

                modelHotText.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelFansText.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
                modelRecommendText.setTextColor(getActivity().getResources().getColor(R.color.gray));

                modelHotLine.setVisibility(View.INVISIBLE);
                modelFansLine.setVisibility(View.VISIBLE);
                modelRecommendLine.setVisibility(View.INVISIBLE);

                modelHotImage1.setImageResource(R.drawable.fensibanga);
                modelFansImage1.setImageResource(R.drawable.god1);
                modelRecommendImage1.setImageResource(R.drawable.tuhao2);

                modelHotText1.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelFansText1.setTextColor(getActivity().getResources().getColor(R.color.dark_green));
                modelRecommendText1.setTextColor(getActivity().getResources().getColor(R.color.gray));

                modelHotLine1.setVisibility(View.INVISIBLE);
                modelFansLine1.setVisibility(View.VISIBLE);
                modelRecommendLine1.setVisibility(View.INVISIBLE);
                break;
            case 3:
                typeFlag = 3;
                listView.setAdapter(recommendAdapter);
                if (recommendAdapter.getCount() == 0) {
                    loadRcommendModelDatas();
                }
                modelHotImage.setImageResource(R.drawable.fensibanga);
                modelFansImage.setImageResource(R.drawable.god2);
                modelRecommendImage.setImageResource(R.drawable.tuhao1);

                modelHotText.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelFansText.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelRecommendText.setTextColor(getActivity().getResources().getColor(R.color.dark_green));

                modelHotLine.setVisibility(View.INVISIBLE);
                modelFansLine.setVisibility(View.INVISIBLE);
                modelRecommendLine.setVisibility(View.VISIBLE);

                modelHotImage1.setImageResource(R.drawable.fensibanga);
                modelFansImage1.setImageResource(R.drawable.god2);
                modelRecommendImage1.setImageResource(R.drawable.tuhao1);

                modelHotText1.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelFansText1.setTextColor(getActivity().getResources().getColor(R.color.gray));
                modelRecommendText1.setTextColor(getActivity().getResources().getColor(R.color.dark_green));

                modelHotLine1.setVisibility(View.INVISIBLE);
                modelFansLine1.setVisibility(View.INVISIBLE);
                modelRecommendLine1.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        listView.onRefreshComplete();
        SYSlistView.onRefreshComplete();
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_HOTPHOTOGRAPHERS_INT:
                if ("0000".equals(data.getCode())) {
                    parseSysDatas(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.LOAD_HOT_MODEL_INFO_INT:
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_MODEL_FANS:
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_MODEL_RECOMMEND:
                if ("0000".equals(data.getCode())) {
                    parseRecommendModelDatas(data);
                } else {
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
        }
    }

    @Override
    protected void responseFaith() {
        super.responseFaith();
        if (isModelView) {
            listView.onRefreshComplete();
        } else {
            SYSlistView.onRefreshComplete();
        }
    }

    private void loadBannerDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_MODEL_BANNER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODEL_BANNER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadRcommendModelDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", recomendPage).put("pageSize", pageSize));
            if (recomendPage > 1) {
                obj.put("reqTime", recommendReqTime);
            } else {
                ShowMsgDialog.showNoMsg(getActivity(), true);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_MODEL_RECOMMEND);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODEL_RECOMMEND);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadFansModelDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", fansPage).put("pageSize", pageSize));
            if (fansPage > 1) {
                obj.put("reqTime", fansReqTime);
            } else {
                ShowMsgDialog.showNoMsg(getActivity(), true);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_MODEL_FANS);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODEL_FANS);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

//    private void loadHotModelDatas() {
//        RequestBean item = new RequestBean();
//        try {
//            JSONObject obj = new JSONObject();
//            obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
//            if (hotPage > 1) {
//                obj.put("reqTime", hotReqTime);
//            } else {
//                ShowMsgDialog.showNoMsg(getActivity(), true);
//            }
//            item.setData(obj);
//            item.setServiceURL(ConstTaskTag.LOAD_HOT_MODEL_INFO_URL);
//            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.LOAD_HOT_MODEL_INFO_INT);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//    }

    private void loadSysDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", sysPage).put("pageSize", pageSize));
            if (sysPage > 1) {
                obj.put("reqTime", sysReqTime);
            } else {
                ShowMsgDialog.showNoMsg(getActivity(), true);
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUERY_HOTPHOTOGRAPHERS_URL);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_HOTPHOTOGRAPHERS_INT);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void parseSysDatas(ResponseBean data) {
//        HotPhotographerView resultHotPhotographers = JSON.parseObject(data.getRecord(), HotPhotographerView.class);
//        if (resultHotPhotographers != null) {
//            sysReqTime = String.valueOf(resultHotPhotographers.getReqTime());
//            List<HotPhotographerVo> photops = resultHotPhotographers.getPhotops();
//            List<FanErBean> datas = new ArrayList<>();
//            for (HotPhotographerVo it : photops) {
//                FanErBean item = new FanErBean();
//                item.setUsernick(it.getUsernick());
//                item.setUserIcon(it.getUserIcon());
//                item.setUserId(it.getUserId());
//                datas.add(item);
//            }
//            if (datas.size() < pageSize) {
//                sysIsLoading = false;
//            }
//            sysAdapter.addDatas(datas, sysPage);
//        } else {
//            sysIsLoading = false;
//        }
    }

    private void parseHotModelDatas(ResponseBean data) {
        if (!TextUtils.isEmpty(data.getRecord())&&!"null".equals(data.getRecord())) {
            List<FanErBean> datas = new ArrayList<>();
            try {
                JSONObject object=new JSONObject(data.getRecord());
                hotReqTime=object.getString("reqTime");
                String users=object.getString("users");
                if (!TextUtils.isEmpty(users)&&!"[null]".equals(users)&&!"null".equals(users)&&!"[]".equals(users)){
                    JSONArray jsonArray=new JSONArray(users);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object1=jsonArray.getJSONObject(i);
                        FanErBean item = new FanErBean();
                        item.setUsernick(object1.getString("usernick"));
                        item.setUserIcon(object1.getString("userIcon"));
                        item.setUserId(object1.getLong("userId"));
                        datas.add(item);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                hotIsLoading = false;
            }
            hotAdapter.addDatas(datas, hotPage);
        } else {
            hotIsLoading = false;
        }
    }

    private void parseFansModelDatas(ResponseBean data) {
        if (!TextUtils.isEmpty(data.getRecord())&&!"null".equals(data.getRecord())) {
            List<FanErBean> datas = new ArrayList<>();
            try {
                JSONObject object=new JSONObject(data.getRecord());
                fansReqTime=object.getString("reqTime");
                String users=object.getString("users");
                if (!TextUtils.isEmpty(users)&&!"[null]".equals(users)&&!"null".equals(users)&&!"[]".equals(users)){
                    JSONArray jsonArray=new JSONArray(users);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object1=jsonArray.getJSONObject(i);
                        FanErBean item = new FanErBean();
                        item.setUsernick(object1.getString("usernick"));
                        item.setUserIcon(object1.getString("userIcon"));
                        item.setUserId(object1.getLong("userId"));
                        datas.add(item);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                fansIsLoading = false;
            }
            fansAdapter.addDatas(datas, fansPage);
        } else {
            fansIsLoading = false;
        }
    }

    private void parseRecommendModelDatas(ResponseBean data) {
        if (!TextUtils.isEmpty(data.getRecord())&&!"null".equals(data.getRecord())) {
            List<FanErBean> datas = new ArrayList<>();
            try {
                JSONObject object=new JSONObject(data.getRecord());
                recommendReqTime=object.getString("reqTime");
                String users=object.getString("users");
                if (!TextUtils.isEmpty(users)&&!"[null]".equals(users)&&!"null".equals(users)&&!"[]".equals(users)){
                    JSONArray jsonArray=new JSONArray(users);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object1=jsonArray.getJSONObject(i);
                        FanErBean item = new FanErBean();
                        item.setUsernick(object1.getString("usernick"));
                        item.setUserIcon(object1.getString("userIcon"));
                        item.setUserId(object1.getLong("userId"));
                        datas.add(item);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                recommendIsLoading = false;
            }
            recommendAdapter.addDatas(datas, recomendPage);
        } else {
            recommendIsLoading = false;
        }
    }

    private void parseModelBannerDatas(ResponseBean data) {
        try {
            String record=data.getRecord();
            if (ConstTaskTag.isTrueForArrayObj(record)){
                List<BannerBean> resBannerList=new ArrayList<>();
                JSONArray array=new JSONArray(record);
                for (int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);
                    BannerBean item=new BannerBean();
                    item.setPicUrl(StringUtils.getJsonValue(object, "picUrl"));
//                    item.setId(StringUtils.getJsonIntValue(object,"picUrl"));
                    item.setParam(StringUtils.getJsonValue(object,"param"));
                    item.setTargetUrl(StringUtils.getJsonValue(object, "targetUrl"));
                    item.setTitle(StringUtils.getJsonValue(object, "title"));
                    item.setType(StringUtils.getJsonValue(object, "type"));
                    resBannerList.add(item);
                }
                if (resBannerList.size()>0){
                    CacheService.getInstance().cacheBannerBeanDatas(ConstTaskTag.CACHE_BLACK_LIST_DATAS,resBannerList);
                    initialize(resBannerList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.NEW_MESSAGE_ACTION.equals(intent.getAction())){
                boolean newFlag=intent.getBooleanExtra("newsMessage",false);
                if (newFlag){
                    quaryUnReadNews();
                }
            }else if ("com.xygame.push.dynamic.message.list.action".equals(intent.getAction())){
                quaryUnReadNews();
            }
        }
    };

    public void registerLoginListener() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_ACTION);
        myIntentFilter.addAction("com.xygame.push.dynamic.message.list.action");
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unRegisterLoginListener() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterLoginListener();
    }

    @Override
    public void onResume() {
        super.onResume();
//        loactionTex.setText(BaiduPreferencesUtil.getCity(getActivity()));
        quaryUnReadNews();
    }

    private void quaryUnReadNews() {
        if (UserPreferencesUtil.isOnline(getActivity())) {
            int totalCount = 0;
            int chatNewsCount = NewsEngine.quaryUnReadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int daymicNewsCount = NewsEngine.quaryUnReadDaymicNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            int discNewsCount = TempGroupNewsEngine.quaryUnReadChatNews(getActivity(), UserPreferencesUtil.getUserId(getActivity()));
            totalCount = chatNewsCount + daymicNewsCount+discNewsCount;
            if (totalCount != 0) {
                nuReadNews.setVisibility(View.VISIBLE);
                nuReadNews.setText(String.valueOf(totalCount));
            } else {
                nuReadNews.setVisibility(View.GONE);
            }
        }else{
            nuReadNews.setVisibility(View.GONE);
        }
    }
}
