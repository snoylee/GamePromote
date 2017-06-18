package com.xygame.second.sg.biggod.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/12/19.
 */
public class GodLableBean implements Serializable{
    private String titleId,titleName,price,localIndex;

    public String getLocalIndex() {
        return localIndex;
    }

    public void setLocalIndex(String localIndex) {
        this.localIndex = localIndex;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
