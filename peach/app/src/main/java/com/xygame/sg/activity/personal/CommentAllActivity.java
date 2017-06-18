package com.xygame.sg.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.personal.adapter.CommentAdapter;
import com.xygame.sg.activity.personal.bean.ScoreListVo;
import com.xygame.sg.task.indivual.GetScoreList;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import java.util.ArrayList;
import java.util.List;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class CommentAllActivity extends SGBaseActivity implements  View.OnClickListener  {
    private View backButton;
    private TextView titleName;
    VisitUnit visitUnit = new VisitUnit();
    private String seeUserId;
    private String utype;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private PageBean pageBean;
    private PullToRefreshListView2 listView;
    private CommentAdapter adapter;
    private List<ScoreListVo> scoreList;

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;
    private TextView sub_tip_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_comment_all, null));
        seeUserId = getIntent().getStringExtra("seeUserId");
        utype = getIntent().getStringExtra("utype");
        initViews();
        initDatas();
        addListener();
        requestData();
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText(getString(R.string.title_activity_comment_all));
        empty_root_ll = (LinearLayout) findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView) findViewById(R.id.primary_tip_tv);
        sub_tip_tv = (TextView) findViewById(R.id.sub_tip_tv);
        listView = (PullToRefreshListView2) findViewById(R.id.listview);
    }

    private void initDatas() {
        pageBean = new PageBean();
        pageBean.setPageSize(pageSize);
        pageBean.setPageIndex(mCurrentPage);
    }

    private void addListener() {
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
//        visitUnit.getDataUnit().getRepo().put("seeUserId", seeUserId);
//        visitUnit.getDataUnit().getRepo().put("page", JSON.toJSONString(pageBean));
//        new Action(GetScoreList.class, "${scoreList},seeUserId=${seeUserId},page=${page}", this, null, visitUnit).run();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
        }
    }


    public void responseScoreList(List<ScoreListVo> resScoreList) {
        if (resScoreList != null) {
            listView.setVisibility(View.VISIBLE);
            if (mCurrentPage > 1) {
                scoreList.addAll(resScoreList);
                adapter.notifyDataSetChanged();
            } else {
                scoreList = resScoreList;
                adapter = new CommentAdapter(this, scoreList,utype);
                listView.setAdapter(adapter);
            }
            empty_root_ll.setVisibility(View.GONE);
            primary_tip_tv.setText("");
            sub_tip_tv.setText("");
        }else {
            if (mCurrentPage == 1) {
                scoreList = new ArrayList<ScoreListVo>();
                adapter = new CommentAdapter(this, scoreList,utype);
                listView.setAdapter(adapter);
                listView.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
                primary_tip_tv.setText("还没有评价！");
            }

        }

        listView.onRefreshComplete();
    }
}
