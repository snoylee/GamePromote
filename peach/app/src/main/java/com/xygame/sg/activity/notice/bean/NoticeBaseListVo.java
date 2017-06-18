package com.xygame.sg.activity.notice.bean;

import java.util.Date;

/**
 * @author tianxr
 * @date 2015年12月10日
 */
public class NoticeBaseListVo {
	private String noticeId;
	private int shootType;
	private int noticeType;
	private int pgrapherCount;
	private String subject;
	private NoticePublisher publisher;
	private Date joinStartTime;
	private Date joinEndTime;
	private String remark;

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public int getShootType() {
		return shootType;
	}

	public void setShootType(int shootType) {
		this.shootType = shootType;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public NoticePublisher getPublisher() {
		return publisher;
	}

	public void setPublisher(NoticePublisher publisher) {
		this.publisher = publisher;
	}

	public Date getJoinStartTime() {
		return joinStartTime;
	}

	public void setJoinStartTime(Date joinStartTime) {
		this.joinStartTime = joinStartTime;
	}
	public Date getJoinEndTime() {
		return joinEndTime;
	}

	public void setJoinEndTime(Date joinEndTime) {
		this.joinEndTime = joinEndTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getPgrapherCount() {
		return pgrapherCount;
	}

	public void setPgrapherCount(int pgrapherCount) {
		this.pgrapherCount = pgrapherCount;
	}

}
