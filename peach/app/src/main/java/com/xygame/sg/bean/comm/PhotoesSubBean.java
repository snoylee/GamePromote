package com.xygame.sg.bean.comm;

import java.io.Serializable;

public class PhotoesSubBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dateTimer;
	private Boolean isSelect;
	private String imageUrls,url;
	private String imageId;
	private String isCover;
	private String itemIndex,lengthPx,widthPx,picSize;

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

	public String getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(String itemIndex) {
		this.itemIndex = itemIndex;
	}

	public String getLengthPx() {
		return lengthPx;
	}

	public void setLengthPx(String lengthPx) {
		this.lengthPx = lengthPx;
	}

	public String getWidthPx() {
		return widthPx;
	}

	public void setWidthPx(String widthPx) {
		this.widthPx = widthPx;
	}

	public String getPicSize() {
		return picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}

	public String getIsCover() {
		return isCover;
	}

	public void setIsCover(String isCover) {
		this.isCover = isCover;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
