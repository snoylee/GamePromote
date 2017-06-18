package com.xygame.second.sg.comm.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.xygame.second.sg.comm.bean.ActManagerBean;
import com.xygame.second.sg.jinpai.activity.JinPaiPlushActivity;
import com.xygame.second.sg.jinpai.adapter.ActManagerAdapter;
import com.xygame.second.sg.personal.activity.VideoCertifyActivity;
import com.xygame.second.sg.personal.activity.VideoCertifyChangeActivity;
import com.xygame.second.sg.sendgift.activity.YuePaiPlushActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.personal.ModelIdentyFirstActivity;
import com.xygame.sg.activity.personal.TabEntity;
import com.xygame.sg.adapter.comm.MyPagerAdapter;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/8/22.
 */
public class ActManagerActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2, ViewPager.OnPageChangeListener {

    private TextView titleName;
    private View backButton, rightButton;
    private ImageView rightbuttonIcon;
    private PopupWindow mPopWindow;
    /**
     * viewPage部分
     */
    private LayoutInflater mInflater;
    private ViewPager mPager;// 页内容
    private List<View> listViews; // 页面列表
    private MyPagerAdapter adapter;

    private CommonTabLayout tab_l;
    private String[] titles = {"活动中", "已完成", "已关闭"};
    private int[] iconUnselectIds = {R.drawable.tab_data_unselect, R.drawable.tab_data_unselect, R.drawable.tab_data_unselect};
    private int[] iconSelectIds = {R.drawable.tab_data_select, R.drawable.tab_data_select, R.drawable.tab_data_select};
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();

    private PullToRefreshListView2 PaiSList, finishList, closeList;
    private int loadFlag = 0;// 0：进行中；1：已完成；2：已关闭
    private int pageSize = 21;//每页显示的数量
    private int hotPage = 1, fansPage = 1, recomendPage = 1;//当前显示页数
    private String hotReqTime, fansReqTime, recommendReqTime;
    private boolean hotIsLoading = true, fansIsLoading = true, recommendIsLoading = true;
    private ActManagerAdapter paiSAdapter, finishAdapter, closeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_manag_layout);
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
        PaiSList.setOnRefreshListener(this);
        finishList.setOnRefreshListener(this);
        closeList.setOnRefreshListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initViews() {
        // TODO Auto-generated method stub
        titleName = (TextView) findViewById(R.id.titleName);
        backButton = findViewById(R.id.backButton);
        rightButton = findViewById(R.id.rightButton);
        tab_l = (CommonTabLayout) findViewById(R.id.tab_l);
        rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
        mPager = (ViewPager) findViewById(R.id.mPager);
        listViews = new ArrayList<>();
        mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
        listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
        listViews.add(mInflater.inflate(R.layout.sg_notice_layout, null));
        PaiSList = (PullToRefreshListView2) listViews.get(0).findViewById(R.id.noticeList);
        finishList = (PullToRefreshListView2) listViews.get(1).findViewById(R.id.noticeList);
        closeList = (PullToRefreshListView2) listViews.get(2).findViewById(R.id.noticeList);
        PaiSList.setMode(PullToRefreshBase.Mode.BOTH);
        finishList.setMode(PullToRefreshBase.Mode.BOTH);
        closeList.setMode(PullToRefreshBase.Mode.BOTH);

        adapter = new MyPagerAdapter(listViews);
        mPager.setOffscreenPageLimit(adapter.getCount());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(this);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        rightbuttonIcon.setVisibility(View.GONE);
        titleName.setText("活动管理");
        paiSAdapter = new ActManagerAdapter(this, null);
        finishAdapter = new ActManagerAdapter(this, null);
        closeAdapter = new ActManagerAdapter(this, null);
        PaiSList.setAdapter(paiSAdapter);
        finishList.setAdapter(finishAdapter);
        closeList.setAdapter(closeAdapter);

        //Tab菜单
        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabEntity(titles[i], iconSelectIds[i], iconUnselectIds[i]));
        }
        tab_l.setTabData(tabEntities);
        tab_l.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        tab_l.setCurrentTab(0);
        loadDatas(0);
    }

    private void loadDatas(int index) {//index:1进行中 2已完成 3已关闭
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("status", index + 1);
            switch (index) {
                case 0:
                    obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
                    if (hotPage > 1) {
                        obj.put("reqTime", hotReqTime);
                    } else {
                        ShowMsgDialog.showNoMsg(this, true);
                    }
                    break;
                case 1:
                    obj.put("page", new JSONObject().put("pageIndex", fansPage).put("pageSize", pageSize));
                    if (fansPage > 1) {
                        obj.put("reqTime", fansReqTime);
                    } else {
                        ShowMsgDialog.showNoMsg(this, true);
                    }
                    break;
                case 2:
                    obj.put("page", new JSONObject().put("pageIndex", recomendPage).put("pageSize", pageSize));
                    if (recomendPage > 1) {
                        obj.put("reqTime", recommendReqTime);
                    } else {
                        ShowMsgDialog.showNoMsg(this, true);
                    }
                    break;
            }
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_ACT_MANAGER);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_MANAGER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.rightButton) {
            boolean islogin = UserPreferencesUtil.isOnline(ActManagerActivity.this);
            if (islogin) {
//                String idStatus = UserPreferencesUtil.getUserIDAuth(ActManagerActivity.this);
                String videoStatus = UserPreferencesUtil.getUserVideoAuth(ActManagerActivity.this);
//                if ("2".equals(idStatus)) {
//                    if ("2".equals(videoStatus)) {
//                        showPOP();
//                    } else {
//                        showForwardsDialog("您还没有进行视频认证，认证后才能发布活动", 2);
//                    }
//                } else {
//                    showForwardsDialog("您还没有进行身份认证，认证后才能发布活动", 1);
//                }
                if ("2".equals(videoStatus)) {
                    showPOP();
                } else {
                    showForwardsDialog("您还没有进行视频认证，认证后才能发布活动", 2);
                }
            } else {
                Intent intent = new Intent(ActManagerActivity.this, LoginWelcomActivity.class);
                startActivity(intent);
            }
        }
    }

    private void showForwardsDialog(String content, final int flag) {
        OneButtonDialog dialog = new OneButtonDialog(ActManagerActivity.this, content, "去认证", R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                        actForwardsAct(flag);
                    }
                });
        dialog.show();
    }

    private void actForwardsAct(int index) {
        switch (index) {
            case 1:
                String idStatus = UserPreferencesUtil.getUserIDAuth(ActManagerActivity.this);
                if (idStatus != null) {
                    if ("0".equals(idStatus)) {
                        startActivity(new Intent(ActManagerActivity.this, ModelIdentyFirstActivity.class));
                    } else if ("1".equals(idStatus)) {
                        showOneButtonDialog("您的身份认证审核中，请耐心等待");
                    } else if ("4".equals(idStatus)) {
                        showOneButtonDialog("您的身份认证审核中，请耐心等待");
                    } else if ("2".equals(idStatus)) {
                        showOneButtonDialog("恭喜您，您的身份认证已通过");
                    } else if ("3".equals(idStatus)) {
                        showTwoButtonDialog("抱歉！您的身份认证被拒绝，原因：".concat(UserPreferencesUtil.getUserIDAuthRefuseReason(ActManagerActivity.this)), "下次再说", "前往认证");
                    }
                } else {
                    startActivity(new Intent(ActManagerActivity.this, ModelIdentyFirstActivity.class));
                }
                break;
            case 2:
                String videoStatus = UserPreferencesUtil.getUserVideoAuth(ActManagerActivity.this);
                if (videoStatus != null) {
                    if ("0".equals(videoStatus)) {
                        Intent intent12 = new Intent(ActManagerActivity.this, VideoCertifyActivity.class);
                        startActivity(intent12);
                    } else if ("1".equals(videoStatus)) {
                        Intent intent11 = new Intent(ActManagerActivity.this, VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    } else if ("4".equals(videoStatus)) {
                        Intent intent11 = new Intent(ActManagerActivity.this, VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    } else if ("2".equals(videoStatus)) {
                        Intent intent11 = new Intent(ActManagerActivity.this, VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    } else if ("3".equals(videoStatus)) {
                        Intent intent11 = new Intent(ActManagerActivity.this, VideoCertifyChangeActivity.class);
                        startActivity(intent11);
                    }
                } else {
                    Intent intent13 = new Intent(ActManagerActivity.this, VideoCertifyActivity.class);
                    startActivity(intent13);
                }
                break;
        }
    }

    private void showTwoButtonDialog(String content, String leftText, String rightText) {
        TwoButtonDialog dialog = new TwoButtonDialog(ActManagerActivity.this, content, leftText, rightText, R.style.dineDialog,
                new ButtonTwoListener() {

                    @Override
                    public void confrimListener() {
                    }

                    @Override
                    public void cancelListener() {
                        startActivity(new Intent(ActManagerActivity.this, ModelIdentyFirstActivity.class));
                    }
                });
        dialog.show();
    }

    private void showOneButtonDialog(String content) {
        OneButtonDialog dialog = new OneButtonDialog(ActManagerActivity.this, content, R.style.dineDialog,
                new ButtonOneListener() {

                    @Override
                    public void confrimListener(Dialog dialog) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onPageSelected(int arg0) {
        tab_l.setCurrentTab(arg0);
        switch (arg0) {
            case 0:
                loadFlag = 0;
                if (paiSAdapter.getCount() == 0) {
                    loadDatas(0);
                }
                break;
            case 1:
                loadFlag = 1;
                if (finishAdapter.getCount() == 0) {
                    loadDatas(1);
                }
                break;

            case 2:
                loadFlag = 2;
                if (closeAdapter.getCount() == 0) {
                    loadDatas(2);
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
//                boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
//                if (result) {
//                    if (flagIndex==0){
//                        daiSAdapter.updatePayStatus(noticeBeanItem);
//                    }else if (flagIndex==1){
//                        zhaoMAdapter.updatePayStatus(noticeBeanItem);
//                    }
//
//                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //刷新操作
        switch (loadFlag) {
            case 0:
                hotIsLoading = true;
                hotPage = 1;
                loadDatas(0);
                break;
            case 1:
                fansIsLoading = true;
                fansPage = 1;
                loadDatas(1);
                break;
            case 2:
                recommendIsLoading = true;
                recomendPage = 1;
                loadDatas(2);
                break;
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //加载操作
        switch (loadFlag) {
            case 0:
                if (hotIsLoading) {
                    hotPage = hotPage + 1;
                    loadDatas(0);
                } else {
                    falseDatas(0);
                }
                break;
            case 1:
                if (fansIsLoading) {
                    fansPage = fansPage + 1;
                    loadDatas(1);
                } else {
                    falseDatas(1);
                }
                break;
            case 2:
                if (recommendIsLoading) {
                    recomendPage = recomendPage + 1;
                    loadDatas(2);
                } else {
                    falseDatas(2);
                }
                break;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    PaiSList.onRefreshComplete();
                    break;
                case 1:
                    finishList.onRefreshComplete();
                    break;
                case 2:
                    closeList.onRefreshComplete();
                    break;
                default:
                    break;
            }
        }
    };


    private void falseDatas(final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = index;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ACT_MANAGER:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    stopListRefresh();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void stopListRefresh() {
        switch (loadFlag) {
            case 0:
                PaiSList.onRefreshComplete();
                break;
            case 1:
                finishList.onRefreshComplete();
                break;
            case 2:
                closeList.onRefreshComplete();
                break;
            default:
                break;
        }
    }

    private void parseDatas(ResponseBean data) {
        switch (loadFlag) {
            case 0:
                PaiSList.onRefreshComplete();
                parseHotModelDatas(data);
                break;
            case 1:
                finishList.onRefreshComplete();
                parseFansModelDatas(data);
                break;
            case 2:
                closeList.onRefreshComplete();
                parseRecommendModelDatas(data);
                break;
            default:
                break;
        }
    }

    private void parseHotModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<ActManagerBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
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
            paiSAdapter.addDatas(datas, hotPage);
        } else {
            hotIsLoading = false;
        }
    }

    private void parseFansModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<ActManagerBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
                fansReqTime = object.getString("reqTime");
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
                fansIsLoading = false;
            }
            finishAdapter.addDatas(datas, fansPage);
        } else {
            fansIsLoading = false;
        }
    }

    private void parseRecommendModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<ActManagerBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
                recommendReqTime = object.getString("reqTime");
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
                recommendIsLoading = false;
            }
            closeAdapter.addDatas(datas, recomendPage);
        } else {
            recommendIsLoading = false;
        }
    }

    private void showPOP() {
        mPopWindow = initPopWindow(ActManagerActivity.this,
                R.layout.pop_manage_layout, true);
        View root = mPopWindow.getContentView();
        View jingPaiYueView = root.findViewById(R.id.jingPaiYueView);
        View fufeiYueView = root.findViewById(R.id.fufeiYueView);
        View giftYueView = root.findViewById(R.id.giftYueView);
        View bottomNullView = root.findViewById(R.id.bottomNullView);

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mPopWindow != null) {
                    mPopWindow = null;
                }
            }
        });

        jingPaiYueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                Intent intent1 = new Intent(ActManagerActivity.this, JinPaiPlushActivity.class);
                intent1.putExtra("wichFlag", "jingpaiYue");
                startActivityForResult(intent1, 0);
            }
        });

        fufeiYueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                Intent intent = new Intent(ActManagerActivity.this, JinPaiPlushActivity.class);
                intent.putExtra("wichFlag", "fufeiYue");
                startActivityForResult(intent, 0);
            }
        });

        giftYueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                Intent intent = new Intent(ActManagerActivity.this, YuePaiPlushActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        bottomNullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });

        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
            mPopWindow = null;
        } else {
            mPopWindow.showAsDropDown(findViewById(R.id.optionsView));
        }
    }

    private PopupWindow initPopWindow(Context context, int popWinLayout,
                                      boolean isDismissMenuOutsideTouch) {

        PopupWindow mPopWin = new PopupWindow(
                ((LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(popWinLayout, null),
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        if (isDismissMenuOutsideTouch)
            mPopWin.setBackgroundDrawable(new BitmapDrawable());
        mPopWin.setOutsideTouchable(true);
        mPopWin.setFocusable(true);
        if (mPopWin.isShowing()) {
            mPopWin.dismiss();
            mPopWin = null;
        }
        return mPopWin;
    }
}
