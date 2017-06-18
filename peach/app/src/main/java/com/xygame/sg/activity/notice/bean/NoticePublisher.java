package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2015年4月16日
 */
public class NoticePublisher implements Serializable {
	private static final long serialVersionUID = -3454666020644519464L;

	private long userId;
	private String usernick;
	private String userIcon;
	private int authStatus;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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

	public int getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(int authStatus) {
		this.authStatus = authStatus;
	}
}