package com.xygame.second.sg.jinpai.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/11/22.
 */
public class DuanWeiBean implements Serializable {
    private String id,name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
