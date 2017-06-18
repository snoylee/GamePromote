package com.xygame.sg.bean.circle;

import java.io.Serializable;

/**
 * Created by tony on 2016/5/20.
 */
public class CircleRess implements Serializable {
    private String resId;
    private String resUrl;
    private int resType;
    private Integer resWidth;
    private Integer resHeight;
    private Integer resSize;

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

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public Integer getResWidth() {
        return resWidth;
    }

    public void setResWidth(Integer resWidth) {
        this.resWidth = resWidth;
    }

    public Integer getResHeight() {
        return resHeight;
    }

    public void setResHeight(Integer resHeight) {
        this.resHeight = resHeight;
    }

    public Integer getResSize() {
        return resSize;
    }

    public void setResSize(Integer resSize) {
        this.resSize = resSize;
    }
}
