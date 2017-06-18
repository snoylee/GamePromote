package com.xygame.sg.bean.comm;

import java.io.Serializable;

/**
 * Created by tony on 2017/2/14.
 */
public class TimerCountBean implements Serializable{
    private String id,groupId,startTime,duringLength,userId,orderExpireTime,payExpireTime;

    public String getOrderExpireTime() {
        return orderExpireTime;
    }

    public void setOrderExpireTime(String orderExpireTime) {
        this.orderExpireTime = orderExpireTime;
    }

    public String getPayExpireTime() {
        return payExpireTime;
    }

    public void setPayExpireTime(String payExpireTime) {
        this.payExpireTime = payExpireTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuringLength() {
        return duringLength;
    }

    public void setDuringLength(String duringLength) {
        this.duringLength = duringLength;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
