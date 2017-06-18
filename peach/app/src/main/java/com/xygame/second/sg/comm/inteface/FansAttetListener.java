package com.xygame.second.sg.comm.inteface;

import com.xygame.second.sg.personal.bean.GuanZhuFansBean;

/**
 * Created by tony on 2016/9/1.
 */
public interface FansAttetListener {
    void attetionComfim(GuanZhuFansBean bean);
    void attetionCancel(GuanZhuFansBean bean);
}
