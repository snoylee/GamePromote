package com.xy.im.util;

import android.content.Context;

import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 2016/11/17.
 */
public class EixstMultiRoomsUtils {

    public static void addMutiRoomsItem(MultiUserChat chat,String groupId,Context context){
        Map<String,MultiUserChat> eixstMultiRooms= CacheService.getInstance().getCacheGroupRoomDatas(UserPreferencesUtil.getUserId(context));
        if (eixstMultiRooms==null){
            eixstMultiRooms=new HashMap<>();
        }
        eixstMultiRooms.put(groupId,chat);
        CacheService.getInstance().cacheGroupRoomDatas(UserPreferencesUtil.getUserId(context), eixstMultiRooms);
    }

    public static Map<String,MultiUserChat> getAllMutiRooms(Context context){
        Map<String,MultiUserChat> eixstMultiRooms=CacheService.getInstance().getCacheGroupRoomDatas(UserPreferencesUtil.getUserId(context));
        if (eixstMultiRooms==null){
            eixstMultiRooms=new HashMap<>();
        }
        return eixstMultiRooms;
    }

    public static MultiUserChat getMultiUserChat(String groupId,Context context){
        MultiUserChat multiUserChat=null;
        Map<String,MultiUserChat> eixstMultiRooms= CacheService.getInstance().getCacheGroupRoomDatas(UserPreferencesUtil.getUserId(context));
        if (eixstMultiRooms!=null){
            if (eixstMultiRooms.containsKey(groupId)){
                multiUserChat=eixstMultiRooms.get(groupId);
            }
        }
        return multiUserChat;
    }

    public static void deleMultiUserChat(String groupId,Context context){
        Map<String,MultiUserChat> eixstMultiRooms= CacheService.getInstance().getCacheGroupRoomDatas(UserPreferencesUtil.getUserId(context));
        if (eixstMultiRooms!=null){
            if (eixstMultiRooms.containsKey(groupId)){
                eixstMultiRooms.remove(groupId);
            }
        }
        CacheService.getInstance().cacheGroupRoomDatas(UserPreferencesUtil.getUserId(context),eixstMultiRooms);
    }
}
