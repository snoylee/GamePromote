package com.xygame.second.sg.comm.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/9/2.
 */
public class NoticeStatusManagerBean implements Serializable {
    private int chatNotify=1,orderNotify=1,sysNotify=1;//1是开，2是关

    public int getChatNotify() {
        return chatNotify;
    }

    public void setChatNotify(int chatNotify) {
        this.chatNotify = chatNotify;
    }

    public int getOrderNotify() {
        return orderNotify;
    }

    public void setOrderNotify(int orderNotify) {
        this.orderNotify = orderNotify;
    }

    public int getSysNotify() {
        return sysNotify;
    }

    public void setSysNotify(int sysNotify) {
        this.sysNotify = sysNotify;
    }
}
