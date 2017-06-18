/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.ModelRequestAdapter;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import base.ViewBinder;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class PlushNoticeRequestModelActivity extends SGBaseActivity implements OnClickListener, OnItemClickListener {

	private TextView titleName;
	private ImageView rightbuttonIcon;
	private View backButton, rightButton, addNewModel, nextButton;
	private ListView modelList;
	private ModelRequestAdapter adapter;
	private PlushNoticeBean pnBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_plush_notice_model_request_layout, null));

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
		rightButton.setOnClickListener(this);
		addNewModel.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		modelList.setOnItemClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		rightbuttonIcon = (ImageView) findViewById(R.id.rightbuttonIcon);
		modelList = (ListView) findViewById(R.id.modelList);
		View addFooterView = LayoutInflater.from(this).inflate(R.layout.sg_plush_notice_add_model_request, null);
		addNewModel = addFooterView.findViewById(R.id.addNewModel);
		View nextFooterView = LayoutInflater.from(this).inflate(R.layout.sg_plush_notice_footer_button_view, null);
		nextButton = nextFooterView.findViewById(R.id.nextButton);
		modelList.addFooterView(addFooterView);
		modelList.addFooterView(nextFooterView);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		pnBean = (PlushNoticeBean) getIntent().getSerializableExtra("bean");
		titleName.setText("招募模特要求");
		rightButton.setVisibility(View.VISIBLE);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightbuttonIcon.setImageResource(R.drawable.sg_con_service);
		if (pnBean.getModelBeans() != null) {
			if (pnBean.getModelBeans().size()>0){
				adapter = new ModelRequestAdapter(this, pnBean.getModelBeans());
				modelList.setAdapter(adapter);
			}else {
				adapter = new ModelRequestAdapter(this, null);
				modelList.setAdapter(adapter);
				Intent firstIntent = new Intent(this, ModelRequestActivity.class);
				firstIntent.putExtra("flag", "new");
				firstIntent.putExtra("index", adapter.getCount());
				startActivityForResult(firstIntent, 0);
			}
		} else {
			adapter = new ModelRequestAdapter(this, null);
			modelList.setAdapter(adapter);
			Intent firstIntent = new Intent(this, ModelRequestActivity.class);
			firstIntent.putExtra("flag", "new");
			firstIntent.putExtra("index", adapter.getCount());
			startActivityForResult(firstIntent, 0);
		}
	}

	/**
	 * 重载方法
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			pnBean.setModelBeans(adapter.getDatas());
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, pnBean);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			pnBean.setModelBeans(adapter.getDatas());
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, pnBean);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (v.getId() == R.id.rightButton) {
//			//联系客服
//			if (UserPreferencesUtil.getServicePhone(this)!=null&&!"null".equals(UserPreferencesUtil.getServicePhone(this))){
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserPreferencesUtil.getServicePhone(this)));
//				startActivity(intent);
//			}else{
//				Toast.makeText(this, "系统维护中", Toast.LENGTH_SHORT).show();
//			}
		} else if (v.getId() == R.id.addNewModel) {
			Intent firstIntent = new Intent(this, ModelRequestActivity.class);
			firstIntent.putExtra("flag", "new");
			firstIntent.putExtra("index", adapter.getCount());
			startActivityForResult(firstIntent, 0);
		} else if (v.getId() == R.id.nextButton) {
			if(adapter.getCount()>0){
				pnBean.setModelBeans(adapter.getDatas());
				Intent firstIntent = new Intent(this, PlushNoticeBrowersActivity.class);
				firstIntent.putExtra("bean", pnBean);
				startActivityForResult(firstIntent, 1);
			}else{
				Toast.makeText(getApplicationContext(), "至少添加一条要求", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			ModelRequestBean item = (ModelRequestBean) data.getSerializableExtra(Constants.COMEBACK);
			boolean flag = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			if (flag) {
				adapter.addNewItem(item);
			} else {
				adapter.updateItem(item);
			}
			break;
		}

		case 1: {
			pnBean = (PlushNoticeBean) data.getSerializableExtra(Constants.COMEBACK);
			boolean flag = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			if (flag) {
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, pnBean);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, flag);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
			break;
		}

		default:
			break;
		}
	}

	/**
	 * 重载方法
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 < adapter.getCount()) {
			ModelRequestBean item = adapter.getItem(arg2);
			Intent firstIntent = new Intent(this, ModelRequestActivity.class);
			firstIntent.putExtra("flag", "older");
			firstIntent.putExtra("bean", item);
			startActivityForResult(firstIntent, 0);
		}
	}

}
