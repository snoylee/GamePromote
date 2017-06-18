/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.indivual;

import android.widget.Toast;

import com.xygame.sg.activity.personal.SettingNoticeActivity;
import com.xygame.sg.activity.personal.bean.NotifySettingBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;

import java.util.List;

import base.action.Action.Param;


public class QueryNotifySetting extends NetWorkUtil {
	

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
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {

//			String resStr = aparam.getResultunit().get("record").toString();
//			SettingNoticeActivity activity = (SettingNoticeActivity) aparam.getActivity();
//			String jsonStr = JSON.toJSONString(aparam.getResultunit().get("record"));
//			NotifySettingBean bean = JSON.parseObject(jsonStr, NotifySettingBean.class);
//			activity.responseQuerySetting(bean);
		} else {
			String msg=aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
		super.callback(aparam, object);
	}


	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		return super.run(methodname, params, aparam);
	}
}