package com.xygame.second.sg.Group;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xygame.second.sg.Group.bean.BlackGroupBean;
import com.xygame.second.sg.Group.bean.GroupNoticeMessageBean;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.provider.BlackGroupMetaData;
import com.xygame.sg.provider.GroupNoticeMetaData;
import com.xygame.sg.provider.SGNewsMetaData;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/7.
 */
public class GroupNewNoticeEngeer {

    public static void inserBlackGroup(Context context,String userId,String groupId) {
        ContentValues values = new ContentValues();
        values.put(BlackGroupMetaData.BlackGroupTableMetaData.USER_ID, userId);
        values.put(BlackGroupMetaData.BlackGroupTableMetaData.GROUP_ID, groupId);
        context.getContentResolver().insert(BlackGroupMetaData.BlackGroupTableMetaData.CONTENT_URI, values);
    }

    public static List<BlackGroupBean> quaryBlackGroups(Context context, String userId) {
        List<BlackGroupBean> lists = new ArrayList<>();
        BlackGroupBean item;
        Cursor c = context.getContentResolver().query(BlackGroupMetaData.BlackGroupTableMetaData.CONTENT_URI,
                new String[]{
                        BlackGroupMetaData.BlackGroupTableMetaData.USER_ID,
                        BlackGroupMetaData.BlackGroupTableMetaData.GROUP_ID},
                "userId=?", new String[]{userId}, BlackGroupMetaData.BlackGroupTableMetaData.DEFAULT_SORT_ORDER);
        for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
            item = new BlackGroupBean();
            item.setGroupId(c.getString(c.getColumnIndex(BlackGroupMetaData.BlackGroupTableMetaData.GROUP_ID)));
            lists.add(item);
        }
        c.close();
        return lists;
    }

    public static void inserGroupNoticeMsg(Context context, GroupNoticeMessageBean item) {
        ContentValues values = new ContentValues();
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.PACKET_ID, item.getPacketId());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.USER_ID, item.getUserId());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.GROUP_JID, item.getGroupJid());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.SEND_USER_ID, item.getSendUserId());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TYPE, item.getMsgType());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TIMER, item.getMsgTimer());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_CONTENT, item.getMsgContent());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.NOTICE_JSON, item.getNoticeJson());
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS, item.getMsgStatus());
        context.getContentResolver().insert(GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_URI, values);
    }

    public static void deleteGroupNoticeMsg(Context context,String userId, String groupJid) {
        context.getContentResolver().delete( GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_URI, "groupJid=? and userId=?", new String[]{groupJid, userId});
    }

    public static List<GroupNoticeMessageBean> quaryGroupNoticeMsg(Context context, String userId, String groupJid) {
        List<GroupNoticeMessageBean> lists = new ArrayList<>();
        GroupNoticeMessageBean item;
        Cursor c = context.getContentResolver().query(GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_URI,
                new String[]{GroupNoticeMetaData.GroupNoticeTableMetaData._ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.PACKET_ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.USER_ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.GROUP_JID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.SEND_USER_ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TYPE,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TIMER,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_CONTENT,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.NOTICE_JSON,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS},
                "groupJid=? and userId=?", new String[]{groupJid, userId}, GroupNoticeMetaData.GroupNoticeTableMetaData.DEFAULT_SORT_ORDER);
        for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
            item = new GroupNoticeMessageBean();
            item.set_id(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData._ID)));
            item.setPacketId(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.PACKET_ID)));
            item.setGroupJid(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.GROUP_JID)));
            item.setMsgContent(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_CONTENT)));
            item.setMsgTimer(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TIMER)));
            item.setMsgType(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TYPE)));
            item.setNoticeJson(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.NOTICE_JSON)));
            item.setSendUserId(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.SEND_USER_ID)));
            item.setUserId(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.USER_ID)));
            item.setMsgStatus(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS)));
            lists.add(item);
        }
        c.close();
        return lists;
    }

    public static void updatMsgStatus(Context context, GroupNoticeMessageBean item) {
        ContentValues values = new ContentValues();
        values.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS, item.getMsgStatus());
        context.getContentResolver().update(GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_URI, values, "userId=? and msgTimer=?", new String[]{UserPreferencesUtil.getUserId(context),item.getMsgTimer()});
    }

    public static GroupNoticeMessageBean quaryGroupMsgItem(Context context, String userId, String packetId) {
        GroupNoticeMessageBean item=null;
        Cursor c = context.getContentResolver().query(GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_URI,
                new String[]{GroupNoticeMetaData.GroupNoticeTableMetaData._ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.PACKET_ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.USER_ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.GROUP_JID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.SEND_USER_ID,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TYPE,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TIMER,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_CONTENT,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.NOTICE_JSON,
                        GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS},
                "userId=? and packetId=?", new String[]{ userId,packetId}, GroupNoticeMetaData.GroupNoticeTableMetaData.DEFAULT_SORT_ORDER);
        for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
            item = new GroupNoticeMessageBean();
            item.set_id(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData._ID)));
            item.setPacketId(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.PACKET_ID)));
            item.setGroupJid(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.GROUP_JID)));
            item.setMsgContent(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_CONTENT)));
            item.setMsgTimer(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TIMER)));
            item.setMsgType(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TYPE)));
            item.setNoticeJson(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.NOTICE_JSON)));
            item.setSendUserId(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.SEND_USER_ID)));
            item.setUserId(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.USER_ID)));
            item.setMsgStatus(c.getString(c.getColumnIndex(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS)));
        }
        c.close();
        return item;
    }
}
