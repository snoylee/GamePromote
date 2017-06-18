package com.xygame.sg.activity.notice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.NoticeAdapter;
import com.xygame.sg.activity.notice.adapter.SearchHisAdapter;
import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.SearchNoticesListBean;
import com.xygame.sg.define.listview.OnRefreshListener;
import com.xygame.sg.define.listview.RefreshListView;
import com.xygame.sg.task.notice.SearchNotices;
import com.xygame.sg.widget.ClearEditText;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import base.ViewBinder;
import base.action.Action;
import base.adapter.ListenerBox;
import base.adapter.MapAdapter;
import base.adapter.MapContent;
import base.data.net.http.JsonUtil;
import base.file.shpref.SPrefUtil;
import base.frame.VisitUnit;

public class SearchNoticeActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private ImageView back_iv;
    private ClearEditText search_content_et;
    private TextView search_tv;


    private PullToRefreshListView2 listView;
    private ListView search_history_lv;
    private TextView clear_history_tv;
    private LinearLayout history_ll;
    VisitUnit visitUnit = new VisitUnit(this);

    private LinearLayout empty_root_ll;
    private TextView primary_tip_tv;
    private TextView sub_tip_tv;

    private List<String> searchHisStrList = new ArrayList<String>();

    private NoticeAdapter adapter;
    private int pageSize = 8;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private NoticeListBean noticeList = new NoticeListBean();//通告主体数据
    //    private Date reqTime;
    private Long reqTime;
    private SearchNoticesListBean searchNoticesListBean;

    private SharedPreferences preferences;
    private static final String CACHE_SPF_NAME = "search_his_spf";
    private static final String CACHE_KEY_NAME = "search_his_key";


    /**
     * 标志是否从查看某个模特的入口进入
     */
    private boolean isQuery = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.activity_search_notice, null));
        preferences = getSharedPreferences(CACHE_SPF_NAME, 0);
        initViews();
        initDatas();
        addListener();
    }

    private void initViews() {
        back_iv = (ImageView) findViewById(R.id.back_iv);
        search_content_et = (ClearEditText) findViewById(R.id.search_content_et);
        search_tv = (TextView) findViewById(R.id.search_tv);
        search_history_lv = (ListView) findViewById(R.id.search_history_lv);
        history_ll = (LinearLayout) findViewById(R.id.history_ll);
        listView = (PullToRefreshListView2) findViewById(R.id.result_lv);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        clear_history_tv = (TextView) findViewById(R.id.clear_history_tv);

        empty_root_ll = (LinearLayout) findViewById(R.id.empty_root_ll);
        primary_tip_tv = (TextView) findViewById(R.id.primary_tip_tv);
        sub_tip_tv = (TextView) findViewById(R.id.sub_tip_tv);
    }

    private void initDatas() {
        String searchHisStrs = preferences.getString(CACHE_KEY_NAME,"");
        load(searchHisStrs);

        searchNoticesListBean = new SearchNoticesListBean();
        searchNoticesListBean.getPage().setPageSize(pageSize);
        searchNoticesListBean.getPage().setPageIndex(mCurrentPage);

        adapter = new NoticeAdapter(this, noticeList, isQuery);
        listView.setAdapter(adapter);
    }

    private void addListener() {
        listView.setOnRefreshListener(this);
        back_iv.setOnClickListener(this);
        search_tv.setOnClickListener(this);
        search_content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    search_tv.setEnabled(true);
                } else {
                    search_tv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        clear_history_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_tv:
                mCurrentPage = 1;
                search();
                break;
            case R.id.clear_history_tv:
                preferences.edit().clear().apply();
                searchHisStrList.clear();
                load(searchHisStrList.toString());
                break;
        }
    }

    private void search() {
        String searchStr = search_content_et.getText().toString();
        searchHisStrList.remove(searchStr);
        searchHisStrList.add(searchStr);
        String str = searchHisStrList.toString();
        preferences.edit().putString(CACHE_KEY_NAME,str ).apply();

        history_ll.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        requestNoticesList();
    }

    private void load(String result) {
        if (result != null && !result.equals("")) {
            searchHisStrList = (List) new JsonUtil().from(result);
            if (searchHisStrList != null && searchHisStrList.size() > 0) {
                List hisTemp = new ArrayList();
                if (searchHisStrList.size() > 5) {
                    hisTemp.addAll(searchHisStrList.subList(searchHisStrList.size() - 5, searchHisStrList.size()));
                    searchHisStrList.clear();
                    searchHisStrList.addAll(hisTemp);
                }

                List toReverseList = new ArrayList();
                toReverseList.addAll(searchHisStrList);
                Collections.reverse(toReverseList);
                SearchHisAdapter adapter = new SearchHisAdapter(this,toReverseList);
                search_history_lv.setAdapter(adapter);
                search_history_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String searchStr = ((TextView) view.findViewById(R.id.history_text)).getText().toString();
                        search_content_et.setText(searchStr);
                        search();
                    }
                });
                history_ll.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                history_ll.setVisibility(View.GONE);
            }
        } else {
            history_ll.setVisibility(View.GONE);
        }
    }

    private void requestNoticesList() {
        searchNoticesListBean.setContent(search_content_et.getText().toString());
        new Action(SearchNotices.class, "${searchNotices}", this, null, visitUnit).run();
    }

    public SearchNoticesListBean getSearchNoticesListBean() {
        return searchNoticesListBean;
    }

    public void responseNoticeList(NoticeListBean resultNoticeList) {
        if (resultNoticeList != null) {
            listView.setVisibility(View.VISIBLE);
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
            empty_root_ll.setVisibility(View.GONE);
            primary_tip_tv.setText("");
            sub_tip_tv.setText("");
        } else {
            if (mCurrentPage == 1){
                noticeList = new NoticeListBean();
                adapter = new NoticeAdapter(this, noticeList, isQuery);
                listView.setAdapter(adapter);
                listView.setVisibility(View.GONE);
                empty_root_ll.setVisibility(View.VISIBLE);
                primary_tip_tv.setText("没有找到合适的通告!");
                sub_tip_tv.setText("请更改搜索关键字");
            }

        }
        listView.onRefreshComplete();
    }

//    public void onRefresh() {
//        mCurrentPage = 1;
//        searchNoticesListBean.setReqtime(null);
//        searchNoticesListBean.getPage().setPageIndex(mCurrentPage);
//        requestNoticesList();
//    }
//
//    public void onLoadMoring() {
//        mCurrentPage += 1;
//        if (reqTime != null) {
//            searchNoticesListBean.setReqtime(reqTime + "");
//        } else {
//            searchNoticesListBean.setReqtime(new Date().getTime() + "");
//        }
//
//        searchNoticesListBean.getPage().setPageIndex(mCurrentPage);
//        requestNoticesList();
//    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mCurrentPage = 1;
        searchNoticesListBean.setReqtime(null);
        searchNoticesListBean.getPage().setPageIndex(mCurrentPage);
        requestNoticesList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mCurrentPage += 1;
        if (reqTime != null) {
            searchNoticesListBean.setReqtime(reqTime + "");
        } else {
            searchNoticesListBean.setReqtime(new Date().getTime() + "");
        }

        searchNoticesListBean.getPage().setPageIndex(mCurrentPage);
        requestNoticesList();
    }

    public PullToRefreshListView2 getListView() {
        return listView;
    }
}
