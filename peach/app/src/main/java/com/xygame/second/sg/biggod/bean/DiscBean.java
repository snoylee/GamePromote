package com.xygame.second.sg.biggod.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/12/21.
 */
public class DiscBean implements Serializable{
    private String id,oral;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOral() {
        return oral;
    }

    public void setOral(String oral) {
        this.oral = oral;
    }
}
