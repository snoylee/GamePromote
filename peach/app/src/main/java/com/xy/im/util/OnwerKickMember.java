package com.xy.im.util;

import android.content.Context;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.personal.bean.UserBeanInfo;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;

/**
 * Created by tony on 2016/11/17.
 */
public class OnwerKickMember implements Runnable{
    private Context context;
    private SGNewsBean sendBean;
    private List<UserBeanInfo> userBeanInfos;
    public OnwerKickMember(Context context, SGNewsBean sendBean, List<UserBeanInfo> userBeanInfos){
        this.context=context;
        this.sendBean=sendBean;
        this.userBeanInfos=userBeanInfos;
    }
    @Override
    public void run() {
        MultiUserChat chat=EixstMultiRoomsUtils.getMultiUserChat(sendBean.getNoticeId(),context);
        if(SGApplication.getInstance().getConnection().isConnected()){
            if (chat!=null){
                for (UserBeanInfo item:userBeanInfos){
                    try {
                        chat.kickParticipant(item.getUserId(),"无理由拒绝");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
