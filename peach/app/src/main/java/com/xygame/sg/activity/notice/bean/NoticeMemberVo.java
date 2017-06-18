/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author 	zhoujian
 * @date	2015年12月15日 下午2:27:18
 * @desc	
 */
import java.util.List;
public class NoticeMemberVo implements Serializable {
	
	private static final long serialVersionUID = 1751970015030658926L;
	private long memId;
	private long userId;
	private String userIcon;
	private String userNick;
	private int age;
	private int userType;
	private int province;//定位
	private int city;
	private int gender;
	private List<NoticeMemberLabelVo> labels = new ArrayList<NoticeMemberLabelVo>();

	private int match;//匹配度

	/**
	 * 是否选中
	 */
	private boolean isSelected;

	public List<NoticeMemberLabelVo> getLabels() {
		return labels;
	}

	public void setLabels(List<NoticeMemberLabelVo> labels) {
		this.labels = labels;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}



	public long getMemId() {
		return memId;
	}

	public void setMemId(long memId) {
		this.memId = memId;
	}

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

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getMatch() {
		return match;
	}

	public void setMatch(int match) {
		this.match = match;
	}
	public boolean isSelected() {
		return isSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
