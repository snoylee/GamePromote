package com.xygame.second.sg.jinpai.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/7/13.
 */
public class JinPaiBigTypeBean implements Serializable {
    private String name;
    private String id;
    private String url;
    private String subStr;
    private String categoryName;
    private boolean isSelect=false;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubStr() {
        return subStr;
    }

    public void setSubStr(String subStr) {
        this.subStr = subStr;
    }
}
