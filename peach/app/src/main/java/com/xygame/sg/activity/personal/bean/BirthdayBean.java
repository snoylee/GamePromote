package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

public class BirthdayBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String brithday,age;

	public String getBrithday() {
		return brithday;
	}

	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	
}
