/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.news;

import android.widget.Toast;

import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.ShowDynamicActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.action.Action.Param;


public class RefuseActionForCamrer extends NetWorkUtil {
	private ShowDynamicActivity activity;
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
		ShowMsgDialog.cancel();
		// TODO Auto-generated method stub
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
//			activity.refuseSucess();
		}else {
			String msg=aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT)
					.show();
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
		activity = ((ShowDynamicActivity) aparam.getActivity());
		SGNewsBean sgNewsBean=activity.getSGNewsBean();
		String ext=sgNewsBean.getNoticeSubject();
		String memIds=null;
		try{
			JSONObject obj=new JSONObject(ext);
			memIds= obj.getString("memRecordId");
		}catch (Exception e){
			e.printStackTrace();
		}
		params.add("memRecordId="+memIds);
		ShowMsgDialog.show(activity, "请求中...", false);
		return super.run(methodname, params, aparam);
	}
}
