/*
 * 文 件 名:  RegisterFristPageActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import base.ViewBinder;
import base.action.Action;
import base.action.CenterRepo;
import base.frame.VisitUnit;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.PatternUtils;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月3日
 * @action [用户注册第一个界面]
 */
public class RegisterFristPageActivity extends SGBaseActivity implements
		OnClickListener ,OnCheckedChangeListener{

	private View backLoginWel, closeLoginWel, forgetPwd,pwdControl,showPwdView,hidePwdView,comfirm;
	private Button verifyButton;
	private EditText inputPwd,cellphone,verifyCode;
	private CheckBox pwdChangeButton;
	private boolean isSend = true,isClick=true;
	private String phone,vCode,pwd,typeIndex;

	/**
	 * 重载方法
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_register_firstpage_layout);
		typeIndex=getIntent().getStringExtra("typeIndex");
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
		closeLoginWel = findViewById(R.id.closeLoginWel);
		backLoginWel = findViewById(R.id.backLoginWel);
		forgetPwd = findViewById(R.id.forgetPwd);
		showPwdView=findViewById(R.id.showPwdView);
		pwdControl=findViewById(R.id.pwdControl);
		hidePwdView=findViewById(R.id.hidePwdView);
		inputPwd = (EditText) findViewById(R.id.inputPwd);
		verifyCode=(EditText)findViewById(R.id.verifyCode);
		cellphone=(EditText)findViewById(R.id.cellphone);
		verifyButton=(Button)findViewById(R.id.verifyButton);
		pwdChangeButton = (CheckBox) findViewById(R.id.pwdChangeButton);
		comfirm=findViewById(R.id.comfirm);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		closeLoginWel.setOnClickListener(this);
		backLoginWel.setOnClickListener(this);
		forgetPwd.setOnClickListener(this);
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
		if (v.getId() == R.id.closeLoginWel) {
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (v.getId() == R.id.backLoginWel) {
			finish();
		} else if (v.getId() == R.id.forgetPwd) {
			Intent intent = new Intent(this, UserRulsActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.pwdControl){
			pwdControlViews();
		}else if (v.getId()==R.id.comfirm){

//			Intent intent = new Intent(this, RegisterSecondPageActivity.class);
//			startActivity(intent);

			vCode = verifyCode.getText().toString().trim();
			pwd = inputPwd.getText().toString().trim();
			phone=cellphone.getText().toString().trim();
			if (!"".equals(phone)) {
				if (phone.matches(PatternUtils.MOBILE_PHONE)) {
					if (!"".equals(pwd)) {
						if (6 <= pwd.length() && pwd.length() <= 16) {
							if (!"".equals(vCode)) {
								if (isClick){
									loadDatas();
								}
							} else {
								Toast.makeText(this, "验证码不能为空",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(this, "密码长度在6-16位",
									Toast.LENGTH_SHORT).show();
						}

					} else {
						Toast.makeText(this, "密码不能为空",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "请输入正确的手机号",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT)
						.show();
			}

		}else if (v.getId()==R.id.verifyButton){
			phone=cellphone.getText().toString().trim();
			if (!"".equals(phone)) {
				if (phone.matches(PatternUtils.MOBILE_PHONE)) {
					if (isSend()) {
						setSend(false);
						timerCount(60 * 1000, 1000);
						loadDatasVerfy();
					}
				} else {
					Toast.makeText(this, "请输入正确的手机号",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT)
						.show();
			}
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

	public void loadDatas() {
		isClick=false;
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject obj = new JSONObject();
			obj.put("telephone",phone);
			obj.put("verifyCode",vCode);
			item.setData(obj);
			ShowMsgDialog.show(this, "验证码校验中...", false);
			item.setServiceURL(ConstTaskTag.QUEST_CHECK_FORGET_VERFIY);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CHECK_FORGET_VERFIY);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void loadDatasVerfy() {
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject obj = new JSONObject();
			obj.put("telephone",phone);
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUEST_GET_REGISTER_VERFIY);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GET_REGISTER_VERFIY);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.QUERY_CHECK_FORGET_VERFIY:
				if ("0000".equals(data.getCode())){
					String token=data.getRecord();
					UserPreferencesUtil.setUserToken(this, token);
					isClick=true;
					Intent intent = new Intent(this, RegisterSecondPageActivity.class);
					intent.putExtra("cellPhone",cellphone.getText().toString().trim());
					intent.putExtra("typeIndex",typeIndex);
					intent.putExtra("pwd",pwd);
					startActivityForResult(intent, 0);
				}else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case ConstTaskTag.QUERY_GET_REGISTER_VERFIY:
				if ("0000".equals(data.getCode())){
					Toast.makeText(this, "验证码发送成功", Toast.LENGTH_SHORT)
							.show();
				}else {
					Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	protected void responseFaith(ResponseBean data) {
		super.responseFaith(data);
		Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
	}


	public void checkVerfiyFaith() {
		isClick=true;
	}

	private void timerCount(int minute, int second) {
		// TODO Auto-generated method stub
		new CountDownTimer(minute, second) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				verifyButton.setText(String.valueOf(millisUntilFinished / 1000)
						.concat("秒后重发"));
				verifyButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
				setSend(false);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				verifyButton.setText("发送");
				verifyButton.setBackgroundResource(R.drawable.shape_rect_dark_green);
				setSend(true);
			}
		}.start();

	}
}
