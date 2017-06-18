package com.xygame.sg.im;

import java.io.Serializable;

/**
 * Created by tony on 2016/1/14.
 */
public class ToChatBean implements Serializable{
    private String noticeId,recruitLocIndex,noticeSubject,userId,userIcon,usernick,recruitId;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getRecruitLocIndex() {
        return recruitLocIndex;
    }

    public void setRecruitLocIndex(String recruitLocIndex) {
        this.recruitLocIndex = recruitLocIndex;
    }

    public String getNoticeSubject() {
        return noticeSubject;
    }

    public void setNoticeSubject(String noticeSubject) {
        this.noticeSubject = noticeSubject;
    }

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

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

	public String getRecruitId() {
		return recruitId;
	}

	public void setRecruitId(String recruitId) {
		this.recruitId = recruitId;
	}
    
    
}
