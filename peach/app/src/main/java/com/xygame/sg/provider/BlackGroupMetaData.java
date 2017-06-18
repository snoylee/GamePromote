package com.xygame.sg.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class BlackGroupMetaData {

	public static final String AUTHORIY = "com.xygame.sg.provider.BlackGroupProvider";

	public static final class BlackGroupTableMetaData implements BaseColumns {

		public static final String TABLE_NAME = "blackGroup";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORIY
				+ "/blackGroup");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.firstprovider.blackGroup";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.firstprovider.blackGroup";

		public static final String GROUP_ID = "groupId";
		public static final String USER_ID="userId";

		public static final String DEFAULT_SORT_ORDER = "_id asc";
		public static final String DEFAULT_SORT_DESC = "_id desc";

		private BlackGroupTableMetaData() {
		}

		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " TEXT," + GROUP_ID	+ " TEXT);";
	}
}
