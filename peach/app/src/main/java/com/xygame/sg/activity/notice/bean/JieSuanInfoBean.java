package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

public class JieSuanInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message,frozenCount,frozenAmount,shootCount,parpayAmount,oldNeedPayAmount,availAmount,incomeAmount,walletAmount;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFrozenCount() {
		return frozenCount;
	}

	public void setFrozenCount(String frozenCount) {
		this.frozenCount = frozenCount;
	}

	public String getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(String frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public String getShootCount() {
		return shootCount;
	}

	public void setShootCount(String shootCount) {
		this.shootCount = shootCount;
	}

	public String getParpayAmount() {
		return parpayAmount;
	}

	public void setParpayAmount(String parpayAmount) {
		this.parpayAmount = parpayAmount;
	}

	public String getOldNeedPayAmount() {
		return oldNeedPayAmount;
	}

	public void setOldNeedPayAmount(String oldNeedPayAmount) {
		this.oldNeedPayAmount = oldNeedPayAmount;
	}

	public String getAvailAmount() {
		return availAmount;
	}

	public void setAvailAmount(String availAmount) {
		this.availAmount = availAmount;
	}

	public String getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(String incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public String getWalletAmount() {
		return walletAmount;
	}

	public void setWalletAmount(String walletAmount) {
		this.walletAmount = walletAmount;
	}
	
	
	
	
}
