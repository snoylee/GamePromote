package com.xygame.second.sg.comm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/8/11.
 */
public class ServiceTimeDateBean implements Serializable {
    private String date,weekened,id,dscStr;
    private List<ServiceTimeBean> timeBeans;
    private int fromPoint=0,toPoint=0;

    public int getFromPoint() {
        return fromPoint;
    }

    public void setFromPoint(int fromPoint) {
        this.fromPoint = fromPoint;
    }

    public int getToPoint() {
        return toPoint;
    }

    public void setToPoint(int toPoint) {
        this.toPoint = toPoint;
    }

    public List<ServiceTimeBean> getTimeBeans() {
        return timeBeans;
    }

    public void setTimeBeans(List<ServiceTimeBean> timeBeans) {
        this.timeBeans = timeBeans;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeekened() {
        return weekened;
    }

    public void setWeekened(String weekened) {
        this.weekened = weekened;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDscStr() {
        return dscStr;
    }

    public void setDscStr(String dscStr) {
        this.dscStr = dscStr;
    }
}
