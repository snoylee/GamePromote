package com.xygame.second.sg.comm.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/13.
 */
public class GiftBean implements Serializable{
    private String id,name,showUrl,worthAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getWorthAmount() {
        return worthAmount;
    }

    public void setWorthAmount(String worthAmount) {
        this.worthAmount = worthAmount;
    }
}
