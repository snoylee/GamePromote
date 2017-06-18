/*
 * 文 件 名:  RegisterFristPageActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月3日
 * @action [用户注册第一个界面]
 */
public class BangCellPhoneActivity extends SGBaseActivity implements
		OnClickListener ,OnCheckedChangeListener{
	private TextView titleName;
	private View pwdControl,showPwdView,hidePwdView,comfirm,verifyButton,inputPwdEditor,backButton;
	private EditText inputPwd,cellphone,verifyCode;
	private CheckBox pwdChangeButton;
	private TextView cellphoneText;
	private boolean isSend = true;
//	private String bindType;

//	public String getBindType (){
//		return bindType;
//	}

	/**
	 * 重载方法
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(
				R.layout.bang_cellphone_layout, null));
		initViews();
		initListeners();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		cellphoneText=(TextView)findViewById(R.id.cellphoneText);
		inputPwdEditor=findViewById(R.id.inputPwdEditor);
		showPwdView=findViewById(R.id.showPwdView);
		pwdControl=findViewById(R.id.pwdControl);
		hidePwdView=findViewById(R.id.hidePwdView);
		inputPwd = (EditText) findViewById(R.id.inputPwd);
		verifyCode=(EditText)findViewById(R.id.verifyCode);
		cellphone=(EditText)findViewById(R.id.cellphone);
		verifyButton=findViewById(R.id.verifyButton);
		pwdChangeButton = (CheckBox) findViewById(R.id.pwdChangeButton);
		comfirm=findViewById(R.id.comfirm);

		if (UserPreferencesUtil.getCellPhone(this)!=null){
			titleName.setText("更换绑手机");
			cellphoneText.setVisibility(View.VISIBLE);
			cellphoneText.setText("您的手机号码：".concat(UserPreferencesUtil.getCellPhone(this)));
			inputPwdEditor.setVisibility(View.GONE);
		}else{
			titleName.setText("绑定手机");
			inputPwdEditor.setVisibility(View.VISIBLE);
			cellphoneText.setVisibility(View.GONE);
		}
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		backButton.setOnClickListener(this);
		pwdControl.setOnClickListener(this);
		pwdChangeButton.setOnCheckedChangeListener(this);
		verifyButton.setOnClickListener(this);
		comfirm.setOnClickListener(this);
	}

	private void setCoursePosion(){
		CharSequence text = inputPwd.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable)text;
			Selection.setSelection(spanText, text.length());
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0,
								 boolean flag) {
		if (flag) {
			inputPwd.setTransformationMethod(HideReturnsTransformationMethod
					.getInstance());
			inputPwd.postInvalidate();
			setCoursePosion();
		} else {
			inputPwd.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			inputPwd.postInvalidate();
			setCoursePosion();
		}
	}

	/**
	 * 重载方法
	 *
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.pwdControl){
			pwdControlViews();
		}else if (v.getId()==R.id.comfirm){
			loadDatas();
		}else if (v.getId()==R.id.verifyButton){
			loadDatasVerfy();
		}else if (v.getId() == R.id.backButton) {
			finish();
		}
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void pwdControlViews() {
		if(showPwdView.getVisibility()==View.VISIBLE){
			showPwdView.setVisibility(View.GONE);
			inputPwd.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			inputPwd.postInvalidate();
			setCoursePosion();
		}else{
			showPwdView.setVisibility(View.VISIBLE);
			inputPwd.setTransformationMethod(HideReturnsTransformationMethod
					.getInstance());
			inputPwd.postInvalidate();
			setCoursePosion();
		}
		
		if(hidePwdView.getVisibility()==View.VISIBLE){
			hidePwdView.setVisibility(View.GONE);
		}else{
			hidePwdView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			boolean result = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG,false);
			if (result) {
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}else{
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
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
	 * 获取 isSend
	 * 
	 * @return 返回 isSend
	 */
	public boolean isSend() {
		return isSend;
	}

	/**
	 * 设置 isSend
	 *
	 */
	public void setSend(boolean isSend) {
		this.isSend = isSend;
	}

	public void intentNextActivity() {
		showDialog("修改手机号码成功,下次登录时请用您的新手机号码");
	}

	public void loadDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.login.ResponseBandCheckVerify(${bindUserPhone})", this, null, visit).run();
	}

	public void loadDatasVerfy() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.login.ResponseBangVerify(${user_sendRegVerifyCode})", this, null, visit).run();
	}

	private void showDialog(String tip){
		OneButtonDialog dialog = new OneButtonDialog(this, tip,R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						closePage();
					}
				});
				dialog.show();
	}

	private void closePage(){
		Intent intent = new Intent();
		intent.putExtra("flag", true);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
