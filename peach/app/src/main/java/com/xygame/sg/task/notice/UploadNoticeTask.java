/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xygame.sg.activity.notice.PlushNoticeBrowersActivity;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeAreaBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.bean.comm.PhotoesBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [模特编辑头像任务]
 */
public class UploadNoticeTask extends NetWorkUtil {
	
	private PlushNoticeBrowersActivity activity;
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
			String noticeId = aparam.getResultunit().getRawMap().get("record");
			activity.finishUpload(noticeId);
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
		activity = ((PlushNoticeBrowersActivity) aparam.getActivity());
//		String imageStr=getPics(activity.getUploadImages());
		String baseNotice=getBaseNotice(activity.getPnBean());
		String shoot=getShoot(activity.getPnBean());
		String  recruits =getRecruits(activity.getPnBean());
		params.add("baseNotice="+baseNotice);
		params.add("shoot="+shoot);
//		params.add("pics="+imageStr);
		params.add("recruits="+recruits);
		ShowMsgDialog.show(activity, "提交中...", false);
		return super.run(methodname, params, aparam);
	}
	
	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param pnBean
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private String getShoot(PlushNoticeBean pnBean) {
		String str=null;
		JSONObject obj=new JSONObject();
		try {
			obj.put("startTime",pnBean.getStarTime());
			obj.put("endTime",pnBean.getEndTime());
			PlushNoticeAreaBean area=pnBean.getCameraArea();
			obj.put("addrProvince",area.getProvinceId());
			obj.put("addrCity",area.getCityId());
			if(area.getAddress()!=null){
				obj.put("address",area.getAddress());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		str=obj.toString();
		return str;
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param pnBean
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private String getBaseNotice(PlushNoticeBean pnBean) {
		String str=null;
		JSONObject obj=new JSONObject();
		try {
			obj.put("shootType",pnBean.getCameraParantTypeId());
			obj.put("noticeType",pnBean.getNoticeType());
			obj.put("subject",pnBean.getCameraTheme());
			if(pnBean.getReportTime()!=null){
				obj.put("joinEndTime", pnBean.getReportTime());
			}
			if(pnBean.getCamerNum()!=null){
				obj.put("pgrapherCount", pnBean.getCamerNum());
			}
			if(pnBean.getNoticeTip()!=null){
				obj.put("remark", pnBean.getNoticeTip());
				obj.put("remarkType", 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		str=obj.toString();
		return str;
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private String getPics(List<PhotoesBean> datas) {
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			for(PhotoesBean it:datas){
				JSONObject obj=new JSONObject();
				obj.put("picUrl", it.getImageUrl());
				jsonArray.put(obj);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}

	private String getRecruits(PlushNoticeBean pnBean){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		List<ModelRequestBean> datas=pnBean.getModelBeans();
		try {
			for(ModelRequestBean it:datas){
				JSONObject obj=new JSONObject();
				obj.put("gender", it.getSexId());
				obj.put("count", it.getNeedNum());
				obj.put("reward", String.valueOf(new Float(Float.parseFloat(it.getNeedPrice())*100).intValue()));
				if(it.getCountryName()!=null){
					obj.put("country", it.getCountryId());
				}
				if(it.getProvinceName()!=null){
					obj.put("province", it.getProvinceId());
				}
				if(it.getCityName()!=null){
					obj.put("city", it.getCityId());
				}
				if(it.getSmallAge()!=null){
					obj.put("minAge", it.getSmallAge());
				}
				if(it.getBigAge()!=null){
					obj.put("maxAge", it.getBigAge());
				}
				if(it.getSmallBodyHight()!=null){
					obj.put("minHeight", it.getSmallBodyHight());
				}
				if(it.getBigBodyHight()!=null){
					obj.put("maxHeight", it.getBigBodyHight());
				}
				if(it.getSmallWeight()!=null){
					obj.put("minWeight", it.getSmallWeight());
				}
				if(it.getBigWeight()!=null){
					obj.put("maxWeight", it.getBigWeight());
				}
				if(it.getSmallXiongWei()!=null){
					obj.put("minBust", it.getSmallXiongWei());
				}
				if(it.getBigXiongWei()!=null){
					obj.put("maxBust", it.getBigXiongWei());
				}
				if(it.getSmallYaoWei()!=null){
					obj.put("minWaist", it.getSmallYaoWei());
				}
				if(it.getBigYaoWei()!=null){
					obj.put("maxWaist", it.getBigYaoWei());
				}
				if(it.getSmallTunWei()!=null){
					obj.put("minHip", it.getSmallTunWei());
				}
				if(it.getBigTunWei()!=null){
					obj.put("maxHip", it.getBigTunWei());
				}
				if(it.getSamllCupId()!=null){
					obj.put("minCup", it.getSamllCupId());
				}
				if(it.getBigCupId()!=null){
					obj.put("maxCup", it.getBigCupId());
				}
				if(it.getSmallShoese()!=null){
					obj.put("minShoescode", it.getSmallShoese());
				}
				if(it.getBigShoese()!=null){
					obj.put("maxShoescode", it.getBigShoese());
				}
				if(it.isBaoXiaoCaiLv()){
					obj.put("affordTravelFee", "1");
				}else{
					obj.put("affordTravelFee","2");
				}
				if(it.isBaoXiaoZhuSu()){
					obj.put("affordAccomFee", "1");
				}else{
					obj.put("affordAccomFee", "2");
				}
				if(it.getBeizhuStr()!=null){
					obj.put("remark", it.getBeizhuStr());
				}
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
