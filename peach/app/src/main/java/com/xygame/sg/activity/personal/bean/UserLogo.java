package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;




/**
 * @author tianxr
 * @date 2015年11月12日
 */
public class UserLogo implements Serializable {
	private static final long serialVersionUID = 4142756415433183993L;

	private long id;
	private long userId;
	private String logoUrl;
	private int locIndex;
	private Long lastUpdateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public int getLocIndex() {
		return locIndex;
	}

	public void setLocIndex(int locIndex) {
		this.locIndex = locIndex;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
