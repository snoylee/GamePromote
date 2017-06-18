package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2015年11月26日
 */
public class UserTypeVo implements Serializable{
	private int userType;
	private int authStatus;
	private Long auditTime;
	private String refuseReason;

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(int authStatus) {
		this.authStatus = authStatus;
	}

	public Long getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Long auditTime) {
		this.auditTime = auditTime;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
}
