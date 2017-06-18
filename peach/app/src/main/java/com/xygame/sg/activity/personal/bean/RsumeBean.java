/*
 * 文 件 名:  RsumeBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月23日
 */
package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月23日
 * @action  [功能描述]
 */
public class RsumeBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String startTime,endTime,experDesc,resumeId;
	private int locIndex;
	private boolean isReport;

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

	public String getExperDesc() {
		return experDesc;
	}

	public void setExperDesc(String experDesc) {
		this.experDesc = experDesc;
	}

	public String getResumeId() {
		return resumeId;
	}

	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}

	public boolean isReport() {
		return isReport;
	}

	public void setReport(boolean isReport) {
		this.isReport = isReport;
	}

	public int getLocIndex() {
		return locIndex;
	}

	public void setLocIndex(int locIndex) {
		this.locIndex = locIndex;
	}
	
	
}
