package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author tianxr
 * @date 2015年11月9日
 */
public class QueryModelPriceView implements Serializable {
	private int priceType;
	private String priceTypeName;
	private List<ModelPriceVo> prices;

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}

	public String getPriceTypeName() {
		return priceTypeName;
	}

	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}

	public List<ModelPriceVo> getPrices() {
		return prices;
	}

	public void setPrices(List<ModelPriceVo> prices) {
		this.prices = prices;
	}
}
