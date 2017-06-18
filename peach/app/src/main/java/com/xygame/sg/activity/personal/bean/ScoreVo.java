package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;


public class ScoreVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int tradeCount;//交易次数
	
	private int evlCount;//评价总数
	
	private double evlScore;//平均得分

	private int picLevel;
	
	private int experienceLevel;
	
	private int coordinateLevel;
	
	private int noticeLevel;
	
	private int pgrapExperLevel;
	
	private int payLevel;
	
	public int getTradeCount() {
		return tradeCount;
	}

	public void setTradeCount(int tradeCount) {
		this.tradeCount = tradeCount;
	}

	public int getEvlCount() {
		return evlCount;
	}

	public void setEvlCount(int evlCount) {
		this.evlCount = evlCount;
	}

	public double getEvlScore() {
		return evlScore;
	}

	public void setEvlScore(double evlScore) {
		this.evlScore = evlScore;
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

	public int getPgrapExperLevel() {
		return pgrapExperLevel;
	}

	public void setPgrapExperLevel(int pgrapExperLevel) {
		this.pgrapExperLevel = pgrapExperLevel;
	}

	public int getPayLevel() {
		return payLevel;
	}

	public void setPayLevel(int payLevel) {
		this.payLevel = payLevel;
	}
	
	
	
	
}
