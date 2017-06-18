package com.xygame.second.sg.personal.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/9/1.
 */
public class GuanZhuFansBean implements Serializable {
    private String age,gender,introDesc,userIcon,userId,usernick,hasAttent,visitTime;

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getHasAttent() {
        return hasAttent;
    }

    public void setHasAttent(String hasAttent) {
        this.hasAttent = hasAttent;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntroDesc() {
        return introDesc;
    }

    public void setIntroDesc(String introDesc) {
        this.introDesc = introDesc;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

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
