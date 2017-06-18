package com.xygame.second.sg.Group.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/9/5.
 */
public class GoupNoticeBean implements Serializable {
    private String groupType,groupId,groupName,noticeCount;

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(String noticeCount) {
        this.noticeCount = noticeCount;
    }
}
