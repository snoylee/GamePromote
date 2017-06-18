package com.xygame.sg.bean.comm;

import java.io.Serializable;
import java.util.List;

public class PhotoesTotalBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dateTimer;
	private Boolean isSelect;
	private List<PhotoesSubBean> imageObjects;

	public String getDateTimer() {
		return dateTimer;
	}

	public void setDateTimer(String dateTimer) {
		this.dateTimer = dateTimer;
	}

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}

	public List<PhotoesSubBean> getImageObjects() {
		return imageObjects;
	}

	public void setImageObjects(List<PhotoesSubBean> imageObjects) {
		this.imageObjects = imageObjects;
	}

}
