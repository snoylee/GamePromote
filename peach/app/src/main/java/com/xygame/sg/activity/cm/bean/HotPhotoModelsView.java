package com.xygame.sg.activity.cm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author tianxr
 * @date 2016年3月17日
 */
public class HotPhotoModelsView implements Serializable {
	private Long reqTime;
	private List<HotPhotographerVo> users;
	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}

	public List<HotPhotographerVo> getModels() {
		return users;
	}

	public void setModels(List<HotPhotographerVo> models) {
		this.users = models;
	}
}
