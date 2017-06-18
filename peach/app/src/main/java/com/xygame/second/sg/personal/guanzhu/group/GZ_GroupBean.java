package com.xygame.second.sg.personal.guanzhu.group;

import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/10/8.
 */
public class GZ_GroupBean implements Serializable{
    private String id,name,subString,count;
    public SlideView slideView;
    private String sortLetters;  //显示数据拼音的首字母

    private List<GroupMemberBean> tempDatas;

    public List<GroupMemberBean> getTempDatas() {
        return tempDatas;
    }

    public void setTempDatas(List<GroupMemberBean> tempDatas) {
        this.tempDatas = tempDatas;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

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

    public String getSubString() {
        return subString;
    }

    public void setSubString(String subString) {
        this.subString = subString;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
