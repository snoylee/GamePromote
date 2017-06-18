package com.xygame.sg.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.activity.notice.adapter.JJRNoticeAdapter;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.JJRPublisher;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JJRNoticeFragment extends SGBaseFragment implements PullToRefreshBase.OnRefreshListener2 {
    private PullToRefreshListView2 listView;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private JJRNoticeAdapter adapter;
    private String reqTime;
    private boolean isLoading=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sg_jjr_notice_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        listView = (PullToRefreshListView2) getView().findViewById(R.id.refreshlistview);
        listView.setMode(PullToRefreshBase.Mode.BOTH);

    }

    private void initDatas() {
        registerBroadcastReceiver();
        adapter = new JJRNoticeAdapter(getActivity(), null);
        listView.setAdapter(adapter);
    }

    private void requestNoticesList() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page",new JSONObject().put("pageIndex",mCurrentPage).put("pageSize",pageSize));
            if (mCurrentPage>1){
                obj.put("reqTime",reqTime);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
//        ShowMsgDialog.showNoMsg(getActivity(), true);
        item.setServiceURL(ConstTaskTag.QUEST_JJR_NOTICE_LIST);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JJR_NOTICE_LIST);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        listView.onRefreshComplete();
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_JJR_NOTICE_LIST:
                if ("0000".equals(data.getCode())){
                    parseDatas(data);
                }else{
                    Toast.makeText(getActivity(),data.getMsg(),Toast.LENGTH_SHORT).show();
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
    protected void responseFaith() {
        super.responseFaith();
        listView.onRefreshComplete();
//        listView.setEmptyView();
    }

    private void initListeners() {
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
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    listView.onRefreshComplete();
//                    Toast.makeText(getActivity(),"已全部加载",Toast.LENGTH_SHORT).show();
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
                try {
                    Thread.sleep(1000);
                    android.os.Message m = handler.obtainMessage();
                    m.what=2;
                    m.sendToTarget();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    private void registerBroadcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.xygame.jjren.plush.notice.action");
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.xygame.jjren.plush.notice.action".equals(intent.getAction())) {
                mCurrentPage=1;
                requestNoticesList();
            }
        }
    };

    public void queryDatas() {
        requestNoticesList();
    }

    public int adapterCout() {
        return adapter.getCount();
    }
}
