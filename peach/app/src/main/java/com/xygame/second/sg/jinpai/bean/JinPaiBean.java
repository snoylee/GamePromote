package com.xygame.second.sg.jinpai.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/7/18.
 */
public class JinPaiBean implements Serializable {
    private String actId,actTitle,bidEndTime,showCoverUrl,totalBidCount,userId,usernick,scanCount,price,actNature;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActNature() {
        return actNature;
    }

    public void setActNature(String actNature) {
        this.actNature = actNature;
    }

    public String getScanCount() {
        return scanCount;
    }

    public void setScanCount(String scanCount) {
        this.scanCount = scanCount;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public String getBidEndTime() {
        return bidEndTime;
    }

    public void setBidEndTime(String bidEndTime) {
        this.bidEndTime = bidEndTime;
    }

    public String getShowCoverUrl() {
        return showCoverUrl;
    }

    public void setShowCoverUrl(String showCoverUrl) {
        this.showCoverUrl = showCoverUrl;
    }

    public String getTotalBidCount() {
        return totalBidCount;
    }

    public void setTotalBidCount(String totalBidCount) {
        this.totalBidCount = totalBidCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }
}
