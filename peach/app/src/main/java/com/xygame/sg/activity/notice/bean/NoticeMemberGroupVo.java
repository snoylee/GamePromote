/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * @author 	zhoujian
 * @date	2015年12月15日 下午4:05:30
 * @desc	
 */
public class NoticeMemberGroupVo implements Serializable {
	
	private static final long serialVersionUID = 7022358637728668491L;
	
	private int status;
	private int sum;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
}
