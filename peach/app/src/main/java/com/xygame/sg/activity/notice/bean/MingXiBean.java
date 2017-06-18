package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

public class MingXiBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id,userId,financeType,amount,dealDesc,dealNote,changeRecordId,noticeId,dealType,dealChannel,dealTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFinanceType() {
		return financeType;
	}

	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDealDesc() {
		return dealDesc;
	}

	public void setDealDesc(String dealDesc) {
		this.dealDesc = dealDesc;
	}

	public String getDealNote() {
		return dealNote;
	}

	public void setDealNote(String dealNote) {
		this.dealNote = dealNote;
	}

	public String getChangeRecordId() {
		return changeRecordId;
	}

	public void setChangeRecordId(String changeRecordId) {
		this.changeRecordId = changeRecordId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getDealChannel() {
		return dealChannel;
	}

	public void setDealChannel(String dealChannel) {
		this.dealChannel = dealChannel;
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}
	
}
