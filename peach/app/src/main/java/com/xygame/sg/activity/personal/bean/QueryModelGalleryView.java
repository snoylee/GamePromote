package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2015年11月12日
 */
public class QueryModelGalleryView implements Serializable{
	private long galId;
	private String galDesc;
	private Long lastUpdateTime;
	private ModelGalleryPicView cover;
	private int count;
	private int praiseCount;

	public long getGalId() {
		return galId;
	}

	public void setGalId(long galId) {
		this.galId = galId;
	}

	public String getGalDesc() {
		return galDesc;
	}

	public void setGalDesc(String galDesc) {
		this.galDesc = galDesc;
	}

	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public ModelGalleryPicView getCover() {
		return cover;
	}

	public void setCover(ModelGalleryPicView cover) {
		this.cover = cover;
	}
}
