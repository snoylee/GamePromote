package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2015年10月19日
 */
public class UserBody implements Serializable {
	private static final long serialVersionUID = -22165600003726309L;

	private long userId;
	private Integer height;
	private Integer weight;
	private Integer bust;
	private Integer waist;
	private Integer hip;
	private Integer cup;
	private Integer shoesCode;

	public Integer getWeight() {
		return weight;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getBust() {
		return bust;
	}

	public void setBust(Integer bust) {
		this.bust = bust;
	}

	public Integer getWaist() {
		return waist;
	}

	public void setWaist(Integer waist) {
		this.waist = waist;
	}

	public Integer getHip() {
		return hip;
	}

	public void setHip(Integer hip) {
		this.hip = hip;
	}

	public Integer getCup() {
		return cup;
	}

	public void setCup(Integer cup) {
		this.cup = cup;
	}

	public Integer getShoesCode() {
		return shoesCode;
	}

	public void setShoesCode(Integer shoesCode) {
		this.shoesCode = shoesCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
