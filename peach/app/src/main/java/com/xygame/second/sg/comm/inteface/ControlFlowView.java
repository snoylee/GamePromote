package com.xygame.second.sg.comm.inteface;

import com.xygame.sg.im.SGNewsBean;

/**
 * Created by tony on 2016/9/10.
 */
public interface ControlFlowView {
    void flowViewStatus(boolean flag, String actionId, String memberId, SGNewsBean item);
}
