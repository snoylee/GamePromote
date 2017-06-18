package com.xygame.sg.bean.comm;

import java.io.Serializable;

public class PhotoesBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String imageUrl,imageIndex,imageId,lengthPx,widthPx,picSize;
	private Long videoSize;
	private Long videoTime;
	private int type = 0;//0:图片，1：视频
	private String lastVideoId;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageIndex() {
		return imageIndex;
	}

	public void setImageIndex(String imageIndex) {
		this.imageIndex = imageIndex;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
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

	public Long getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Long videoSize) {
		this.videoSize = videoSize;
	}

	public Long getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(Long videoTime) {
		this.videoTime = videoTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getLastVideoId() {
		return lastVideoId;
	}

	public void setLastVideoId(String lastVideoId) {
		this.lastVideoId = lastVideoId;
	}
}
