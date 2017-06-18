package com.xygame.sg.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class GroupMetaData {

	public static final String AUTHORIY = "com.xygame.sg.provider.GroupProvider";

	public static final class GroupTableMetaData implements BaseColumns {

		public static final String TABLE_NAME = "groupTable";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORIY
				+ "/groupTable");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.firstprovider.groupTable";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.firstprovider.groupTable";

		public static final String USER_ID = "userId";
		public static final String GROUP_ID = "groupId";
		public static final String GROUP_TYPE = "groupType";
		public static final String TIMER_INTO="lastIntoTimer";//最后一次进群时间

		public static final String DEFAULT_SORT_ORDER = "_id asc";
		public static final String DEFAULT_SORT_DESC = "_id desc";

		private GroupTableMetaData() {
		}

		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " TEXT,"  + GROUP_ID + " TEXT," + GROUP_TYPE + " TEXT," + TIMER_INTO + " TEXT);";
	}
}
