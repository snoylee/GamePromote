package com.xygame.second.sg.xiadan.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/12/28.
 */
public class ReasonBean implements Serializable {
    private String content,id;
    private boolean isSelect;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
