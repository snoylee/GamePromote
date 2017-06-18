package com.xygame.sg.bean.comm;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2016年3月17日
 */
public class FanErBean implements Serializable {
	private Long userId;
	private String userIcon;
	private String usernick;

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
}
