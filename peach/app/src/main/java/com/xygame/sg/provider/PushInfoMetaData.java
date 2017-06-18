package com.xygame.sg.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class PushInfoMetaData {

	public static final String AUTHORIY = "com.xygame.sg.provider.PushInfoProvider";

	public static final class PushInfoTableMetaData implements BaseColumns {

		public static final String TABLE_NAME = "PushInfo";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORIY
				+ "/PushInfo");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.firstprovider.PushInfo";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.firstprovider.PushInfo";

		public static final String MESSAGE_TITLE = "msgTitle";
		public static final String MESSAGE_CONTENT = "msgContent";
		public static final String USER_ID="userId";

		public static final String DEFAULT_SORT_ORDER = "_id asc";
		public static final String DEFAULT_SORT_DESC = "_id desc";

		private PushInfoTableMetaData() {
		}

		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " TEXT," + MESSAGE_TITLE + " TEXT," + MESSAGE_CONTENT	+ " TEXT);";
	}
}
