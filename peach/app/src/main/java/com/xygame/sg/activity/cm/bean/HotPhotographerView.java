package com.xygame.sg.activity.cm.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tianxr
 * @date 2016年3月17日
 */
public class HotPhotographerView implements Serializable {
	private Long reqTime;
	private List<HotPhotographerVo> photops;

	public HotPhotographerView() {}

	public HotPhotographerView(Long reqTime, List<HotPhotographerVo> photops) {
		this.reqTime = reqTime;
		this.photops = photops;
	}

	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}

	public List<HotPhotographerVo> getPhotops() {
		return photops;
	}

	public void setPhotops(List<HotPhotographerVo> photops) {
		this.photops = photops;
	}
}
