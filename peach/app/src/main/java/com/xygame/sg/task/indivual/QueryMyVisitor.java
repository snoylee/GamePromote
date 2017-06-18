/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.indivual;

import android.widget.Toast;

import com.xygame.sg.activity.personal.bean.QueryRecentListBean;
import com.xygame.sg.activity.personal.bean.UserSeeHistoryView;
import com.xygame.sg.activity.personal.fragment.RecentBaseFragment;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.List;

import base.action.Action.Param;


public class QueryMyVisitor extends NetWorkUtil {
	
//	private NoticeFragment noticeFragment;

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
//			String resStr = aparam.getResultunit().get("record").toString();
//			RecentBaseFragment fragment = (RecentBaseFragment) aparam.getFragment();
//			String jsonStr = JSON.toJSONString(aparam.getResultunit().get("record"));
//			UserSeeHistoryView seeHistoryView = JSON.parseObject(jsonStr, UserSeeHistoryView.class);
//			fragment.responseRecentList(seeHistoryView);
		}else {
			String msg=aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		ShowMsgDialog.showNoMsg(aparam.getActivity(), false);
		RecentBaseFragment fragment = ((RecentBaseFragment) aparam.getFragment());
		QueryRecentListBean queryBean = fragment.getQueryBean();

		params.add("qtype=" + queryBean.getQtype());
//		params.add("page=" + JSON.toJSONString(queryBean.getPage()));
		params.add("firstAccess="+queryBean.getFirstAccess());
		params.add("reqTime="+queryBean.getReqTime());
		params.add("lastReadTime="+queryBean.getLastReadTime());

		return super.run(methodname, params, aparam);
	}
}
