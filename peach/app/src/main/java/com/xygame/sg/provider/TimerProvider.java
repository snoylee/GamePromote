package com.xygame.sg.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.xygame.sg.utils.DatabaseHelper;

import java.sql.SQLException;
import java.util.HashMap;

public class TimerProvider extends ContentProvider {

	public static final UriMatcher uriMatcher;
	public static final int INCOMING_USER_COLLECTION = 1;
	public static final int INCOMING_USER_SINGLE = 2;
	public static HashMap<String, String> UserProjectMap;
	private DatabaseHelper dh;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(TimerMetaData.AUTHORIY, "/timerCount",
				INCOMING_USER_COLLECTION);
		uriMatcher.addURI(TimerMetaData.AUTHORIY, "/timerCount/#",
				INCOMING_USER_SINGLE);
	}

	static {
		UserProjectMap = new HashMap<String, String>();
		UserProjectMap.put(TimerMetaData.TimerTableMetaData._ID, TimerMetaData.TimerTableMetaData._ID);
		UserProjectMap.put(TimerMetaData.TimerTableMetaData.USER_ID, TimerMetaData.TimerTableMetaData.USER_ID);
		UserProjectMap.put(TimerMetaData.TimerTableMetaData.ORDER_TIME, TimerMetaData.TimerTableMetaData.ORDER_TIME);
		UserProjectMap.put(TimerMetaData.TimerTableMetaData.PAY_TIME, TimerMetaData.TimerTableMetaData.PAY_TIME);
		UserProjectMap.put(TimerMetaData.TimerTableMetaData.ORDER_ID, TimerMetaData.TimerTableMetaData.ORDER_ID);
		UserProjectMap.put(TimerMetaData.TimerTableMetaData.START_TIME, TimerMetaData.TimerTableMetaData.START_TIME);
		UserProjectMap.put(TimerMetaData.TimerTableMetaData.DURING_LENGTH, TimerMetaData.TimerTableMetaData.DURING_LENGTH);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int del_lab=db.delete(TimerMetaData.TimerTableMetaData.TABLE_NAME, selection, selectionArgs);
		return del_lab;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			return TimerMetaData.TimerTableMetaData.CONTENT_TYPE;
		case INCOMING_USER_SINGLE:
			return TimerMetaData.TimerTableMetaData.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(TimerMetaData.TimerTableMetaData.TABLE_NAME,null, values);
		if (rowId > 0) {
			Uri insertedAreaUri = ContentUris.withAppendedId(
					TimerMetaData.TimerTableMetaData.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(insertedAreaUri,
					null);
			return insertedAreaUri;
		}
		try {
			throw new SQLException("Failed to insert row into" + uri);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		dh = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			qb.setProjectionMap(UserProjectMap);
			break;
		case INCOMING_USER_SINGLE:
			qb.setProjectionMap(UserProjectMap);
			qb.appendWhere(TimerMetaData.TimerTableMetaData._ID+"="+uri.getPathSegments().get(1));
			break;
		}
		qb.setTables(TimerMetaData.TimerTableMetaData.TABLE_NAME);
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy= TimerMetaData.TimerTableMetaData.DEFAULT_SORT_ORDER;
		}else{
			orderBy=sortOrder;
		}
		SQLiteDatabase db=dh.getWritableDatabase();
		Cursor c=qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int update_lab=db.update(TimerMetaData.TimerTableMetaData.TABLE_NAME, values, selection, selectionArgs);
		return update_lab;
	}

}
