package com.xygame.sg.bean.comm;

import java.io.Serializable;

/**
 * Created by xy on 2016/1/18.
 */
public class ModelStyleBean implements Serializable {
    private int typeId;
    private String typeName;
    private int hueR;
    private int hueG;
    private int hueB;
    private int exclusType;

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

    public int getHueR() {
        return hueR;
    }

    public void setHueR(int hueR) {
        this.hueR = hueR;
    }

    public int getHueG() {
        return hueG;
    }

    public void setHueG(int hueG) {
        this.hueG = hueG;
    }

    public int getHueB() {
        return hueB;
    }

    public void setHueB(int hueB) {
        this.hueB = hueB;
    }

    public int getExclusType() {
        return exclusType;
    }

    public void setExclusType(int exclusType) {
        this.exclusType = exclusType;
    }
}
