package com.xygame.sg.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.xygame.sg.provider.SGNewsMetaData;
import com.xygame.sg.provider.SGNoticeNewsMetaData;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.UserPreferencesUtil;

public class NewsEngine {

	public static void inserChatNew(Context context, SGNewsBean item) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.USER_ID, item.getUserId());
		values.put(SGNewsMetaData.SGNewsTableMetaData.FRIEND_USERID, item.getFriendUserId());
		values.put(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, item.getIsShow());
		values.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS, item.getMessageStatus());
		values.put(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS, item.getMsgStatus());
		values.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		values.put(SGNewsMetaData.SGNewsTableMetaData.TYPE, item.getType());
		values.put(SGNewsMetaData.SGNewsTableMetaData.FROM_USER, item.getFromUser());
		values.put(SGNewsMetaData.SGNewsTableMetaData.TO_USER, item.getToUser());
		values.put(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT, item.getNoticeSubject());
		values.put(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX, item.getRecruitLocIndex());
		values.put(SGNewsMetaData.SGNewsTableMetaData.IN_OUT, item.getInout());
		values.put(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, item.getTimestamp());
		values.put(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID, item.getNoticeId());
		values.put(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, item.getRecruitId());
		values.put(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE, item.getNewType());
		context.getContentResolver().insert(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values);
	}

	public static void inserNoticeChatNew(Context context, SGNewsBean item) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID, item.getUserId());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FRIEND_USERID, item.getFriendUserId());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW, item.getIsShow());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS, item.getMessageStatus());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS, item.getMsgStatus());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE, item.getType());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER, item.getFromUser());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER, item.getToUser());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT, item.getNoticeSubject());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX, item.getRecruitLocIndex());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT, item.getInout());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP, item.getTimestamp());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID, item.getNoticeId());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID, item.getRecruitId());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE, item.getNewType());
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.OPERATOR_FLAG, item.getOperatorFlag()==null?"":item.getOperatorFlag());
		context.getContentResolver().insert(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, values);
	}

	public static int quaryUnReadNoticeChatNews(Context context, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI,
				new String[]{SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER,SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE},
				"messageStatus=? and userId=?",
				new String[]{Constants.NEWS_UNREAD, userId},
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}

	public static void updateHideOrShowNoticeChatItem(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW, item.getIsShow());
		context.getContentResolver().delete(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, "newType=? and friendUserId=? and userId=? and recruitId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId, item.getRecruitId()});
	}

	public static List<SGNewsBean> quaryGroupNoticeChatHistory(Context context, SGNewsBean sgBean, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI,
				new String[]{SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.OPERATOR_FLAG},
				"newType=? and friendUserId=? and userId=? and recruitId=?",
				new String[]{Constants.NEWS_CHAT, sgBean.getFriendUserId(),userId,sgBean.getRecruitId()},
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS)));
			item.setOperatorFlag(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.OPERATOR_FLAG)));
			if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getFromUserIcon(item, context));
				item.setFriendUserId(SenderUser.getFromUserId(item, context));
				item.setFriendNickName(SenderUser.getFromUserName(item, context));
			} else if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getToUserIcon(item, context));
				item.setFriendUserId(SenderUser.getToUserId(item, context));
				item.setFriendNickName(SenderUser.getToUserName(item, context));
			}
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static int quaryUnReadNoticeChatNews(Context context, SGNewsBean item, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI,
				new String[]{SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE},
				"messageStatus=? and newType=? and userId=? and friendUserId=? and recruitId=?",
				new String[]{Constants.NEWS_UNREAD, Constants.NEWS_CHAT, userId, item.getFriendUserId(),item.getRecruitId()},
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}

	public static void deleteNoticeChatsHistory(Context context, SGNewsBean item,
										  String userId) {
		context.getContentResolver().delete(
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI,
				"newType=? and friendUserId=? and userId=? and recruitId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId, item.getRecruitId()});

	}

	public static void updateNoticeChatNewsStatus(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS, Constants.NEWS_READ);
		context.getContentResolver().update(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, values,
				"newType=? and friendUserId=? and userId=? and recruitId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId, item.getRecruitId()});
	}

	public static void updateNoticeChatNewsOperatorStatus(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.OPERATOR_FLAG, Constants.NEWS_OPERATOR);
		context.getContentResolver().update(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, values,
				"newType=? and friendUserId=? and userId=? and timestamp=? and recruitId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId,item.getTimestamp(),item.getRecruitId()});
	}

	public static void updateNoticeChatSendStatus(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS, item.getMsgStatus());
		context.getContentResolver().update(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, values,
				"timestamp=? and friendUserId=? and recruitId=?",
				new String[]{item.getTimestamp(), item.getFriendUserId(),item.getRecruitId()});
	}

	public static void updateNoticeChatSendStatusForImages(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS, item.getMsgStatus());
		context.getContentResolver().update(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, values,
				"recruitLocIndex=? and friendUserId=? and recruitId=?",
				new String[]{item.getRecruitLocIndex(), item.getFriendUserId(),item.getRecruitId()});
	}

	public static void updateNoticeChatSendContent(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		context.getContentResolver().update(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, values,
				"recruitId=? and friendUserId=? and timestamp=?",
				new String[]{item.getRecruitId(),item.getFriendUserId(), item.getTimestamp()});
	}

	public static void updateNoticeChatSendContentForImages(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		context.getContentResolver().update(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, values,
				"recruitId=? and newType=? and friendUserId=? and userId=? and recruitLocIndex=?",
				new String[]{item.getRecruitId(), Constants.NEWS_CHAT, item.getFriendUserId(), userId,
						item.getRecruitLocIndex()});
	}

	public static List<SGNewsBean> loadChatNews(Context context, String userId) {
		Map<String,SGNewsBean> midleMap=new HashMap<>();
		List<SGNewsBean> resultDatas = new ArrayList<SGNewsBean>();
		List<SGNewsBean> quaryDatas = quaryChatNews(context, userId);
		for (SGNewsBean it : quaryDatas) {
			String mapKey=it.getFriendUserId();
			SGNewsBean mapValue=midleMap.get(mapKey);
			if (mapValue == null) {
				midleMap.put(mapKey,it);
				resultDatas.add(it);
			}
		}
		SGNewsBean latestBean = quaryLatestDaymicNew(context, userId);
		if (latestBean != null) {
			resultDatas.add(latestBean);
		}
		return resultDatas;
	}

	public static List<SGNewsBean> loadNoticeChatNews(Context context, String userId) {
		Map<String,SGNewsBean> midleMap=new HashMap<>();
		List<SGNewsBean> resultDatas = new ArrayList<SGNewsBean>();
		List<SGNewsBean> quaryDatas = quaryNoticeChatNews(context, userId);
		for (SGNewsBean it : quaryDatas) {
			String mapKey=it.getRecruitId();
			SGNewsBean mapValue=midleMap.get(mapKey);
			if (mapValue == null) {
				midleMap.put(mapKey,it);
				resultDatas.add(it);
			}
		}
		return resultDatas;
	}

	public static List<SGNewsBean> quaryNoticeChatNews(Context context, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI,
				new String[]{SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID,
						SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE},
				"newType=? and userId=?",
				new String[]{Constants.NEWS_CHAT, userId},
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS)));
			if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getFromUserIcon(item, context));
				item.setFriendUserId(SenderUser.getFromUserId(item, context));
				item.setFriendNickName(SenderUser.getFromUserName(item, context));
			} else if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getToUserIcon(item, context));
				item.setFriendUserId(SenderUser.getToUserId(item, context));
				item.setFriendNickName(SenderUser.getToUserName(item, context));
			}
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static List<SGNewsBean> quaryChatNews(Context context, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=? and userId=? and isShow=?",
				new String[]{Constants.NEWS_CHAT, userId, Constants.NEWS_SHOW},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));
			if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getFromUserIcon(item, context));
				item.setFriendUserId(SenderUser.getFromUserId(item, context));
				item.setFriendNickName(SenderUser.getFromUserName(item, context));
			} else if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getToUserIcon(item, context));
				item.setFriendUserId(SenderUser.getToUserId(item, context));
				item.setFriendNickName(SenderUser.getToUserName(item, context));
			}
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static List<SGNewsBean> quaryChatHistory(Context context, SGNewsBean sgBean, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=? and friendUserId=? and userId=?",
				new String[]{Constants.NEWS_CHAT, sgBean.getFriendUserId(),userId},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));
			if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getFromUserIcon(item, context));
				item.setFriendUserId(SenderUser.getFromUserId(item, context));
				item.setFriendNickName(SenderUser.getFromUserName(item, context));
			} else if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getToUserIcon(item, context));
				item.setFriendUserId(SenderUser.getToUserId(item, context));
				item.setFriendNickName(SenderUser.getToUserName(item, context));
			}
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static int quaryUnReadChatNewsByRecruitLocIndex(Context context, SGNewsBean item, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"messageStatus=? and newType=? and userId=? and friendUserId=?",
				new String[]{Constants.NEWS_UNREAD, Constants.NEWS_CHAT, userId, item.getFriendUserId()},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}

	public static int quaryUnReadChatNews(Context context, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"messageStatus=? and newType=? and userId=?",
				new String[]{Constants.NEWS_UNREAD, Constants.NEWS_CHAT, userId},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}

	public static void updateChatNewsStatus(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS, Constants.NEWS_READ);
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"newType=? and friendUserId=? and userId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId});
	}

	public static void updateChatSendStatus(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS, item.getMsgStatus());
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"timestamp=? and friendUserId=?",
				new String[]{item.getTimestamp(), item.getFriendUserId()});
	}

	public static void updateChatSendStatusForImages(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS, item.getMsgStatus());
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"recruitLocIndex=? and friendUserId=?",
				new String[]{item.getRecruitLocIndex(), item.getFriendUserId()});
	}

	public static void updateChatSendContent(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"friendUserId=? and timestamp=?",
				new String[]{item.getFriendUserId(), item.getTimestamp()});
	}

	public static void updateHideOrShowChatItem(Context context, SGNewsBean item, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, item.getIsShow());
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"newType=? and friendUserId=? and userId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId});
	}

	public static void deleteChatsHistory(Context context, SGNewsBean item,
										  String userId) {
		context.getContentResolver().delete(
				SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				"newType=? and friendUserId=? and userId=?",
				new String[]{Constants.NEWS_CHAT, item.getFriendUserId(), userId});
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
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=? and userId=? and isShow=? and (noticeSubject like ? or msgContent like ?)", new String[]{
						Constants.NEWS_CHAT, userId, Constants.NEWS_SHOW, "%" + content + "%", "%" + content + "%"},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
			item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));
			if (Constants.NEWS_DYNAMIC.equals(item.getNewType())){
				item.setFriendUserId(item.getFromUser());
				item.setFriendNickName(item.getNoticeId());
				item.setFriendUserIcon(item.getRecruitId());
			}else if (Constants.NEWS_CHAT.equals(item.getNewType())){
				if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(item, context))) {
					item.setFriendUserIcon(SenderUser.getFromUserIcon(item, context));
					item.setFriendUserId(SenderUser.getFromUserId(item, context));
					item.setFriendNickName(SenderUser.getFromUserName(item, context));
				} else if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(item, context))) {
					item.setFriendUserIcon(SenderUser.getToUserIcon(item, context));
					item.setFriendUserId(SenderUser.getToUserId(item, context));
					item.setFriendNickName(SenderUser.getToUserName(item, context));
				}
			}
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static SGNewsBean getSGNewBeanByFriendUserId(Context context, String userId, String friendUserId) {
		SGNewsBean item = null;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=? and userId=? and friendUserId=?", new String[]{Constants.NEWS_CHAT, userId, friendUserId},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));
			if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getFromUserIcon(item, context));
				item.setFriendUserId(SenderUser.getFromUserId(item, context));
				item.setFriendNickName(SenderUser.getFromUserName(item, context));
			} else if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(item, context))) {
				item.setFriendUserIcon(SenderUser.getToUserIcon(item, context));
				item.setFriendUserId(SenderUser.getToUserId(item, context));
				item.setFriendNickName(SenderUser.getToUserName(item, context));
			}
		}
		c.close();
		return item;
	}


	// 系统消息
	public static SGNewsBean getOneSystemNews(Context context) {
		SGNewsBean item = null;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=?", new String[]{Constants.NEWS_SYSTEM},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));
		}
		c.close();
		return item;
	}

	public static List<SGNewsBean> getAllSystemNews(Context context) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=?",
				new String[]{Constants.NEWS_SYSTEM},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));
//			if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getFromUserId(item, context))) {
//				item.setFriendUserIcon(SenderUser.getFromUserIcon(item, context));
//				item.setFriendUserId(SenderUser.getFromUserId(item, context));
//				item.setFriendNickName(SenderUser.getFromUserName(item, context));
//			} else if (!UserPreferencesUtil.getUserId(context).equals(SenderUser.getToUserId(item, context))) {
//				item.setFriendUserIcon(SenderUser.getToUserIcon(item, context));
//				item.setFriendUserId(SenderUser.getToUserId(item, context));
//				item.setFriendNickName(SenderUser.getToUserName(item, context));
//			}
			lists.add(item);
		}
		c.close();
		return lists;
	}


	/**
	 * 动态消息的相关操作
	 */

	public static int quaryUnReadDaymicNews(Context context, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"messageStatus=? and newType=? and userId=?",
				new String[]{Constants.NEWS_UNREAD, Constants.NEWS_DYNAMIC, userId},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
		}
		c.close();
		return unreadCount;
	}


	public static List<SGNewsBean> quaryALLDaymicNews(Context context, String userId) {
		List<SGNewsBean> lists = new ArrayList<SGNewsBean>();
		SGNewsBean item;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=? and userId=?",
				new String[]{Constants.NEWS_DYNAMIC, userId},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new SGNewsBean();
			item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
			item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
			item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
			item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
			item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
			item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
			item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
			item.setRecruitLocIndex(
					c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
			item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
			item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
			item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
			item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
			item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
			item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
			item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
			item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));

			item.setFriendUserId(item.getFromUser());
			item.setFriendNickName(item.getNoticeId());
			item.setFriendUserIcon(item.getRecruitId());

			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static SGNewsBean quaryLatestDaymicNew(Context context, String userId) {
		SGNewsBean item = null;
		boolean flag=true;
		Cursor c = context.getContentResolver().query(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				new String[]{SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData.USER_ID,
						SGNewsMetaData.SGNewsTableMetaData.IS_SHOW, SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
						SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, SGNewsMetaData.SGNewsTableMetaData.TYPE,
						SGNewsMetaData.SGNewsTableMetaData.FROM_USER, SGNewsMetaData.SGNewsTableMetaData.TO_USER,
						SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
						SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP, SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
						SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID, SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE},
				"newType=? and userId=?",
				new String[]{Constants.NEWS_DYNAMIC, userId},
				SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_DESC);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			if (flag){
				flag=false;
				item = new SGNewsBean();
				item.setIsShow(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW)));
				item.set_id(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData._ID)));
				item.setMsgStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS)));
				item.setInout(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.IN_OUT)));
				item.setFromUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.FROM_USER)));
				item.setToUser(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TO_USER)));
				item.setNoticeSubject(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT)));
				item.setRecruitLocIndex(
						c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX)));
				item.setMsgContent(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT)));
				item.setNewType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE)));
				item.setNoticeId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID)));
				item.setRecruitId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID)));
				item.setTimestamp(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP)));
				item.setType(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.TYPE)));
				item.setUserId(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.USER_ID)));
				item.setMessageStatus(c.getString(c.getColumnIndex(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS)));
				item.setFriendUserId(item.getFromUser());
				item.setFriendNickName(item.getNoticeId());
				item.setFriendUserIcon(item.getRecruitId());
			}
		}
		c.close();
		return item;
	}

	public static void deleteDynamicNew(Context context, SGNewsBean item,
										String userId) {
		context.getContentResolver().delete(
				SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI,
				"newType=? and _id=? and userId=?",
				new String[]{Constants.NEWS_DYNAMIC, item.get_id(), userId});

	}

	public static void updateDynamicStatus(Context context, String userId) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS, Constants.NEWS_READ);
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"newType=? and userId=?",
				new String[]{Constants.NEWS_DYNAMIC, userId});
	}

	public static void updateOptionDynamic(Context context, String userId,SGNewsBean item) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.IN_OUT, item.getInout());
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"userId=? and _id=?",
				new String[]{userId,item.get_id()});
	}

	public static void updateType(Context context, String userId,SGNewsBean item) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.TYPE, item.getType());
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"userId=? and _id=?",
				new String[]{userId,item.get_id()});
	}

	public static void updateContent(Context context, String userId,SGNewsBean item) {
		ContentValues values = new ContentValues();
		values.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT, item.getMsgContent());
		context.getContentResolver().update(SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, values,
				"userId=? and _id=?",
				new String[]{userId,item.get_id()});
	}
}
