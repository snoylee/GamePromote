package com.xygame.sg.activity.personal.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.CreateModelPicActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.activity.personal.adapter.PageAdapterTab;
import com.xygame.sg.activity.personal.adapter.WorkAdapter;
import com.xygame.sg.activity.personal.bean.QueryModelGalleryView;
import com.xygame.sg.define.draggrid.DataTools;
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

import java.util.List;

public class WorkTabFragment extends ScrollTabHolderFragment implements View.OnClickListener {

    private PullToRefreshListView listView;

    private View placeHolderView;

    private WorkAdapter workAdapter;


    private Handler handler;

    private int mPreviousVisibleItem;
    private FloatingActionButton fab;

    private List<QueryModelGalleryView> dataList;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数



    private  int addFootViewNum = 0;
    private View empty_footer_view;

    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;

    private String userId;


    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    private boolean isBottom = false;

    public WorkTabFragment() {
        this.setFragmentId(PageAdapterTab.PAGE_TAB1.fragmentId);
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
        return inflater.inflate(R.layout.fragment_work_tab, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditorUserInfoBroadcastListener.registerEditorUserInfoListener(getActivity(), mBroadcastReceiver);
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
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(this);
        } else {
            fab.setVisibility(View.GONE);
        }


        empty_footer_view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_footer_view, null);
    }

    private void initListView() {
//        setListViewListener();
//        listViewAddHeader();
        requestData();
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

                if (!isBottom){
                    loadMore();
                } else {
                    if (listView.isRefreshing()){
                        new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what){
                                    case 1:
                                        listView.onRefreshComplete();
                                        Toast.makeText(getActivity(), "没有更多作品集了!", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }.sendEmptyMessageDelayed(1, 1000);
                    }
                }
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


    private void requestData() {
        if (isQuery){
            userId = ((PersonInfoActivity)getActivity()).getQueryedUserId();
        }else {
            userId = UserPreferencesUtil.getUserId(getActivity());
        }

        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId",userId);
            obj.put("page", "{\"pageIndex\":\"" + mCurrentPage + "\",\"pageSize\":\"" + pageSize + "\"}");
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUERY_MODELGALLERY_URL);


        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_MODELGALLERY_INT);
    }
    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        if ("0000".equals(data.getCode())) {
            parseJson(data);
        } else {
            if(data!=null){
                Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void parseJson(ResponseBean data) {
//        List<QueryModelGalleryView> resultList = JSON.parseArray(data.getRecord(), QueryModelGalleryView.class);
        if (listView.isRefreshing()) {
            listView.onRefreshComplete();
        }
//        if (!resultList.isEmpty()) {
//            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//        }
//        isBottom = resultList.size() < pageSize;
//        if (mCurrentPage ==1){//刚进入页面第一次请求数据
//            dataList = resultList;
//            workAdapter = new WorkAdapter(getActivity(),dataList,userId,isQuery);
//            listView.setAdapter(workAdapter);
//        }else {
//            dataList.addAll(resultList);
//            workAdapter.setDataList(dataList);
//            workAdapter.notifyDataSetChanged();
//        }

        int size = 0;
        if (dataList.size()<=2){
            if (dataList.size() == 0){
                size = 0;
            } else {
                size = 1;
            }
        } else {
            if (dataList.size()%2==0){
                size = dataList.size()/2;
            } else {
                size = dataList.size()/2+1;
            }
        }
        int h = 0;
        for (int i = 0; i < size; i++) {
            int height = DataTools.getScrennSize(getActivity())[0]/2;
            h = h + height;
        }
        int statusbarH = DataTools.getStatusBarHeight(getActivity());
        int screenH = DataTools.getScrennSize(getActivity())[1];
        int spaceH = screenH - 3*statusbarH-100;
        int leftH = spaceH -h;
        if (leftH > 0 && addFootViewNum==0) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,leftH);
            empty_footer_view.setLayoutParams(params);
            listView.getRefreshableView().addFooterView(empty_footer_view);
            addFootViewNum+=1;
        }

        if (leftH < 0&& addFootViewNum >0){
            listView.getRefreshableView().removeFooterView(empty_footer_view);
        }
    }
    private void loadMore() {
        mCurrentPage++;
        requestData();
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
            	Intent intent=new Intent(getActivity(), CreateModelPicActivity.class);
            	startActivity(intent);
                break;
        }
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
                boolean isChange = intent.getBooleanExtra("flagStr",false);
                if (isChange){
                    mCurrentPage = 1;
                    requestData();
                }

            }

        }
    };
}