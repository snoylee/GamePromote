/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.indivual;

import android.widget.Toast;

import com.xygame.sg.activity.personal.EditSecondCategoryActivity;
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


public class CudModelPrice extends NetWorkUtil {
	
	private EditSecondCategoryActivity activity;

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
		ShowMsgDialog.showNoMsg(aparam.getActivity(), false);
		activity = ((EditSecondCategoryActivity) aparam.getActivity());
		PriceBean priceBean = activity.getPriceBean();
		if (priceBean.getItemName().equals("")){
			Toast.makeText(activity,"请选择拍摄小类",Toast.LENGTH_SHORT).show();
			ShowMsgDialog.cancel();
			return null;
		}
		if (priceBean.getPrice() == -1){
			Toast.makeText(activity,"请填写拍摄报价",Toast.LENGTH_SHORT).show();
			ShowMsgDialog.cancel();
			return null;
		} else if (priceBean.getPrice() == 0){
			Toast.makeText(activity,"拍摄报价请大于0元",Toast.LENGTH_SHORT).show();
			ShowMsgDialog.cancel();
			return null;
		}
		if (priceBean.getPriceUnit().equals("")){
			Toast.makeText(activity,"请选择报价单位",Toast.LENGTH_SHORT).show();
			ShowMsgDialog.cancel();
			return null;
		}
		String imageStr=getJsonStr(priceBean);
		params.add("userId="+UserPreferencesUtil.getUserId(activity));

		if (priceBean.getId()==null){//新增
			params.add("appendPrices=" + imageStr);
		} else {//修改
			params.add("editPrices=" + imageStr);
		}
		return super.run(methodname, params, aparam);
	}
	
	private String getJsonStr(PriceBean priceBean){
		String str=null;
		JSONArray jsonArray=new JSONArray();
		try {
			JSONObject obj=new JSONObject();

			obj.put("itemName",priceBean.getItemName());
			obj.put("price", priceBean.getPrice());
			obj.put("priceUnit",priceBean.getPriceUnit());
			obj.put("limitParter",priceBean.getLimitParter());
			if (priceBean.getId()==null){//新增
				obj.put("uucode",priceBean.getUucode());
				obj.put("priceType", priceBean.getPriceType());
				obj.put("locIndex", priceBean.getLocIndex());
			} else {//修改
				obj.put("id",priceBean.getId());
				obj.put("locIndex", priceBean.getLocIndex());
			}

			jsonArray.put(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		str=jsonArray.toString();
		return str;
	}
}
