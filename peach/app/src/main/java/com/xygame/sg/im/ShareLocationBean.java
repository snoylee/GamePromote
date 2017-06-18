package com.xygame.sg.im;

import java.io.Serializable;

public class ShareLocationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String latitude,Longitude,photoPath,address;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
