/*
 * 文 件 名:  NoticeBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年12月21日
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年12月21日
 * @action [功能描述]
 */
public class NoticeBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String signStatus,evalFlag,noticeId, noticeType, subject, startTime, endTime, openStatus, payStatus, amount, total,hiredSum,hiredAmount,receiptSum,receiptAmount;
	private List<NoticeItemBean> notices;
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<NoticeItemBean> getNotices() {
		return notices;
	}
	public void setNotices(List<NoticeItemBean> notices) {
		this.notices = notices;
	}
	public String getHiredSum() {
		return hiredSum;
	}
	public void setHiredSum(String hiredSum) {
		this.hiredSum = hiredSum;
	}
	public String getHiredAmount() {
		return hiredAmount;
	}
	public void setHiredAmount(String hiredAmount) {
		this.hiredAmount = hiredAmount;
	}
	public String getReceiptSum() {
		return receiptSum;
	}
	public void setReceiptSum(String receiptSum) {
		this.receiptSum = receiptSum;
	}
	public String getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(String receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getEvalFlag() {
		return evalFlag;
	}
	public void setEvalFlag(String evalFlag) {
		this.evalFlag = evalFlag;
	}

	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
}
