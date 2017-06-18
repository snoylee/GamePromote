package com.xygame.sg.im;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.provider.SGNoticeNewsMetaData;
import com.xygame.sg.provider.TimerMetaData;

import java.util.ArrayList;
import java.util.List;

public class TimerEngine {

	public static void inserTimerBean(Context context, TimerCountBean item) {
		ContentValues values = new ContentValues();
		values.put(TimerMetaData.TimerTableMetaData.USER_ID, item.getUserId());
		values.put(TimerMetaData.TimerTableMetaData.ORDER_TIME, item.getOrderExpireTime());
		values.put(TimerMetaData.TimerTableMetaData.PAY_TIME, item.getPayExpireTime());
		values.put(TimerMetaData.TimerTableMetaData.ORDER_ID, item.getGroupId());
		values.put(TimerMetaData.TimerTableMetaData.START_TIME, item.getStartTime());
		values.put(TimerMetaData.TimerTableMetaData.DURING_LENGTH, item.getDuringLength());
		context.getContentResolver().insert(TimerMetaData.TimerTableMetaData.CONTENT_URI, values);
	}

	public static void updateTimerBean(Context context, TimerCountBean item) {
		ContentValues values = new ContentValues();
		values.put(TimerMetaData.TimerTableMetaData.START_TIME, item.getStartTime());
		context.getContentResolver().delete(TimerMetaData.TimerTableMetaData.CONTENT_URI, "userId=? and duringLength=?",
				new String[]{item.getUserId(), item.getDuringLength()});
	}

	public static List<TimerCountBean> quaryTimerBeans(Context context, String userId) {
		List<TimerCountBean> lists = new ArrayList<>();
		TimerCountBean item;
		Cursor c = context.getContentResolver().query(TimerMetaData.TimerTableMetaData.CONTENT_URI,
				new String[]{TimerMetaData.TimerTableMetaData._ID,TimerMetaData.TimerTableMetaData.USER_ID,TimerMetaData.TimerTableMetaData.ORDER_TIME,TimerMetaData.TimerTableMetaData.PAY_TIME,
						TimerMetaData.TimerTableMetaData.ORDER_ID, TimerMetaData.TimerTableMetaData.START_TIME,
						TimerMetaData.TimerTableMetaData.DURING_LENGTH},"userId=?",new String[]{userId},SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new TimerCountBean();
			item.setDuringLength(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.DURING_LENGTH)));
			item.setGroupId(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.ORDER_ID)));
			item.setId(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData._ID)));
			item.setStartTime(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.START_TIME)));
			item.setUserId(userId);
			item.setOrderExpireTime(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.ORDER_TIME)));
			item.setPayExpireTime(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.PAY_TIME)));
			lists.add(item);
		}
		c.close();
		return lists;
	}

	public static void updateTimerBeanInfo(Context context, TimerCountBean item) {
		ContentValues values = new ContentValues();
		values.put(TimerMetaData.TimerTableMetaData.ORDER_ID, item.getGroupId());
		values.put(TimerMetaData.TimerTableMetaData.START_TIME, item.getStartTime());
		values.put(TimerMetaData.TimerTableMetaData.PAY_TIME,item.getPayExpireTime());
		values.put(TimerMetaData.TimerTableMetaData.ORDER_TIME,item.getOrderExpireTime());
		context.getContentResolver().update(TimerMetaData.TimerTableMetaData.CONTENT_URI, values,
				"userId=? and duringLength=?",
				new String[]{item.getUserId(), item.getDuringLength()});
	}

	public static TimerCountBean quaryTimerBeansByDuringLength(Context context, String userId,String duringLength) {
		TimerCountBean item=null;
		Cursor c = context.getContentResolver().query(TimerMetaData.TimerTableMetaData.CONTENT_URI,
				new String[]{TimerMetaData.TimerTableMetaData._ID,TimerMetaData.TimerTableMetaData.USER_ID,TimerMetaData.TimerTableMetaData.ORDER_TIME,TimerMetaData.TimerTableMetaData.PAY_TIME,
						TimerMetaData.TimerTableMetaData.ORDER_ID, TimerMetaData.TimerTableMetaData.START_TIME,
						TimerMetaData.TimerTableMetaData.DURING_LENGTH},"userId=? and duringLength=?",new String[]{userId,duringLength},SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.DEFAULT_SORT_ORDER);
		for (c.moveToFirst(); !(c.isAfterLast()); c.moveToNext()) {
			item = new TimerCountBean();
			item.setDuringLength(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.DURING_LENGTH)));
			item.setGroupId(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.ORDER_ID)));
			item.setId(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData._ID)));
			item.setStartTime(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.START_TIME)));
			item.setUserId(userId);
			item.setOrderExpireTime(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.ORDER_TIME)));
			item.setPayExpireTime(c.getString(c.getColumnIndex(TimerMetaData.TimerTableMetaData.PAY_TIME)));
		}
		c.close();
		return item;
	}
}
