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
import com.xygame.sg.activity.personal.CreateHeadPicActivity;
import com.xygame.sg.activity.personal.bean.IdentyBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [模特编辑头像任务]
 */
public class UploadHeadPic extends NetWorkUtil {
	boolean hasVideo = false;
	boolean isUpdate = false;
	
	private CreateHeadPicActivity activity;
	/**
	 * 重载方法
	 * 
	 * @param result
	 * @return
	 */
	@Override
	public String runResult(String result) {
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
		activity = ((CreateHeadPicActivity) aparam.getActivity());
		String imageStr=getJsonStr(activity.getUploadImages());
		params.add("userId="+UserPreferencesUtil.getUserId(activity));
		params.add("logos=" + imageStr);
		if (hasVideo){
			String videoStr=getVideoJsonStr(activity.getUploadImages());
			params.add("appendVideos=" + videoStr);
			IdentyBean identyBean=getIdentyBean();
			if (identyBean!=null){
				int authStatus = identyBean.getAuthStatus();
				int userType = identyBean.getUserType();

				params.add("userType=" + userType);
				params.add("authStatus=" + authStatus);
			}
		}

		if (isUpdate){
			String videoStrId=getVideoJsonIdStr(activity.getUploadImages());
			params.add("delVideos=" + videoStrId);
		}
		ShowMsgDialog.show(activity, "请求中...", false);
		return super.run(methodname, params, aparam);
	}

	private IdentyBean getIdentyBean(){
		IdentyBean identyBean=null;
//		String jsonStr = UserPreferencesUtil.getUserTypeJsonStr(activity);
//		if (!StringUtils.isEmpty(jsonStr)){
//			List<IdentyBean> resultList = JSON.parseArray(jsonStr, IdentyBean.class);
//			if (resultList == null || resultList.size() == 0) {
//				return null;
//			}
//			identyBean = resultList.get(0);
//		}
		return identyBean;
	}
	
	private String getJsonStr(List<PhotoesBean> datas){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			for(PhotoesBean it:datas){
				if (it.getType() == 0){
					JSONObject obj=new JSONObject();
					obj.put("logoUrl", it.getImageUrl());
					obj.put("locIndex", Integer.parseInt(it.getImageIndex()));
					jsonArray.put(obj);
				} else if (it.getType() == 1){
					hasVideo = true;
					if (!StringUtils.isEmpty(it.getLastVideoId())){
						isUpdate = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}

	private String getVideoJsonStr(List<PhotoesBean> datas){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			for(PhotoesBean it:datas){
				if (it.getType() == 1){
					JSONObject obj=new JSONObject();
					obj.put("videoUrl", it.getImageUrl());
					obj.put("videoSize", it.getVideoSize());
					obj.put("videoTime", it.getVideoTime());
					jsonArray.put(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}

	private String getVideoJsonIdStr(List<PhotoesBean> datas){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			for(PhotoesBean it:datas){
				if (it.getType() == 1){
					JSONObject obj=new JSONObject();
					obj.put("id", it.getLastVideoId());
					jsonArray.put(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}
}
