package com.xygame.sg.activity.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class AllModelBean implements Serializable{
    private Long reqTime;
    private List<AllModelItemBean> models;

    public List<AllModelItemBean> getModels() {
        return models;
    }

    public void setModels(List<AllModelItemBean> models) {
        this.models = models;
    }

    public Long getReqTime() {
        return reqTime;
    }

    public void setReqTime(Long reqTime) {
        this.reqTime = reqTime;
    }
}
