package com.xygame.sg.activity.testpay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;

import com.xygame.second.sg.defineview.RefreshableView;
import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.PriseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/13.
 */
public class MediaListActivity extends Activity implements  RefreshableView.PullToRefreshListener{
    private RefreshableView refreshView;
    private RecyclerView media_list;
    private TestCeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushleftloadmore_view);
        WebView show = (WebView)findViewById(R.id.webView1);
        show.loadUrl("file:///android_asset/ddd2.html");
//        media_list=(RecyclerView)findViewById(R.id.media_list);
//
//        //设置固定大小
//        media_list.setHasFixedSize(true);
//        //创建线性布局
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        //垂直方向
//        mLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
//        //给RecyclerView设置布局管理器
//        media_list.setLayoutManager(mLayoutManager);
//
//        refreshView = (RefreshableView) findViewById(R.id.refreshable_view);
//        refreshView.setOnRefreshListener(this, 0);
//        List<PriseBean> strList=new ArrayList<>();
//        for (int i=0;i<10;i++){
//            PriseBean priseBean=new PriseBean();
//            strList.add(priseBean);
//        }
//        adapter=new TestCeAdapter(this,strList);
//        media_list.setAdapter(adapter);
    }

    /**
     * 侧拉加载的回调接口
     */
    @Override
    public void onRefresh() {
        //此处执行刷新操作
        refreshView.finishRefreshing();
    }
}
