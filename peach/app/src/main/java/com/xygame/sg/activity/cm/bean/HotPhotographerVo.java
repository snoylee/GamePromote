package com.xygame.sg.activity.cm.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2016年3月17日
 */
public class HotPhotographerVo implements Serializable {

	private String userIcon;
	private Long userId;
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
