/*
 * 文 件 名:  ResponseBaidu.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月4日
 */
package com.xygame.sg.task.baidu;

import java.util.List;

import android.widget.TextView;
import base.action.Action.Param;

import com.xygame.sg.R;
import com.xygame.sg.utils.NetWorkUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月4日
 * @action  [百度上传坐标返回]
 */
public class ResponseBaidu extends NetWorkUtil{
	
	/**
	 * 重载方法
	 * @param result
	 * @return
	 */
	@Override
	public String runResult(String result) {
		// TODO Auto-generated method stub
		String res=result;
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
