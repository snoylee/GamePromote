/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.activity.personal.SelectFirstCategoryActivity;
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
public class PlushNoticePlaceActivity extends SGBaseActivity implements OnClickListener {

	private TextView titleName, rightButtonText,provinceCity;
	private View backButton, rightButton,choiceView;
	private PlushNoticeAreaBean areaBean;
	private EditText detailAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.sg_plush_notice_area_layout, null));

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
		choiceView.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		provinceCity = (TextView) findViewById(R.id.provinceCity);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		detailAddress=(EditText)findViewById(R.id.detailAddress);

		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		choiceView = findViewById(R.id.choiceView);

		areaBean=(PlushNoticeAreaBean) getIntent().getSerializableExtra("bean");
		
		updateAreaText();
		
		if(areaBean.getAddress()!=null){
			detailAddress.setText(areaBean.getAddress());
		}
	}
	
	private void updateAreaText(){
		if(areaBean.getProvinceName()!=null){
			if(areaBean.getCityName()!=null){
				if(areaBean.getProvinceName().equals(areaBean.getCityName())){
					provinceCity.setText(areaBean.getProvinceName());
				}else{
					provinceCity.setText(areaBean.getProvinceName().concat(" ").concat(areaBean.getCityName()));
				}
			}else{
				provinceCity.setText(areaBean.getProvinceName());
			}
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		titleName.setText("拍摄地点");
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText("完成");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.choiceView) {
			 Intent firstIntent = new Intent(this,PlushNoticePlaceProvinceActivity.class);
			 firstIntent.putExtra("noLimitFlag", true);
             firstIntent.putExtra("bean",areaBean);
             startActivityForResult(firstIntent, 0);
		}  else if (v.getId() == R.id.rightButton) {
			if("".equals(provinceCity.getText().toString())){
				Toast.makeText(getApplicationContext(), "请选择拍摄地点", Toast.LENGTH_SHORT).show();
			}else{
				areaBean.setAddress(detailAddress.getText().toString().trim());
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, areaBean);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		} 
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			areaBean= (PlushNoticeAreaBean) data.getSerializableExtra(Constants.COMEBACK);
			updateAreaText();
			break;
		}
		default:
			break;
		}
	}
}
