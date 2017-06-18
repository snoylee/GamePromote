/*
 * 文 件 名:  BodyInfoBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月21日
 */
package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月21日
 * @action  [身体基本信息]
 */
public class BodyInfoBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String userHeight,userWeight,userBust,userWaist,userHip,userCup,userShoesCode;

	public String getUserHeight() {
		return userHeight;
	}

	public void setUserHeight(String userHeight) {
		this.userHeight = userHeight;
	}

	public String getUserWeight() {
		return userWeight;
	}

	public void setUserWeight(String userWeight) {
		this.userWeight = userWeight;
	}

	public String getUserBust() {
		return userBust;
	}

	public void setUserBust(String userBust) {
		this.userBust = userBust;
	}

	public String getUserWaist() {
		return userWaist;
	}

	public void setUserWaist(String userWaist) {
		this.userWaist = userWaist;
	}

	public String getUserHip() {
		return userHip;
	}

	public void setUserHip(String userHip) {
		this.userHip = userHip;
	}

	public String getUserCup() {
		return userCup;
	}

	public void setUserCup(String userCup) {
		this.userCup = userCup;
	}

	public String getUserShoesCode() {
		return userShoesCode;
	}

	public void setUserShoesCode(String userShoesCode) {
		this.userShoesCode = userShoesCode;
	}
	
	
}
