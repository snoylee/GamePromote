package com.xygame.sg.activity.personal.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.adapter.DataAdapter;
import com.xygame.sg.activity.personal.adapter.PageAdapterTab;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.activity.personal.bean.QueryModelGalleryView;
import com.xygame.sg.activity.personal.bean.UserInfoView;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.broadcast.EditorUserInfoBroadcastListener;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTabFragment extends ScrollTabHolderFragment {
    private ModelDataVo modelDataVo;

    private PullToRefreshListView listView;

    private View placeHolderView;

    private DataAdapter dataAdapter;

    private Handler handler;


    private UserInfoView dataMap = new UserInfoView();
    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    public DataTabFragment() {
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
        return inflater.inflate(R.layout.fragment_data_tab, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditorUserInfoBroadcastListener.registerEditorUserInfoListener(getActivity(), mBroadcastReceiver);
        findViews();
//        initListView();
        setListViewListener();
        listViewAddHeader();
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

        listView.setOnScrollListener(new OnScrollListener() {

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
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, getResources()
                .getDimensionPixelSize(R.dimen.max_header_height));
        placeHolderView.setLayoutParams(params);
        listView.getRefreshableView().addHeaderView(placeHolderView);
        dataAdapter = new DataAdapter(getActivity(),dataMap,isQuery);
        listView.setAdapter(dataAdapter);
    }

    protected void listViewLoadData() {
//        dataAdapter = new DataAdapter(getActivity(),dataMap,isQuery);
//        listView.setAdapter(dataAdapter);
        requestData();
    }

//    private void requestData() {
//        if (isQuery){
//            unit.getDataUnit().getRepo().put("userId", ((PersonInfoActivity)getActivity()).getQueryedUserId());
//            unit.getDataUnit().getRepo().put("userType", ((PersonInfoActivity)getActivity()).getQueryUserType());
//        }else {
//            unit.getDataUnit().getRepo().put("userId", UserPreferencesUtil.getUserId(getActivity()));
//            unit.getDataUnit().getRepo().put("userType", UserPreferencesUtil.getUserType(getActivity()));
//        }
//        new Action(EchoModelInfo.class,"${echoModelInfo},userType=${userType},userId=${userId}", getActivity(), this, unit).run();
//    }

//    public void responseHandler(Map resultMap) {
//        if (resultMap != null){
//            dataAdapter.setData(resultMap);
//            dataAdapter.notifyDataSetChanged();
//        }
//    }


    private void requestData() {
        String userId = "";
        String userType = "";
        if (isQuery){
            userId = ((PersonInfoActivity)getActivity()).getQueryedUserId();
            userType = ((PersonInfoActivity)getActivity()).getQueryUserType();

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

    private void requestCommentData() {
        RequestBean item = new RequestBean();
        String seeUserId = "";
        if (isQuery){
            seeUserId = ((PersonInfoActivity)getActivity()).getQueryedUserId();
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

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()){
            case ConstTaskTag.ECHO_MODELINFO_INT:
                if ("0000".equals(data.getCode())) {
                    requestCommentData();
                    parseJson(data);
                } else {
                    Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.RESPOSE_MODEL_INFO:
                parseComment(data);
                break;
        }
    }

    public void parseComment(ResponseBean data) {
//        modelDataVo = JSON.parseObject(data.getRecord(), ModelDataVo.class);
        dataAdapter.addModelDataVo(modelDataVo);
        dataAdapter.notifyDataSetChanged();
    }

    private void parseJson(ResponseBean data) {
//        UserInfoView resultMap = JSON.parseObject(data.getRecord(), UserInfoView.class);
//        if (resultMap != null){
//            dataAdapter.setData(resultMap);
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

            }

        }
    };

}