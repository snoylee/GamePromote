package com.xygame.second.sg.personal.guanzhu.member;

import java.io.Serializable;

/**
 * Created by tony on 2016/10/9.
 */
public class GroupMemberBean implements Serializable {
    public SlideView slideView;
    private String age,gender,userIcon,userId,usernick;
    private boolean isAlivable=true;

    public boolean isAlivable() {
        return isAlivable;
    }

    public void setIsAlivable(boolean isAlivable) {
        this.isAlivable = isAlivable;
    }

    private boolean isSelect=false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    private String sortLetters;  //显示数据拼音的首字母


    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
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
