package com.xygame.second.sg.jinpai;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/18.
 */
public class JinPaiLowerPriceBean implements Serializable{
    private int highPrice,lowPrice;
    private String typeId;

    public int getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(int highPrice) {
        this.highPrice = highPrice;
    }

    public int getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(int lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
