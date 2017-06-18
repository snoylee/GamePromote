package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;

/**
 * Created by xy on 2016/1/6.
 */
public class NotifySettingBean implements Serializable {
    /**
     * 通告消息（1：开，2：关）
     */
    private int noticeNotify = 1;
    private int chatNotify = 1;
    private int visitorNotify = 1;

    public int getNoticeNotify() {
        return noticeNotify;
    }

    public void setNoticeNotify(int noticeNotify) {
        this.noticeNotify = noticeNotify;
    }

    public int getChatNotify() {
        return chatNotify;
    }

    public void setChatNotify(int chatNotify) {
        this.chatNotify = chatNotify;
    }

    public int getVisitorNotify() {
        return visitorNotify;
    }

    public void setVisitorNotify(int visitorNotify) {
        this.visitorNotify = visitorNotify;
    }
}
