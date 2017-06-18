/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import android.widget.Toast;

import com.xygame.sg.activity.notice.SearchNoticeActivity;
import com.xygame.sg.activity.notice.bean.SearchNoticesListBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.List;
import java.util.Map;

import base.action.Action.Param;


public class SearchNotices extends NetWorkUtil {
	

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
		SearchNoticeActivity searchNoticeActivity = ((SearchNoticeActivity) aparam.getActivity());
		searchNoticeActivity.getListView().onRefreshComplete();
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			if (aparam.getResultunit().get("record")!= null){
				String resStr = aparam.getResultunit().get("record").toString();
				if(resStr != "null"){
					Map map = (Map) (aparam.getResultunit().get("record"));
//					String jsonStr =  JSON.toJSONString(map,SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteMapNullValue);
//					NoticeListBean noticeList = JSON.parseObject(jsonStr, NoticeListBean.class);
//					JSON.parseObject(jsonStr, NoticeListBean.class);
//					searchNoticeActivity.responseNoticeList(noticeList);
				} else {
					searchNoticeActivity.responseNoticeList(null);
				}
			} else {
				searchNoticeActivity.responseNoticeList(null);
			}
		}else {
			String msg=aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		ShowMsgDialog.showNoMsg(aparam.getActivity(),false);
		SearchNoticeActivity searchNoticeActivity = ((SearchNoticeActivity) aparam.getActivity());
		SearchNoticesListBean searchNoticesListBean = searchNoticeActivity.getSearchNoticesListBean();

//		params.add("page=" + JSON.toJSONString(searchNoticesListBean.getPage()));
		params.add("utype="+searchNoticesListBean.getUtype());
		params.add("reqtime="+searchNoticesListBean.getReqtime());
		params.add("content="+searchNoticesListBean.getContent());
		return super.run(methodname, params, aparam);
	}
}
