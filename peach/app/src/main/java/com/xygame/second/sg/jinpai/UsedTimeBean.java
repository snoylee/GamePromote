package com.xygame.second.sg.jinpai;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/20.
 */
public class UsedTimeBean implements Serializable {

    private long bookStartTime=0,bookEndTime=0;

    public long getBookStartTime() {
        return bookStartTime;
    }

    public void setBookStartTime(long bookStartTime) {
        this.bookStartTime = bookStartTime;
    }

    public long getBookEndTime() {
        return bookEndTime;
    }

    public void setBookEndTime(long bookEndTime) {
        this.bookEndTime = bookEndTime;
    }
}
