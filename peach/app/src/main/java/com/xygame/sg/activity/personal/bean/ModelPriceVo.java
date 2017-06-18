package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * @author tianxr
 * @date 2015年11月10日
 */
public class ModelPriceVo implements Serializable{
	private long id;
	private String itemName;
	private int price;
	private String priceUnit;
	private Integer limitParter;
	private Integer locIndex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public Integer getLimitParter() {
		return limitParter;
	}

	public void setLimitParter(Integer limitParter) {
		this.limitParter = limitParter;
	}

	public Integer getLocIndex() {
		return locIndex;
	}

	public void setLocIndex(Integer locIndex) {
		this.locIndex = locIndex;
	}
}
