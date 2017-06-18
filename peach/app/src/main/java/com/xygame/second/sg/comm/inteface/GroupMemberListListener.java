package com.xygame.second.sg.comm.inteface;

import com.xygame.second.sg.personal.guanzhu.member.GroupMemberBean;

/**
 * Created by tony on 2016/9/2.
 */
public interface GroupMemberListListener {
    void cancelGZListener(GroupMemberBean blackMemberBean);
    void dividerListener(GroupMemberBean blackMemberBean);
}
