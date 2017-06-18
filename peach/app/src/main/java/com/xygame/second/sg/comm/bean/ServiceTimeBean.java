package com.xygame.second.sg.comm.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/11.
 */
public class ServiceTimeBean implements Serializable {
    private String id,time;
    private boolean isSelect,isUnUsed=false;
    private int index;

    private boolean isUsed,isUsing;

    public boolean isUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setIsUsing(boolean isUsing) {
        this.isUsing = isUsing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isUnUsed() {
        return isUnUsed;
    }

    public void setIsUnUsed(boolean isUnUsed) {
        this.isUnUsed = isUnUsed;
    }
}
