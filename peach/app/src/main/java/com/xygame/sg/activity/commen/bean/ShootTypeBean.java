package com.xygame.sg.activity.commen.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/12/23.
 */
public class ShootTypeBean implements Serializable{
    private int typeId;
    private String typeName;
    private String iconDefaultUrl;
    private String iconSelectedUrl;
    private String noticeListBg;
    private List<ShootSubTypeBean> subTypes = new ArrayList<ShootSubTypeBean>();

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

    public String getIconDefaultUrl() {
        return iconDefaultUrl;
    }

    public void setIconDefaultUrl(String iconDefaultUrl) {
        this.iconDefaultUrl = iconDefaultUrl;
    }

    public String getIconSelectedUrl() {
        return iconSelectedUrl;
    }

    public void setIconSelectedUrl(String iconSelectedUrl) {
        this.iconSelectedUrl = iconSelectedUrl;
    }

    public String getNoticeListBg() {
        return noticeListBg;
    }

    public void setNoticeListBg(String noticeListBg) {
        this.noticeListBg = noticeListBg;
    }

    public List<ShootSubTypeBean> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<ShootSubTypeBean> subTypes) {
        this.subTypes = subTypes;
    }
}
