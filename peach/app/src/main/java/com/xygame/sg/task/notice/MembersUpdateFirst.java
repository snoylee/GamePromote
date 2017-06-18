/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.notice;

import android.widget.Toast;

import com.xygame.sg.activity.notice.bean.NoticeMemberUpdateFirstVo;
import com.xygame.sg.activity.notice.fragment.EnrollFragment;
import com.xygame.sg.activity.notice.fragment.TBDFragment;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;

import java.util.List;
import java.util.Map;

import base.action.Action.Param;


public class MembersUpdateFirst extends NetWorkUtil {
	

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
//		ShowMsgDialog.cancel();
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			Map resultMap=(Map) aparam.getResultunit().get("record");
//			String jsonStr = JSON.toJSONString(resultMap);
//			NoticeMemberUpdateFirstVo bean = JSON.parseObject(jsonStr, NoticeMemberUpdateFirstVo.class);
			if (aparam.getFragment() instanceof EnrollFragment){
				EnrollFragment fragment = ((EnrollFragment) aparam.getFragment());
//				fragment.responseMemberUpdateFirst(bean);
			} else if(aparam.getFragment() instanceof TBDFragment){
				TBDFragment fragment = ((TBDFragment) aparam.getFragment());
//				fragment.responseMemberUpdateFirst(bean);
			}

		}else {
			String msg=aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
//		ShowMsgDialog.showNoMsg(aparam.getFragment().getActivity(), false);
		return super.run(methodname, params, aparam);
	}
}
