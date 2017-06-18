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

public class TempGroupNewsProvider extends ContentProvider {

	public static final UriMatcher uriMatcher;
	public static final int INCOMING_USER_COLLECTION = 1;
	public static final int INCOMING_USER_SINGLE = 2;
	public static HashMap<String, String> UserProjectMap;
	private DatabaseHelper dh;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(TempGroupNewsMetaData.AUTHORIY, "/temp_group",
				INCOMING_USER_COLLECTION);
		uriMatcher.addURI(TempGroupNewsMetaData.AUTHORIY, "/temp_group/#",
				INCOMING_USER_SINGLE);
	}

	static {
		UserProjectMap = new HashMap<String, String>();
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData._ID, TempGroupNewsMetaData.TempGroupTableMetaData._ID);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID,
				TempGroupNewsMetaData.TempGroupTableMetaData.USER_ID);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.FRIEND_USERID,
				TempGroupNewsMetaData.TempGroupTableMetaData.FRIEND_USERID);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW,
				TempGroupNewsMetaData.TempGroupTableMetaData.IS_SHOW);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT,
				TempGroupNewsMetaData.TempGroupTableMetaData.IN_OUT);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS,
				TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_STATUS);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS,
				TempGroupNewsMetaData.TempGroupTableMetaData.MSG_STATUS);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT,
				TempGroupNewsMetaData.TempGroupTableMetaData.MESSAGE_CONTENT);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.TYPE,
				TempGroupNewsMetaData.TempGroupTableMetaData.TYPE);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER,
				TempGroupNewsMetaData.TempGroupTableMetaData.FROM_USER);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER,
				TempGroupNewsMetaData.TempGroupTableMetaData.TO_USER);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT,
				TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_SUBJECT);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX,
				TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_LOCAL_INDEX);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP,
				TempGroupNewsMetaData.TempGroupTableMetaData.TIMESTAMP);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID,
				TempGroupNewsMetaData.TempGroupTableMetaData.NOTICE_ID);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID,
				TempGroupNewsMetaData.TempGroupTableMetaData.RECRUIT_ID);
		UserProjectMap.put(TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE,
				TempGroupNewsMetaData.TempGroupTableMetaData.NEW_TYPE);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int del_lab=db.delete(TempGroupNewsMetaData.TempGroupTableMetaData.TABLE_NAME, selection, selectionArgs);
		return del_lab;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			return TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_TYPE;
		case INCOMING_USER_SINGLE:
			return TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(TempGroupNewsMetaData.TempGroupTableMetaData.TABLE_NAME,null, values);
		if (rowId > 0) {
			Uri insertedAreaUri = ContentUris.withAppendedId(
					TempGroupNewsMetaData.TempGroupTableMetaData.CONTENT_URI, rowId);
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
			qb.appendWhere(TempGroupNewsMetaData.TempGroupTableMetaData._ID+"="+uri.getPathSegments().get(1));
			break;
		}
		qb.setTables(TempGroupNewsMetaData.TempGroupTableMetaData.TABLE_NAME);
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy= TempGroupNewsMetaData.TempGroupTableMetaData.DEFAULT_SORT_ORDER;
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
		int update_lab=db.update(TempGroupNewsMetaData.TempGroupTableMetaData.TABLE_NAME, values, selection, selectionArgs);
		return update_lab;
	}

}
