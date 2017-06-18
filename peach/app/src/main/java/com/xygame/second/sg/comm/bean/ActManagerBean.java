package com.xygame.second.sg.comm.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/22.
 */
public class ActManagerBean implements Serializable {
    private String actId,actNature,actTitle,partType,price,showCoverUrl,userId;

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getActNature() {
        return actNature;
    }

    public void setActNature(String actNature) {
        this.actNature = actNature;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShowCoverUrl() {
        return showCoverUrl;
    }

    public void setShowCoverUrl(String showCoverUrl) {
        this.showCoverUrl = showCoverUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
