/*
 * 文 件 名:  ResponseRegister.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.login;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.DoUserInfoActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [注册请求]
 */
public class JiHuoUser extends NetWorkUtil {
	private EditText nickName;
	private TextView birthdayView;
	private DoUserInfoActivity activity;

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
			List<Map> listMap=(List<Map>) map.get("userTypes");
			for(Map it:listMap){
				String utype=it.get("utype").toString();
				UserPreferencesUtil.setUserType(activity, utype);
				String status=it.get("status").toString();
				UserPreferencesUtil.setUserVerifyStatus(activity, status);
			}
			activity.closePage(true);
		} else {
			ShowMsgDialog.cancel();
			UserPreferencesUtil.setIsOnline(activity, false);
			String msg = aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
			activity.closePage(false);
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
		activity = ((DoUserInfoActivity) aparam.getActivity());
		nickName = (EditText) aparam.getActivity().findViewById(R.id.nickName);
		birthdayView = (TextView) aparam.getActivity().findViewById(R.id.birthdayView);
		String nickNameValue = nickName.getText().toString().trim();
		String birthDayVaule = birthdayView.getText().toString().trim();
		String sexValue = activity.getSexType();
		String carrieValue = activity.getCarrieType();
		String picValue = activity.getPhotoPath();
		if (!"".equals(nickNameValue)) {
			if (!"".equals(birthDayVaule)) {
				String typeIndex = UserPreferencesUtil.getUserLoginType(activity);
				UserPreferencesUtil.setSex(activity, sexValue);
				UserPreferencesUtil.setBirthday(activity, getTime(birthDayVaule));
				UserPreferencesUtil.setHeadPic(activity, picValue);
				UserPreferencesUtil.setUserNickName(activity, nickNameValue);
				UserPreferencesUtil.setUserType(activity, carrieValue);
				if ("2".equals(typeIndex)) {
					String loginName = "weixin_".concat(UserPreferencesUtil.getOtherPlatfromId(activity));
					String userId = UserPreferencesUtil.getUserId(activity);
					params.add("userId=" + userId);
					params.add("userIcon=" + picValue);
					params.add("loginName=" + loginName);
					params.add("gender=" + sexValue);
					params.add("birthday=" + getTime(birthDayVaule));
					ShowMsgDialog.show(activity, "激活中...", false);
					JiHuoUser.super.run(methodname, params, aparam);
				} else if ("3".equals(typeIndex)) {
					String loginName = "weibo_".concat(UserPreferencesUtil.getOtherPlatfromId(activity));
					String userId = UserPreferencesUtil.getUserId(activity);
					params.add("userId=" + userId);
					params.add("userIcon=" + picValue);
					params.add("loginName=" + loginName);
					params.add("gender=" + sexValue);
					params.add("birthday=" + getTime(birthDayVaule));
					ShowMsgDialog.show(activity, "激活中...", false);
					JiHuoUser.super.run(methodname, params, aparam);
				} else if ("4".equals(typeIndex)) {
					String loginName = "qq_".concat(UserPreferencesUtil.getOtherPlatfromId(activity));
					String userId = UserPreferencesUtil.getUserId(activity);
					params.add("userId=" + userId);
					params.add("userIcon=" + picValue);
					params.add("loginName=" + loginName);
					params.add("gender=" + sexValue);
					params.add("birthday=" + getTime(birthDayVaule));
					ShowMsgDialog.show(activity, "激活中...", false);
					JiHuoUser.super.run(methodname, params, aparam);
				} else if ("1".equals(typeIndex)) {
					String cellphone = UserPreferencesUtil.getCellPhone(activity);
					String userId = UserPreferencesUtil.getUserId(activity);
					params.add("userId=" + userId);
					params.add("userIcon=" + picValue);
					params.add("loginName=" + "phone_".concat(cellphone));
					params.add("gender=" + sexValue);
					params.add("birthday=" + getTime(birthDayVaule));
					ShowMsgDialog.show(activity, "激活中...", false);
					JiHuoUser.super.run(methodname, params, aparam);
				}

			} else {
				Toast.makeText(aparam.getActivity(), "请选择生日", Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(aparam.getActivity(), "请输入昵称", Toast.LENGTH_SHORT).show();
		}

		return null;
	}

	public String getTime(String arg) {
		String str = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date date = df.parse(arg);
			long timeStemp = date.getTime();
			str = String.valueOf(timeStemp);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return str;
	}
}
