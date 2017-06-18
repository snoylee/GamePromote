package com.xygame.sg.activity.commen.bean;

import java.io.Serializable;
import java.util.List;



/**
 * @author tianxr
 * @date 2015年12月19日
 */
public class SearchUserVo implements Serializable{
	private long userId;
	private String userIcon;
	private String usernick;
	private Integer gender;
	private Integer province;
	private Integer city;
	private int age;
	private Integer price;
	private List<Integer> priceTypes;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public List<Integer> getPriceTypes() {
		return priceTypes;
	}

	public void setPriceTypes(List<Integer> priceTypes) {
		this.priceTypes = priceTypes;
	}
}
