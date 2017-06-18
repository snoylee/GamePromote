package com.xygame.sg.activity.commen.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2015年11月2日
 */
public class UserType implements Serializable {
	private int utype;
	private int status;

	public int getUtype() {
		return utype;
	}

	public void setUtype(int utype) {
		this.utype = utype;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
