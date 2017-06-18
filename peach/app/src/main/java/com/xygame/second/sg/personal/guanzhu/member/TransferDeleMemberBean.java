package com.xygame.second.sg.personal.guanzhu.member;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tony on 2016/10/11.
 */
public class TransferDeleMemberBean implements Serializable {
    private List<String> deleteMembers;

    public List<String> getDeleteMembers() {
        return deleteMembers;
    }

    public void setDeleteMembers(List<String> deleteMembers) {
        this.deleteMembers = deleteMembers;
    }
}
