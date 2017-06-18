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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.RecruitActivity;
import com.xygame.sg.activity.notice.adapter.RecruitModeAdapter;
import com.xygame.sg.activity.notice.adapter.RecruitPageAdapterTab;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberGroupVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberStatsVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberUpdateBean;
import com.xygame.sg.activity.notice.bean.NoticeMemberUpdateFirstVo;
import com.xygame.sg.activity.notice.bean.NoticeMemberVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.personal.fragment.ScrollTabHolderFragment;
import com.xygame.sg.define.draggrid.DataTools;
import com.xygame.sg.task.notice.Members;
import com.xygame.sg.task.notice.MembersUpdate;
import com.xygame.sg.task.notice.MembersUpdateFirst;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.action.Action;
import base.frame.VisitUnit;


public class EnrollFragment extends ScrollTabHolderFragment implements View.OnClickListener {

    private PullToRefreshListView listView;
    private TextView tbd_tv;
    private TextView eliminate_tv;
    private TextView employ_tv;
    private ImageView select_iv;
    private TextView select_num_tv;
    private LinearLayout action_ll;

    private View placeHolderView;

    private RecruitModeAdapter adapter;


    private Handler handler;
    private int mPreviousVisibleItem;

    private VisitUnit unit = new VisitUnit();

    private int addFootViewNum = 0;
    private View empty_footer_view;


    private long recruitId;
    private String noticeId;
    private NoticeDetailVo notice;
    private NoticeRecruitVo noticeRecruitListVo;

    private NoticeMemberStatsVo moticeMemberStatsVo = new NoticeMemberStatsVo();
    private List<NoticeMemberVo> members = new ArrayList<NoticeMemberVo>();
    private List<NoticeMemberGroupVo> groups = new ArrayList<NoticeMemberGroupVo>();
    private OnFragmentInteractionListener mListener;

    private List<NoticeMemberVo> selectedMembers = new ArrayList<NoticeMemberVo>();
    private NoticeMemberUpdateBean noticeMemberUpdateBean;

    public EnrollFragment() {
        this.setFragmentId(RecruitPageAdapterTab.PAGE_TAB1.fragmentId);
    }
    
    public NoticeDetailVo getNotice(){
    	return notice;
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
    
    public String getRecruitId(){
    	return String.valueOf(recruitId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enroll, container, false);
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

        tbd_tv = (TextView) getView().findViewById(R.id.tbd_tv);
        eliminate_tv = (TextView) getView().findViewById(R.id.eliminate_tv);
        employ_tv = (TextView) getView().findViewById(R.id.employ_tv);
        select_iv = (ImageView) getView().findViewById(R.id.select_iv);
        select_num_tv = (TextView) getView().findViewById(R.id.select_num_tv);
        action_ll = (LinearLayout) getView().findViewById(R.id.action_ll);

        if (((RecruitActivity) getActivity()).getNotice().getRecordStatus() == 3 || ((RecruitActivity) getActivity()).getNotice().getRecordStatus() == 4){
            action_ll.setVisibility(View.GONE);
        } else {
            action_ll.setVisibility(View.VISIBLE);
        }
        empty_footer_view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_footer_view, null);
    }

    private void initListView() {
        setListViewListener();
        listViewAddHeader();
        requestData();
        addListener();
    }

    private void addListener() {
        tbd_tv.setOnClickListener(this);
        eliminate_tv.setOnClickListener(this);
        employ_tv.setOnClickListener(this);
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
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, ((RecruitActivity) getActivity()).getHeaderHeight());
        placeHolderView.setLayoutParams(params);
        listView.getRefreshableView().addHeaderView(placeHolderView);
    }

    private void requestData() {
        unit.getDataUnit().getRepo().put("noticeId", noticeId);//
        unit.getDataUnit().getRepo().put("status", RecruitActivity.ENROLL_STATUS);//报名
        unit.getDataUnit().getRepo().put("recruitId", recruitId);
        new Action(Members.class, "${membersQuery},noticeId=${noticeId},status=${status},recruitId=${recruitId}", getActivity(), this, unit).run();
    }


    public void responseHandler(NoticeMemberStatsVo resultNoticeMemberStatsVo) {
        moticeMemberStatsVo = resultNoticeMemberStatsVo;
        members = moticeMemberStatsVo.getMembers();
        groups = moticeMemberStatsVo.getGroups();
        mListener.onFragmentInteraction(RecruitActivity.ENROLL_FRAGMENT, groups);
        if(notice.getRecordStatus() == 3 || notice.getRecordStatus() == 4){
            adapter = new RecruitModeAdapter(getActivity(),this, members, RecruitActivity.ENROLL_FRAGMENT,false);
        } else {
            adapter = new RecruitModeAdapter(getActivity(),this, members, RecruitActivity.ENROLL_FRAGMENT,true);
        }

        listView.setAdapter(adapter);
//        if(notice.getRecordStatus() != 3 && notice.getRecordStatus() != 4){
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    adapter.setSelectedItem(i - 2);
//                    selectedMembers = adapter.getSelectDatas();
//                    setSelectSatus();
//                }
//            });
//        }

        int h = 0;
        for (int i = 0; i < members.size(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            int height = listItem.getMeasuredHeight();
            h = h + height;
        }

        int statusbarH = DataTools.getStatusBarHeight(getActivity());
        int screenH = DataTools.getScrennSize(getActivity())[1];
        int spaceH = screenH - 3 * statusbarH;
        int leftH = spaceH - h;
        Log.i("^^^^^^2", statusbarH + "   " + screenH + "    " + spaceH + "  " + leftH);
        if (leftH > 0 && addFootViewNum == 0) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, leftH);
            empty_footer_view.setLayoutParams(params);
            listView.getRefreshableView().addFooterView(empty_footer_view);
            addFootViewNum += 1;
        }

        if (leftH < 0 && addFootViewNum > 0) {
            listView.getRefreshableView().removeFooterView(empty_footer_view);
        }

    }

    public void responseUpdateHandler(Map resultMap) {
        int status =Integer.parseInt((String) (resultMap.get("status")));
        if (status ==1){
            Intent intent = new Intent();
            intent.setAction(Constants.ACTION_RECRUIT_CHANGE);
            getActivity().sendBroadcast(intent);
            if (noticeMemberUpdateBean.getStatus() == RecruitActivity.TBD_STATUS ){
                ((RecruitActivity)getActivity()).getViewPager().setCurrentItem(1);
            } else if (noticeMemberUpdateBean.getStatus() == RecruitActivity.EMPLOY_STATUS ){
                ((RecruitActivity)getActivity()).getViewPager().setCurrentItem(3);
            }
            select_iv.setImageResource(R.drawable.gou_null);
            select_num_tv.setText(0 + "");
            tbd_tv.setEnabled(false);
            eliminate_tv.setEnabled(false);
            employ_tv.setEnabled(false);
        } else if(status == 2){

        } else if(status == 3){
            Toast.makeText(getActivity(), "出错,请重试", Toast.LENGTH_SHORT).show();
        }

    }

    public void setSelectSatus() {
        selectedMembers = adapter.getSelectDatas();
        if (selectedMembers.size() > 0) {
            select_iv.setImageResource(R.drawable.gou);
            select_num_tv.setText(selectedMembers.size() + "");
            tbd_tv.setEnabled(true);
            eliminate_tv.setEnabled(true);
            employ_tv.setEnabled(true);
        } else {
            select_iv.setImageResource(R.drawable.gou_null);
            select_num_tv.setText(0 + "");
            tbd_tv.setEnabled(false);
            eliminate_tv.setEnabled(false);
            employ_tv.setEnabled(false);
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
        if (selectedMembers.size() > 0) {
            tbd_tv.setEnabled(true);
            eliminate_tv.setEnabled(true);
            employ_tv.setEnabled(true);
        } else {
            tbd_tv.setEnabled(false);
            eliminate_tv.setEnabled(false);
            employ_tv.setEnabled(false);
            return;
        }
        noticeMemberUpdateBean = new NoticeMemberUpdateBean();
        for (NoticeMemberVo noticeMemberVo : selectedMembers) {
            noticeMemberUpdateBean.getMemIds().add(noticeMemberVo.getMemId());
        }
        switch (view.getId()) {
            case R.id.tbd_tv:
                noticeMemberUpdateBean.setStatus(RecruitActivity.TBD_STATUS);
                break;
            case R.id.eliminate_tv:
                noticeMemberUpdateBean.setStatus(RecruitActivity.ELIMINATE_STATUS);
                break;
            case R.id.employ_tv:
                noticeMemberUpdateBean.setStatus(RecruitActivity.EMPLOY_STATUS);
                break;
        }

        if (view.getId() == R.id.tbd_tv || view.getId() == R.id.eliminate_tv){
            String msgStr = "";
            if (view.getId() == R.id.tbd_tv){
                msgStr = "确认待定吗？";
            } else if (view.getId() == R.id.eliminate_tv){
                msgStr = "确认淘汰吗？";
            }
            TwoButtonDialog dialog=new TwoButtonDialog(getActivity(), msgStr, R.style.dineDialog,new ButtonTwoListener() {
                @Override
                public void confrimListener() {
                    toRequest();
                }
                @Override
                public void cancelListener() {
                }
            });
            dialog.show();
        } else if(view.getId() == R.id.employ_tv){
            TwoButtonDialog dialog=new TwoButtonDialog(getActivity(), "确认录用吗？", R.style.dineDialog,new ButtonTwoListener() {
                @Override
                public void confrimListener() {
                    toUpdateFirst();
                }
                @Override
                public void cancelListener() {
                }
            });
            dialog.show();

        }
    }
    private void toRequest() {
//        unit.getDataUnit().getRepo().put("status", noticeMemberUpdateBean.getStatus() + "");//
//        unit.getDataUnit().getRepo().put("memIds", JSON.toJSONString(noticeMemberUpdateBean.getMemIds()));//
//        new Action(MembersUpdate.class, "${membersUpdate},status=${status},memIds=${memIds}", getActivity(), this, unit).run();
    }
    private void toUpdateFirst() {
//        unit.getDataUnit().getRepo().put("memIds", JSON.toJSONString(noticeMemberUpdateBean.getMemIds()));//
//        new Action(MembersUpdateFirst.class, "${membersUpdateFirst},memIds=${memIds}", getActivity(), this, unit).run();
    }

    public void responseMemberUpdateFirst(NoticeMemberUpdateFirstVo memberUpdateFirstVo) {
        if (memberUpdateFirstVo.getStatus() == 1){
            if (memberUpdateFirstVo.getNeedPayAmount()>0){
                ////跳转到追加预付款页面
                UIHelper.toAddPay(getActivity(),memberUpdateFirstVo,noticeMemberUpdateBean);
            } else {
                toRequest();
            }
        } else if (memberUpdateFirstVo.getStatus() == 2){
            //跳转到支付全款页面
            UIHelper.toFirstPay(getActivity(),notice);
        }else if (memberUpdateFirstVo.getStatus() == 3){
            Toast.makeText(getActivity(), "出错了！", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK != resultCode || null == data) {
            return;
        }
        switch (requestCode) {
            case 0: {
                boolean result = data.getBooleanExtra(Constants.COMEBACK, false);
                if (result) {
                    requestData();
                }
                break;
            }
            case 1: {
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_RECRUIT_CHANGE);
                getActivity().sendBroadcast(intent);
                if (noticeMemberUpdateBean.getStatus() == RecruitActivity.EMPLOY_STATUS ){
                    ((RecruitActivity)getActivity()).getViewPager().setCurrentItem(3);
                }
            }
            default:
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


}
