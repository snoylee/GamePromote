/*
 * 文 件 名:  LoginWelcomActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月3日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.txr.codec.digest.DigestUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
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

import java.util.Map;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author 王琪
 * @date 2015年11月3日
 * @action [登录欢迎页面]
 */
public class LoginWelcomActivity extends SGBaseActivity implements OnClickListener{

	private String typeIndex,whereFlag;
	private View registerButton, loginButton, weiXinLoginButton, qqLoginButton, weiboLoginButton, closeLoginWel,
			laucher,registerMerhantButton;
	private UMShareAPI mShareAPI = null;
	private String loginName = "", loginPwd;

	/**
	 * 重载方法
	 *
	 * @param savedInstanceState\
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_login_welcom_layout);
		initViews();
		initListeners();
		mShareAPI = UMShareAPI.get(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @action [初始化控件]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		registerButton = findViewById(R.id.registerButton);
		loginButton = findViewById(R.id.loginButton);
		weiXinLoginButton = findViewById(R.id.weiXinLoginButton);
		qqLoginButton = findViewById(R.id.qqLoginButton);
		weiboLoginButton = findViewById(R.id.weiboLoginButton);
		closeLoginWel = findViewById(R.id.closeLoginWel);
		laucher = findViewById(R.id.laucher);
		registerMerhantButton=findViewById(R.id.registerMerhantButton);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @action [初始化监听]
	 */
	private void initListeners() {
		whereFlag=getIntent().getStringExtra("whereFlag");
		// TODO Auto-generated method stub
		registerButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		weiXinLoginButton.setOnClickListener(this);
		qqLoginButton.setOnClickListener(this);
		weiboLoginButton.setOnClickListener(this);
		closeLoginWel.setOnClickListener(this);
		laucher.setOnClickListener(this);
		registerMerhantButton.setOnClickListener(this);
	}

	/**
	 * 重载方法
	 *
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.registerButton) {
			typeIndex = "1";
			UserPreferencesUtil.setUserLoginType(this, "1");
			Intent intent = new Intent(this, RegisterFristPageActivity.class);
			intent.putExtra("typeIndex",typeIndex);
			startActivityForResult(intent, 0);
		} else if (v.getId() == R.id.loginButton) {
			UserPreferencesUtil.setUserLoginType(this, "1");
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 0);
		} else if (v.getId() == R.id.weiXinLoginButton) {
			typeIndex = "2";
			SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
			mShareAPI.doOauthVerify(this, platform, umAuthListener);
		} else if (v.getId() == R.id.qqLoginButton) {
			typeIndex = "4";
			SHARE_MEDIA platform = SHARE_MEDIA.QQ;
			mShareAPI.doOauthVerify(this, platform, umAuthListener);
		} else if (v.getId() == R.id.weiboLoginButton) {
			typeIndex = "3";
			SHARE_MEDIA platform = SHARE_MEDIA.SINA;
			mShareAPI.doOauthVerify(this, platform, umAuthListener);
		} else if (v.getId() == R.id.closeLoginWel) {
			sendFaithBroadcast();
		} else if (v.getId() == R.id.laucher) {
			sendFaithBroadcast();
		}else if (v.getId()==R.id.registerMerhantButton){
			Intent intent = new Intent(this, MerchantRegFristStepActivity.class);
			startActivityForResult(intent, 0);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			sendFaithBroadcast();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void sendSuccessBroadcast() {
//		if ("mainMe".equals(whereFlag)){
//			Intent intent = new Intent(Constants.ACTION_LOGIN_SUCCESS);
//			sendBroadcast(intent);
//		}
		Intent intent = new Intent(Constants.ACTION_LOGIN_SUCCESS);
		sendBroadcast(intent);
		finish();
	}

	private void sendFaithBroadcast() {
//		if ("mainMe".equals(whereFlag)){
//			Intent intent = new Intent(Constants.ACTION_LOGIN_FAILTH);
//			sendBroadcast(intent);
//		}
		Intent intent = new Intent(Constants.ACTION_LOGIN_FAILTH);
		sendBroadcast(intent);
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 0: {
				if (Activity.RESULT_OK != resultCode || null == data) {
					return;
				}
				String result = data.getStringExtra(Constants.COMEBACK);
				if (Constants.COMEBACK.equals(result)) {
					boolean flag=data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
					if(flag){
						sendSuccessBroadcast();
					}else{
						sendFaithBroadcast();
					}
				}
				break;
			}
			default:
				/**使用SSO授权必须添加如下代码 */
				mShareAPI.onActivityResult(requestCode, resultCode, data);
				break;
		}
	}

	/** auth callback interface**/
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			String uid;
			if (SHARE_MEDIA.WEIXIN.equals(platform)){
				uid=data.get("openid").toString();
			}else{
				uid=data.get("uid").toString();
			}
			Message message = Message.obtain();
			message.obj = uid;
			message.what = 0;
			handler.sendMessage(message);
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText( getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText( getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					String uid = (String) msg.obj;
					loginOtherAccount(uid);
					break;

				default:
					break;
			}
		}
	};

	private void loginOtherAccount(String uid){
		try {
			// TODO Auto-generated method stub
			UserPreferencesUtil.setOtherPlatfromId(this, uid);

			if ("2".equals(typeIndex)) {
				UserPreferencesUtil.setUserLoginType(this, "2");
				loginName = "weixin_".concat(uid);
			} else if ("3".equals(typeIndex)) {
				UserPreferencesUtil.setUserLoginType(this, "3");
				loginName = "weibo_".concat(uid);
			} else if ("4".equals(typeIndex)) {
				UserPreferencesUtil.setUserLoginType(this, "4");
				loginName = "qq_".concat(uid);
			}
			loginPwd= DigestUtils.md5Hex(loginName.concat("sgappkey"));

			RequestBean item = new RequestBean();
			item.setIsPublic(false);
			JSONObject obj = new JSONObject();
			obj.put("registerType",typeIndex);
			obj.put("loginName", loginName);
			obj.put("loginPwd", loginPwd);
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

	/**
	 * <一句话功能简述> <功能详细描述>
	 *
	 * @action [关闭界面]
	 */
	public void closePage() {
		if (SGApplication.getInstance().getConnection()==null||UserPreferencesUtil.isRefract(this)){
			XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
		}else{
			ShowMsgDialog.cancel();
			Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
			UserPreferencesUtil.setIsOnline(this, true);
			sendSuccessBroadcast();
		}
	}

	@Override
	protected void xmppRespose() {
		super.xmppRespose();
		ShowMsgDialog.cancel();
		Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
		UserPreferencesUtil.setIsOnline(this, true);
		sendSuccessBroadcast();
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.QUERY_USER_LOGIN:
				if ("0000".equals(data.getCode())){
					paerseDatas(data.getRecord());
				}else if("1006".equals(data.getCode())){
					intoNextPage();
				}else {
					Toast.makeText(this,data.getMsg(),Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	public void intoNextPage() {
		Intent intent = new Intent(this, RegisterSecondPageActivity.class);
		intent.putExtra("typeIndex",typeIndex);
		startActivityForResult(intent, 0);
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
			UserPreferencesUtil.setLoginName(this,loginName);
			UserPreferencesUtil.setLoginPwd(this,loginPwd);
			if (SGApplication.getInstance().getConnection()==null||UserPreferencesUtil.isRefract(this)){
				XMPPUtils.loginXMPP(this, UserPreferencesUtil.getPwd(this), UserPreferencesUtil.getUserNickName(this));
				ShowMsgDialog.cancel();
				Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
				UserPreferencesUtil.setIsOnline(this, true);
				Intent intent = new Intent();
				intent.putExtra(Constants.COMEBACK, Constants.COMEBACK);
				intent.putExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, true);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}else{
				UserPreferencesUtil.setIsNewConnect(this,false);
				ShowMsgDialog.cancel();
				Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
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
}
