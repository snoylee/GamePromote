/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.indivual;

import android.widget.Toast;

import com.xygame.sg.activity.notice.bean.NoticeListBean;
import com.xygame.sg.activity.notice.bean.NoticeListVo;
import com.xygame.sg.activity.personal.CollectActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.ShowMsgDialog;

import java.util.Date;
import java.util.List;

import base.action.Action.Param;


public class QueryUserCollectNotices extends NetWorkUtil {
	
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
			String resStr = aparam.getResultunit().get("record").toString();
			CollectActivity activity = (CollectActivity) aparam.getActivity();
			if(resStr != "null"){
//				String jsonStr = JSON.toJSONString(aparam.getResultunit().get("record"));
//				List<NoticeListVo> noticeList = JSON.parseArray(jsonStr, NoticeListVo.class);
//				NoticeListBean resultNoticeList = new NoticeListBean();
//				if (noticeList != null && noticeList.size()>0){
//					resultNoticeList.setNotices(noticeList);
//					resultNoticeList.setReqTime(new Date().getTime());
//					activity.responseNoticeList(resultNoticeList);
//				} else {
//					activity.responseNoticeList(null);
//				}

			} else {

			}
		}else {
			String msg=aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		ShowMsgDialog.showNoMsg(aparam.getActivity(), false);
		return super.run(methodname, params, aparam);
	}
}
