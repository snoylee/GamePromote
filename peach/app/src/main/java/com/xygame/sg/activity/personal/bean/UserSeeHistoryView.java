package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tianxr
 * @date 2016年1月4日
 */
public class UserSeeHistoryView implements Serializable {
//	private Date reqTime;
//	private Date lastReadTime;
    private Long reqTime;
	private Long lastReadTime;
	private List<UserSeeHistoryVo> historys;

//	public Date getReqTime() {
//		return reqTime;
//	}
//
//	public void setReqTime(Date reqTime) {
//		this.reqTime = reqTime;
//	}
//
//	public Date getLastReadTime() {
//		return lastReadTime;
//	}
//
//	public void setLastReadTime(Date lastReadTime) {
//		this.lastReadTime = lastReadTime;
//	}

	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}

	public Long getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(Long lastReadTime) {
		this.lastReadTime = lastReadTime;
	}

	public List<UserSeeHistoryVo> getHistorys() {
		return historys;
	}

	public void setHistorys(List<UserSeeHistoryVo> historys) {
		this.historys = historys;
	}
}
