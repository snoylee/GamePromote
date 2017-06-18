package com.xygame.sg.im;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xygame.sg.provider.SGNewsMetaData;
import com.xygame.sg.provider.TempGroupNewsMetaData;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempGroupNewsEngine {

	public static void inserChatNew(Context context, SGNewsBean item) {
		ContentValues values = new ContentValues();
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID, item.getUserId());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.FRIEND_USERID, item.getFriendUserId());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, item.getIsShow());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS, item.getMessageStatus());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS, item.getMsgStatus());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.TYPE, item.getType());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, item.getFromUser());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER, item.getToUser());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT, item.getNoticeSubject());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX, item.getRecruitLocIndex());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT, item.getInout());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, item.getTimestamp());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID, item.getNoticeId());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, item.getRecruitId());
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE, item.getNewType());
		context.getContentResolver().insert(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, values);
	}


	public static List<SGNewsBean> loadChatNews(Context context, String userId) {
		Map<String,SGNewsBean> midleMap=new HashMap<>();
		List<SGNewsBean> resultDatas = new ArrayList<SGNewsBean>();
		List<SGNewsBean> quaryDatas = quaryChatNews(context, userId);
		for (SGNewsBean it : quaryDatas) {
			String mapKey=it.getNoticeId();
			SGNewsBean mapValue=midleMap.get(mapKey);
			if (mapValue == null) {
				midleMap.put(mapKey,it);
				resultDatas.add(it);
			}
		}
		return resultDatas;
	}

	public static List<SGNewsBean> quaryChatNews(Context context, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				new String[]{TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
						TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
						TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
						TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE},
				"newType=? and userId=? and isShow=?",
				new String[]{Constants.GROUP_CHAT, userId, Constants.NEWS_SHOW},
				TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS)));
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static List<SGNewsBean> quaryChatHistory(Context context, SGNewsBean sgBean, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				new String[]{TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
						TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
						TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
						TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE},
				"newType=? and noticeId=? and userId=?",
				new String[]{Constants.GROUP_CHAT, sgBean.getNoticeId(),userId},
				TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS)));
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static int quaryUnReadChatNewsByRecruitLocIndex(Context context, SGNewsBean item, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				new String[]{TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
						TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
						TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
						TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE},
				"messageStatus=? and newType=? and userId=? and noticeId=?",
				new String[]{Constants.NEWS_UNREAD, Constants.GROUP_CHAT, userId, item.getNoticeId()},
				TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}

	public static int quaryUnReadChatNews(Context context, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				new String[]{TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
						TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
						TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
						TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE},
				"messageStatus=? and userId=?",
				new String[]{Constants.NEWS_UNREAD, userId},
				TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}

	public static void updateGroupName(Context context, String discGroupName,String groupId, String userId) {
		ContentValues values = new ContentValues();
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT, discGroupName);
		context.getContentResolver().update(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, values,
				"noticeId=? and userId=?",
				new String[]{groupId, userId});
	}

	public static void updateChatNewsStatus(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS, Constants.NEWS_READ);
		context.getContentResolver().update(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, values,
				"noticeId=? and userId=?",
				new String[]{item.getNoticeId(), userId});
	}

	public static void updateChatSendStatus(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS, item.getMsgStatus());
		context.getContentResolver().update(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, values,
				"timestamp=? and noticeId=?",
				new String[]{item.getTimestamp(), item.getNoticeId()});
	}

	public static void updateChatSendStatusForImages(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS, item.getMsgStatus());
		context.getContentResolver().update(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, values,
				"recruitLocIndex=? and noticeId=?",
				new String[]{item.getRecruitLocIndex(), item.getNoticeId()});
	}

	public static void updateChatSendContent(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		context.getContentResolver().update(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, values,
				"noticeId=? and timestamp=?",
				new String[]{item.getNoticeId(), item.getTimestamp()});
	}

	public static void updateHideOrShowChatItem(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, item.getIsShow());
		context.getContentResolver().update(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, values,
				"newType=? and noticeId=? and userId=?",
				new String[]{Constants.GROUP_CHAT, item.getNoticeId(), userId});
	}

	public static void deleteChatsHistory(Context context, SGNewsBean item,
										  String userId) {
		context.getContentResolver().delete(
				TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				"newType=? and friendUserId=? and userId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId});
	}

	public static void deleteDisHistory(Context context, SGNewsBean item,
										  String userId) {
		context.getContentResolver().delete(
				TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				"noticeId=? and userId=?",
				new String[]{item.getNoticeId(), userId});
	}

	public static List<SGNewsBean> loadSearchAllChatNews(Context context, String content, String userId) {
		List<SGNewsBean> resultDatas = new ArrayList<SGNewsBean>();
		SGNewsBean tempBean = null;
		List<SGNewsBean> quaryDatas = searchAllChatNews(context, content, userId);
		for (SGNewsBean it : quaryDatas) {
			if (tempBean != null) {
				if (!it.getFriendUserId().equals(tempBean.getFriendUserId())) {
					tempBean = it;
					resultDatas.add(it);
				}
			} else {
				tempBean = it;
				resultDatas.add(it);
			}
		}
		return resultDatas;
	}

	public static List<SGNewsBean> searchAllChatNews(Context context, String content, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				new String[]{TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
						TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
						TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
						TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE},
				"newType=? and userId=? and isShow=? and (noticeSubject like ? or msgContent like ?)", new String[]{
						Constants.GROUP_CHAT, userId, Constants.NEWS_SHOW, "%" + content + "%", "%" + content + "%"},
				TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData._ID)));
			item.setInout(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT)));
			item.setMsgStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS)));
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static SGNewsBean getSGNewBeanByFriendUserId(Context context, String userId, String friendUserId) {
		SGNewsBean item = null;
		Cursor c = context.getContentResolver().query(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				new String[]{TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
						TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
						TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
						TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE},
				"newType=? and userId=? and friendUserId=?", new String[]{Constants.GROUP_CHAT, userId, friendUserId},
				TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS)));
		}
		c.close();
		return item;
	}

	/**
	 * 动态消息的相关操作
	 */

	public static int quaryUnReadDaymicNews(Context context, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI,
				new String[]{TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW, TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
						TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT, TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
						TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER, TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
						TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
						TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP, TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
						TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID, TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE},
				"messageStatus=? and userId=?",
				new String[]{Constants.NEWS_UNREAD, userId},
				TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}
}
