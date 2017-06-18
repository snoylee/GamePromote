package com.xygame.second.sg.sendgift.activity;


import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.LuQuListener;
import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.sendgift.adapter.GiftHistoryAdapter;
import com.xygame.second.sg.sendgift.adapter.GiftRankAdapter;
import com.xygame.second.sg.sendgift.bean.GiftPresenter;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.second.sg.sendgift.bean.RankBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;
import com.xygame.sg.widget.refreash.PullToRefreshListView3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GiftRankActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2,LuQuListener {
    private View backButton,headView,viewBlock1,lvQuButton1,viewBlock2,lvQuButton2,viewBlock3,lvQuButton3,sex_age_bg1,sex_age_bg2,sex_age_bg3;
    private PullToRefreshListView3 listView;
    private int pageSize = 21;//每页显示的数量
    private int hotPage = 1;//当前显示页数
    private String hotReqTime,hireUserId;
    private boolean hotIsLoading = true;
    private GiftRankAdapter adapter;
    private CircularImage avatar_iv1,avatar_iv2,avatar_iv3;
    private TextView userName1,sexAge1,gxValue1,userName2,sexAge2,gxValue2,userName3,sexAge3,gxValue3, lqText1,lqText2,lqText3;
    private ImageView sexIcon1,sexIcon2,sexIcon3;
    private String actId,fromFlag,publihserId,actTitle;
    private ImageLoader mImageLoader;
    private Presenter currPresenter;
    private  List<RankBean>  templeTopDatas;
    private boolean isLvYunAialable=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_rank_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        listView=(PullToRefreshListView3)findViewById(R.id.listView);
        headView = LayoutInflater.from(this).inflate(R.layout.gift_rank_head, null);
        //头部第一部分
        viewBlock1=headView.findViewById(R.id.viewBlock1);
        lvQuButton1=headView.findViewById(R.id.lvQuButton1);
        avatar_iv1=(CircularImage)headView.findViewById(R.id.avatar_iv1);
        userName1=(TextView)headView.findViewById(R.id.userName1);
        sexAge1=(TextView)headView.findViewById(R.id.sexAge1);
        gxValue1=(TextView)headView.findViewById(R.id.gxValue1);
        sexIcon1=(ImageView)headView.findViewById(R.id.sexIcon1);
        sex_age_bg1=headView.findViewById(R.id.sex_age_bg1);
        lqText1=(TextView)headView.findViewById(R.id.lqText1);
        //头部第二部分
        viewBlock2=headView.findViewById(R.id.viewBlock2);
        lvQuButton2=headView.findViewById(R.id.lvQuButton2);
        avatar_iv2=(CircularImage)headView.findViewById(R.id.avatar_iv2);
        userName2=(TextView)headView.findViewById(R.id.userName2);
        sexAge2=(TextView)headView.findViewById(R.id.sexAge2);
        gxValue2=(TextView)headView.findViewById(R.id.gxValue2);
        sexIcon2=(ImageView)headView.findViewById(R.id.sexIcon2);
        sex_age_bg2=headView.findViewById(R.id.sex_age_bg2);
        lqText2=(TextView)headView.findViewById(R.id.lqText2);
        //头部第三部分
        viewBlock3=headView.findViewById(R.id.viewBlock3);
        lvQuButton3=headView.findViewById(R.id.lvQuButton3);
        avatar_iv3=(CircularImage)headView.findViewById(R.id.avatar_iv3);
        userName3=(TextView)headView.findViewById(R.id.userName3);
        sexAge3=(TextView)headView.findViewById(R.id.sexAge3);
        gxValue3=(TextView)headView.findViewById(R.id.gxValue3);
        sexIcon3=(ImageView)headView.findViewById(R.id.sexIcon3);
        sex_age_bg3=headView.findViewById(R.id.sex_age_bg3);
        lqText3=(TextView)headView.findViewById(R.id.lqText3);

        viewBlock1.setVisibility(View.GONE);
        viewBlock2.setVisibility(View.GONE);
        viewBlock3.setVisibility(View.GONE);

        listView.addHeadViewAction(headView);
    }

    private void initDatas() {
        isLvYunAialable=getIntent().getBooleanExtra("isLvYunAialable",false);
        hireUserId=getIntent().getStringExtra("hireUserId");
        actTitle=getIntent().getStringExtra("actTitle");
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        publihserId=getIntent().getStringExtra("publihserId");
        fromFlag=getIntent().getStringExtra("fromFlag");
        actId=getIntent().getStringExtra("actId");
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter=new GiftRankAdapter(this,null,fromFlag,publihserId,isLvYunAialable);
        adapter.setLuQuActListener(this);
        listView.setAdapter(adapter);
        loadJinPaiDatas();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        listView.setOnRefreshListener(this);
        lvQuButton1.setOnClickListener(this);
        lvQuButton2.setOnClickListener(this);
        lvQuButton3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.lvQuButton1:
                if (TextUtils.isEmpty(hireUserId)){
                    Presenter item=templeTopDatas.get(0).getPresenter();
                    luQuAction(item);
                }
                break;
            case R.id.lvQuButton2:
                if (TextUtils.isEmpty(hireUserId)){
                    Presenter item1=templeTopDatas.get(1).getPresenter();
                    luQuAction(item1);
                }
                break;
            case R.id.lvQuButton3:
                if (TextUtils.isEmpty(hireUserId)){
                    Presenter item2=templeTopDatas.get(2).getPresenter();
                    luQuAction(item2);
                }
                break;
        }
    }

    public void loadJinPaiDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
            obj.put("actId", actId);
            obj.put("userId",publihserId);
            if (hotPage > 1) {
                obj.put("reqTime", hotReqTime);
            } else {
                adapter.clearDatas();
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_ODER_SENDER);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ODER_SENDER);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ODER_SENDER:
                listView.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(GiftRankActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_FREE_LQ:
                if ("0000".equals(data.getCode())) {
                    hireUserId=currPresenter.getUserId();
                    updateTopLYButton();
                    adapter.updateUseStatus(hireUserId);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void updateTopLYButton(){
        for (int i=0;i<templeTopDatas.size();i++){
            switch (i){
                case 0:
                    updateTopView1(templeTopDatas.get(i));
                    break;
                case 1:
                    updateTopView2(templeTopDatas.get(i));
                    break;
                case 2:
                    updateTopView3(templeTopDatas.get(i));
                    break;
            }
        }
    }

    private void parseHotModelDatas(ResponseBean data) {
        try {
            JSONObject object = new JSONObject(data.getRecord());
            String partDynamics=StringUtils.getJsonValue(object, "rank");
            hotReqTime=StringUtils.getJsonValue(object, "reqTime");
            if (hotPage==1){
                hireUserId=StringUtils.getJsonValue(object, "hireUserId");
            }
            if (!TextUtils.isEmpty(partDynamics)&&!"[]".equals(partDynamics)&&!"null".equals(partDynamics)&&!"[null]".equals(partDynamics)){
                List<RankBean> giftPresenters=new ArrayList<>();
                JSONArray array2=new JSONArray(partDynamics);
                for (int i=0;i<array2.length();i++){
                    JSONObject object1 = array2.getJSONObject(i);
                    RankBean item=new RankBean();
                    item.setAmount(StringUtils.getJsonValue(object1, "amount"));
                    String participator=StringUtils.getJsonValue(object1, "user");
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
                if (giftPresenters.size() < pageSize) {
                    hotIsLoading = false;
                }
                if (hotPage==1){
                    if (giftPresenters.size()>3){
                        List<RankBean> templeDatas=new ArrayList<>();
                        templeTopDatas=new ArrayList<>();
                        for (int k=0;k<3;k++){
                            templeTopDatas.add(giftPresenters.get(k));
                        }
                        for (int p=3;p<giftPresenters.size();p++){
                            templeDatas.add(giftPresenters.get(p));
                        }
                        updateTopViews(templeTopDatas);
                        adapter.addDatas(templeDatas, hotPage,hireUserId);
                    }else{
                        templeTopDatas=giftPresenters;
                        updateTopViews(giftPresenters);
                    }
                }else{
                    adapter.addDatas(giftPresenters, hotPage,hireUserId);
                }
            }else {
                hotIsLoading = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTopViews(List<RankBean> templeTopDatas) {
        switch (templeTopDatas.size()){
            case 0:
                viewBlock1.setVisibility(View.GONE);
                viewBlock2.setVisibility(View.GONE);
                viewBlock3.setVisibility(View.GONE);
                break;
            case 1:
                viewBlock1.setVisibility(View.VISIBLE);
                viewBlock2.setVisibility(View.GONE);
                viewBlock3.setVisibility(View.GONE);
                RankBean item=templeTopDatas.get(0);
                updateTopView1(item);
                break;
            case 2:
                viewBlock1.setVisibility(View.VISIBLE);
                viewBlock2.setVisibility(View.VISIBLE);
                viewBlock3.setVisibility(View.GONE);
                updateTopView1(templeTopDatas.get(0));
                updateTopView2(templeTopDatas.get(1));
                break;
            case 3:
                viewBlock1.setVisibility(View.VISIBLE);
                viewBlock2.setVisibility(View.VISIBLE);
                viewBlock3.setVisibility(View.VISIBLE);
                updateTopView1(templeTopDatas.get(0));
                updateTopView2(templeTopDatas.get(1));
                updateTopView3(templeTopDatas.get(2));
                break;
        }
    }

    private void updateTopView1(RankBean item){
        Presenter presenter = item.getPresenter();
        mImageLoader.loadImage(presenter.getUserIcon(), avatar_iv1, true);
        userName1.setText(presenter.getUserNick());
        gxValue1.setText(String.valueOf(ConstTaskTag.getDoublePrice(item.getAmount())));
        String sexStr = presenter.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            sex_age_bg1.setBackgroundResource(R.drawable.sex_bg);
            sexIcon1.setImageResource(R.drawable.sg_woman_light_icon);
            sexAge1.setText(presenter.getAge());
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            sex_age_bg1.setBackgroundResource(R.drawable.sex_male_bg);
            sexIcon1.setImageResource(R.drawable.sg_man_light_icon);
            sexAge1.setText(presenter.getAge());
        }
        if (publihserId.equals(UserPreferencesUtil.getUserId(this))){
            if (isLvYunAialable){
                lvQuButton1.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(hireUserId)){
                    if (presenter.getUserId().equals(hireUserId)){
                        lqText1.setText("已录用");
                    }
                    lqText1.setTextColor(getResources().getColor(R.color.gray));
                    lvQuButton1.setBackgroundResource(R.drawable.shape_rect_gray);
                }
            }else{
                lvQuButton1.setVisibility(View.GONE);
            }
        }else{
            lvQuButton1.setVisibility(View.GONE);
        }
    }

    private void updateTopView2(RankBean item){
        Presenter presenter = item.getPresenter();
        mImageLoader.loadImage(presenter.getUserIcon(), avatar_iv2, true);
        userName2.setText(presenter.getUserNick());
        gxValue2.setText(String.valueOf(ConstTaskTag.getDoublePrice(item.getAmount())));
        String sexStr = presenter.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            sex_age_bg2.setBackgroundResource(R.drawable.sex_bg);
            sexIcon2.setImageResource(R.drawable.sg_woman_light_icon);
            sexAge2.setText(presenter.getAge());
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            sex_age_bg2.setBackgroundResource(R.drawable.sex_male_bg);
            sexIcon2.setImageResource(R.drawable.sg_man_light_icon);
            sexAge2.setText(presenter.getAge());
        }
        if (publihserId.equals(UserPreferencesUtil.getUserId(this))){
            if (isLvYunAialable){
                lvQuButton2.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(hireUserId)){
                    if (presenter.getUserId().equals(hireUserId)){
                        lqText2.setText("已录用");
                    }
                    lqText2.setTextColor(getResources().getColor(R.color.gray));
                    lvQuButton2.setBackgroundResource(R.drawable.shape_rect_gray);
                }
            }else{
                lvQuButton2.setVisibility(View.GONE);
            }
        }else{
            lvQuButton2.setVisibility(View.GONE);
        }
    }

    private void updateTopView3(RankBean item){
        Presenter presenter = item.getPresenter();
        mImageLoader.loadImage(presenter.getUserIcon(), avatar_iv3, true);
        userName3.setText(presenter.getUserNick());
        gxValue3.setText(String.valueOf(ConstTaskTag.getDoublePrice(item.getAmount())));
        String sexStr = presenter.getGender();
        if (Constants.SEX_WOMAN.equals(sexStr)) {
            sex_age_bg3.setBackgroundResource(R.drawable.sex_bg);
            sexIcon3.setImageResource(R.drawable.sg_woman_light_icon);
            sexAge3.setText(presenter.getAge());
        } else if (Constants.SEX_MAN.equals(sexStr)) {
            sex_age_bg3.setBackgroundResource(R.drawable.sex_male_bg);
            sexIcon3.setImageResource(R.drawable.sg_man_light_icon);
            sexAge3.setText(presenter.getAge());
        }
        if (publihserId.equals(UserPreferencesUtil.getUserId(this))){
            if (isLvYunAialable){
                lvQuButton3.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(hireUserId)){
                    if (presenter.getUserId().equals(hireUserId)){
                        lqText3.setText("已录用");
                    }
                    lqText3.setTextColor(getResources().getColor(R.color.gray));
                    lvQuButton3.setBackgroundResource(R.drawable.shape_rect_gray);
                }
            }else{
                lvQuButton3.setVisibility(View.GONE);
            }
        }else{
            lvQuButton3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        hotIsLoading = true;
        hotPage = 1;
        loadJinPaiDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (hotIsLoading) {
            hotPage = hotPage + 1;
            loadJinPaiDatas();
        } else {
            falseDatasModel();
        }
    }

    private void falseDatasModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = 1;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    listView.onRefreshComplete();
                    Toast.makeText(GiftRankActivity.this, "已全部加载", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void luQuAction(Presenter presenter) {
        currPresenter=presenter;
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("memberId",presenter.getUserId());
            obj.put("actId", actId);
            obj.put("userId",publihserId);
            obj.put("actTitle",actTitle);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, false);
        item.setServiceURL(ConstTaskTag.QUEST_FREE_LQ);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_FREE_LQ);
    }
}
