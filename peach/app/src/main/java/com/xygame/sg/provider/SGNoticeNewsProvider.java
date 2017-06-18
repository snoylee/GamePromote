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

public class SGNoticeNewsProvider extends ContentProvider {

	public static final UriMatcher uriMatcher;
	public static final int INCOMING_USER_COLLECTION = 1;
	public static final int INCOMING_USER_SINGLE = 2;
	public static HashMap<String, String> UserProjectMap;
	private DatabaseHelper dh;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(SGNoticeNewsMetaData.AUTHORIY, "/noticeNews",
				INCOMING_USER_COLLECTION);
		uriMatcher.addURI(SGNoticeNewsMetaData.AUTHORIY, "/noticeNews/#",
				INCOMING_USER_SINGLE);
	}

	static {
		UserProjectMap = new HashMap<String, String>();
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID, SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.USER_ID);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FRIEND_USERID,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FRIEND_USERID);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IS_SHOW);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.IN_OUT);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_STATUS);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MSG_STATUS);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.MESSAGE_CONTENT);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TYPE);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.FROM_USER);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TO_USER);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_SUBJECT);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_LOCAL_INDEX);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TIMESTAMP);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NOTICE_ID);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.RECRUIT_ID);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.NEW_TYPE);
		UserProjectMap.put(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.OPERATOR_FLAG,
				SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.OPERATOR_FLAG);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int del_lab=db.delete(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TABLE_NAME, selection, selectionArgs);
		return del_lab;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			return SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_TYPE;
		case INCOMING_USER_SINGLE:
			return SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TABLE_NAME,null, values);
		if (rowId > 0) {
			Uri insertedAreaUri = ContentUris.withAppendedId(
					SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CONTENT_URI, rowId);
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
			qb.appendWhere(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData._ID+"="+uri.getPathSegments().get(1));
			break;
		}
		qb.setTables(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TABLE_NAME);
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy= SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.DEFAULT_SORT_ORDER;
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
		int update_lab=db.update(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.TABLE_NAME, values, selection, selectionArgs);
		return update_lab;
	}

}
