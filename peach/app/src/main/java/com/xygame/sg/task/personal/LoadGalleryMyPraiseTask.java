/*
 * 文 件 名:  ResponseLogin.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.personal;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.xygame.sg.activity.personal.PrisePersonActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import android.widget.Toast;
import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [请添加内容描述]
 */
public class LoadGalleryMyPraiseTask extends NetWorkUtil {

	private PrisePersonActivity activity;

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
	 * @param aparam
	 * @param object
	 */
	@Override
	public void callback(Param aparam, Object object) {
		// TODO Auto-generated method stub
		ShowMsgDialog.cancel();
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			Map map=(Map) aparam.getResultunit().get("record");
			activity.parseMyDatasRefresh(map);
		} else {
			String msg=aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT)
					.show();
		}
		super.callback(aparam, object);
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
	 * @param methodname
	 * @param params
	 * @param aparam
	 * @return
	 */
	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		// TODO Auto-generated method stub
		activity = ((PrisePersonActivity) aparam.getActivity());
		String galId=activity.getGlaryId();
		String seeUserId=activity.getSeeUserId();
		String imageId=activity.getImageId();
		String typeFlag=activity.getTypeFlag();
		params.add("resId="+imageId);
		params.add("userId="+seeUserId);
		params.add("galId="+galId);
		params.add("page="+getPage());
		params.add("type="+typeFlag);
		ShowMsgDialog.showNoMsg(activity, false);
		return super.run(methodname, params, aparam);
	}
	
	private String getPage(){
		String str=null;
		try {
			JSONObject ob=new JSONObject();
			ob.put("pageIndex", "1");
			ob.put("pageSize", "10000");
			str=ob.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return str;
	}
}
