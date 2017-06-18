package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * crud 报价的bean
 * Created by xy on 2015/11/24.
 */
public class PriceBean implements Serializable {
    /**
     * 每一条报价单的id
     */
    private String id;
    /**
     * 拍摄大类name
     */
    private String priceTypeName;
    /**
     * 拍摄大类id
     */
    private String priceType;
    /**
     * 拍摄小类名称
     */
    private String itemName;
    /**
     * 价格
     */
    private int price;
    /**
     * 价格单位
     */
    private String priceUnit;
    /**
     * 最多不超过多少人（数字）
     */
    private String limitParter;
    /**
     * uucode
     */
    private String uucode;



    private int locIndex;

    /**
     * 是否选中
     */
    private boolean isSelected;

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public String getUucode() {
        return uucode;
    }

    public void setUucode(String uucode) {
        this.uucode = uucode;
    }

    public String getLimitParter() {
        return limitParter;
    }

    public void setLimitParter(String limitParter) {
        this.limitParter = limitParter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public int getLocIndex() {
        return locIndex;
    }

    public void setLocIndex(int locIndex) {
        this.locIndex = locIndex;
    }

}
