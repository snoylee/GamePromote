package com.xygame.sg.activity.personal.bean;

/**
 * @author tianxr
 * @date 2015年11月12日
 */
public class ModelGalleryPicView {
	private long resId;
	private String resUrl;
	private Integer lengthPx;
	private Integer widthPx;
	private Long picSize;
	private Integer isCover;

	public long getResId() {
		return resId;
	}

	public void setResId(long resId) {
		this.resId = resId;
	}

	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public Integer getLengthPx() {
		return lengthPx;
	}

	public void setLengthPx(Integer lengthPx) {
		this.lengthPx = lengthPx;
	}

	public Integer getWidthPx() {
		return widthPx;
	}

	public void setWidthPx(Integer widthPx) {
		this.widthPx = widthPx;
	}

	public Long getPicSize() {
		return picSize;
	}

	public void setPicSize(Long picSize) {
		this.picSize = picSize;
	}

	public Integer getIsCover() {
		return isCover;
	}

	public void setIsCover(Integer isCover) {
		this.isCover = isCover;
	}
}
