package com.xygame.second.sg.comm.inteface;

import com.xygame.second.sg.xiadan.activity.godlist.BlackMemberBean;

/**
 * Created by tony on 2016/9/2.
 */
public interface GodListListener {
    void cancelBlackListListener(BlackMemberBean blackMemberBean);
    void playGodVoice(BlackMemberBean blackMemberBean);
    void playGodVideo(BlackMemberBean blackMemberBean);
}
