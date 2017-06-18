package com.xygame.second.sg.personal.bean;

import java.util.List;

/**
 * Created by tony on 2016/9/3.
 */
public class PhotoListBean {
    private String timerDesc;
    private List<PhotoBean> photoes;

    public String getTimerDesc() {
        return timerDesc;
    }

    public void setTimerDesc(String timerDesc) {
        this.timerDesc = timerDesc;
    }

    public List<PhotoBean> getPhotoes() {
        return photoes;
    }

    public void setPhotoes(List<PhotoBean> photoes) {
        this.photoes = photoes;
    }
}
