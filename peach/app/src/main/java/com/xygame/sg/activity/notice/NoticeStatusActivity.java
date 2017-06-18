/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.NoticeStatusAdapter;
import com.xygame.sg.activity.notice.bean.CheckStatusBean;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class NoticeStatusActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName;
	private View backButton;
	private ListView listView;
	private NoticeStatusAdapter adapter;
	private String noticeId;
	
	public String getNoticeId(){
		return noticeId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.notice_status_layout, null));
		initViews();
		initListeners();
		initDatas();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		noticeId=getIntent().getStringExtra("noticeId");
		titleName = (TextView) findViewById(R.id.titleName);
		backButton = findViewById(R.id.backButton);
		listView=(ListView)findViewById(R.id.listView);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		titleName.setText("通告状态");
		adapter=new NoticeStatusAdapter(this, null);
		listView.setAdapter(adapter);
		loadDatas();
	}

	private void loadDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadNoticeStatusTask(${flows})", this, null, visit).run();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} 
	}

	public void resultInfo(List<Map> map) {
		// TODO Auto-generated method stub
		List<CheckStatusBean> datas=new ArrayList<CheckStatusBean>();
		for(Map sMap:map){
			CheckStatusBean item=new CheckStatusBean();
			item.setAmount(sMap.get("amount").toString());
			item.setCreateTime(sMap.get("createTime").toString());
			item.setFlowDesc(sMap.get("flowDesc").toString());
			item.setFlowTitle(sMap.get("flowTitle").toString());
			item.setRelateType(sMap.get("relateType").toString());
			item.setUserId(sMap.get("userId").toString());
			item.setUserNick(sMap.get("userNick").toString());
			datas.add(item);
		}
		adapter.addDatas(datas);
	}
}
