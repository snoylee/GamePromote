package com.xygame.second.sg.personal.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/11/16.
 */
public class UserBeanInfo implements Serializable {
    private String userName,userImage,userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
