/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.personal;

import java.util.ArrayList;
import java.util.List;

import com.haarman.listviewanimations.ArrayAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.haarman.listviewanimations.view.DynamicListView;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.OrderResumeAdapter;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.activity.personal.bean.TransferDeleResumeBean;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月9日
 * @action  [举报第一个界面]
 */
public class OrderResumeActivity extends SGBaseActivity implements OnClickListener{
	
	private TextView titleName,rightButtonText;
	private View backButton,rightButton;
	private DynamicListView mListView;
	private List<RsumeBean> datas,orderDatas;
	private ArrayAdapter<RsumeBean> adapter;
	private TransferDeleResumeBean bean;
	private LinearLayout listContentView;

	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(
				R.layout.sg_order_resume_layout, null));
		
		initViews();
		initListeners();
		initDatas();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText("确定");
		titleName.setText("履历排序");
		rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
		orderDatas=new ArrayList<RsumeBean>();
		bean=(TransferDeleResumeBean) getIntent().getSerializableExtra("bean");
		datas=bean.getDeleDatas();
		
		mListView = new DynamicListView(this);
		mListView.setDivider(null);
		
		adapter=new OrderResumeAdapter(this, datas);
		
		AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(adapter);
		animAdapter.setAbsListView(mListView);
		mListView.setAdapter(animAdapter);
		listContentView.addView(mListView);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName=(TextView)findViewById(R.id.titleName);
		listContentView=(LinearLayout)findViewById(R.id.listContentView);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		
		backButton=findViewById(R.id.backButton);
		rightButton=findViewById(R.id.rightButton);
		
	}

	/**
	 * 重载方法
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.backButton){
			getDatasBack();
		}else if(v.getId()==R.id.rightButton){
			List<RsumeBean> tempDatas=adapter.getAll();
			for(int i=0;i<tempDatas.size();i++){
				RsumeBean it=tempDatas.get(i);
				it.setLocIndex(i+1);
				orderDatas.add(it);
			}
			transferLocationService();
		}
	}
	
	/**
	 * 重载方法
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if(keyCode==KeyEvent.KEYCODE_BACK){
			 getDatasBack();
		 }
		return super.onKeyDown(keyCode, event);
	}
	
	public void finishDelete() {
		Toast.makeText(getApplicationContext(), "排序成功", Toast.LENGTH_SHORT).show();
		commitBack();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void commitBack() {
		// TODO Auto-generated method stub
		bean.setDeleDatas(adapter.getAll());
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, bean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	private void getDatasBack(){
		bean.setDeleDatas(adapter.getAll());
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, bean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void transferLocationService() {
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.OrderUserResumeTask(${cudModelResume})", this, null, visit).run();
	}

	public List<RsumeBean> getDeleDatas() {
		return orderDatas;
	}
}
