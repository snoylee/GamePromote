/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.personal;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xygame.sg.activity.personal.EditDetailRecordActivity;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [模特编辑头像任务]
 */
public class EditorUserResumeTask extends NetWorkUtil {
	
	private EditDetailRecordActivity activity;
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
		// TODO Auto-generated method stub
		ShowMsgDialog.cancel();
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			activity.finishEditor();
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
		activity = ((EditDetailRecordActivity) aparam.getActivity());
		String imageStr=getJsonStr(activity.getResumeBean());
		params.add("userId="+UserPreferencesUtil.getUserId(activity));
		params.add("editResumes="+imageStr);
		ShowMsgDialog.show(activity, "请求中...", false);
		return super.run(methodname, params, aparam);
	}
	
	private String getJsonStr(RsumeBean it){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			JSONObject obj=new JSONObject();
			obj.put("startDate", it.getStartTime());
			obj.put("endDate",it.getEndTime());
			obj.put("experDesc", it.getExperDesc());
			obj.put("id",it.getResumeId());
			obj.put("locIndex",it.getLocIndex());
			jsonArray.put(obj);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}
}
