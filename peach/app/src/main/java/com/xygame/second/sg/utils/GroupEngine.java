package com.xygame.second.sg.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.sg.activity.main.MainFrameActivity;
import com.xygame.sg.provider.DeleGroupMetaData;
import com.xygame.sg.provider.GroupMetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/12.
 */
public class GroupEngine {
    public static void inserGroup(Context context, GroupBean item) {
        ContentValues values = new ContentValues();
        values.put(GroupMetaData.GroupTableMetaData.USER_ID, item.getUserId());
        values.put(GroupMetaData.GroupTableMetaData.GROUP_ID, item.getGroupId());
        values.put(GroupMetaData.GroupTableMetaData.GROUP_TYPE, item.getGroupoType());
        values.put(GroupMetaData.GroupTableMetaData.TIMER_INTO, item.getLastIntoTimer());
        context.getContentResolver().insert(GroupMetaData.GroupTableMetaData.CONTENT_URI, values);
    }

    public static GroupBean quaryGroupBean(Context context, GroupBean sgBean, String userId) {
        GroupBean item = null;
        Cursor c = context.getContentResolver().query(GroupMetaData.GroupTableMetaData.CONTENT_URI,
                new String[]{GroupMetaData.GroupTableMetaData._ID,
                        GroupMetaData.GroupTableMetaData.USER_ID,
                        GroupMetaData.GroupTableMetaData.GROUP_ID,
                        GroupMetaData.GroupTableMetaData.GROUP_TYPE,
                        GroupMetaData.GroupTableMetaData.TIMER_INTO},
                "userId=? and groupId=?", new String[]{userId, sgBean.getGroupId()}, GroupMetaData.GroupTableMetaData.DEFAULT_SORT_ORDER);
        for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
            item = new GroupBean();
            item.set_id(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData._ID)));
            item.setGroupoType(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.GROUP_TYPE)));
            item.setGroupId(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.GROUP_ID)));
            item.setLastIntoTimer(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.TIMER_INTO)));
            item.setUserId(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.USER_ID)));
        }
        c.close();
        return item;
    }

    public static List<GroupBean> quaryGroups(Context context, String userId) {
        List<GroupBean> datas = new ArrayList<>();
        GroupBean item = null;
        Cursor c = context.getContentResolver().query(GroupMetaData.GroupTableMetaData.CONTENT_URI,
                new String[]{GroupMetaData.GroupTableMetaData._ID,
                        GroupMetaData.GroupTableMetaData.USER_ID,
                        GroupMetaData.GroupTableMetaData.GROUP_ID,
                        GroupMetaData.GroupTableMetaData.GROUP_TYPE,
                        GroupMetaData.GroupTableMetaData.TIMER_INTO},
                "userId=?", new String[]{userId}, GroupMetaData.GroupTableMetaData.DEFAULT_SORT_ORDER);
        for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
            item = new GroupBean();
            item.set_id(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData._ID)));
            item.setGroupoType(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.GROUP_TYPE)));
            item.setGroupId(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.GROUP_ID)));
            item.setLastIntoTimer(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.TIMER_INTO)));
            item.setUserId(c.getString(c.getColumnIndex(GroupMetaData.GroupTableMetaData.USER_ID)));
            datas.add(item);
        }
        c.close();
        return datas;
    }

    public static void updateGroupLastTime(Context context, GroupBean item, String userId) {
        ContentValues values = new ContentValues();
        values.put(GroupMetaData.GroupTableMetaData.TIMER_INTO, item.getLastIntoTimer());
        context.getContentResolver().update(GroupMetaData.GroupTableMetaData.CONTENT_URI, values, "userId=? and groupId=?", new String[]{userId, item.getGroupId()});
    }

    public static void deleteGroup(Context context, GroupBean item,
                                   String userId) {
        context.getContentResolver().delete(
                GroupMetaData.GroupTableMetaData.CONTENT_URI,
                "userId=? and groupId=?",
                new String[]{userId, item.getGroupId()});

    }

    public static void deleteGroupByGroupId(Context context, String groupId, String userId) {
        context.getContentResolver().delete(
                GroupMetaData.GroupTableMetaData.CONTENT_URI,
                "userId=? and groupId=?",
                new String[]{userId,groupId});
    }

    public static void inserDeleGroup(Context context, GroupBean item) {
        ContentValues values = new ContentValues();
        values.put(DeleGroupMetaData.GroupTableMetaData.USER_ID, item.getUserId());
        values.put(DeleGroupMetaData.GroupTableMetaData.GROUP_ID, item.getGroupId());
        values.put(DeleGroupMetaData.GroupTableMetaData.GROUP_TYPE, item.getGroupoType());
        values.put(DeleGroupMetaData.GroupTableMetaData.TIMER_INTO, item.getLastIntoTimer());
        context.getContentResolver().insert(DeleGroupMetaData.GroupTableMetaData.CONTENT_URI, values);
    }

    public static GroupBean quaryDeleGroupBeanByGroupId(Context context,  String groupId, String userId) {
        GroupBean item = null;
        Cursor c = context.getContentResolver().query(DeleGroupMetaData.GroupTableMetaData.CONTENT_URI,
                new String[]{DeleGroupMetaData.GroupTableMetaData._ID,
                        DeleGroupMetaData.GroupTableMetaData.USER_ID,
                        DeleGroupMetaData.GroupTableMetaData.GROUP_ID,
                        DeleGroupMetaData.GroupTableMetaData.GROUP_TYPE,
                        DeleGroupMetaData.GroupTableMetaData.TIMER_INTO},
                "userId=? and groupId=?", new String[]{userId, groupId}, DeleGroupMetaData.GroupTableMetaData.DEFAULT_SORT_ORDER);
        for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
            item = new GroupBean();
            item.set_id(c.getString(c.getColumnIndex(DeleGroupMetaData.GroupTableMetaData._ID)));
            item.setGroupoType(c.getString(c.getColumnIndex(DeleGroupMetaData.GroupTableMetaData.GROUP_TYPE)));
            item.setGroupId(c.getString(c.getColumnIndex(DeleGroupMetaData.GroupTableMetaData.GROUP_ID)));
            item.setLastIntoTimer(c.getString(c.getColumnIndex(DeleGroupMetaData.GroupTableMetaData.TIMER_INTO)));
            item.setUserId(c.getString(c.getColumnIndex(DeleGroupMetaData.GroupTableMetaData.USER_ID)));
        }
        c.close();
        return item;
    }

    public static void deleteDeleGroupByGroupId(Context context, String groupId, String userId) {
        context.getContentResolver().delete(
                DeleGroupMetaData.GroupTableMetaData.CONTENT_URI,
                "userId=? and groupId=?",
                new String[]{userId, groupId});
    }
}
