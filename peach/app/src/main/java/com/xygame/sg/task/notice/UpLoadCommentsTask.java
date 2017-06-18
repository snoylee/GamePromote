/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import java.util.List;
import java.util.Map;

import com.xygame.sg.activity.notice.CommentActivity;
import com.xygame.sg.activity.notice.bean.NoticeStatusBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [模特编辑头像任务]
 */
public class UpLoadCommentsTask extends NetWorkUtil {
	
	private CommentActivity activity;
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
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			activity.finishComments();
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
		activity = ((CommentActivity) aparam.getActivity());
		NoticeStatusBean item=activity.getBeanInfo();
		String noticeId=activity.getnoticeId();
		String fromUserId=UserPreferencesUtil.getUserId(activity);
		String toUserId="0";
		if(item!=null){
			toUserId=item.getUserId();
		}
		String picLevel=String.valueOf(new Float(activity.getfuheRatingBar()).intValue());
		String experienceLevel=String.valueOf(new Float(activity.getzhuanyeRatingBar()).intValue());
		String coordinateLevel=String.valueOf(new Float(activity.getpeiheRatingBar()).intValue());
		String evalContent=String.valueOf(activity.getContent());
		params.add("noticeId="+noticeId);
		params.add("fromUserId="+fromUserId);
		params.add("toUserId="+toUserId);
		
		params.add("picLevel="+picLevel);
		params.add("experienceLevel="+experienceLevel);
		params.add("coordinateLevel="+coordinateLevel);
		params.add("evalContent="+evalContent);
		return super.run(methodname, params, aparam);
	}
}
