/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 	zhoujian
 * @date	2015年12月8日 下午4:44:35
 * @desc	拍摄信息（1：1）
 */
public class NoticeShootVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8646716355613192746L;
	private Date startTime;
	private Date endTime;
	private int provice;
	private int city;
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
	public int getProvice() {
		return provice;
	}
	public void setProvice(int provice) {
		this.provice = provice;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
