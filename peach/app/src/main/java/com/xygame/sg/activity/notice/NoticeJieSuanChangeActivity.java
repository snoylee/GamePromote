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
import com.xygame.sg.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
public class NoticeJieSuanChangeActivity extends SGBaseActivity implements OnClickListener ,TextWatcher{

	private TextView titleName,rightButtonText;
	private View backButton,rightButton;
	private EditText inputNums;
	private String daiMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.notice_jiesuan_change_layout, null));
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
		inputNums.addTextChangedListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		backButton = findViewById(R.id.backButton);
		rightButton=findViewById(R.id.rightButton);
		inputNums=(EditText)findViewById(R.id.inputNums);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		daiMoney=getIntent().getStringExtra("daiMoney");
		titleName.setText("更改拍摄结算款");
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText("确定");
		rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
		inputNums.setHint("请输入金额大于或等于"+daiMoney);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} else if(v.getId()==R.id.rightButton){
			if("".equals(inputNums.getText().toString().trim())){
				Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_SHORT).show();
			}else{
				if(Float.parseFloat(inputNums.getText().toString().trim())>=Float.parseFloat(daiMoney)){
					Intent intent = new Intent();
					intent.putExtra(Constants.COMEBACK, inputNums.getText().toString().trim());
					setResult(Activity.RESULT_OK, intent);
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "输入金额必须大于或等于待结算金额", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String str=s.toString();
		
		if(str.length()==0){
			inputNums.setHint("请输入金额大于或等于"+daiMoney);
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
}
