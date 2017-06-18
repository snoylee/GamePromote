package com.xygame.sg.im;

import com.xygame.second.sg.personal.bean.UserBeanInfo;
import com.xygame.sg.bean.circle.CirclePraisers;

import java.io.Serializable;
import java.util.List;

public class TransferBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String latitude,longitude;

	private List<UserBeanInfo> userBeanInfos;

	private List<CirclePraisers> discGroupMembers;

	public List<CirclePraisers> getDiscGroupMembers() {
		return discGroupMembers;
	}

	public void setDiscGroupMembers(List<CirclePraisers> discGroupMembers) {
		this.discGroupMembers = discGroupMembers;
	}

	public List<UserBeanInfo> getUserBeanInfos() {
		return userBeanInfos;
	}

	public void setUserBeanInfos(List<UserBeanInfo> userBeanInfos) {
		this.userBeanInfos = userBeanInfos;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
}
