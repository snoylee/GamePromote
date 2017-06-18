package com.xygame.sg.activity.notice.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xygame.sg.R;
import com.xygame.sg.activity.notice.RecruitActivity;
import com.xygame.sg.activity.notice.adapter.RecruitModeAdapter;
import com.xygame.sg.activity.notice.adapter.RecruitPageAdapterTab;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberGroupVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberStatsVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.personal.fragment.ScrollTabHolderFragment;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.task.notice.Members;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import base.action.Action;
import base.frame.VisitUnit;


public class EliminateFragment extends ScrollTabHolderFragment implements View.OnClickListener {
    private PullToRefreshListView listView;

    private View placeHolderView;

    private RecruitModeAdapter adapter;


    private Handler handler;
    private int mPreviousVisibleItem;

    private VisitUnit unit = new VisitUnit();

    private  int addFootViewNum = 0;
    private View empty_footer_view;

    private long recruitId;
    private String noticeId;
    private NoticeDetailVo notice;
    private NoticeRecruitVo noticeRecruitListVo;

    private NoticeMemberStatsVo moticeMemberStatsVo = new NoticeMemberStatsVo();
    private List<NoticeMemberVo> members = new ArrayList<NoticeMemberVo>();
    private List<NoticeMemberGroupVo> groups = new ArrayList<NoticeMemberGroupVo>();
    private OnFragmentInteractionListener mListener;

    public EliminateFragment() {
        this.setFragmentId(RecruitPageAdapterTab.PAGE_TAB3.fragmentId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recruitId = getArguments().getLong("recruitId");
            noticeId = getArguments().getString("noticeId");
        }

        notice=((RecruitActivity) getActivity()).getNotice();
        noticeRecruitListVo=((RecruitActivity) getActivity()).getNoticeRecruitListVo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eliminate, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //注册广播刷新数据
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_RECRUIT_CHANGE);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
        findViews();
        initListView();
    }

    @SuppressLint("InflateParams")
    private void findViews() {
        handler = new Handler(Looper.getMainLooper());
        listView = (PullToRefreshListView) getView().findViewById(R.id.page_tab_listview);
        listView.setVerticalScrollBarEnabled(true);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);

        empty_footer_view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_footer_view, null);
    }

    private void initListView() {
        setListViewListener();
        listViewAddHeader();
        requestData();
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollTabHolder != null) {
                    scrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, getFragmentId());
                }

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
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, ((RecruitActivity)getActivity()).getHeaderHeight());
        placeHolderView.setLayoutParams(params);
        listView.getRefreshableView().addHeaderView(placeHolderView);
    }

    private void requestData() {
        unit.getDataUnit().getRepo().put("noticeId", noticeId);//
        unit.getDataUnit().getRepo().put("status", RecruitActivity.ELIMINATE_STATUS);//
        unit.getDataUnit().getRepo().put("recruitId", recruitId);
        new Action(Members.class,"${membersQuery},noticeId=${noticeId},status=${status},recruitId=${recruitId}", getActivity(), this, unit).run();
    }


    public void responseHandler(NoticeMemberStatsVo resultNoticeMemberStatsVo) {
        moticeMemberStatsVo = resultNoticeMemberStatsVo;
        members = moticeMemberStatsVo.getMembers();
        groups = moticeMemberStatsVo.getGroups();
        mListener.onFragmentInteraction(RecruitActivity.ELIMINATE_FRAGMENT,groups);
        adapter = new RecruitModeAdapter(getActivity(),this,members,RecruitActivity.ELIMINATE_FRAGMENT,false);
        listView.setAdapter(adapter);

        int h = 0;
        for (int i = 0; i < members.size(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            int height = listItem.getMeasuredHeight();
            h = h + height;
        }
        int statusbarH = DataTools.getStatusBarHeight(getActivity());
        int screenH = DataTools.getScrennSize(getActivity())[1];
        int spaceH = screenH - 3*statusbarH;
        int leftH = spaceH -h;
        Log.i("^^^^^^2", statusbarH + "   " + screenH + "    " + spaceH + "  " + leftH);
        if (leftH > 0  && addFootViewNum == 0) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,leftH);
            empty_footer_view.setLayoutParams(params);
            listView.getRefreshableView().addFooterView(empty_footer_view);
            addFootViewNum+=1;
        }

        if (leftH < 0&& addFootViewNum >0){
            listView.getRefreshableView().removeFooterView(empty_footer_view);
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
//        switch (view.getId()) {
//            case R.id.fab:
//                Intent intent = new Intent(getActivity(), CreatePriceActivity.class);
//                startActivity(intent);
//                break;
//        }
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
            if (Constants.ACTION_RECRUIT_CHANGE.equals(intent.getAction())) {
                requestData();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void responseUpdateHandler() {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_RECRUIT_CHANGE);
        getActivity().sendBroadcast(intent);
    }
}
