package com.xygame.second.sg.comm.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/8/10.
 */
public class FreeTimeBean implements Serializable{
    private String name,id;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
