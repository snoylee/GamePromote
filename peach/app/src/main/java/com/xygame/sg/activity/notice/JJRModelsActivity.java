package com.xygame.sg.activity.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.notice.adapter.JJRModelsAdapter;
import com.xygame.sg.activity.notice.adapter.JJRNoticeAdapter;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.JJRPublisher;
import com.xygame.sg.activity.notice.bean.NoticeMemberVo;
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


public class JJRModelsActivity extends SGBaseActivity implements PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshListView2 listView;
    private int pageSize = 10;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private JJRModelsAdapter adapter;
    private String reqTime;
    private boolean isLoading=true;
    private JJRNoticeBean itemBean;
    private TextView titleName;
    private View backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jjr_models_layout);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        backButton=findViewById(R.id.backButton);
        titleName=(TextView)findViewById(R.id.titleName);
        listView = (PullToRefreshListView2)findViewById(R.id.refreshlistview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);

    }

    private void initDatas() {
        titleName.setText("报名人员");
        itemBean=(JJRNoticeBean)getIntent().getSerializableExtra("item");
        adapter = new JJRModelsAdapter(this, null,itemBean);
        listView.setAdapter(adapter);
        requestNoticesList();
    }

    private void requestNoticesList() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page",new JSONObject().put("pageIndex",mCurrentPage).put("pageSize",pageSize));
            obj.put("noticeId",itemBean.getNoticeId());
            if (mCurrentPage>1){
                obj.put("reqTime",reqTime);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_JJR_NOTICE_MODELS);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_JJR_NOTICE_MODELS);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        listView.onRefreshComplete();
        switch (data.getPosionSign()){
            case ConstTaskTag.RESPOSE_JJR_NOTICE_MODELS:
                if ("0000".equals(data.getCode())){
                    parseDatas(data);
                }else{
                    Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void parseDatas(ResponseBean data) {
        try {
            List<NoticeMemberVo> datas=new ArrayList<>();
            JSONObject object=new JSONObject(data.getRecord());
            reqTime= StringUtils.getJsonValue(object, "reqTime");
            String notices=StringUtils.getJsonValue(object, "members");
            if (!TextUtils.isEmpty(notices)&&!"[]".equals(notices)){
                JSONArray array=new JSONArray(notices);
                for (int i=0;i<array.length();i++){
                    JSONObject subObj=array.getJSONObject(i);
                    NoticeMemberVo item=new NoticeMemberVo();
                    item.setUserNick(StringUtils.getJsonValue(subObj, "usernick"));
                    item.setUserIcon(StringUtils.getJsonValue(subObj, "userIcon"));
                    item.setUserId(Long.parseLong(StringUtils.getJsonValue(subObj, "userId")));
                    item.setUserType(Integer.parseInt(StringUtils.getJsonValue(subObj, "userType")));
                    item.setAge(Integer.parseInt(StringUtils.getJsonValue(subObj, "age")));
                    item.setProvince(Integer.parseInt(StringUtils.getJsonValue(subObj, "province")));
                    item.setCity(Integer.parseInt(StringUtils.getJsonValue(subObj, "city")));
                    item.setGender(Integer.parseInt(StringUtils.getJsonValue(subObj, "gender")));
                    datas.add(item);
                }
                if (datas.size()<pageSize){
                    isLoading=false;
                }
                adapter.addDatas(datas,mCurrentPage);
            }else{
                isLoading=false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        listView.onRefreshComplete();
//        listView.setEmptyView();
    }

    private void initListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isLoading=true;
        mCurrentPage=1;
        requestNoticesList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isLoading){
            mCurrentPage=mCurrentPage+1;
            requestNoticesList();
        }else{
            falseDatas();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    listView.onRefreshComplete();
                    break;
                default:
                    break;
            }

        }
    };

    private void falseDatas(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                        Message m = handler.obtainMessage();
                        m.what=2;
                        m.sendToTarget();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
