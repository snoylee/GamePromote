package com.xygame.sg.activity.personal.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.RecentVisitActivity;
import com.xygame.sg.activity.personal.adapter.RecentModelAdapter;
import com.xygame.sg.activity.personal.bean.QueryRecentListBean;
import com.xygame.sg.activity.personal.bean.UserSeeHistoryView;
import com.xygame.sg.task.indivual.QueryMyVisitor;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;


public class RecentModelFragment extends RecentBaseFragment {
    private PullToRefreshListView listView;
    private RecentModelAdapter adapter;
    private UserSeeHistoryView seeHistoryView;
    private VisitUnit unit = new VisitUnit();

    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private long lastReadTime;
    private long reqTime;

    private QueryRecentListBean queryBean ;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    public RecentModelFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = new ViewBinder(this.getActivity(), unit.setDisplay(this)).inflate(R.layout.fragment_recent_model, null);
        initViews(mView);
//        initDatas();
        initListeners();
        isPrepared = true;
        lazyLoad();
        return mView;
    }


    private void initViews(View mView) {
        listView = (PullToRefreshListView) mView.findViewById(R.id.page_tab_listview);
        listView.setVerticalScrollBarEnabled(true);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    private void initDatas() {
        queryBean = new QueryRecentListBean();
        queryBean.setQtype(1);//查询类型（1：模特，2：摄影师）
        queryBean.setFirstAccess(1);

        queryBean.getPage().setPageSize(pageSize);
        queryBean.getPage().setPageIndex(mCurrentPage);
        mHasLoadedOnce = true;
    }



    private void initListeners() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMore();
            }

        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible|| mHasLoadedOnce ) {
            return;
        }
        initDatas();
        requestData();

    }
    private void loadMore() {
        mCurrentPage++;
        queryBean.setFirstAccess(2);
        queryBean.setReqTime(((RecentVisitActivity)getActivity()).getReqTime());
        queryBean.setLastReadTime(((RecentVisitActivity)getActivity()).getLastReadTime());
        queryBean.getPage().setPageIndex(mCurrentPage);
        requestData();
    }
    private void requestData() {
        new Action(QueryMyVisitor.class, "${queryMyVisitor}", getActivity(), this, unit).run();
    }
    @Override
    public  void responseRecentList(UserSeeHistoryView resultSeeHistoryView) {
        if (resultSeeHistoryView != null && resultSeeHistoryView.getHistorys() != null) {
            if (resultSeeHistoryView.getLastReadTime() != null) {
                lastReadTime = resultSeeHistoryView.getLastReadTime();
                ((RecentVisitActivity)getActivity()).setLastReadTime(lastReadTime);
            }
            if (resultSeeHistoryView.getReqTime() != null){
                reqTime = resultSeeHistoryView.getReqTime();
                ((RecentVisitActivity)getActivity()).setReqTime(reqTime);
            }
            if (mCurrentPage > 1) {
                seeHistoryView.setReqTime(resultSeeHistoryView.getReqTime());
                seeHistoryView.setLastReadTime(resultSeeHistoryView.getLastReadTime());
                seeHistoryView.getHistorys().addAll(resultSeeHistoryView.getHistorys());
                adapter.notifyDataSetChanged();
            } else {
                seeHistoryView = resultSeeHistoryView;
                adapter = new RecentModelAdapter(getActivity(), seeHistoryView);
                listView.setAdapter(adapter);
            }
        }
        listView.onRefreshComplete();
    }
    @Override
    public QueryRecentListBean getQueryBean() {
        return queryBean;
    }


}
