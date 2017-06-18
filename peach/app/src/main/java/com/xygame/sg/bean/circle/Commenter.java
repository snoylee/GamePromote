package com.xygame.sg.bean.circle;

import java.io.Serializable;

/**
 * Created by tony on 2016/5/24.
 */
public class Commenter implements Serializable {
    private String userId;
    private String usernick;

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
}
