/*
 * 文 件 名:  ForgetPwdFirstPageActivity.java
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import base.ViewBinder;
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
 * @action [忘记密码第一个界面]
 */
public class ForgetPwdFirstPageActivity extends SGBaseActivity implements
		OnClickListener {

	private View backLoginWel, closeLoginWel,comfirm;
	private boolean isSend = true;
	private EditText cellphone,verifyCode;
	private String phone,vCode;
	private Button verifyButton;

	/**
	 * 重载方法
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_forgetpwd_firstpage_layout);
		initViews();
		initListeners();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		comfirm.setOnClickListener(this);
		verifyButton.setOnClickListener(this);
		backLoginWel.setOnClickListener(this);
		closeLoginWel.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		comfirm=findViewById(R.id.comfirm);
		verifyButton=(Button)findViewById(R.id.verifyButton);
		backLoginWel = findViewById(R.id.backLoginWel);
		closeLoginWel = findViewById(R.id.closeLoginWel);
		cellphone=(EditText)findViewById(R.id.cellphone);
		verifyCode=(EditText)findViewById(R.id.verifyCode);
	}

	/**
	 * 重载方法
	 *
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backLoginWel) {
			finish();
		} else if (v.getId() == R.id.closeLoginWel) {
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}else if(v.getId()==R.id.verifyButton){
			phone = cellphone.getText().toString().trim();
			if (!"".equals(phone)) {
				if (phone.matches(PatternUtils.MOBILE_PHONE)) {
					if (isSend()) {
						setSend(false);
						requestVerify();
					}
				} else {
					Toast.makeText(this, "请输入正确的手机号",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT)
						.show();
			}
		}else if (v.getId()==R.id.comfirm){
			phone = cellphone.getText().toString().trim();
			vCode = verifyCode.getText().toString().trim();
			if (!"".equals(phone)) {
				if (phone.matches(PatternUtils.MOBILE_PHONE)) {
					if (!"".equals(vCode)) {
						UserPreferencesUtil.setCellPhone(this, phone);
						checkVerify();
					} else {
						Toast.makeText(this, "验证码不能为空",
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
		}
	}

	private void checkVerify(){
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject obj = new JSONObject();
			obj.put("telephone",phone);
			obj.put("verifyCode",vCode);
			item.setData(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ShowMsgDialog.show(this, "验证码校验中...", false);
		item.setServiceURL(ConstTaskTag.QUEST_CHECK_FORGET_VERFIY);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CHECK_FORGET_VERFIY);
	}

	private void requestVerify() {
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject obj = new JSONObject();
			obj.put("telephone",phone);
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUEST_GET_FORGET_VERFIY);
			ShowMsgDialog.showNoMsg(this,false);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_GET_FORGET_VERFIY);
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
					Intent intent = new Intent(this, FogrgetPwdSecondPageActivity.class);
					intent.putExtra("cellphone", cellphone.getText().toString().trim());
					startActivityForResult(intent, 0);
				}else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case ConstTaskTag.QUERY_GET_FORGET_VERFIY:
				if ("0000".equals(data.getCode())){
					timerCount(60 * 1000, 1000);
					Toast.makeText(this, "验证码发送成功", Toast.LENGTH_SHORT)
							.show();
				}else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT)
							.show();
					setSend(true);
				}
				break;
		}
	}

	@Override
	protected void responseFaith(ResponseBean data) {
		super.responseFaith(data);
		Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
		setSend(true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			boolean flag=data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			if (flag) {
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, flag);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}else{
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
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
