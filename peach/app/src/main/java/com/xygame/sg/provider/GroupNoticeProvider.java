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

public class GroupNoticeProvider extends ContentProvider {

	public static final UriMatcher uriMatcher;
	public static final int INCOMING_USER_COLLECTION = 1;
	public static final int INCOMING_USER_SINGLE = 2;
	public static HashMap<String, String> UserProjectMap;
	private DatabaseHelper dh;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(GroupNoticeMetaData.AUTHORIY, "/GroupNotice",
				INCOMING_USER_COLLECTION);
		uriMatcher.addURI(GroupNoticeMetaData.AUTHORIY, "/GroupNotice/#",
				INCOMING_USER_SINGLE);
	}

	static {
		UserProjectMap = new HashMap<String, String>();
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData._ID, GroupNoticeMetaData.GroupNoticeTableMetaData._ID);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.PACKET_ID, GroupNoticeMetaData.GroupNoticeTableMetaData.PACKET_ID);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.USER_ID, GroupNoticeMetaData.GroupNoticeTableMetaData.USER_ID);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.GROUP_JID, GroupNoticeMetaData.GroupNoticeTableMetaData.GROUP_JID);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.SEND_USER_ID, GroupNoticeMetaData.GroupNoticeTableMetaData.SEND_USER_ID);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TYPE, GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TYPE);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TIMER, GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_TIMER);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_CONTENT, GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_CONTENT);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.NOTICE_JSON, GroupNoticeMetaData.GroupNoticeTableMetaData.NOTICE_JSON);
		UserProjectMap.put(GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS, GroupNoticeMetaData.GroupNoticeTableMetaData.MSG_STATUS);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int del_lab=db.delete(GroupNoticeMetaData.GroupNoticeTableMetaData.TABLE_NAME, selection, selectionArgs);
		return del_lab;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case INCOMING_USER_COLLECTION:
			return GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_TYPE;
		case INCOMING_USER_SINGLE:
			return GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(GroupNoticeMetaData.GroupNoticeTableMetaData.TABLE_NAME,null, values);
		if (rowId > 0) {
			Uri insertedAreaUri = ContentUris.withAppendedId(
					GroupNoticeMetaData.GroupNoticeTableMetaData.CONTENT_URI, rowId);
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
			qb.appendWhere(GroupNoticeMetaData.GroupNoticeTableMetaData._ID+"="+uri.getPathSegments().get(1));
			break;
		}
		qb.setTables(GroupNoticeMetaData.GroupNoticeTableMetaData.TABLE_NAME);
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy= GroupNoticeMetaData.GroupNoticeTableMetaData.DEFAULT_SORT_ORDER;
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
		int update_lab=db.update(GroupNoticeMetaData.GroupNoticeTableMetaData.TABLE_NAME, values, selection, selectionArgs);
		return update_lab;
	}

}
