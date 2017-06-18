package com.xygame.sg.activity.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.NoticeAdapter;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.notice.bean.QueryNoticesListBean;
import com.xygame.sg.task.indivual.QueryUserCollectNotices;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class CollectActivity extends SGBaseActivity implements View.OnClickListener {
    private TextView titleName;
    private View backButton;
    VisitUnit visitUnit = new VisitUnit(this);
    private PullToRefreshListView listView;

    private NoticeAdapter adapter;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private NoticeListBean noticeList = new NoticeListBean();//通告主体数据
    //    private Date reqTime;
    private Long reqTime;
    private PageBean pageBean;

    /**
     * 标志是否从查看的入口进入（因为此页面有摄影师头像点击可查看摄影师资料，因此要区分是摄影师自己进入资料页面还是从查看入口进入）
     */
    private boolean isQuery = false;




    private QueryNoticesListBean queryNoticesListBean;

    private VisitUnit unit = new VisitUnit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(
                R.layout.activity_collect, null));
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.COLLECT_STATUS_CHANGE);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        initViews();
        initDatas();
        initListeners();
        requestData();

    }

    private void initViews() {
        titleName=(TextView)findViewById(R.id.titleName);
        backButton=findViewById(R.id.backButton);
        titleName.setText(getResources().getString(R.string.title_activity_collect));

        listView = (PullToRefreshListView) findViewById(R.id.listview);
        listView.setVerticalScrollBarEnabled(true);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

    }


    private void initDatas() {
        adapter = new NoticeAdapter(this, noticeList, isQuery);
        listView.setAdapter(adapter);
        //查询条件
        pageBean = new PageBean();
        pageBean.setPageSize(pageSize);
        pageBean.setPageIndex(mCurrentPage);
    }

    private void initListeners() {
        backButton.setOnClickListener(this);
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
        pageBean.setPageIndex(mCurrentPage);
        requestData();
    }
    private void requestData() {
//        unit.getDataUnit().getRepo().put("page", JSON.toJSONString(pageBean));
//        new Action(QueryUserCollectNotices.class, "${queryUserCollectNotices},page=${page}", this, null, unit).run();
    }

    public void responseNoticeList(NoticeListBean resultNoticeList) {
        if (resultNoticeList != null) {
            reqTime = resultNoticeList.getReqTime();
            if (mCurrentPage > 1) {
                noticeList.setReqTime(resultNoticeList.getReqTime());
                noticeList.getNotices().addAll(resultNoticeList.getNotices());
                adapter.notifyDataSetChanged();
            } else {
                noticeList = resultNoticeList;
                adapter = new NoticeAdapter(this, noticeList, isQuery);
                listView.setAdapter(adapter);
            }
        }else {
            if (mCurrentPage == 1) {
                noticeList = new NoticeListBean();
                adapter = new NoticeAdapter(this, noticeList, isQuery);
                listView.setAdapter(adapter);
            }
        }

        listView.onRefreshComplete();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backButton){
            finish();
        }
    }
    @Override
	public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
    /**
     * 监听广播回调结果
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.COLLECT_STATUS_CHANGE.equals(intent.getAction())) {
                mCurrentPage=1;
                pageBean.setPageIndex(mCurrentPage);
                requestData();
            }
        }
    };


}
