package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tianxr
 * @date 2015年11月9日
 */
public class ModelResumeVo implements Serializable{
	private long id;
	private Long startDate;
	private Long endDate;
	private String experDesc;
	private Integer locIndex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getExperDesc() {
		return experDesc;
	}

	public void setExperDesc(String experDesc) {
		this.experDesc = experDesc;
	}

	public Integer getLocIndex() {
		return locIndex;
	}

	public void setLocIndex(Integer locIndex) {
		this.locIndex = locIndex;
	}
}
