/*
 * 文 件 名:  LoginActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;

import org.json.JSONObject;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月3日
 * @action  [登录页面]
 */
public class LoginActivity extends SGBaseActivity implements OnClickListener{
	
	private View backLoginWel,closeLoginWel,forgetPwd,comfirm;
	private EditText inputAccountEditor,inputPwdEditor;
	private String accountValue;
	private String pwdVaule,password,loginName;

	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_login_layout);
		initViews();
		initListeners();
		initDatas();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		comfirm=findViewById(R.id.comfirm);
		backLoginWel=findViewById(R.id.backLoginWel);
		closeLoginWel=findViewById(R.id.closeLoginWel);
		forgetPwd=findViewById(R.id.forgetPwd);
		inputAccountEditor=(EditText)findViewById(R.id.inputAccountEditor);
		inputPwdEditor=(EditText)findViewById(R.id.inputPwdEditor);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		comfirm.setOnClickListener(this);
		forgetPwd.setOnClickListener(this);
		closeLoginWel.setOnClickListener(this);
		backLoginWel.setOnClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 重载方法
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.forgetPwd){
			Intent intent=new Intent(this, ForgetPwdFirstPageActivity.class);
			startActivityForResult(intent, 0);
		}else if(v.getId()==R.id.closeLoginWel){
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}else if(v.getId()==R.id.backLoginWel){
			finish();
		}else if (v.getId()==R.id.comfirm){
			accountValue = inputAccountEditor.getText().toString().trim();
			pwdVaule = inputPwdEditor.getText().toString().trim();
			if (!"".equals(accountValue)) {
				if (!"".equals(pwdVaule)) {
					password= DigestUtils.md5Hex(pwdVaule.concat("sgappkey"));
					UserPreferencesUtil.setCellPhone(this, accountValue);
					UserPreferencesUtil.setPwd(this, password);
					uploadInfo();
				} else {
					Toast.makeText(this, "密码不能为空",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void uploadInfo(){
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject obj = new JSONObject();
			obj.put("loginPwd",password);
			loginName="phone_".concat(accountValue);
			obj.put("loginName",loginName);
			String typeIndex=UserPreferencesUtil.getUserLoginType(this);
			obj.put("registerType",typeIndex);
			ProvinceBean areaBean= CacheService.getInstance().getCacheCommenAreaBean(Constants.COMMEN_AREA_FLAG);
			if (areaBean!=null){
				obj.put("province", areaBean.getProvinceCode());
				CityBean cityBean=areaBean.getCityBean();
				if (cityBean!=null){
					obj.put("city", cityBean.getCityCode());
				}
			}
			item.setData(obj);
			ShowMsgDialog.showNoMsg(this, false);
			item.setServiceURL(ConstTaskTag.QUEST_USER_LOGIN);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_USER_LOGIN);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.QUERY_USER_LOGIN:
				if ("0000".equals(data.getCode())){
					paerseDatas(data.getRecord());
				}else {
					Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	protected void responseFaith(ResponseBean data) {
		super.responseFaith(data);
		Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
	}

	private void paerseDatas(String record) {
		try {
			JSONObject obj=new JSONObject(record);
			String userId=obj.getString("userId");
			UserPreferencesUtil.setUserId(this, userId);
			String usernick=obj.getString("usernick");
			UserPreferencesUtil.setUserNickName(this, usernick);
			String userIcon=obj.getString("userIcon");
			UserPreferencesUtil.setHeadPic(this, userIcon);
			String gender=obj.getString("gender");
			UserPreferencesUtil.setSex(this, gender);
			String checkOff= StringUtils.getJsonValue(obj,"chatOff");
			UserPreferencesUtil.setCheckOff(this,checkOff);
			String age=obj.getString("age");
			String videoAuthStatus=null,identityAuthStatus=null;
			if (!obj.isNull("videoAuthStatus")){
				videoAuthStatus=obj.getString("videoAuthStatus");
			}
			if (!obj.isNull("identityAuthStatus")){
				identityAuthStatus=obj.getString("identityAuthStatus");
			}
			UserPreferencesUtil.setUserVideoAuth(this, videoAuthStatus);
			UserPreferencesUtil.setUserIDAuth(this, identityAuthStatus);
			UserPreferencesUtil.setExpertAuth(this, StringUtils.getJsonValue(obj, "expertAuth"));
			UserPreferencesUtil.setUserAge(this, age);
			UserPreferencesUtil.setLoginName(this, loginName);
			UserPreferencesUtil.setLoginPwd(this, password);
			String orderExpireTime= StringUtils.getJsonValue(obj,"orderExpireTime");
			String payExpireTime=StringUtils.getJsonValue(obj, "payExpireTime");
			UserPreferencesUtil.setorderExpireTime(this,orderExpireTime);
			UserPreferencesUtil.setpayExpireTime(this,payExpireTime);
			if (SGApplication.getInstance().getConnection()==null||UserPreferencesUtil.isRefract(this)){
				XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
				ShowMsgDialog.cancel();
				Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
				UserPreferencesUtil.setIsOnline(this, true);
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}else{
				UserPreferencesUtil.setIsNewConnect(this,false);
				ShowMsgDialog.cancel();
				Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
				UserPreferencesUtil.setIsOnline(this, true);
				Intent intent1 = new Intent(Constants.NOTICE_REQUEST_GROUP);
				sendBroadcast(intent1);
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
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
			}
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void xmppRespose() {
		super.xmppRespose();
		ShowMsgDialog.cancel();
		Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
		UserPreferencesUtil.setIsOnline(this, true);
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
		intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
