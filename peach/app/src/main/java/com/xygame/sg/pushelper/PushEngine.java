package com.xygame.sg.pushelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.xygame.sg.provider.PushInfoMetaData;

public class PushEngine {

	public static void inserPushBean(Context context, PushHeperBean item) {
		ContentValues values = new ContentValues();
		values.put(PushInfoMetaData.PushInfoTableMetaData.USER_ID, item.getUserId());
		values.put(PushInfoMetaData.PushInfoTableMetaData.MESSAGE_TITLE, item.getTitle());
		values.put(PushInfoMetaData.PushInfoTableMetaData.MESSAGE_CONTENT, item.getContent());
		context.getContentResolver().insert(PushInfoMetaData.PushInfoTableMetaData.CONTENT_URI, values);
	}

	public static int getUnreadCount(Context context, String userId) {
		int unreadCount = 0;
		Cursor c = context.getContentResolver().query(PushInfoMetaData.PushInfoTableMetaData.CONTENT_URI,
				new String[]{PushInfoMetaData.PushInfoTableMetaData._ID, PushInfoMetaData.PushInfoTableMetaData.USER_ID,
						PushInfoMetaData.PushInfoTableMetaData.MESSAGE_TITLE,
						PushInfoMetaData.PushInfoTableMetaData.MESSAGE_CONTENT},
				"userId=?",new String[]{userId},PushInfoMetaData.PushInfoTableMetaData.DEFAULT_SORT_ORDER);
		if (c != null) {
			unreadCount = c.getCount();
			c.close();
		}
		return unreadCount;
	}

	public static void deletePushInfo(Context context,String userId) {
		context.getContentResolver().delete(PushInfoMetaData.PushInfoTableMetaData.CONTENT_URI,"userId=?",new String[]{ userId});
	}

}
