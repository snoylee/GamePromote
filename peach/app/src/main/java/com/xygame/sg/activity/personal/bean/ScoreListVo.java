package com.xygame.sg.activity.personal.bean;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

public class ScoreListVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private long userId;
	private String usernick;
	private String userIcon;
	//摄影师评价模特
	private int picLevel;//模特照片符合度
	private int experienceLevel;//专业度
	private int coordinateLevel;//配合度
	//模特评价通告
	private int noticeLevel;//通告实际符合度
	private int pgExperLevel;//摄影师专业度
	private int payLevel;//付款效率
	
	private String evalContent;//评论
	
	private Long createTime;//评论时间

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

	public int getPicLevel() {
		return picLevel;
	}

	public void setPicLevel(int picLevel) {
		this.picLevel = picLevel;
	}

	public int getExperienceLevel() {
		return experienceLevel;
	}

	public void setExperienceLevel(int experienceLevel) {
		this.experienceLevel = experienceLevel;
	}

	public int getCoordinateLevel() {
		return coordinateLevel;
	}

	public void setCoordinateLevel(int coordinateLevel) {
		this.coordinateLevel = coordinateLevel;
	}

	public int getNoticeLevel() {
		return noticeLevel;
	}

	public void setNoticeLevel(int noticeLevel) {
		this.noticeLevel = noticeLevel;
	}

	public int getPgExperLevel() {
		return pgExperLevel;
	}

	public void setPgExperLevel(int pgExperLevel) {
		this.pgExperLevel = pgExperLevel;
	}

	public int getPayLevel() {
		return payLevel;
	}

	public void setPayLevel(int payLevel) {
		this.payLevel = payLevel;
	}

	public String getEvalContent() {
		return evalContent;
	}

	public void setEvalContent(String evalContent) {
		this.evalContent = evalContent;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	
}
