/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 	zhoujian
 * @date	2015年12月15日 下午4:05:30
 * @desc	
 */
public class NoticeMemberUpdateBean implements Serializable {
	
	private static final long serialVersionUID = 7022358637728668491L;
	
	private int status;
	private List<Long> memIds = new ArrayList<Long>();

	private boolean useBanlance;
	private String payType;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public List<Long> getMemIds() {
		return memIds;
	}

	public void setMemIds(List<Long> memIds) {
		this.memIds = memIds;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public boolean isUseBanlance() {
		return useBanlance;
	}

	public void setUseBanlance(boolean useBanlance) {
		this.useBanlance = useBanlance;
	}
}
