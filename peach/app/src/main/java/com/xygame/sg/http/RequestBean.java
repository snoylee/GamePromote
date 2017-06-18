package com.xygame.sg.http;

import android.content.Context;

import com.xygame.sg.utils.UserPreferencesUtil;

import java.io.Serializable;

import org.json.JSONObject;

public class RequestBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JSONObject data;

	private String osType;

	private String serviceURL;

	private boolean isPublic;

	public RequestBean(){
		this.isPublic=false;
	}
	
	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}
}
