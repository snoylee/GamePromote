package com.xygame.second.sg.Group.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/9/8.
 */
public class GroupBean implements Serializable {

    private String groupId,groupoType,lastIntoTimer,userId,_id,groupName,createUserId;

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastIntoTimer() {
        return lastIntoTimer;
    }

    public void setLastIntoTimer(String lastIntoTimer) {
        this.lastIntoTimer = lastIntoTimer;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupoType() {
        return groupoType;
    }

    public void setGroupoType(String groupoType) {
        this.groupoType = groupoType;
    }
}
