package com.xygame.second.sg.personal.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/6/6.
 */
public class PopPresentBean implements Serializable{
    private Integer id;
    private String name;
    private boolean hasSlect;
    private Long worthAmount;
    private String showUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasSlect() {
        return hasSlect;
    }

    public void setHasSlect(boolean hasSlect) {
        this.hasSlect = hasSlect;
    }

    public Long getWorthAmount() {
        return worthAmount;
    }

    public void setWorthAmount(Long worthAmount) {
        this.worthAmount = worthAmount;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
