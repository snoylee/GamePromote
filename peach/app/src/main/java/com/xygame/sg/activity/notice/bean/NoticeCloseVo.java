/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * @author 	zhoujian
 * @date	2015年12月30日 上午11:15:10
 * @desc	
 */
public class NoticeCloseVo implements Serializable {
	
	private static final long serialVersionUID = -5543936491759425165L;
	private int total;//计划招募人数
	private long prepay;//已预付款
	private int receiptNum;//已录用并结算人
	private long receiptAmount;//已录用并结算金额
	private long couponAmount;//已使用优惠券金额
	private long balance;//余额
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public long getPrepay() {
		return prepay;
	}
	public void setPrepay(long prepay) {
		this.prepay = prepay;
	}
	public int getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(int receiptNum) {
		this.receiptNum = receiptNum;
	}
	public long getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(long receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public long getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(long couponAmount) {
		this.couponAmount = couponAmount;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
}
