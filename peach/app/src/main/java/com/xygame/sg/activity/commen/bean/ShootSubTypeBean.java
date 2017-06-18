package com.xygame.sg.activity.commen.bean;

import java.io.Serializable;

/**
 * Created by xy on 2015/12/23.
 */
public class ShootSubTypeBean implements Serializable{
    private int typeId;
    private String typeName;

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
}
