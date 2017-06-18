package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

public class CommentBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String picLevel,experienceLevel,coordinateLevel,evalContent,ind,userNick,userIcon,flag;

	public String getPicLevel() {
		return picLevel;
	}

	public void setPicLevel(String picLevel) {
		this.picLevel = picLevel;
	}

	public String getExperienceLevel() {
		return experienceLevel;
	}

	public void setExperienceLevel(String experienceLevel) {
		this.experienceLevel = experienceLevel;
	}

	public String getCoordinateLevel() {
		return coordinateLevel;
	}

	public void setCoordinateLevel(String coordinateLevel) {
		this.coordinateLevel = coordinateLevel;
	}

	public String getEvalContent() {
		return evalContent;
	}

	public void setEvalContent(String evalContent) {
		this.evalContent = evalContent;
	}

	public String getInd() {
		return ind;
	}

	public void setInd(String ind) {
		this.ind = ind;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	
}
