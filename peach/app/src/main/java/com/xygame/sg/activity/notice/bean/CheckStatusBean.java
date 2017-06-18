package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

public class CheckStatusBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String amount,createTime,relateType,flowTitle,flowDesc,userNick,userId;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRelateType() {
		return relateType;
	}

	public void setRelateType(String relateType) {
		this.relateType = relateType;
	}

	public String getFlowTitle() {
		return flowTitle;
	}

	public void setFlowTitle(String flowTitle) {
		this.flowTitle = flowTitle;
	}

	public String getFlowDesc() {
		return flowDesc;
	}

	public void setFlowDesc(String flowDesc) {
		this.flowDesc = flowDesc;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
		
}
