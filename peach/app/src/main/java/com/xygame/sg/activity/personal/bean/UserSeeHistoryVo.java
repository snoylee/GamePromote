package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tianxr
 * @date 2016年1月4日
 */
public class UserSeeHistoryVo implements Serializable {
	private long userId;
	private String userIcon;
	private String usernick;
	private Integer gender;
	private int age;
	private Integer userType;
	private Integer authStatus;
	private Date visitTime;
//	private long visitTime;
	private boolean isGone;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUsernick() {
		return usernick;
	}

	public void setUsernick(String usernick) {
		this.usernick = usernick;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public boolean isGone() {
		return isGone;
	}

	public void setIsGone(boolean isGone) {
		this.isGone = isGone;
	}

//	public long getVisitTime() {
//		return visitTime;
//	}
//
//	public void setVisitTime(long visitTime) {
//		this.visitTime = visitTime;
//	}
}
