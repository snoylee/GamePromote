/*
 * 文 件 名:  TransferBrowersBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月30日
 */
package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月30日
 * @action  [功能描述]
 */
public class TransferBrowersBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private List<BrowerPhotoesBean> browersDatas;

	public List<BrowerPhotoesBean> getBrowersDatas() {
		return browersDatas;
	}

	public void setBrowersDatas(List<BrowerPhotoesBean> browersDatas) {
		this.browersDatas = browersDatas;
	}
}
