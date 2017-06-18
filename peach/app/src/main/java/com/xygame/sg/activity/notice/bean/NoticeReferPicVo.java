/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * @author 	zhoujian
 * @date	2015年12月8日 下午4:42:49
 * @desc	通告参考作品
 */
public class NoticeReferPicVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4103439410828819473L;
	private long picId;
	private String picUrl;
	
	public long getPicId() {
		return picId;
	}
	public void setPicId(long picId) {
		this.picId = picId;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
