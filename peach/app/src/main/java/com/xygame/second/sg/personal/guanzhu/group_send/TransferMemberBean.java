package com.xygame.second.sg.personal.guanzhu.group_send;

import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/10/10.
 */
public class TransferMemberBean implements Serializable {
    private List<MemberBean> vector;

    private  List<GroupMemberBean> selectMembers;

    public List<GroupMemberBean> getSelectMembers() {
        return selectMembers;
    }

    public void setSelectMembers(List<GroupMemberBean> selectMembers) {
        this.selectMembers = selectMembers;
    }

    public List<MemberBean> getVector() {
        return vector;
    }

    public void setVector(List<MemberBean> vector) {
        this.vector = vector;
    }
}
