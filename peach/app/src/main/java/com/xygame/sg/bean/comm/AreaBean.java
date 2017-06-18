/*
 * 文 件 名:  CityBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月22日
 */
package com.xygame.sg.bean.comm;

import com.xygame.sg.task.utils.AssetDataBaseManager;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月22日
 * @action  [城市实体]
 */
public class AreaBean extends AssetDataBaseManager.CityBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String cityName,cityCode;

	private boolean isSelect;

	public boolean isSelect() {
		return isSelect;
	}

	public void setIsSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getCityName() {
		get();
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		get();
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void get(){
		setCityName(super.getName());
		setCityCode(getId()+"");
	}
}
