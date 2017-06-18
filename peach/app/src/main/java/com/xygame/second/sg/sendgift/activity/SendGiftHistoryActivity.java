package com.xygame.second.sg.sendgift.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.xygame.second.sg.comm.bean.TransferGift;
import com.xygame.second.sg.comm.inteface.LuQuListener;
import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.sendgift.adapter.GiftHistoryAdapter;
import com.xygame.second.sg.sendgift.bean.GiftPresenter;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SendGiftHistoryActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2,LuQuListener {
    private View backButton;
    private PullToRefreshListView2 listView;
    private int pageSize = 21;//每页显示的数量
    private int hotPage = 1;//当前显示页数
    private String hotReqTime,hireUserId;
    private boolean hotIsLoading = true;
    private GiftHistoryAdapter adapter;
    private String actId,fromFlag,publihserId,actTitle;
    private Presenter currPresenter;
    private boolean isLvYunAialable=false;

    @Override
    public void finish() {
        if (adapter.getCount()>0){
            TransferGift tempDatas=new TransferGift();
            tempDatas.setVector(adapter.getDatas());
            Intent intent = new Intent();
            intent.putExtra(Constants.COMEBACK, tempDatas);
            setResult(Activity.RESULT_OK, intent);
        }
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_history_layout);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        listView=(PullToRefreshListView2)findViewById(R.id.listView);
    }

    private void initDatas() {
        isLvYunAialable=getIntent().getBooleanExtra("isLvYunAialable",false);
        actTitle=getIntent().getStringExtra("actTitle");
        publihserId=getIntent().getStringExtra("publihserId");
        fromFlag=getIntent().getStringExtra("fromFlag");
        actId=getIntent().getStringExtra("actId");
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter=new GiftHistoryAdapter(this,null,fromFlag,publihserId,isLvYunAialable);
        adapter.setLuQuActListener(this);
        listView.setAdapter(adapter);
        loadJinPaiDatas();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
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
        item.setServiceURL(ConstTaskTag.QUEST_ALLGIFT_SENDER);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ALLGIFT_SENDER);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ALLGIFT_SENDER:
                listView.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(SendGiftHistoryActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case  ConstTaskTag.QUERY_FREE_LQ1:
                if ("0000".equals(data.getCode())) {
                    hireUserId=currPresenter.getUserId();
                    adapter.updateUseStatus(hireUserId);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseHotModelDatas(ResponseBean data) {
        try {
            JSONObject object = new JSONObject(data.getRecord());
            String partDynamics=StringUtils.getJsonValue(object, "dynamics");
            hotReqTime=StringUtils.getJsonValue(object, "reqTime");
            if (hotPage==1){
                hireUserId=StringUtils.getJsonValue(object, "hireUserId");
            }
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
                if (giftPresenters.size() < pageSize) {
                    hotIsLoading = false;
                }
            adapter.addDatas(giftPresenters, hotPage,hireUserId);
            }else {
                hotIsLoading = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    Toast.makeText(SendGiftHistoryActivity.this, "已全部加载", Toast.LENGTH_SHORT).show();
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
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_FREE_LQ1);
    }
}
