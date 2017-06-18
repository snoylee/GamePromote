package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author tianxr
 * @date 2015年11月9日
 */
public class UserInfoView implements Serializable{
	private UserBody userBody;
	private List<ModelResumeVo> resumes;
	private List<ModelStyleVo> styles;
	private String userPin;
	private String usernick;
	private Integer gender;
	private Date birthday;
	private int age;
	private String country;
	private Integer province;
	private Integer city;
	private Integer occupType;
	private String introDesc;
	private List<Integer> shootTypes;

	public UserBody getUserBody() {
		return userBody;
	}

	public void setUserBody(UserBody userBody) {
		this.userBody = userBody;
	}

	public List<ModelResumeVo> getResumes() {
		return resumes;
	}

	public void setResumes(List<ModelResumeVo> resumes) {
		this.resumes = resumes;
	}

	public List<ModelStyleVo> getStyles() {
		return styles;
	}

	public void setStyles(List<ModelStyleVo> styles) {
		this.styles = styles;
	}

	public String getUserPin() {
		return userPin;
	}

	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getOccupType() {
		return occupType;
	}

	public void setOccupType(Integer occupType) {
		this.occupType = occupType;
	}

	public String getIntroDesc() {
		return introDesc;
	}

	public void setIntroDesc(String introDesc) {
		this.introDesc = introDesc;
	}

	public String getUsernick() {
		return usernick;
	}

	public void setUsernick(String usernick) {
		this.usernick = usernick;
	}

	public List<Integer> getShootTypes() {
		return shootTypes;
	}

	public void setShootTypes(List<Integer> shootTypes) {
		this.shootTypes = shootTypes;
	}
}
