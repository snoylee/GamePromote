package com.xygame.sg.activity.personal.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.PlushNoticeActivity;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.notice.bean.QueryUserNoticesListBean;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.adapter.CMNoticeAdapter;
import com.xygame.sg.activity.personal.adapter.PageAdapterTab;
import com.xygame.sg.activity.personal.bean.ModelDataVo;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.define.view.ChoiceNoticeView;
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
import java.util.Map;

import base.frame.VisitUnit;

public class CMNoticeTabFragment extends ScrollTabHolderFragment implements View.OnClickListener {

    private PullToRefreshListView listView;

    private View placeHolderView;


    private Handler handler;
    private int mPreviousVisibleItem;
    private FloatingActionButton fab;

    private List<Map> dataList;
    private VisitUnit unit = new VisitUnit();

    private  int addFootViewNum = 0;
    private View empty_footer_view;

    private CMNoticeAdapter adapter;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private NoticeListBean noticeList = new NoticeListBean();//通告主体数据
    //    private Date reqTime;
    private Long reqTime;

    private QueryUserNoticesListBean queryNoticesListBean;
    private PageBean page;

    private List<Map> shootTypetList = new ArrayList<Map>();//拍摄类型数据

    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = false;
    private String userId;

    private ModelDataVo modelDataVo;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    public CMNoticeTabFragment() {
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
        return inflater.inflate(R.layout.fragment_cm_notice_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //注册广播刷新数据
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_PRICE_SUCCESS);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
        findViews();
        //查询条件
        queryNoticesListBean = new QueryUserNoticesListBean();
        queryNoticesListBean.getPage().setPageSize(pageSize);
        queryNoticesListBean.getPage().setPageIndex(mCurrentPage);


        setListViewListener();
        listViewAddHeader();
//        initListView();
        lazyLoad();
    }

    @SuppressLint("InflateParams")
    private void findViews() {
        handler = new Handler(Looper.getMainLooper());
        listView = (PullToRefreshListView) getView().findViewById(R.id.page_tab_listview);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setVerticalScrollBarEnabled(true);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);

        if (!isQuery){
            fab.hide(false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab.show(true);
                    fab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                    fab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
                }
            }, 300);
            fab.setOnClickListener(this);
        } else {
            fab.hide(true);
        }

        if (!isQuery){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(this);
        } else {
            fab.setVisibility(View.GONE);
        }

        empty_footer_view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_footer_view, null);
    }

    private void initListView() {
//        adapter = new CMNoticeAdapter(getActivity(),noticeList,commentMap,isQuery);
//        listView.setAdapter(adapter);

//        //查询条件
//        queryNoticesListBean = new QueryUserNoticesListBean();
//        queryNoticesListBean.getPage().setPageSize(pageSize);
//        queryNoticesListBean.getPage().setPageIndex(mCurrentPage);
//
//
//        setListViewListener();
//        listViewAddHeader();
//        listViewLoadData();
//        requestData();
        requestCommentData();
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
                mCurrentPage += 1;
                queryNoticesListBean.setReqtime(reqTime + "");
                queryNoticesListBean.getPage().setPageIndex(mCurrentPage);
                requestNoticesList();
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
    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        switch (data.getPosionSign()) {

            case ConstTaskTag.RESPOSE_MODEL_INFO:
                parseComment(data);
                break;
            case ConstTaskTag.QUERY_USERNOTICES_INT:
                responseNoticeList(data);
                break;

        }
    }
    public void parseComment(ResponseBean data) {
//        modelDataVo = JSON.parseObject(data.getRecord(), ModelDataVo.class);
        requestData();
    }
    private void requestData() {
//        requestShootType();
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


    public void responseNoticeList(ResponseBean data) {
//        NoticeListBean resultNoticeList = JSON.parseObject(data.getRecord(), NoticeListBean.class);
        if (listView.isRefreshing()) {
            listView.onRefreshComplete();
        }
//        if (resultNoticeList != null) {
//            reqTime = resultNoticeList.getReqTime();
//            if (mCurrentPage > 1) {
//                noticeList.setReqTime(resultNoticeList.getReqTime());
//                noticeList.getNotices().addAll(resultNoticeList.getNotices());
//                adapter.notifyDataSetChanged();
//            } else {
//                noticeList = resultNoticeList;
//                adapter = new CMNoticeAdapter(getActivity(), noticeList, modelDataVo,isQuery);
//                listView.setAdapter(adapter);
//            }
//            setListView(noticeList);
//        }
    }


    private void setListView(NoticeListBean noticeList) {
        if (noticeList != null && noticeList.getNotices() != null) {
            int h = 0;
            for (int i = 0; i < noticeList.getNotices().size(); i++) {
                View listItem = adapter.getView(i, null, listView);
                listItem.measure(0, 0);
                int height = listItem.getMeasuredHeight();
                h = h + height;
            }
            int statusbarH = DataTools.getStatusBarHeight(getActivity());
            int screenH = DataTools.getScrennSize(getActivity())[1];
            int spaceH = screenH - 3 * statusbarH;
            int leftH = spaceH - h;
            if (leftH > 0 && addFootViewNum == 0) {
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, leftH);
                empty_footer_view.setLayoutParams(params);
                listView.getRefreshableView().addFooterView(empty_footer_view);
                addFootViewNum += 1;
            }

            if (leftH < 0 && addFootViewNum > 0) {
                listView.getRefreshableView().removeFooterView(empty_footer_view);
            }
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
                Intent intent = new Intent(getActivity(), ChoiceNoticeView.class);
                startActivityForResult(intent, 0);
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
            if (Constants.ACTION_EDITOR_USER_INFO.equals(intent.getAction())) {

                boolean isChange = intent.getBooleanExtra("flagStr",false);
                if (isChange){
                    requestCommentData();
                }
            }

        }
    };

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			String flag = data.getStringExtra(Constants.COMEBACK);
			if ("galary".equals(flag)) {
				Intent intent = new Intent(getActivity(), PlushNoticeActivity.class);
				intent.putExtra("noticeFlag", "pay");
				startActivity(intent);
			} else if ("camera".equals(flag)) {
				if(Constants.USER_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))){
					Intent intent = new Intent(getActivity(), PlushNoticeActivity.class);
					intent.putExtra("noticeFlag", "noPay");
					startActivity(intent);
				}else if(Constants.USER_NO_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))){
					showDialog(getString(R.string.to_publish_no_verify));
				}else if(Constants.USER_VERIFING_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))){
					showOneButtonDialog(getString(R.string.to_publish_verifing));
				}else if(Constants.USER_VERIFING_REFUSED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(getActivity()))){
					showDialog(getString(R.string.to_publish_refused));
				}
			}
			break;
		}
		default:
			break;
		}
	}

    public QueryUserNoticesListBean getQueryNoticesListBean() {
        return queryNoticesListBean;
    }
    
    private void showOneButtonDialog(String tip){
    	OneButtonDialog dialog=new OneButtonDialog(getActivity(), tip, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
			}
		});
		dialog.show();
    }
    
    private void showDialog(String tip){
    	TwoButtonDialog dialog=new TwoButtonDialog(getActivity(), "尚未认证的摄影师只能发布预付费通告或立即前往认证噢！", R.style.dineDialog,new ButtonTwoListener() {

			@Override
			public void confrimListener() {
				goToCerti();
			}

			@Override
			public void cancelListener() {
			}
		});
		dialog.show();
    }
    
    private void goToCerti(){
        Intent intent = new Intent(getActivity(), CMIdentyFirstActivity.class);
        startActivity(intent);

    }
}
