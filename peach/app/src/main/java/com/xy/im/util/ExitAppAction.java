package com.xy.im.util;

import android.content.Context;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;
import java.util.Map;

/**
 * Created by tony on 2016/11/17.
 */
public class ExitAppAction implements Runnable{
    private Context context;
    public ExitAppAction(Context context){
        this.context=context;
    }
    @Override
    public void run() {
        Map<String,MultiUserChat> multiUserChatMap= EixstMultiRoomsUtils.getAllMutiRooms(context);
        List<GroupBean> disgroupDatas= CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(context));
        List<GroupBean> groupDatas=CacheService.getInstance().getCacheGroupDatas(UserPreferencesUtil.getUserId(context));
        if (disgroupDatas!=null){
            for (int i=0;i<disgroupDatas.size();i++){
                GroupBean item=disgroupDatas.get(i);
                GroupBean temp = GroupEngine.quaryGroupBean(context, item, UserPreferencesUtil.getUserId(context));
                item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
                if (temp == null) {
                    GroupEngine.inserGroup(context, item);
                }
//                if (temp != null) {
//                    GroupEngine.updateGroupLastTime(context, item, UserPreferencesUtil.getUserId(context));
//                } else {
//                    GroupEngine.inserGroup(context, item);
//                }
                MultiUserChat multiUserChat=multiUserChatMap.get(item.getGroupId());
                if (multiUserChat!=null){
                    if (SGApplication.getInstance().getConnection().isConnected()) {
                        multiUserChat.leave();
                    }
                }
            }
        }

        if (groupDatas!=null){
            for (int i=0;i<groupDatas.size();i++){
                GroupBean item=groupDatas.get(i);
                GroupBean temp = GroupEngine.quaryGroupBean(context, item, UserPreferencesUtil.getUserId(context));
                item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
                if (temp == null) {
                    GroupEngine.inserGroup(context, item);
                }
//                if (temp != null) {
//                    GroupEngine.updateGroupLastTime(context, item, UserPreferencesUtil.getUserId(context));
//                } else {
//                    GroupEngine.inserGroup(context, item);
//                }
                MultiUserChat multiUserChat=multiUserChatMap.get(item.getGroupId());
                if (multiUserChat!=null){
                    if (SGApplication.getInstance().getConnection().isConnected()) {
                        multiUserChat.leave();
                    }
                }
            }
        }

        if(SGApplication.getInstance().getConnection()!=null){
            if (SGApplication.getInstance().getConnection().isConnected()){
                SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.unavailable));
            }
            SGApplication.getInstance().getConnection().disconnect();
        }
    }
}