package com.xygame.second.sg.sendgift.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.xygame.second.sg.comm.bean.TransferGift;
import com.xygame.second.sg.jinpai.adapter.JinPaiMemberAdapter;
import com.xygame.second.sg.jinpai.bean.JinPaiCanYuBean;
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


public class JinPaiMembersActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private View backButton;
    private PullToRefreshListView2 listView;
    private int pageSize = 21;//每页显示的数量
    private int hotPage = 1;//当前显示页数
    private boolean hotIsLoading = true;
    private JinPaiMemberAdapter adapter;
    private String actId,hotReqTime;

    @Override
    public void finish() {
        if (adapter.getCount()>0){
            TransferGift tempDatas=new TransferGift();
            tempDatas.setJinPaiCanYuBeans(adapter.getDatas());
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
        actId=getIntent().getStringExtra("actId");
        listView.setMode(PullToRefreshBase.Mode.BOTH);
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
        adapter=new JinPaiMemberAdapter(this,null);
        listView.setAdapter(adapter);
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", hotPage).put("pageSize", pageSize));
            obj.put("actId", actId);
            if (hotPage > 1) {
                obj.put("reqTime", hotReqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_JINPAI_LIST);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JINPAI_LIST);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_JINPAI_LIST:
                listView.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseHotModelDatas(data);
                } else {
                    Toast.makeText(JinPaiMembersActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseHotModelDatas(ResponseBean data) {
        try {
            JSONObject object = new JSONObject(data.getRecord());
            String partDynamics=StringUtils.getJsonValue(object, "bidFlows");
            hotReqTime=StringUtils.getJsonValue(object, "reqTime");
            if (!TextUtils.isEmpty(partDynamics)&&!"[]".equals(partDynamics)&&!"null".equals(partDynamics)&&!"[null]".equals(partDynamics)){
                List<JinPaiCanYuBean> giftPresenters=new ArrayList<>();
                JSONArray array2=new JSONArray(partDynamics);
                for (int i=0;i<array2.length();i++){
                    JSONObject object1 = array2.getJSONObject(i);
                    JinPaiCanYuBean item=new JinPaiCanYuBean();
                    item.setBidPrice(StringUtils.getJsonValue(object1, "bidPrice"));
                    item.setBidTime(StringUtils.getJsonValue(object1, "bidTime"));
                    String participator=StringUtils.getJsonValue(object1, "bidUser");
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
            adapter.addDatas(giftPresenters, hotPage);
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
                    Toast.makeText(JinPaiMembersActivity.this, "已全部加载", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
