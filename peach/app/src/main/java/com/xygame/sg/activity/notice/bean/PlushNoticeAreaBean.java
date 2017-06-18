/*
 * 文 件 名:  PlushNoticeAreaBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年12月7日
 */
package com.xygame.sg.activity.notice.bean;

import com.xygame.sg.bean.comm.CityBean;

import java.io.Serializable;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年12月7日
 * @action  [功能描述]
 */
public class PlushNoticeAreaBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String provinceId,provinceName,cityId,cityName,address;

	private List<CityBean> cityAreas;

	public List<CityBean> getCityAreas() {
		return cityAreas;
	}

	public void setCityAreas(List<CityBean> cityAreas) {
		this.cityAreas = cityAreas;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
