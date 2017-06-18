package com.xygame.sg.activity.model.bean;

import java.io.Serializable;

/**
 * Created by xy on 2016/1/17.
 */
public class ShootTypeBean implements Serializable {
    private int typeId;
    private String typeName;
    private String icon_url;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
