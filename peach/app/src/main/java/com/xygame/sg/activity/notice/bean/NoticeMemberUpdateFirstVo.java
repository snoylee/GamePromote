package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

public class NoticeMemberUpdateFirstVo implements Serializable{
	
	private static final long serialVersionUID = -5912402337133008958L;

	//状态 
	//1:信息正确
	//2:未付钱,请先付钱
	//3:信息错误,无法计算
	private int status;
	
	private int addCount;//录用数量
	
	private long parpayAmount;//总的已付预付款
	
	private long frozenAmount;//冻结的金额
	
	private long incomeAmount;//已结算的金额
	
	private long availAmount;//空闲预付款金额
	
	private long addAmount;//追加预付款
	
	private long needPayAmount;//需支付金额
	
	private long walletAmount;//钱包金额
	
	private long shootCount;//已拍摄数量
	
	private long frozenCount;//已录用未结算数量
	
	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public long getParpayAmount() {
		return parpayAmount;
	}



	public void setParpayAmount(long parpayAmount) {
		this.parpayAmount = parpayAmount;
	}



	public long getFrozenAmount() {
		return frozenAmount;
	}



	public void setFrozenAmount(long frozenAmount) {
		this.frozenAmount = frozenAmount;
	}



	public long getIncomeAmount() {
		return incomeAmount;
	}



	public void setIncomeAmount(long incomeAmount) {
		this.incomeAmount = incomeAmount;
	}



	public long getAvailAmount() {
		return availAmount;
	}



	public void setAvailAmount(long availAmount) {
		this.availAmount = availAmount;
	}



	public long getAddAmount() {
		return addAmount;
	}



	public void setAddAmount(long addAmount) {
		this.addAmount = addAmount;
	}



	public long getNeedPayAmount() {
		return needPayAmount;
	}



	public void setNeedPayAmount(long needPayAmount) {
		this.needPayAmount = needPayAmount;
	}



	public long getWalletAmount() {
		return walletAmount;
	}



	public void setWalletAmount(long walletAmount) {
		this.walletAmount = walletAmount;
	}



	public long getShootCount() {
		return shootCount;
	}



	public void setShootCount(long shootCount) {
		this.shootCount = shootCount;
	}



	public long getFrozenCount() {
		return frozenCount;
	}



	public void setFrozenCount(long frozenCount) {
		this.frozenCount = frozenCount;
	}



	public int getAddCount() {
		return addCount;
	}



	public void setAddCount(int addCount) {
		this.addCount = addCount;
	}
	
	


}
