package com.xygame.sg.activity.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xy on 2016/1/17.
 */
public class AllModelItemBean implements Serializable {
    private String userId;
    private String usernick;
    private String userIcon;
    private Integer userType;
    private Integer authStatus;//用户状态  0：未申请认证，1：审核中，2：审核通过，3：审核拒绝
    private List<ShootTypeBean> shootType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public List<ShootTypeBean> getShootType() {
        return shootType;
    }

    public void setShootType(List<ShootTypeBean> shootType) {
        this.shootType = shootType;
    }
}
