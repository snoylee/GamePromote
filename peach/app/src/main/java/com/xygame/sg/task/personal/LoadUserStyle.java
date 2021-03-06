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

import com.xygame.sg.activity.personal.EditorStyleActivity;
import com.xygame.sg.activity.personal.bean.StyleBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月21日
 * @action [用户日期修改]
 */
public class LoadUserStyle extends NetWorkUtil {

	private EditorStyleActivity activity;

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
	 * 
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
			List<StyleBean> datas=new ArrayList<StyleBean>();
//			{typeName=性感, exclusType=0, hueG=153, typeId=100, hueB=246, hueR=190}
			List<Map> map=(List<Map>) aparam.getResultunit().get("record");
			for(Map it:map){
				StyleBean bean=new StyleBean();
				bean.setColorB(Integer.parseInt((String)it.get("hueB")));
				bean.setColorG(Integer.parseInt((String)it.get("hueG")));
				bean.setColorR(Integer.parseInt((String)it.get("hueR")));
				bean.setStyleId((String) it.get("typeId"));
				bean.setStyleName((String) it.get("typeName"));
				bean.setSelected(0);
				datas.add(bean);
			}
			activity.refreshActivity(datas);
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
		activity = ((EditorStyleActivity) aparam.getActivity());
		String typeFlag=activity.getTypeFlag();
		if(typeFlag==null){
			if(Constants.SEX_WOMAN.equals(UserPreferencesUtil.getSex(activity))){
				params.add("exclus=0");
			}else if(Constants.SEX_MAN.equals(UserPreferencesUtil.getSex(activity))){
				params.add("exclus=1");
			}
		}else{
			if("male".equals(typeFlag)){
				params.add("exclus=1");
			}else{
				params.add("exclus=0");
			}
		}
		
		
		ShowMsgDialog.show(activity, "数据中...", false);
		return super.run(methodname, params, aparam);
	}
}