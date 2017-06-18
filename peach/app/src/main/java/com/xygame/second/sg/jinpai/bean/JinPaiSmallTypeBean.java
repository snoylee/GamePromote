package com.xygame.second.sg.jinpai.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/7/14.
 */
public class JinPaiSmallTypeBean implements Serializable{
    private String typeName;
    private String typeId;
    private boolean isSelected;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
