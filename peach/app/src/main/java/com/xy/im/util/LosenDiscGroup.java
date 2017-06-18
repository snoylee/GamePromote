package com.xy.im.util;

import android.content.Context;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;

/**
 * Created by tony on 2016/11/17.
 */
public class LosenDiscGroup implements Runnable{
    private Context context;
    private String groupId;
    public LosenDiscGroup(Context context,String groupId){
        this.context=context;
        this.groupId=groupId;
    }
    @Override
    public void run() {
        List<GroupBean> groupDatas= CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(context));
        if (groupDatas!=null){
            for (int i=0;i<groupDatas.size();i++){
                if (groupId.equals(groupDatas.get(i).getGroupId())){
                    GroupEngine.inserDeleGroup(context, groupDatas.get(i));
                    groupDatas.remove(i);
                    break;
                }
            }
            CacheService.getInstance().cacheDiscGroupDatas(UserPreferencesUtil.getUserId(context),groupDatas);
        }
        MultiUserChat chat=EixstMultiRoomsUtils.getMultiUserChat(groupId,context);
        if(SGApplication.getInstance().getConnection().isConnected()){
            if (chat!=null){
                try {
                    String serviceName = SGApplication.getInstance().getConnection().getServiceName();
                    String roomId =groupId.concat("@").concat("conference.").concat(serviceName);
                    chat.destroy("销毁房间", roomId);
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }
        EixstMultiRoomsUtils.deleMultiUserChat(groupId,context);
    }
}
