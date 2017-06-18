package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/12.
 */
public class ShootCondBean implements Serializable {
    /**
     * 开始时间（不限就不传）long
     */
    private String startTime;
    /**
     * 结束时间（不限就不传）long
     */
    private String endTime;
    /**
     * 地点（省）（不限：不传或-1)
     */
    private int province = -1;
    /**
     * 地点（市）（不限：不传或-1)
     */
    private int city = -1;

    public ShootCondBean() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }
}
