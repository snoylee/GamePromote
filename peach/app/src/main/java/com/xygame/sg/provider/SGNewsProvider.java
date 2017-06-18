package com.xygame.sg.provider;

import java.sql.SQLException;
import java.util.HashMap;

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

public class SGNewsProvider extends ContentProvider {

	public static final UriMatcher uriMatcher;
	public static final int INCOMING_USER_COLLECTION = 1;
	public static final int INCOMING_USER_SINGLE = 2;
	public static HashMap<String, String> UserProjectMap;
	private DatabaseHelper dh;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(SGNewsMetaData.AUTHORIY, "/news",
				INCOMING_USER_COLLECTION);
		uriMatcher.addURI(SGNewsMetaData.AUTHORIY, "/news/#",
				INCOMING_USER_SINGLE);
	}

	static {
		UserProjectMap = new HashMap<String, String>();
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData._ID, SGNewsMetaData.SGNewsTableMetaData._ID);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.USER_ID,
				SGNewsMetaData.SGNewsTableMetaData.USER_ID);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.FRIEND_USERID,
				SGNewsMetaData.SGNewsTableMetaData.FRIEND_USERID);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.IS_SHOW,
				SGNewsMetaData.SGNewsTableMetaData.IS_SHOW);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.IN_OUT,
				SGNewsMetaData.SGNewsTableMetaData.IN_OUT);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
				SGNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
				SGNewsMetaData.SGNewsTableMetaData.MSG_STATUS);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT,
				SGNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.TYPE,
				SGNewsMetaData.SGNewsTableMetaData.TYPE);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.FROM_USER,
				SGNewsMetaData.SGNewsTableMetaData.FROM_USER);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.TO_USER,
				SGNewsMetaData.SGNewsTableMetaData.TO_USER);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
				SGNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
				SGNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP,
				SGNewsMetaData.SGNewsTableMetaData.TIMESTAMP);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
				SGNewsMetaData.SGNewsTableMetaData.NOTICE_ID);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID,
				SGNewsMetaData.SGNewsTableMetaData.RECRUIT_ID);
		UserProjectMap.put(SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE,
				SGNewsMetaData.SGNewsTableMetaData.NEW_TYPE);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int del_lab=db.delete(SGNewsMetaData.SGNewsTableMetaData.TABLE_NAME, selection, selectionArgs);
		return del_lab;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			return SGNewsMetaData.SGNewsTableMetaData.CONTENT_TYPE;
		case INCOMING_USER_SINGLE:
			return SGNewsMetaData.SGNewsTableMetaData.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(SGNewsMetaData.SGNewsTableMetaData.TABLE_NAME,null, values);
		if (rowId > 0) {
			Uri insertedAreaUri = ContentUris.withAppendedId(
					SGNewsMetaData.SGNewsTableMetaData.CONTENT_URI, rowId);
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
			qb.appendWhere(SGNewsMetaData.SGNewsTableMetaData._ID+"="+uri.getPathSegments().get(1));
			break;
		}
		qb.setTables(SGNewsMetaData.SGNewsTableMetaData.TABLE_NAME);
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy= SGNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER;
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
		int update_lab=db.update(SGNewsMetaData.SGNewsTableMetaData.TABLE_NAME, values, selection, selectionArgs);
		return update_lab;
	}

}
