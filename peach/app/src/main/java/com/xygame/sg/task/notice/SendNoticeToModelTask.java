/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import com.xygame.sg.activity.notice.InviteModelActivity;
import com.xygame.sg.activity.notice.bean.InviteBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [模特编辑头像任务]
 */
public class SendNoticeToModelTask extends NetWorkUtil {
	
	private InviteModelActivity activity;
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
			activity.finishSend();
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
		activity = ((InviteModelActivity) aparam.getActivity());
		params.add("curUserId=" + UserPreferencesUtil.getUserId(activity));
		params.add("userId="+activity.getModelId());
		params.add("noticeIds="+getNoticeIds(activity.getSelectedDatas()));
		ShowMsgDialog.show(activity, "通告请求发送中...", false);
		return super.run(methodname, params, aparam);
	}

	private List<Long> getNoticeIds(List<InviteBean> selectedDatas) {
		List<Long> datas=new ArrayList<>();
		for (InviteBean item:selectedDatas){
			datas.add(Long.parseLong(item.getNoticeId()));
		}
		return datas;
	}
}
