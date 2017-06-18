package com.xygame.sg.activity.cm.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2016年3月17日
 */
public class AllPhotographerVo implements Serializable {
	private Long userId;
	private String userIcon;
	private String usernick;
	private Integer gender;
//	private Integer province;
	private Integer city;
	private Integer age;
	private Integer userType;
	private Integer authStatus;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

//	public Integer getProvince() {
//		return province;
//	}
//
//	public void setProvince(Integer province) {
//		this.province = province;
//	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
