package com.xygame.second.sg.personal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/6/15.
 */
public class StoneMXBean implements Serializable {

    private List<StoneMXItemBean> flows;

    private Long reqTime;

    public List<StoneMXItemBean> getFlows() {
        return flows;
    }

    public void setFlows(List<StoneMXItemBean> flows) {
        this.flows = flows;
    }

    public Long getReqTime() {
        return reqTime;
    }

    public void setReqTime(Long reqTime) {
        this.reqTime = reqTime;
    }
}
