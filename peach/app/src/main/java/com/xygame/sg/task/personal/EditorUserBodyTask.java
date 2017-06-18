/*
 * 文 件 名:  EditorUserBodyTask.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月21日
 */
package com.xygame.sg.task.personal;

import java.util.List;

import com.xygame.sg.activity.personal.EditorBodyInfoActivity;
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
 * @action  [编辑用户身体信息任务]
 */
public class EditorUserBodyTask extends NetWorkUtil {
	
	private EditorBodyInfoActivity activity;
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
			activity.finishActivity();
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
		activity = ((EditorBodyInfoActivity) aparam.getActivity());
		ShowMsgDialog.showNoMsg(activity,  false);
		params.add("userId="+UserPreferencesUtil.getUserId(activity));
		params.add("height="+activity.getUserHeight());
		params.add("weight="+activity.getUserWeight());
		params.add("bust="+activity.getUserBust());
		params.add("waist="+activity.getUserWaist());
		params.add("hip="+activity.getUserHip());
		int index=0;
		String[] scope = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I",
		"J" };
		for(int i=0;i<scope.length;i++){
			if(scope[i].equals(activity.getUserCup())){
				index=i+1;
			}
		}
		params.add("cup="+String.valueOf(index));
		params.add("shoesCode="+activity.getUserShoesCode());
		return super.run(methodname, params, aparam);
	}
}
