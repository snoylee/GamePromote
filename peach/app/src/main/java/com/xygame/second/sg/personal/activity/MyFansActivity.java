package com.xygame.second.sg.personal.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.comm.inteface.FansAttetListener;
import com.xygame.second.sg.personal.adapter.MyFansAdapter;
import com.xygame.second.sg.personal.bean.GuanZhuFansBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/8/22.
 */
public class MyFansActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2,FansAttetListener {

    private TextView titleName;
    private View backButton;
    private PullToRefreshListView2 fansList;
    private int pageSize = 21;//每页显示的数量
    private int fansPage = 1;//当前显示页数
    private String fansReqTime,userId;
    private boolean fansIsLoading = true;
    private GuanZhuFansBean tempActionBean;
    private MyFansAdapter adapter;
    private boolean tempBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_fans_layout);
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
        fansList.setOnRefreshListener(this);
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
        fansList = (PullToRefreshListView2) findViewById(R.id.fansList);
        fansList.setMode(PullToRefreshBase.Mode.BOTH);
    }

    /**
     * <一句话功能简述> <功能详细描述>
     *
     * @action [请添加内容描述]
     */
    private void initDatas() {
        userId=getIntent().getStringExtra("userId");
        titleName.setText("粉丝");
        adapter = new MyFansAdapter(this, null,userId);
        adapter.addActListener(this);
        fansList.setAdapter(adapter);
        loadDatas();
    }

    private void loadDatas() {//index:1进行中 2已完成 3已关闭
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", fansPage).put("pageSize", pageSize));
            obj.put("userId",userId);
            if (fansPage > 1) {
                obj.put("reqTime", fansReqTime);
            } else {
                ShowMsgDialog.showNoMsg(this, true);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_USER_FANS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USER_FANS);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //刷新操作
        fansIsLoading = true;
        fansPage = 1;
        loadDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //加载操作
        if (fansIsLoading) {
            fansPage = fansPage + 1;
            loadDatas();
        } else {
            falseDatas();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    fansList.onRefreshComplete();
                    break;
                default:
                    break;
            }

        }
    };


    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what = 0;
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
            case ConstTaskTag.QUERY_USER_FANS:
                fansList.onRefreshComplete();
                if ("0000".equals(data.getCode())) {
                    parseFansModelDatas(data);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_USER_ATTEN:
                if ("0000".equals(data.getCode())) {
                   adapter.updateDatas(tempActionBean,tempBoolean);
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void parseFansModelDatas(ResponseBean data) {
        String resposeStr = data.getRecord();
        if (!TextUtils.isEmpty(resposeStr) && !"null".equals(resposeStr)) {
            List<GuanZhuFansBean> datas = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data.getRecord());
                fansReqTime = object.getString("reqTime");
                String actions = object.getString("users");
                if (ConstTaskTag.isTrueForArrayObj(actions)) {
                    JSONArray array2 = new JSONArray(actions);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject object1 = array2.getJSONObject(i);
                        GuanZhuFansBean item = new GuanZhuFansBean();
                        item.setUsernick(StringUtils.getJsonValue(object1, "usernick"));
                        item.setUserIcon(StringUtils.getJsonValue(object1, "userIcon"));
                        item.setUserId(StringUtils.getJsonValue(object1, "userId"));
                        item.setAge(StringUtils.getJsonValue(object1, "age"));
                        item.setGender(StringUtils.getJsonValue(object1, "gender"));
                        item.setIntroDesc(StringUtils.getJsonValue(object1, "introDesc"));
                        item.setHasAttent(StringUtils.getJsonValue(object1, "hasAttent"));
                        datas.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (datas.size() < pageSize) {
                fansIsLoading = false;
            }
            adapter.addDatas(datas, fansPage);
        } else {
            fansIsLoading = false;
        }
    }

    @Override
    public void attetionComfim(GuanZhuFansBean bean) {
        this.tempActionBean=bean;
        this.tempBoolean=false;
        guanZhuAction(false);
    }

    @Override
    public void attetionCancel(GuanZhuFansBean bean) {
        this.tempActionBean=bean;
        this.tempBoolean=true;
        guanZhuAction(true);
    }

    private void guanZhuAction(boolean flag) {
        RequestBean item = new RequestBean();
        item.setIsPublic(false);
        try {
            JSONObject object = new JSONObject();
            object.put("userId", tempActionBean.getUserId());
            if (flag){
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
}
