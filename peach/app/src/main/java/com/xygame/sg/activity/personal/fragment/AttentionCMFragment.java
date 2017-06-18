package com.xygame.sg.activity.personal.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.adapter.AttentionCMAdapter;
import com.xygame.sg.activity.personal.adapter.AttentionModelAdapter;
import com.xygame.sg.activity.personal.bean.AttentUserView;
import com.xygame.sg.activity.personal.bean.QueryAttentionsListBean;
import com.xygame.sg.task.indivual.QueryMyAttentUsers;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.action.Action;
import base.frame.ParentFragment;
import base.frame.VisitUnit;


public class AttentionCMFragment extends AttentionBaseFragment {
    private PullToRefreshListView listView;
    private AttentionCMAdapter adapter;
    List<AttentUserView> attentionList;
    private VisitUnit unit = new VisitUnit();

    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数

    private QueryAttentionsListBean queryBean;


    public AttentionCMFragment() {
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.CM_ATTENTION_STATUS_CHANGE);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = new ViewBinder(this.getActivity(), unit.setDisplay(this)).inflate(R.layout.fragment_attention_cm, null);
        initViews(mView);
        initDatas();
        initListeners();
        requestData();
        return mView;
    }


    private void initViews(View mView) {
        listView = (PullToRefreshListView) mView.findViewById(R.id.page_tab_listview);
        listView.setVerticalScrollBarEnabled(true);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    private void initDatas() {
        queryBean = new QueryAttentionsListBean();
        queryBean.setQtype(2);//查询类型（1：模特，2：摄影师）
        queryBean.getPage().setPageSize(pageSize);
        queryBean.getPage().setPageIndex(mCurrentPage);
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
    private void loadMore() {
        mCurrentPage++;
        queryBean.getPage().setPageIndex(mCurrentPage);
        requestData();
    }
    private void requestData() {
        new Action(QueryMyAttentUsers.class, "${queryMyAttentUsers}", getActivity(), this, unit).run();
    }

    public QueryAttentionsListBean getQueryBean() {
        return queryBean;
    }
    @Override
    public void responseAttentionList(List<AttentUserView> resultAttentionList) {
        if (resultAttentionList != null) {
            if (mCurrentPage > 1) {
                attentionList.addAll(resultAttentionList);
                adapter.notifyDataSetChanged();
            } else {
                attentionList = resultAttentionList;
                adapter = new AttentionCMAdapter(getActivity(), attentionList);
                listView.setAdapter(adapter);
            }
        }else {
            if (mCurrentPage == 1) {
                attentionList = new ArrayList<AttentUserView>();
                adapter = new AttentionCMAdapter(getActivity(), attentionList);
                listView.setAdapter(adapter);
            }
        }
        listView.onRefreshComplete();
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
            if (Constants.CM_ATTENTION_STATUS_CHANGE.equals(intent.getAction())) {
                mCurrentPage=1;
                queryBean.getPage().setPageIndex(mCurrentPage);
                requestData();
            }
        }
    };
}

