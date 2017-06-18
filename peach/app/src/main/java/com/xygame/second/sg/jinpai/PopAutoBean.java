package com.xygame.second.sg.jinpai;

import java.io.Serializable;

/**
 * Created by tony on 2016/7/19.
 */
public class PopAutoBean implements Serializable {
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
