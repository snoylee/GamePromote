package com.xygame.sg.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class GroupNoticeMetaData {

	public static final String AUTHORIY = "com.xygame.sg.provider.GroupNoticeProvider";

	public static final class GroupNoticeTableMetaData implements BaseColumns {

		public static final String TABLE_NAME = "GroupNotice";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORIY
				+ "/GroupNotice");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.firstprovider.GroupNotice";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.firstprovider.GroupNotice";

		public static final String PACKET_ID = "packetId";
		public static final String USER_ID = "userId";
		public static final String GROUP_JID = "groupJid";
		public static final String SEND_USER_ID = "sendUserId";//fromUser的Json串
		public static final String MSG_TYPE="msgType";//和单聊天一致
		public static final String MSG_TIMER = "msgTimer";
		public static final String MSG_CONTENT = "msgContent";
		public static final String NOTICE_JSON="noticeJson";
		public static final String MSG_STATUS="msgStatus";//0发送中，1发送成功,2发送失败

		public static final String DEFAULT_SORT_ORDER = "_id asc";
		public static final String DEFAULT_SORT_DESC = "_id desc";

		private GroupNoticeTableMetaData() {
		}

		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ PACKET_ID + " TEXT," + USER_ID + " TEXT,"+ GROUP_JID + " TEXT,"+ SEND_USER_ID + " TEXT,"+ MSG_TYPE + " TEXT,"+ MSG_TIMER + " TEXT," + MSG_CONTENT + " TEXT," + NOTICE_JSON+ " TEXT," + MSG_STATUS	+ " TEXT);";
	}
}
