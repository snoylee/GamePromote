package com.xygame.sg.activity.model.bean;

import java.io.Serializable;

/**
 * Created by moreidols on 16/3/15.
 */
public class Param implements Serializable {
    private Integer actId;
    private Integer joinType;

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }
}
