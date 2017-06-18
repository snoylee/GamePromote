/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.indivual;

import com.xygame.sg.activity.personal.DeleteCategoryActivity;
import com.xygame.sg.activity.personal.SortCategoryActivity;
import com.xygame.sg.activity.personal.bean.PriceBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import base.action.Action.Param;


public class OrderModelPrice extends NetWorkUtil {
	
	private SortCategoryActivity activity;

	@Override
	public String runResult(String result) {

		System.out.println(result);
		return super.runResult(result);
	}
	

	@Override
	public String runUrl(String url) {
		System.out.println(url);
		return super.runUrl(url);
	}


	@Override
	public void callback(Param aparam, Object object) {
		ShowMsgDialog.cancel();
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			List<Map> map=(List<Map>)aparam.getResultunit().get("record");
			activity.responseHandler(map);
		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		ShowMsgDialog.showNoMsg(aparam.getActivity(), true);
		activity = ((SortCategoryActivity) aparam.getActivity());
		params.add("userId="+UserPreferencesUtil.getUserId(activity));
		params.add("editPrices="+getJsonStr(activity.getOrderDatas()));
		return super.run(methodname, params, aparam);
	}

	private String getJsonStr(List<PriceBean> datas){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			for(PriceBean priceBean:datas){
				JSONObject obj=new JSONObject();
				obj.put("id", priceBean.getId());
				obj.put("itemName",priceBean.getItemName());
				obj.put("price", priceBean.getPrice());
				obj.put("priceUnit",priceBean.getPriceUnit());
				obj.put("limitParter",priceBean.getLimitParter());
				obj.put("locIndex", priceBean.getLocIndex());
				jsonArray.put(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}
}
