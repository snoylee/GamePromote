/**
 * 
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * @author 	zhoujian
 * @date	2015年12月21日 下午3:11:01
 * @desc	
 */
public class NoticeMemberLabelVo implements Serializable {
	
	private static final long serialVersionUID = -7048720166161182062L;
	
	private String type;//大分类
	private String name;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
