/*
 * 文 件 名:  Md5Util.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.utils;

import java.util.List;

import base.action.Action.Param;
import base.action.Task;

import com.txr.codec.digest.DigestUtils;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月5日
 * @action  [请添加内容描述]
 */
public class Md5Util extends Task {

	/**
	 * 重载方法
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	@Override
	public Object run(String arg0, List<String> arg1, Param arg2) {
		// TODO Auto-generated method stub
		return DigestUtils.md5Hex(arg1.get(0).concat("sgappkey"));
	}

}
