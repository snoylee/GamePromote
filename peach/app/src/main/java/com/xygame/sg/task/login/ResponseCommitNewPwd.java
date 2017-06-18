/*
 * 文 件 名:  ResponseCommitNewPwd.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月6日
 */
package com.xygame.sg.task.login;

import java.util.List;
import java.util.Map;

import android.widget.EditText;
import android.widget.Toast;
import base.action.Action.Param;
import base.action.CenterRepo;

import com.txr.codec.digest.DigestUtils;
import com.xygame.sg.R;
import com.xygame.sg.activity.commen.FogrgetPwdSecondPageActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月6日
 * @action  [提交新密码]
 */
public class ResponseCommitNewPwd extends NetWorkUtil{
	private EditText newPwd,newPwdAgin;
	private FogrgetPwdSecondPageActivity activity;
	
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
				String msg=aparam.getResultunit().getRawMap().get("msg");
				Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT)
				.show();
			}
		}
		super.callback(aparam, object);
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
	public Object run(final String methodname, final List<String> params, final Param aparam) {
		// TODO Auto-generated method stub
		activity = ((FogrgetPwdSecondPageActivity) aparam.getActivity());
//		if (!"".equals(pwdValue)) {
//			if (6 <= pwdValue.length() && pwdValue.length() <= 16) {
//					if (!"".equals(pwdReValue)) {
//
//						if(pwdValue.equals(pwdReValue)){
//							String cellphone=activity.getCellphone();
//							String pwdScrect=DigestUtils.md5Hex(pwdValue.concat("sgappkey"));
//							UserPreferencesUtil.setCellPhone(activity, cellphone);
//							params.add("telephone=" + cellphone);
//							params.add("loginPwd="+pwdScrect);
//							params.add("token="+UserPreferencesUtil.getUserToken(activity));
//
//							UserPreferencesUtil.setPwd(activity, pwdScrect);
//							ResponseCommitNewPwd.super.run(methodname, params, aparam);
//						}else{
//							Toast.makeText(aparam.getActivity(), "两次输入密码不一致",
//									Toast.LENGTH_SHORT).show();
//						}
//					} else {
//						Toast.makeText(aparam.getActivity(), "密码不能为空",
//								Toast.LENGTH_SHORT).show();
//					}
//
//				} else {
//					Toast.makeText(aparam.getActivity(), "密码长度在6-16位",
//							Toast.LENGTH_SHORT).show();
//				}
//		} else {
//			Toast.makeText(aparam.getActivity(), "密码不能为空",
//					Toast.LENGTH_SHORT).show();
//		}

		return null;
	}
}