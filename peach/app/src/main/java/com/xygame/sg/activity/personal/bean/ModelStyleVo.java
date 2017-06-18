package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2015年11月9日
 */
public class ModelStyleVo implements Serializable{
	private int styleId;
	private String styleName;
	private int hueR;
	private int hueG;
	private int hueB;
	private int exclusType;

	public int getStyleId() {
		return styleId;
	}

	public void setStyleId(int styleId) {
		this.styleId = styleId;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public int getExclusType() {
		return exclusType;
	}

	public void setExclusType(int exclusType) {
		this.exclusType = exclusType;
	}

	public int getHueR() {
		return hueR;
	}

	public void setHueR(int hueR) {
		this.hueR = hueR;
	}

	public int getHueG() {
		return hueG;
	}

	public void setHueG(int hueG) {
		this.hueG = hueG;
	}

	public int getHueB() {
		return hueB;
	}

	public void setHueB(int hueB) {
		this.hueB = hueB;
	}
}
