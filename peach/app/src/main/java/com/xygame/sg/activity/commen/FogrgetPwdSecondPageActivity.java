/*
 * 文 件 名:  FogrgetPwdSecondPageActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
 * @action  [忘记密码第二个界面]
 */
public class FogrgetPwdSecondPageActivity extends SGBaseActivity implements OnClickListener{
	
	private View closeLoginWel,comfirm;
	private EditText newPwdAgin,newPwd;
	private String cellphone,pwdScrect;
	private String pwdValue,pwdReValue;
	
	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_forgetpwd_secondpage_layout);
		initViews();
		initListeners();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		closeLoginWel=findViewById(R.id.closeLoginWel);
		comfirm=findViewById(R.id.comfirm);
		newPwdAgin=(EditText)findViewById(R.id.newPwdAgin);
		newPwd=(EditText)findViewById(R.id.newPwd);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		closeLoginWel.setOnClickListener(this);
		comfirm.setOnClickListener(this);
		cellphone=getIntent().getStringExtra("cellphone");
	}

	/**
	 * 重载方法
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.closeLoginWel){
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}else if (v.getId()==R.id.comfirm){
			pwdValue=newPwd.getText().toString().trim();
			pwdReValue=newPwdAgin.getText().toString().trim();
			if (!"".equals(pwdValue)) {
				if (6 <= pwdValue.length() && pwdValue.length() <= 16) {
					if (!"".equals(pwdReValue)) {

						if(pwdValue.equals(pwdReValue)){
							pwdScrect= DigestUtils.md5Hex(pwdValue.concat("sgappkey"));
							UserPreferencesUtil.setCellPhone(this, cellphone);
							UserPreferencesUtil.setPwd(this, pwdScrect);
							reSetPwd();
						}else{
							Toast.makeText(this, "两次输入密码不一致",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(this, "密码不能为空",
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
		}
	}

	private void reSetPwd() {
		RequestBean item = new RequestBean();
		item.setIsPublic(false);
		try {
			JSONObject obj = new JSONObject();
			obj.put("telephone",cellphone);
			obj.put("token",UserPreferencesUtil.getUserToken(this));
			obj.put("loginPwd",pwdScrect);
			ProvinceBean areaBean= CacheService.getInstance().getCacheCommenAreaBean(Constants.COMMEN_AREA_FLAG);
			if (areaBean!=null){
				obj.put("province", areaBean.getProvinceCode());
				CityBean cityBean=areaBean.getCityBean();
				if (cityBean!=null){
					obj.put("city", cityBean.getCityCode());
				}
			}
			item.setData(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ShowMsgDialog.show(this, "提交中...", false);
		item.setServiceURL(ConstTaskTag.QUEST_RESET_PWD);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_RESET_PWD);
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.QUERY_RESET_PWD:
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
		Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
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
			 Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
				setResult(Activity.RESULT_OK, intent);
				finish();
		 }
		return super.onKeyDown(keyCode, event);
	}

	private void paerseDatas(String record) {
		try {
			JSONObject obj=new JSONObject(record);
			String orderExpireTime= StringUtils.getJsonValue(obj,"orderExpireTime");
			String payExpireTime=StringUtils.getJsonValue(obj, "payExpireTime");
			UserPreferencesUtil.setorderExpireTime(this,orderExpireTime);
			UserPreferencesUtil.setpayExpireTime(this,payExpireTime);
			String userId=obj.getString("userId");
			UserPreferencesUtil.setUserId(this, userId);
			String usernick=obj.getString("usernick");
			UserPreferencesUtil.setUserNickName(this, usernick);
			String userIcon=obj.getString("userIcon");
			UserPreferencesUtil.setHeadPic(this, userIcon);
			String gender=obj.getString("gender");
			UserPreferencesUtil.setSex(this, gender);
			String checkOff= StringUtils.getJsonValue(obj, "chatOff");
			UserPreferencesUtil.setCheckOff(this, checkOff);
			String age=obj.getString("age");
			String videoAuthStatus=null,identityAuthStatus=null;
			if (!obj.isNull("videoAuthStatus")){
				videoAuthStatus=obj.getString("videoAuthStatus");
			}
			if (!obj.isNull("identityAuthStatus")){
				identityAuthStatus=obj.getString("identityAuthStatus");
			}
			UserPreferencesUtil.setExpertAuth(this,StringUtils.getJsonValue(obj,"expertAuth"));
			UserPreferencesUtil.setUserVideoAuth(this, videoAuthStatus);
			UserPreferencesUtil.setUserIDAuth(this, identityAuthStatus);
			UserPreferencesUtil.setUserAge(this, age);
			UserPreferencesUtil.setLoginPwd(this, pwdScrect);
			ShowMsgDialog.cancel();
			XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
			Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
			UserPreferencesUtil.setIsOnline(this, true);
			Intent intent = new Intent();
			intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
			intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}catch (Exception e){
			e.printStackTrace();
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
