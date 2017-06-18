package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

public class SignedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId,usernick,userIcon,signTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsernick() {
		return usernick;
	}

	public void setUsernick(String usernick) {
		this.usernick = usernick;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	
	
}
