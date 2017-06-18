package com.xygame.second.sg.personal.guanzhu.group;

import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/10/8.
 */
public class GZ_GroupBeanTemp implements Serializable{
    private String id,name,subString;
    private boolean isSelect=false;
    public String getId() {
        return id;
    }
    private List<GroupMemberBean> tempDatas;

    public List<GroupMemberBean> getTempDatas() {
        return tempDatas;
    }

    public void setTempDatas(List<GroupMemberBean> tempDatas) {
        this.tempDatas = tempDatas;
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

    public String getSubString() {
        return subString;
    }

    public void setSubString(String subString) {
        this.subString = subString;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
