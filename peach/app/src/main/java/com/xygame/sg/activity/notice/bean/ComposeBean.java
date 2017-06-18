/*
 * 文 件 名:  ComposeBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年12月18日
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年12月18日
 * @action  [功能描述]
 */
public class ComposeBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String compId,manPrice,useDate,tipText,youHuiPrice,useType,coupName,amount,offdiscount,canUse;

	public String getUseType() {
		return useType;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}

	public String getCoupName() {
		return coupName;
	}

	public void setCoupName(String coupName) {
		this.coupName = coupName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	private boolean isSelect;

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getManPrice() {
		return manPrice;
	}

	public void setManPrice(String manPrice) {
		this.manPrice = manPrice;
	}

	public String getUseDate() {
		return useDate;
	}

	public void setUseDate(String useDate) {
		this.useDate = useDate;
	}

	public String getTipText() {
		return tipText;
	}

	public void setTipText(String tipText) {
		this.tipText = tipText;
	}

	public String getYouHuiPrice() {
		return youHuiPrice;
	}

	public void setYouHuiPrice(String youHuiPrice) {
		this.youHuiPrice = youHuiPrice;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getOffdiscount() {
		return offdiscount;
	}

	public void setOffdiscount(String offdiscount) {
		this.offdiscount = offdiscount;
	}

	public String getCanUse() {
		return canUse;
	}

	public void setCanUse(String canUse) {
		this.canUse = canUse;
	}
	
	
}
