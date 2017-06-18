package com.xygame.second.sg.personal.guanzhu.group_send;

import com.xygame.second.sg.personal.guanzhu.member.SlideView;

import java.io.Serializable;

/**
 * Created by tony on 2016/10/9.
 */
public class MemberBean implements Serializable {
    private String age,gender,userIcon,userId,usernick;
    private boolean isSelected=false;
    private String sortLetters;  //显示数据拼音的首字母

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

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
