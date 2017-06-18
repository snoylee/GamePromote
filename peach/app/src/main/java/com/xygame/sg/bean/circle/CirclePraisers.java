package com.xygame.sg.bean.circle;

import java.io.Serializable;

/**
 * Created by tony on 2016/5/20.
 */
public class CirclePraisers implements Serializable {
    private String userId;
    private String userIcon;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }
}
