package com.xygame.second.sg.jinpai;

import java.io.Serializable;

/**
 * Created by tony on 2016/7/19.
 */
public class PopSexBean implements Serializable {
    private String id,sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
