package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tianxr
 * @date 2015年12月10日
 */
public class NoticeListBean implements Serializable {
	private List<NoticeListVo> notices;
//	private Date reqTime;
	private Long reqTime;

	public List<NoticeListVo> getNotices() {
		return notices;
	}

	public void setNotices(List<NoticeListVo> notices) {
		this.notices = notices;
	}

//	public Date getReqTime() {
//		return reqTime;
//	}
//
//	public void setReqTime(Date reqTime) {
//		this.reqTime = reqTime;
//	}


	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}
}
