/*
 * 文 件 名:  ModelRequestBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年12月7日
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年12月7日
 * @action [功能描述]
 */
public class ModelRequestBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String _id,sexName, sexId, needNum, needPrice, provinceName, provinceId, cityName, cityId, countryName,
			countryId, smallAge, bigAge, smallBodyHight, bigBodyHight, smallWeight, bigWeight, smallXiongWei,
			bigXiongWei, smallYaoWei, bigYaoWei, smallTunWei, bigTunWei, smallCupName, samllCupId, bigCupName, BigCupId,
			smallShoese, bigShoese, beizhuStr;
	private boolean isBaoXiaoCaiLv, isBaoXiaoZhuSu;
	public String getSexName() {
		return sexName;
	}
	
	
	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getSexId() {
		return sexId;
	}
	public void setSexId(String sexId) {
		this.sexId = sexId;
	}
	public String getNeedNum() {
		return needNum;
	}
	public void setNeedNum(String needNum) {
		this.needNum = needNum;
	}
	public String getNeedPrice() {
		return needPrice;
	}
	public void setNeedPrice(String needPrice) {
		this.needPrice = needPrice;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getSmallAge() {
		return smallAge;
	}
	public void setSmallAge(String smallAge) {
		this.smallAge = smallAge;
	}
	public String getBigAge() {
		return bigAge;
	}
	public void setBigAge(String bigAge) {
		this.bigAge = bigAge;
	}
	public String getSmallBodyHight() {
		return smallBodyHight;
	}
	public void setSmallBodyHight(String smallBodyHight) {
		this.smallBodyHight = smallBodyHight;
	}
	public String getBigBodyHight() {
		return bigBodyHight;
	}
	public void setBigBodyHight(String bigBodyHight) {
		this.bigBodyHight = bigBodyHight;
	}
	public String getSmallWeight() {
		return smallWeight;
	}
	public void setSmallWeight(String smallWeight) {
		this.smallWeight = smallWeight;
	}
	public String getBigWeight() {
		return bigWeight;
	}
	public void setBigWeight(String bigWeight) {
		this.bigWeight = bigWeight;
	}
	public String getSmallXiongWei() {
		return smallXiongWei;
	}
	public void setSmallXiongWei(String smallXiongWei) {
		this.smallXiongWei = smallXiongWei;
	}
	public String getBigXiongWei() {
		return bigXiongWei;
	}
	public void setBigXiongWei(String bigXiongWei) {
		this.bigXiongWei = bigXiongWei;
	}
	public String getSmallYaoWei() {
		return smallYaoWei;
	}
	public void setSmallYaoWei(String smallYaoWei) {
		this.smallYaoWei = smallYaoWei;
	}
	public String getBigYaoWei() {
		return bigYaoWei;
	}
	public void setBigYaoWei(String bigYaoWei) {
		this.bigYaoWei = bigYaoWei;
	}
	public String getSmallTunWei() {
		return smallTunWei;
	}
	public void setSmallTunWei(String smallTunWei) {
		this.smallTunWei = smallTunWei;
	}
	public String getBigTunWei() {
		return bigTunWei;
	}
	public void setBigTunWei(String bigTunWei) {
		this.bigTunWei = bigTunWei;
	}
	public String getSmallCupName() {
		return smallCupName;
	}
	public void setSmallCupName(String smallCupName) {
		this.smallCupName = smallCupName;
	}
	public String getSamllCupId() {
		return samllCupId;
	}
	public void setSamllCupId(String samllCupId) {
		this.samllCupId = samllCupId;
	}
	public String getBigCupName() {
		return bigCupName;
	}
	public void setBigCupName(String bigCupName) {
		this.bigCupName = bigCupName;
	}
	public String getBigCupId() {
		return BigCupId;
	}
	public void setBigCupId(String bigCupId) {
		BigCupId = bigCupId;
	}
	public String getSmallShoese() {
		return smallShoese;
	}
	public void setSmallShoese(String smallShoese) {
		this.smallShoese = smallShoese;
	}
	public String getBigShoese() {
		return bigShoese;
	}
	public void setBigShoese(String bigShoese) {
		this.bigShoese = bigShoese;
	}
	public String getBeizhuStr() {
		return beizhuStr;
	}
	public void setBeizhuStr(String beizhuStr) {
		this.beizhuStr = beizhuStr;
	}
	public boolean isBaoXiaoCaiLv() {
		return isBaoXiaoCaiLv;
	}
	public void setBaoXiaoCaiLv(boolean isBaoXiaoCaiLv) {
		this.isBaoXiaoCaiLv = isBaoXiaoCaiLv;
	}
	public boolean isBaoXiaoZhuSu() {
		return isBaoXiaoZhuSu;
	}
	public void setBaoXiaoZhuSu(boolean isBaoXiaoZhuSu) {
		this.isBaoXiaoZhuSu = isBaoXiaoZhuSu;
	}
}
