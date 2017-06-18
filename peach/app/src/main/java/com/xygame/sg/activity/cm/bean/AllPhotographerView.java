package com.xygame.sg.activity.cm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tianxr
 * @date 2016年3月17日
 */
public class AllPhotographerView implements Serializable {
	private Long reqTime;
	private List<AllPhotographerVo> photops = new ArrayList<>();

	public AllPhotographerView() {}

	public AllPhotographerView(Long reqTime, List<AllPhotographerVo> photops) {
		this.reqTime = reqTime;
		this.photops = photops;
	}

	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}

	public List<AllPhotographerVo> getPhotops() {
		return photops;
	}

	public void setPhotops(List<AllPhotographerVo> photops) {
		this.photops = photops;
	}
}
