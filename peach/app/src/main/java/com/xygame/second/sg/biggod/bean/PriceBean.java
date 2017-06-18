package com.xygame.second.sg.biggod.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/12/21.
 */
public class PriceBean implements Serializable {
    private String price,id,titleId,typeId;

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
