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

public class SGGodNewsProvider extends ContentProvider {

	public static final UriMatcher uriMatcher;
	public static final int INCOMING_USER_COLLECTION = 1;
	public static final int INCOMING_USER_SINGLE = 2;
	public static HashMap<String, String> UserProjectMap;
	private DatabaseHelper dh;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(SGGodNewsMetaData.AUTHORIY, "/newsGod",
				INCOMING_USER_COLLECTION);
		uriMatcher.addURI(SGGodNewsMetaData.AUTHORIY, "/newsGod/#",
				INCOMING_USER_SINGLE);
	}

	static {
		UserProjectMap = new HashMap<String, String>();
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData._ID, SGGodNewsMetaData.SGNewsTableMetaData._ID);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.USER_ID,
				SGGodNewsMetaData.SGNewsTableMetaData.USER_ID);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.FRIEND_USERID,
				SGGodNewsMetaData.SGNewsTableMetaData.FRIEND_USERID);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.IS_SHOW,
				SGGodNewsMetaData.SGNewsTableMetaData.IS_SHOW);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.IN_OUT,
				SGGodNewsMetaData.SGNewsTableMetaData.IN_OUT);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS,
				SGGodNewsMetaData.SGNewsTableMetaData.MESSAGE_STATUS);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.MSG_STATUS,
				SGGodNewsMetaData.SGNewsTableMetaData.MSG_STATUS);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT,
				SGGodNewsMetaData.SGNewsTableMetaData.MESSAGE_CONTENT);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.TYPE,
				SGGodNewsMetaData.SGNewsTableMetaData.TYPE);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.FROM_USER,
				SGGodNewsMetaData.SGNewsTableMetaData.FROM_USER);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.TO_USER,
				SGGodNewsMetaData.SGNewsTableMetaData.TO_USER);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT,
				SGGodNewsMetaData.SGNewsTableMetaData.NOTICE_SUBJECT);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX,
				SGGodNewsMetaData.SGNewsTableMetaData.RECRUIT_LOCAL_INDEX);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.TIMESTAMP,
				SGGodNewsMetaData.SGNewsTableMetaData.TIMESTAMP);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.NOTICE_ID,
				SGGodNewsMetaData.SGNewsTableMetaData.NOTICE_ID);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.RECRUIT_ID,
				SGGodNewsMetaData.SGNewsTableMetaData.RECRUIT_ID);
		UserProjectMap.put(SGGodNewsMetaData.SGNewsTableMetaData.NEW_TYPE,
				SGGodNewsMetaData.SGNewsTableMetaData.NEW_TYPE);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int del_lab=db.delete(SGGodNewsMetaData.SGNewsTableMetaData.TABLE_NAME, selection, selectionArgs);
		return del_lab;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			return SGGodNewsMetaData.SGNewsTableMetaData.CONTENT_TYPE;
		case INCOMING_USER_SINGLE:
			return SGGodNewsMetaData.SGNewsTableMetaData.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(SGGodNewsMetaData.SGNewsTableMetaData.TABLE_NAME,null, values);
		if (rowId > 0) {
			Uri insertedAreaUri = ContentUris.withAppendedId(
					SGGodNewsMetaData.SGNewsTableMetaData.CONTENT_URI, rowId);
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
			qb.appendWhere(SGGodNewsMetaData.SGNewsTableMetaData._ID+"="+uri.getPathSegments().get(1));
			break;
		}
		qb.setTables(SGGodNewsMetaData.SGNewsTableMetaData.TABLE_NAME);
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy= SGGodNewsMetaData.SGNewsTableMetaData.DEFAULT_SORT_ORDER;
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
		int update_lab=db.update(SGGodNewsMetaData.SGNewsTableMetaData.TABLE_NAME, values, selection, selectionArgs);
		return update_lab;
	}

}
