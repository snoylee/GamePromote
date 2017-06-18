package com.xygame.second.sg.Group.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/10/14.
 */
public class GroupNoticeTip implements Serializable {
    private String groupId,groupTip;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTip() {
        return groupTip;
    }

    public void setGroupTip(String groupTip) {
        this.groupTip = groupTip;
    }
}
