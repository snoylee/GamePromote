/*
 * 文 件 名:  EditorUserBirthday.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月21日
 */
package com.xygame.sg.task.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.activity.personal.SelectJobTypeActivity;
import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import base.action.Action.Param;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月21日
 * @action  [用户日期修改]
 */
public class LoadUserCarrier extends NetWorkUtil {
	
	private SelectJobTypeActivity activity;
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
			List<CarrierBean> datas=new ArrayList<CarrierBean>();
			List<Map> map=(List<Map>) aparam.getResultunit().get("record");
			for(Map it:map){
				CarrierBean item=new CarrierBean();
				item.setCarrierCode((String)it.get("exclusType"));
				item.setCarrierName((String)it.get("typeName"));
				item.setTypeId((String)it.get("typeId"));
				datas.add(item);
			}
			activity.setDatas(datas);
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
		activity = ((SelectJobTypeActivity) aparam.getActivity());
		ShowMsgDialog.show(activity, "数据加载中...", false);
		return super.run(methodname, params, aparam);
	}
}