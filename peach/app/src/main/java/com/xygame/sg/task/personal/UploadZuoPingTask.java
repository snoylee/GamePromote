/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.personal;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xygame.sg.activity.personal.PersonalPhotoesActivity;
import com.xygame.sg.bean.comm.PhotoesSubBean;
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
public class UploadZuoPingTask extends NetWorkUtil {
	
	private PersonalPhotoesActivity activity;
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
			List<Map> map=(List<Map>) aparam.getResultunit().get("record");
			activity.finishUpload(map);
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
		activity = ((PersonalPhotoesActivity) aparam.getActivity());
		String imageStr=getJsonStr(activity.getNewImages());
		params.add("userId="+UserPreferencesUtil.getUserId(activity));
		params.add("galId="+activity.getGlaryId());
		params.add("appendPics="+imageStr);
		return super.run(methodname, params, aparam);
	}
	
	private String getJsonStr(List<PhotoesSubBean> datas){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			for(PhotoesSubBean it:datas){
				JSONObject obj=new JSONObject();
				obj.put("resUrl", it.getUrl());
				obj.put("uucode", it.getItemIndex());
				obj.put("lengthPx", it.getLengthPx());
				obj.put("widthPx", it.getWidthPx());
				obj.put("picSize", it.getPicSize());
				jsonArray.put(obj);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}
}
