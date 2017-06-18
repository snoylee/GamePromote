/*
 * 文 件 名:  CreatePhotoesTask.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月21日
 */
package com.xygame.sg.task.personal;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xygame.sg.activity.personal.CreateModelPicActivity;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import base.action.Action.Param;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月21日
 * @action  [模特创建作品任务]
 */
public class CreatePhotoesTask extends NetWorkUtil {
	
	private CreateModelPicActivity activity;
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
			activity.finishUpload();
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
		activity = ((CreateModelPicActivity) aparam.getActivity());
//		String oral=activity.getOral();
		String imageStr=getJsonStr(activity.getUploadImages());
		params.add("userId="+UserPreferencesUtil.getUserId(activity));
//		params.add("galDesc="+oral);
		params.add("pics="+imageStr);
		ShowMsgDialog.show(activity, "提交中...", false);
		return super.run(methodname, params, aparam);
	}
	
	private String getJsonStr(List<PhotoesBean> datas){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			for(PhotoesBean it:datas){
				JSONObject obj=new JSONObject();
				obj.put("resUrl", it.getImageUrl());
				obj.put("uucode", Integer.parseInt(it.getImageIndex()));
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
