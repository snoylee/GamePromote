package com.xygame.sg.activity.personal.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.CreatePriceActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.adapter.PageAdapterTab;
import com.xygame.sg.activity.personal.adapter.PriceAdapter;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.activity.personal.bean.ModelPriceVo;
import com.xygame.sg.activity.personal.bean.QueryModelPriceView;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PriceTabFragment extends ScrollTabHolderFragment implements View.OnClickListener {

    private PullToRefreshListView listView;

    private View placeHolderView;

    private PriceAdapter priceAdapter;


    private Handler handler;
    private int mPreviousVisibleItem;
    private FloatingActionButton fab;

    private List<QueryModelPriceView> dataList;

    private  int addFootViewNum = 0;
    private View empty_footer_view;

    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;
    private ModelDataVo modelDataVo;

    private boolean isScroll = false;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    public PriceTabFragment() {
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
//        return inflater.inflate(R.layout.fragment_price_tab, container, false);
        return inflater.inflate(R.layout.fragment_price_tab, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //注册广播刷新数据
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_PRICE_SUCCESS);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
        findViews();
        setListViewListener();
        listViewAddHeader();
//        initListView();
        lazyLoad();
    }

    @SuppressLint("InflateParams")
    private void findViews() {
        handler = new Handler(Looper.getMainLooper());
        listView = (PullToRefreshListView) getView().findViewById(R.id.page_tab_listview);
        listView.setVerticalScrollBarEnabled(true);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);

//        if (!isQuery){
//            fab.hide(false);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    fab.show(true);
//                    fab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
//                    fab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
//                }
//            }, 300);
//            fab.setOnClickListener(this);
//        } else {
//            fab.hide(true);
//        }

        if (!isQuery){
            fab.setVisibility(View.GONE);
//            fab.setOnClickListener(this);
        } else {
            fab.setVisibility(View.GONE);
        }

        empty_footer_view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_footer_view, null);
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
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

        });

        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollTabHolder != null) {
                    scrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, getFragmentId());
                }

//                if (!isQuery){
//                    if (firstVisibleItem > mPreviousVisibleItem) {
//                        fab.hide(true);
//                    } else if (firstVisibleItem < mPreviousVisibleItem) {
//                        fab.show(true);
//                    }
//                }
                mPreviousVisibleItem = firstVisibleItem;
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
    }

    protected void listViewLoadData() {
        requestCommentData();
//        requestData();
    }

//    private void requestCommentData() {
//        if (isQuery){
//            unit.getDataUnit().getRepo().put("seeUserId", ((PersonInfoActivity)getActivity()).getQueryedUserId());
//        }else {
//            unit.getDataUnit().getRepo().put("seeUserId", UserPreferencesUtil.getUserId(getActivity()));
//        }
//        new Action(GetModelData.class, "${modelData},seeUserId=${seeUserId},userId=${seeUserId}", getActivity(), this, unit).run();
//    }

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
        switch (data.getPosionSign()) {
            case ConstTaskTag.RESPOSE_MODEL_INFO:
                parseComment(data);
                break;
            case ConstTaskTag.ECHO_MODELPRICE_INT:
                responseHandler(data);
                break;
        }
    }
    public void parseComment(ResponseBean data) {
//        modelDataVo = JSON.parseObject(data.getRecord(), ModelDataVo.class);

        requestData();
    }
    private void requestData() {
//        if (isQuery){
//            unit.getDataUnit().getRepo().put("userId", ((PersonInfoActivity)getActivity()).getQueryedUserId());
//        }else {
//            unit.getDataUnit().getRepo().put("userId", UserPreferencesUtil.getUserId(getActivity()));
//        }
//        new Action(EchoModelPrice.class,"${echoModelPrice},userId=${userId}", getActivity(), this, unit).run();

        RequestBean item = new RequestBean();
        String userId = "";
        if (isQuery){
            userId = ((PersonInfoActivity)getActivity()).getQueryedUserId();
        }else {
            userId = UserPreferencesUtil.getUserId(getActivity());
        }
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.ECHO_MODELPRICE_URL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.ECHO_MODELPRICE_INT);
    }


    public void responseHandler(ResponseBean data) {
//        dataList = JSON.parseArray(data.getRecord(), QueryModelPriceView.class);
        dataList=new ArrayList<>();
        priceAdapter = new PriceAdapter(getActivity(), dataList,modelDataVo,isQuery);
        listView.setAdapter(priceAdapter);

        int h = 0;
        for (int i = 0; i < dataList.size(); i++) {
            View listItem = priceAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            int height = listItem.getMeasuredHeight();
            h = h + height;
        }
        int statusbarH = DataTools.getStatusBarHeight(getActivity());
        int screenH = DataTools.getScrennSize(getActivity())[1];
        int spaceH = screenH - 3*statusbarH;
        int leftH = spaceH -h;
        if (leftH > 0  && addFootViewNum == 0) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,leftH);
            empty_footer_view.setLayoutParams(params);
            listView.getRefreshableView().addFooterView(empty_footer_view);
            addFootViewNum+=1;
        }
        if (leftH < 0&& addFootViewNum >0){
            listView.getRefreshableView().removeFooterView(empty_footer_view);
        }

        if (isScroll){
            listView.getRefreshableView().setSelectionFromTop(2, 0);
        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(getActivity(), CreatePriceActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_PRICE_SUCCESS.equals(intent.getAction())) {
//                requestData();
                requestCommentData();
                isScroll = true;
            }

        }
    };


}
