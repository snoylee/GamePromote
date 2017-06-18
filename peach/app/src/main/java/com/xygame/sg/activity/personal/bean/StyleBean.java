package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

public class StyleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String styleName;
	
	private String styleId;
	
	private int colorR,colorG,colorB;
	
	/** 
	 * 栏目对应ID
	 *  */
	public Integer id;
	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	public Integer orderId;
	/** 
	 * 栏目是否选中
	 *  */
	public Integer selected;

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public int getColorR() {
		return colorR;
	}

	public void setColorR(int colorR) {
		this.colorR = colorR;
	}

	public int getColorG() {
		return colorG;
	}

	public void setColorG(int colorG) {
		this.colorG = colorG;
	}

	public int getColorB() {
		return colorB;
	}

	public void setColorB(int colorB) {
		this.colorB = colorB;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StyleBean styleBean = (StyleBean) o;

		return styleId.equals(styleBean.styleId);

	}

	@Override
	public int hashCode() {
		return styleId.hashCode();
	}

	@Override
	public String toString() {
		return  styleId;
	}
}
