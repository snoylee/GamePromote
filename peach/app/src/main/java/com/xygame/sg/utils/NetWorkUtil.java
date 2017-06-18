/*
 * 文 件 名:  LoginTask.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月4日
 */
package com.xygame.sg.utils;

import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.List;

import base.action.Action;
import base.action.Action.Param;
import base.action.task.Http;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月4日
 * @action  [请添加内容描述]
 */
public class NetWorkUtil extends Http{


	@Override
	public void callback(Param aparam, Object object) {
		String msg = aparam.getResultunit().getString("msg");
//		if(!msg.contains("成功")){
//			Toast.makeText(aparam.getActivity(),msg,Toast.LENGTH_SHORT).show();
//		}
		super.callback(aparam, object);
	}

	/**
	 * 重载方法
	 * @param connection
	 * @throws ProtocolException
	 */
	@Override
	public void runConnection(HttpURLConnection connection)
			throws ProtocolException {
		super.runConnection(connection);
	}

	/**
	 * 重载方法
	 * @param result
	 * @return
	 */
	@Override
	public String runResult(String result) {

		// TODO Auto-generated method stub
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
		return super.runUrl(url);
	}

	/**
	 * 重载方法
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
