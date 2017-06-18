/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import com.xygame.sg.utils.NetWorkUtil;

import java.util.List;

import base.action.Action.Param;


public class QueryNotices extends NetWorkUtil {
	
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
//		NoticeFragment noticeFragment = ((NoticeFragment) aparam.getFragment());
//		if (noticeFragment.isShowLoading()){
//			ShowMsgDialog.cancel();
//		}
//		String resultCode = aparam.getResultunit().getRawMap().get("success");
//		if (Constants.RESULT_CODE.equals(resultCode)) {
//			String resStr = aparam.getResultunit().get("record").toString();
//			if(resStr != "null"){
//				String jsonStr = JSON.toJSONString((Map) (aparam.getResultunit().get("record")));
//				NoticeListBean noticeList = JSON.parseObject(jsonStr, NoticeListBean.class);
//				noticeFragment.responseNoticeList(noticeList);
//			} else {
//				noticeFragment.responseNoticeList(null);
//			}
//		}else {
//			String msg=aparam.getResultunit().getRawMap().get("msg");
//			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
//		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {

//		NoticeFragment noticeFragment = ((NoticeFragment) aparam.getFragment());
//		if (noticeFragment.isShowLoading()){
//			ShowMsgDialog.showNoMsg(aparam.getFragment().getActivity(), true);
//		}
//		QueryNoticesListBean queryNoticesListBean = noticeFragment.getQueryNoticesListBean();
//
//
//		params.add("page=" + JSON.toJSONString(queryNoticesListBean.getPage()));
//		params.add("shootType="+queryNoticesListBean.getShootType());
//		params.add("orderType="+queryNoticesListBean.getOrderType());
//		params.add("utype="+queryNoticesListBean.getUtype());
//		params.add("reqtime="+queryNoticesListBean.getReqtime());
//		params.add("cond="+JSON.toJSONString(queryNoticesListBean.getCond()));
		return super.run(methodname, params, aparam);
	}
}
