package com.xygame.sg.provider;

import android.net.Uri;
import android.provider.BaseColumns;
public class SGNewsMetaData {

	public static final String AUTHORIY = "com.xygame.sg.provider.SGNewsProvider";

	public static final class SGNewsTableMetaData implements BaseColumns {

		public static final String TABLE_NAME = "news";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORIY
				+ "/news");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.firstprovider.news";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.firstprovider.news";

		public static final String MESSAGE_CONTENT = "msgContent";
		public static final String TYPE = "type";
		public static final String TIMESTAMP="timestamp";
		public static final String NOTICE_ID="noticeId";
		public static final String RECRUIT_ID="recruitId";
		public static final String NEW_TYPE="newType";
		public static final String USER_ID="userId";
		public static final String MESSAGE_STATUS="messageStatus";
		public static final String MSG_STATUS="msgStatus";
		public static final String IN_OUT = "inOut";
		public static final String FROM_USER = "fromUser";
		public static final String TO_USER = "toUser";
		public static final String NOTICE_SUBJECT = "noticeSubject";
		public static final String RECRUIT_LOCAL_INDEX = "recruitLocIndex";
		public static final String IS_SHOW="isShow";
		public static final String FRIEND_USERID="friendUserId";

		public static final String DEFAULT_SORT_ORDER = "_id asc";
		public static final String DEFAULT_SORT_DESC = "_id desc";

		private SGNewsTableMetaData() {
		}

		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " TEXT," + FRIEND_USERID + " TEXT,"  + IS_SHOW + " TEXT,"+ IN_OUT + " TEXT,"  + MESSAGE_STATUS + " TEXT," + MSG_STATUS
				+ " TEXT," + MESSAGE_CONTENT	+ " TEXT," + TYPE + " TEXT," + FROM_USER + " TEXT,"  + TO_USER + " TEXT," + NOTICE_SUBJECT + " TEXT," + RECRUIT_LOCAL_INDEX + " TEXT,"+ TIMESTAMP
				+ " TEXT," + NOTICE_ID + " TEXT," + RECRUIT_ID + " TEXT," + NEW_TYPE + " TEXT);";
	}
}
