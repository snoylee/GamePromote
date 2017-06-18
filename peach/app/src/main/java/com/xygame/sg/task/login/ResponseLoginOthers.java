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

import android.widget.Toast;
import base.action.Action.Param;

import com.xygame.sg.activity.commen.LoginWelcomActivity;
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
public class ResponseLoginOthers extends NetWorkUtil {
	private LoginWelcomActivity activity;

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
		String resultCode = aparam.getResultunit().getRawMap().get("code");
		if ("0000".equals(resultCode)) {
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
			activity.closePage();
		} else if("1006".equals(resultCode)){
			ShowMsgDialog.cancel();
			activity.intoNextPage();
		}else{
			ShowMsgDialog.cancel();
//			String msg=aparam.getResultunit().getRawMap().get("msg");
//			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT)
//			.show();
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
		activity = ((LoginWelcomActivity) aparam.getActivity());
		ShowMsgDialog.show(activity, "登录中...", false);
		return super.run(methodname, params, aparam);
	}
}
