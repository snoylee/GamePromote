package com.xygame.sg.activity.model.bean;

import java.io.Serializable;

/**
 * Created by xy on 2016/1/17.
 */
public class ModelBean implements Serializable {
    private String userId;
    private String userNick;
    private String userIcon;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
