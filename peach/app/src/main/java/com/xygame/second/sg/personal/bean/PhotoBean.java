package com.xygame.second.sg.personal.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/31.
 */
public class PhotoBean implements Serializable {
    private String resUrl,createTime,resId;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
