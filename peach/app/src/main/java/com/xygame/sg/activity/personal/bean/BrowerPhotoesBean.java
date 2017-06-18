/*
 * 文 件 名:  BrowerPhotoesBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月28日
 */
package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月28日
 * @action  [功能描述]
 */
public class BrowerPhotoesBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String imageId,imageUrl,priseCount,lengthPx,widthPx,picSize;
	
	private boolean isPrise;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isPrise() {
		return isPrise;
	}

	public void setPrise(boolean isPrise) {
		this.isPrise = isPrise;
	}

	public String getPriseCount() {
		return priseCount;
	}

	public void setPriseCount(String priseCount) {
		this.priseCount = priseCount;
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
	
	
	
}
