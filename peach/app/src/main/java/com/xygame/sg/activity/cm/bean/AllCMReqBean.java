package com.xygame.sg.activity.cm.bean;

import com.xygame.sg.activity.notice.bean.PageBean;
import com.xygame.sg.activity.personal.bean.StyleBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class AllCMReqBean extends PageReq  {
    private Integer occupType;
    private Integer orderType;

    private CMCondBean cond = new CMCondBean();

    public Integer getOccupType() {
        return occupType;
    }

    public void setOccupType(Integer occupType) {
        this.occupType = occupType;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public CMCondBean getCond() {
        return cond;
    }

    public void setCond(CMCondBean cond) {
        this.cond = cond;
    }
}
