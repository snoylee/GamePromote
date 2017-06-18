package com.xygame.sg.bean.circle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/5/20.
 */
public class CircleDatas implements Serializable {
    private long reqTime;
    private List<CircleBean> moods;

    public long getReqTime() {
        return reqTime;
    }

    public void setReqTime(long reqTime) {
        this.reqTime = reqTime;
    }

    public List<CircleBean> getMoods() {
        return moods;
    }

    public void setMoods(List<CircleBean> moods) {
        this.moods = moods;
    }
}
