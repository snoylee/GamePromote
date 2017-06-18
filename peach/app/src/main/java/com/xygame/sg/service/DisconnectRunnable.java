package com.xygame.sg.service;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2016/4/8.
 */
public class DisconnectRunnable implements Runnable{
    @Override
    public void run() {
        if(SGApplication.getInstance().getConnection()!=null){
            if (SGApplication.getInstance().getConnection().isConnected()){
                SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.unavailable));
            }
            SGApplication.getInstance().getConnection().disconnect();
        }
    }
}
