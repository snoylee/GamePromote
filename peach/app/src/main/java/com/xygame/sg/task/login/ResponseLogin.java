/*
 * 文 件 名:  ResponseLogin.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.login;

import java.util.List;
import java.util.Map;

import android.widget.EditText;
import android.widget.Toast;
import base.action.Action.Param;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.LoginActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [请添加内容描述]
 */
public class ResponseLogin extends NetWorkUtil {

	private EditText inputAccountEditor, inputPwdEditor;
	private LoginActivity activity;

	/**
	 * 重载方法
	 * 
	 * @param result
	 * @return
	 */
	@Override
	public String runResult(String result) {
		// TODO Auto-generated method stub
		String res = result;
		System.out.println(res);
		return super.runResult(result);
	}

	/**
	 * 重载方法
	 * 
	 * @param aparam
	 * @param object
	 */
	@Override
	public void callback(Param aparam, Object object) {
		// TODO Auto-generated method stub
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		String code = aparam.getResultunit().getRawMap().get("code");
		UserPreferencesUtil.setUserId(activity, null);
		UserPreferencesUtil.setUserNickName(activity, null);
		UserPreferencesUtil.setHeadPic(activity, null);
		UserPreferencesUtil.setSex(activity, null);
		UserPreferencesUtil.setUserType(activity, null);
		UserPreferencesUtil.setUserVerifyStatus(activity, null);
		UserPreferencesUtil.setBirthday(activity, null);
		if("1010".equals(code)){
			Map map=(Map) aparam.getResultunit().get("record");
			String birthday=map.get("birthday").toString();
			UserPreferencesUtil.setBirthday(activity, birthday);
			String userId=map.get("userId").toString();
			UserPreferencesUtil.setUserId(activity, userId);
			String usernick=map.get("usernick").toString();
			UserPreferencesUtil.setUserNickName(activity, usernick);
			String userIcon=map.get("userIcon").toString();
			UserPreferencesUtil.setHeadPic(activity, userIcon);
			String gender=map.get("gender").toString();
			UserPreferencesUtil.setSex(activity, gender);
			List<Map> listMap=(List<Map>) map.get("userTypes");
			for(Map it:listMap){
				String utype=it.get("utype").toString();
				UserPreferencesUtil.setUserType(activity, utype);
				String status=it.get("status").toString();
				UserPreferencesUtil.setUserVerifyStatus(activity, status);
			}
//			activity.closePage(false);
		}else{
			if (Constants.RESULT_CODE.equals(resultCode)) {
				Map map=(Map) aparam.getResultunit().get("record");
				String userId=map.get("userId").toString();
				UserPreferencesUtil.setUserId(activity, userId);
				String usernick=map.get("usernick").toString();
				UserPreferencesUtil.setUserNickName(activity, usernick);
				String userIcon=map.get("userIcon").toString();
				UserPreferencesUtil.setHeadPic(activity, userIcon);
				String gender=map.get("gender").toString();
				UserPreferencesUtil.setSex(activity, gender);
//				List<Map> listMap=(List<Map>) map.get("userTypes");
//				for(Map it:listMap){
//					String utype=it.get("utype").toString();
//					UserPreferencesUtil.setUserType(activity, utype);
//					String status=it.get("status").toString();
//					UserPreferencesUtil.setUserVerifyStatus(activity, status);
//				}
//				activity.closePage(true);
			} else {
				ShowMsgDialog.cancel();
				String msg=aparam.getResultunit().getRawMap().get("msg");
				Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT)
				.show();
			}
		}
		super.callback(aparam, object);
	}
	
	/**
	 * 重载方法
	 * @param url
	 * @return
	 */
	@Override
	public String runUrl(String url) {
		// TODO Auto-generated method stub
		System.out.println(url);
		return super.runUrl(url);
	}

	/**
	 * 重载方法
	 * 
	 * @param methodname
	 * @param params
	 * @param aparam
	 * @return
	 */
	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		// TODO Auto-generated method stub
		activity = ((LoginActivity) aparam.getActivity());
		inputAccountEditor = (EditText) aparam.getActivity().findViewById(
				R.id.inputAccountEditor);
		inputPwdEditor = (EditText) aparam.getActivity().findViewById(
				R.id.inputPwdEditor);
		String accountValue = inputAccountEditor.getText().toString().trim();
		String pwdVaule = inputPwdEditor.getText().toString().trim();

		if (!"".equals(accountValue)) {
			if (!"".equals(pwdVaule)) {
				String password=DigestUtils.md5Hex(pwdVaule.concat("sgappkey"));
				UserPreferencesUtil.setCellPhone(activity, accountValue);
				UserPreferencesUtil.setPwd(activity,password);
				ShowMsgDialog.show(activity, "登录中...", false);
				params.add("loginPwd="+password);
				return super.run(methodname, params, aparam);

			} else {
				Toast.makeText(aparam.getActivity(), "密码不能为空",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(aparam.getActivity(), "手机号不能为空", Toast.LENGTH_SHORT)
					.show();
		}

		return null;
	}
}
