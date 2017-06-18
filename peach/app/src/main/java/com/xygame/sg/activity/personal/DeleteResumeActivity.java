/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.personal;

import java.util.List;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.adapter.DeleteResumeAdapter;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.activity.personal.bean.TransferDeleResumeBean;
import com.xygame.sg.task.personal.DeleUserResumeTask;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import base.Actions;
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
public class DeleteResumeActivity extends SGBaseActivity implements OnClickListener,OnItemClickListener{
	
	private TextView titleName,rightButtonText;
	private View backButton,rightButton;
	private ListView reportList;
	private List<RsumeBean> datas,deleDatas;
	private DeleteResumeAdapter adapter;
	private TransferDeleResumeBean bean;

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
				R.layout.sg_delete_resume_layout, null));
		
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
		rightButtonText.setText("删除");
		titleName.setText("删除履历");
		rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
		bean=(TransferDeleResumeBean) getIntent().getSerializableExtra("bean");
		datas=bean.getDeleDatas();
		adapter=new DeleteResumeAdapter(this, datas);
		reportList.setAdapter(adapter);
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
		reportList.setOnItemClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName=(TextView)findViewById(R.id.titleName);
		
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		
		reportList=(ListView)findViewById(R.id.reportList);
		
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
			deleDatas=adapter.getSelectDatas();
			if(deleDatas.size()==0){
				Toast.makeText(getApplicationContext(), "请至少选择一项", Toast.LENGTH_SHORT).show();
			}else{
				transferLocationService();
			}
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
		Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
		commitBack();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	public void commitBack() {
		// TODO Auto-generated method stub
		bean.setDeleDatas(adapter.getLeaveDatas(deleDatas));
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, bean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	private void getDatasBack(){
		bean.setDeleDatas(adapter.getDatas());
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, bean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void transferLocationService() {
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.DeleUserResumeTask(${cudModelResume})", this, null, visit).run();
		new Actions().appenDialaogdOpen("弹出窗口显示的message").append(DeleUserResumeTask.class,"${cudModelResume}").appenDialaogdClose().run(this);
	}

	public List<RsumeBean> getDeleDatas() {
		return deleDatas;
	}

	/**
	 * 重载方法
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		adapter.selectItem(arg2);
	}
}
