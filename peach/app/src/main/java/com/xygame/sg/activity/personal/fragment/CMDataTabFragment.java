package com.xygame.sg.activity.personal.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.bean.JJRNoticeBean;
import com.xygame.sg.activity.notice.bean.JJRPublisher;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.QueryUserNoticesListBean;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.adapter.CMDataAdapter;
import com.xygame.sg.activity.personal.adapter.PageAdapterTab;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.activity.personal.bean.UserInfoView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.utils.broadcast.EditorUserInfoBroadcastListener;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.frame.VisitUnit;

/**
 * Created by xy on 2015/11/27.
 */
public class CMDataTabFragment  extends ScrollTabHolderFragment {

    private PullToRefreshListView listView;
    private  String userType,queryedUserId,editOrQueryFlag;
    private View placeHolderView;

    private CMDataAdapter dataAdapter;
    private QueryUserNoticesListBean queryNoticesListBean;
    private Handler handler;
    private ModelDataVo modelDataVo;
    private String userId;
    private UserInfoView dataMap = new UserInfoView();
    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    public CMDataTabFragment() {
        this.setFragmentId(PageAdapterTab.PAGE_TAB2.fragmentId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isQuery = getArguments().getBoolean("isQuery");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isPrepared = true;
        return inflater.inflate(R.layout.fragment_cm_data_tab, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditorUserInfoBroadcastListener.registerEditorUserInfoListener(getActivity(), mBroadcastReceiver);
        findViews();
        setListViewListener();
        listViewAddHeader();
//        initListView();
        queryNoticesListBean = new QueryUserNoticesListBean();
        queryNoticesListBean.getPage().setPageSize(1000);
        queryNoticesListBean.getPage().setPageIndex(1);
        lazyLoad();
    }

    @SuppressLint("InflateParams")
    private void findViews() {
        handler = new Handler(Looper.getMainLooper());
        listView = (PullToRefreshListView) getView().findViewById(R.id.page_tab_listview);
        listView.setVerticalScrollBarEnabled(true);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    private void initListView() {
//        setListViewListener();
//        listViewAddHeader();
        listViewLoadData();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible|| mHasLoadedOnce ) {
            return;
        }
        mHasLoadedOnce = true;
        initListView();

    }

    private void setListViewListener() {

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollTabHolder != null) {
                    scrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, getFragmentId());
                }
            }
        });

        listView.setOnHeaderScrollListener(new PullToRefreshListView.OnHeaderScrollListener() {

            @Override
            public void onHeaderScroll(boolean isRefreashing, boolean istop, int value) {
                if (scrollTabHolder != null && istop) {
                    scrollTabHolder.onHeaderScroll(isRefreashing, value, getFragmentId());
                }
            }
        });
    }

    private void listViewAddHeader() {
        placeHolderView = new LinearLayout(getActivity());
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, getResources()
                .getDimensionPixelSize(R.dimen.max_header_height));
        placeHolderView.setLayoutParams(params);
        listView.getRefreshableView().addHeaderView(placeHolderView);
        dataAdapter = new CMDataAdapter(getActivity(),dataMap,isQuery);
        listView.setAdapter(dataAdapter);
    }

    protected void listViewLoadData() {
//        dataAdapter = new CMDataAdapter(getActivity(),dataMap,isQuery);
//        listView.setAdapter(dataAdapter);
        requestData();
    }

    private void requestData() {
        String userId = "";
        String userType = "";
        if (isQuery){
            userId = ((CMPersonInfoActivity)getActivity()).getQueryedUserId();
            userType = ((CMPersonInfoActivity)getActivity()).getQueryUserType();

        }else {
            userId = UserPreferencesUtil.getUserId(getActivity());
            userType = UserPreferencesUtil.getUserType(getActivity());

        }
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId",userId);
            obj.put("userType", userType);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.ECHO_MODELINFO_URL);


        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.ECHO_MODELINFO_INT);
    }

    private void requestJJRNoticesList() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page",new JSONObject().put("pageIndex",1).put("pageSize",10000));
            if (editOrQueryFlag.equals(Constants.EDIT_INFO_FLAG)) {
                obj.put("userId",UserPreferencesUtil.getUserId(getActivity()));
            } else if (editOrQueryFlag.equals(Constants.QUERY_INFO_FLAG)) {
                obj.put("userId",queryedUserId);
            }

//            if (mCurrentPage>1){
//                obj.put("reqTime",reqTime);
//            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_JJR_DETAIL_NOTICE_LIST);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JJR_DETAIL_NOTICE_LIST);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.ECHO_MODELINFO_INT:
                if ("0000".equals(data.getCode())) {
                    if (userType!=null){
                        if (Constants.CARRE_PHOTOR.equals(userType)){
                            requestCommentData();
                        }else if(Constants.CARRE_MERCHANT.equals(userType)) {
                            requestJJRNoticesList();
                        }
                    }
                    parseJson(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.RESPOSE_MODEL_INFO:
                if ("0000".equals(data.getCode())) {
                    parseComment(data);
                }
                break;
            case ConstTaskTag.QUERY_USERNOTICES_INT:
                if ("0000".equals(data.getCode())) {
                    responseNoticeList(data);
                }
                break;
            case ConstTaskTag.QUERY_JJR_DETAIL_NOTICE_LIST:
                if ("0000".equals(data.getCode())) {
                    parseDatas(data);
                }
                break;
        }
    }

    private void parseDatas(ResponseBean data) {
        try {
            List<JJRNoticeBean> datas=new ArrayList<>();
            JSONObject object=new JSONObject(data.getRecord());
//            reqTime= StringUtils.getJsonValue(object, "reqTime");
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
                dataAdapter.addJJRNoticeList(datas);
                dataAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void responseNoticeList(ResponseBean data) {
//        NoticeListBean noticeList = JSON.parseObject(data.getRecord(), NoticeListBean.class);
//        dataAdapter.addSYSNoticeList(noticeList);
//        dataAdapter.addModelDataVo(modelDataVo);
//        dataAdapter.notifyDataSetChanged();
    }

    public void parseComment(ResponseBean data) {
//        modelDataVo = JSON.parseObject(data.getRecord(), ModelDataVo.class);
        requestNoticesList();
    }

    private void requestNoticesList() {
        if (isQuery){
            userId = ((CMPersonInfoActivity)getActivity()).getQueryedUserId();
        }else {
            userId = UserPreferencesUtil.getUserId(getActivity());
        }
        queryNoticesListBean.setUserId(userId);
//        new Action(QueryUserNotices.class,"${queryUserNotices}", getActivity(), this, unit).run();

        RequestBean item = new RequestBean();

//        try {
//            JSONObject obj = new JSONObject(JSON.toJSONString(queryNoticesListBean));
//            item.setData(obj);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
        item.setServiceURL(ConstTaskTag.QUERY_USERNOTICES_URL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USERNOTICES_INT);
    }

    private void requestCommentData() {
        RequestBean item = new RequestBean();
        String seeUserId = "";
        if (isQuery){
            seeUserId = ((CMPersonInfoActivity)getActivity()).getQueryedUserId();
        }else {
            seeUserId = UserPreferencesUtil.getUserId(getActivity());
        }
        try {
            JSONObject obj = new JSONObject();
            obj.put("seeUserId",seeUserId);
            obj.put("userId", seeUserId);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_MODEL_INFO);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.RESPOSE_MODEL_INFO);
    }

    private void parseJson(ResponseBean data) {
//        UserInfoView resultMap = JSON.parseObject(data.getRecord(), UserInfoView.class);
//        if (resultMap != null){
//            dataAdapter.setData(resultMap,userType);
//            dataAdapter.notifyDataSetChanged();
//        }
    }



    // PullToRefreshListView 自动添加了一个头部
    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && listView.getRefreshableView().getFirstVisiblePosition() >= 2) {
            return;
        }
        listView.getRefreshableView().setSelectionFromTop(2, scrollHeight);

    }
    @Override
    public void onDestroy() {
        EditorUserInfoBroadcastListener.unRegisterEditorUserInfoListener(getActivity(), mBroadcastReceiver);
        super.onDestroy();
    }
    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_EDITOR_USER_INFO.equals(intent.getAction())) {

                if (intent.hasExtra("flagStr")){
                    boolean isChange = intent.getBooleanExtra("flagStr",false);
                    if (isChange){
                        requestData();
                    }
                } else {
                    requestData();
                }
            }else if ("com.xygame.pinfo.action".equals(intent.getAction())){
                userType=intent.getStringExtra("userType");
                queryedUserId=intent.getStringExtra("queryedUserId");
                editOrQueryFlag=intent.getStringExtra("editOrQueryFlag");
            }

        }
    };

}