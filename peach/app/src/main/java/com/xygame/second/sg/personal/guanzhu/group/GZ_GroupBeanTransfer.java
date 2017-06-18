package com.xygame.second.sg.personal.guanzhu.group;

import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/10/8.
 */
public class GZ_GroupBeanTransfer implements Serializable {
    private GZ_GroupBeanTemp currBean;
    private List<GZ_GroupBeanTemp> alldatas;
    private List<GroupMemberBean> selectMembers0;

    public List<GroupMemberBean> getSelectMembers0() {
        return selectMembers0;
    }

    public void setSelectMembers0(List<GroupMemberBean> selectMembers0) {
        this.selectMembers0 = selectMembers0;
    }

    public GZ_GroupBeanTemp getCurrBean() {
        return currBean;
    }

    public void setCurrBean(GZ_GroupBeanTemp currBean) {
        this.currBean = currBean;
    }

    public List<GZ_GroupBeanTemp> getAlldatas() {
        return alldatas;
    }

    public void setAlldatas(List<GZ_GroupBeanTemp> alldatas) {
        this.alldatas = alldatas;
    }
}
