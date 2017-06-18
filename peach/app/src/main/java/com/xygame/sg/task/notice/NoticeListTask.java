/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import java.util.List;
import java.util.Map;

import com.xygame.sg.activity.notice.NoticeManagmentActivity;
import com.xygame.sg.activity.personal.CMPersonInfoActivity;
import com.xygame.sg.activity.personal.PersonInfoActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.content.Intent;
import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [模特编辑头像任务]
 */
public class NoticeListTask extends NetWorkUtil {
	
	private NoticeManagmentActivity activity;
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
	 * @param aparam
	 * @param object
	 */
	@Override
	public void callback(Param aparam, Object object) {
		ShowMsgDialog.cancel();
		// TODO Auto-generated method stub
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			List<Map> map=(List<Map>) aparam.getResultunit().get("record");
			activity.getNoticeList(map);
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
	public Object run(String methodname, List<String> params, Param aparam) {
		// TODO Auto-generated method stub
		activity = ((NoticeManagmentActivity) aparam.getActivity());
		String userId=UserPreferencesUtil.getUserId(activity);
		int status=activity.getLoadFlag();
		params.add("userId="+userId);
		params.add("status="+status);
		 String typeStr = UserPreferencesUtil.getUserType(activity);
         if (Constants.CARRE_MODEL.equals(typeStr)|| Constants.PRO_MODEL.equals(typeStr)) {
        	 params.add("type=0");
         } else if (Constants.CARRE_PHOTOR.equals(typeStr)) {
        	 params.add("type=1");
         }
		ShowMsgDialog.show(activity, "数据加载中...", true);
		return super.run(methodname, params, aparam);
	}
}
