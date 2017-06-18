package com.xygame.second.sg.sendgift.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/13.
 */
public class OtherActBean implements Serializable{
    private String actId,actNature,actTitle,price,showCoverUrl;

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
}
