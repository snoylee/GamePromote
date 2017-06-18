/*
 * 文 件 名:  ResponseAliParams.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.init;

import java.util.List;
import java.util.Map;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action.Param;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.RegisterSecondPageActivity;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.SGApplication;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [请求阿里参数]
 */
public class ResponseAliParams extends NetWorkUtil {
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
			List<Map> map=(List<Map>) aparam.getResultunit().get("record");
			for(Map it:map){
				if("ali_oss_secret_id".equals(it.get("propName").toString())){
					String accessKey=it.get("propValue").toString();
					AliPreferencesUtil.setAccessKey(aparam.getActivity(), accessKey);
				}
				if("ali_oss_secret_key".equals(it.get("propName").toString())){
					String screctKey=it.get("propValue").toString();
					AliPreferencesUtil.setScrectKey(aparam.getActivity(), screctKey);
				}
				
				if("ali_oss_bucket_name".equals(it.get("propName").toString())){
					String buckekName=it.get("propValue").toString();
					AliPreferencesUtil.setBuckekName(aparam.getActivity(), buckekName);
				}
			}
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
		return super.run(methodname, params, aparam);
	}
}
