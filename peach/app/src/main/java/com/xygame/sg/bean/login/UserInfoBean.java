/*
 * 文 件 名:  UserInfoBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月4日
 */
package com.xygame.sg.bean.login;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月4日
 * @action  [请添加内容描述]
 */
public class UserInfoBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName,pwd,reslut;

	/**
	 * 获取 userName
	 * @return 返回 userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置 userName
	 * @param 对userName进行赋值
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取 pwd
	 * @return 返回 pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置 pwd
	 * @param 对pwd进行赋值
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * 获取 reslut
	 * @return 返回 reslut
	 */
	public String getReslut() {
		return reslut;
	}

	/**
	 * 设置 reslut
	 * @param 对reslut进行赋值
	 */
	public void setReslut(String reslut) {
		this.reslut = reslut;
	}
	
	

}
