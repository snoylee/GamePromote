/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import java.util.List;

import org.json.JSONObject;

import com.xygame.sg.bean.comm.CityBean;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.utils.BaiduPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.zxing.activity.MipcaActivityCapture;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [模特编辑头像任务]
 */
public class ModelSignTask extends NetWorkUtil {
	
	private MipcaActivityCapture activity;
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
			activity.finishUpload(true);
		}else{
			activity.finishUpload(false);
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
		activity = ((MipcaActivityCapture) aparam.getActivity());
		String zxingStr=activity.getResultString();
		try {
			JSONObject obj=new JSONObject(zxingStr);
			params.add("noticeId="+obj.getString("noticeId"));
			
			params.add("noticeId="+obj.getString("noticeId"));
			params.add("userId="+obj.getString("userId"));
			params.add("dimenLng="+obj.getString("dimenLng"));
			params.add("dimenLat="+obj.getString("dimenLat"));
			params.add("dimenProvince="+obj.getString("dimenProvince"));
			params.add("dimenCity="+obj.getString("dimenCity"));
			params.add("dimenAddress="+obj.getString("dimenAddress"));
			params.add("dimenCreateTime="+obj.getString("dimenCreateTime"));
			String signLng = BaiduPreferencesUtil.getLon(activity);
			String signLat = BaiduPreferencesUtil.getLat(activity);
			
			ProvinceBean pbBean=BaiduPreferencesUtil.getLocalProvinceBean(BaiduPreferencesUtil.getProvice(activity));
			CityBean ctBean=BaiduPreferencesUtil.getLocalCityBean(pbBean,  BaiduPreferencesUtil.getCity(activity));
			String signProvince = pbBean.getProvinceCode();
			String signCity =ctBean.getCityCode();
			
			String signAddress = BaiduPreferencesUtil.getStreet(activity);
			String signTime = String.valueOf(System.currentTimeMillis());
			params.add("signLng="+signLng);
			params.add("signLat="+signLat);
			params.add("signProvince="+signProvince);
			params.add("signCity="+signCity);
			params.add("signAddress="+signAddress);
			params.add("signTime="+signTime);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ShowMsgDialog.show(activity, "签到中...", false);
		return super.run(methodname, params, aparam);
	}
}
