/*
 * 文 件 名:  CountryBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月22日
 */
package com.xygame.sg.bean.comm;

import com.xygame.sg.task.utils.AssetDataBaseManager;

import java.io.Serializable;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月22日
 * @action  [省份实体]
 */
public class ProvinceBean extends AssetDataBaseManager.CityBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String provinceName,provinceCode;

	private  List<CityBean> cityAreaDatas;

	private CityBean cityBean;

	public CityBean getCityBean() {
		return cityBean;
	}

	public void setCityBean(CityBean cityBean) {
		this.cityBean = cityBean;
	}

	public String getProvinceName() {
		return provinceName;
	}


	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public List<CityBean> getCityAreaDatas() {
		return cityAreaDatas;
	}

	public void setCityAreaDatas(List<CityBean> cityAreaDatas) {
		this.cityAreaDatas = cityAreaDatas;
	}

	public void get(){
		setProvinceName(getName());
		setProvinceCode(getId()+"");
	}
}
