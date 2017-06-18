package com.xygame.sg.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class TimerMetaData {

	public static final String AUTHORIY = "com.xygame.sg.provider.TimerProvider";

	public static final class TimerTableMetaData implements BaseColumns {

		public static final String TABLE_NAME = "timerCount";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORIY
				+ "/timerCount");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.firstprovider.timerCount";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.firstprovider.timerCount";

		public static final String ORDER_ID = "groupId";
		public static final String START_TIME="startTime";
		public static final String DURING_LENGTH="duringLength";
		public static final String USER_ID="userId";
		public static final String ORDER_TIME="orderExpireTime";
		public static final String PAY_TIME="payExpireTime";

		public static final String DEFAULT_SORT_ORDER = "_id asc";
		public static final String DEFAULT_SORT_DESC = "_id desc";

		private TimerTableMetaData() {
		}

		public static final String ADD_COLUMN_ORDER_TIME="ALTER TABLE "+TABLE_NAME+" ADD COLUMN "+ORDER_TIME+" TEXT;";

		public static final String ADD_COLUMN_PAY_TIME="ALTER TABLE "+TABLE_NAME+" ADD COLUMN "+PAY_TIME+" TEXT;";

		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
				+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " TEXT,"+ ORDER_TIME + " TEXT,"+ PAY_TIME + " TEXT,"+ ORDER_ID + " TEXT,"+ START_TIME + " TEXT," + DURING_LENGTH	+ " TEXT);";
	}
}
