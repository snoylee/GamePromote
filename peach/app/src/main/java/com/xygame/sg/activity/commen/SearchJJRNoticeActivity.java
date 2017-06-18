package com.xygame.sg.activity.commen;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.JJRNoticeAdapter;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.JJRPublisher;
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

public class SearchJJRNoticeActivity extends SGBaseActivity implements PullToRefreshBase.OnRefreshListener2,View.OnClickListener{
    private PullToRefreshListView2 listView;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private JJRNoticeAdapter adapter;
    private String reqTime;
    private View back_iv,serchButton;
    private EditText search_content_et;
    private String content;
    private boolean isLoading=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_jjr_notice);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        serchButton=findViewById(R.id.serchButton);
        search_content_et=(EditText)findViewById(R.id.search_content_et);
        back_iv=findViewById(R.id.back_iv);
        listView = (PullToRefreshListView2)findViewById(R.id.refreshlistview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_iv:
                finish();
                break;
            case R.id.serchButton:
                content =search_content_et.getText().toString();
                if (!TextUtils.isEmpty(content)){
                    mCurrentPage=1;
                    requestNoticesList();
                }else{
                    Toast.makeText(this,"请输入通告标题",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initDatas() {
        serchButton.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        adapter = new JJRNoticeAdapter(this, null);
        listView.setAdapter(adapter);
    }

    private void requestNoticesList() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page",new JSONObject().put("pageIndex",mCurrentPage).put("pageSize",pageSize));
            obj.put("content",content);
            if (mCurrentPage>1){
                obj.put("reqTime",reqTime);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        search_content_et.setText("");
        ShowMsgDialog.showNoMsg(this, true);
        item.setServiceURL(ConstTaskTag.QUEST_JJR_NOTICE_SEARCH_LIST);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JJR_NOTICE_SEARCH_LIST);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        listView.onRefreshComplete();
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_JJR_NOTICE_SEARCH_LIST:
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
            List<JJRNoticeBean> datas=new ArrayList<>();
            JSONObject object=new JSONObject(data.getRecord());
            reqTime= StringUtils.getJsonValue(object, "reqTime");
            String notices=StringUtils.getJsonValue(object, "notices");
            if (!TextUtils.isEmpty(notices)&&!"[]".equals(notices)){
                JSONArray array=new JSONArray(notices);
                for (int i=0;i<array.length();i++){
                    JSONObject subObj=array.getJSONObject(i);
                    JJRNoticeBean item=new JJRNoticeBean();
                    item.setApplyCount(StringUtils.getJsonValue(subObj, "applyCount"));
                    item.setExpand(false);
                    item.setNoticeContent(StringUtils.getJsonValue(subObj, "noticeContent"));
                    item.setNoticeId(StringUtils.getJsonValue(subObj, "noticeId"));
                    item.setSubject(StringUtils.getJsonValue(subObj, "subject"));
                    JSONObject publisherObj=new JSONObject(StringUtils.getJsonValue(subObj, "publisher"));
                    JJRPublisher publisher=new JJRPublisher();
                    publisher.setAuthStatus(StringUtils.getJsonValue(publisherObj, "authStatus"));
                    publisher.setUserIcon(StringUtils.getJsonValue(publisherObj, "userIcon"));
                    publisher.setUserId(StringUtils.getJsonValue(publisherObj, "userId"));
                    publisher.setUsernick(StringUtils.getJsonValue(publisherObj, "usernick"));
                    publisher.setUserType(StringUtils.getJsonValue(publisherObj, "userType"));
                    item.setPublisher(publisher);
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
    }

    private void initListeners() {
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        content =search_content_et.getText().toString();
        if (!TextUtils.isEmpty(content)){
            isLoading=true;
            mCurrentPage=1;
            requestNoticesList();
        }else{
            falseDatas();
        }
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
        public void handleMessage(android.os.Message msg) {
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
                        android.os.Message m = handler.obtainMessage();
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

