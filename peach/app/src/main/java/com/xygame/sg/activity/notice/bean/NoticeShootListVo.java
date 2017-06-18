package com.xygame.sg.activity.notice.bean;

import java.util.Date;

/**
 * @author tianxr
 * @date 2015年12月10日
 */
public class NoticeShootListVo {
	private Date startTime;
	private Date endTime;
	private Integer addrProvince;
	private Integer addrCity;
	private String address;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getAddrProvince() {
		return addrProvince;
	}

	public void setAddrProvince(Integer addrProvince) {
		this.addrProvince = addrProvince;
	}

	public Integer getAddrCity() {
		return addrCity;
	}

	public void setAddrCity(Integer addrCity) {
		this.addrCity = addrCity;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
