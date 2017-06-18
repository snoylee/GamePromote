package com.xygame.sg.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xygame.sg.provider.BlackGroupMetaData;
import com.xygame.sg.provider.DeleGroupMetaData;
import com.xygame.sg.provider.GroupMetaData;
import com.xygame.sg.provider.GroupNoticeMetaData;
import com.xygame.sg.provider.PushInfoMetaData;
import com.xygame.sg.provider.SGGodNewsMetaData;
import com.xygame.sg.provider.SGNewsMetaData;
import com.xygame.sg.provider.SGNoticeNewsMetaData;
import com.xygame.sg.provider.TempGroupNewsMetaData;
import com.xygame.sg.provider.TimerMetaData;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static int VERSION = Constants.DATABASE_VERSION;
	public static final String DATABASE_NAME = "User_db";


	public DatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SGNewsMetaData.SGNewsTableMetaData.CREATE_TABLE);
		db.execSQL(PushInfoMetaData.PushInfoTableMetaData.CREATE_TABLE);
		db.execSQL(GroupNoticeMetaData.GroupNoticeTableMetaData.CREATE_TABLE);
		db.execSQL(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CREATE_TABLE);
		db.execSQL(GroupMetaData.GroupTableMetaData.CREATE_TABLE);
		db.execSQL(BlackGroupMetaData.BlackGroupTableMetaData.CREATE_TABLE);
		db.execSQL(TempGroupNewsMetaData.TempGroupTableMetaData.CREATE_TABLE);
		db.execSQL(DeleGroupMetaData.GroupTableMetaData.CREATE_TABLE);
		db.execSQL(TimerMetaData.TimerTableMetaData.CREATE_TABLE);
		db.execSQL(SGGodNewsMetaData.SGNewsTableMetaData.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (newVersion) {
			case 2:
				db.execSQL(PushInfoMetaData.PushInfoTableMetaData.CREATE_TABLE);
				break;
			case 3:
				db.execSQL(GroupNoticeMetaData.GroupNoticeTableMetaData.CREATE_TABLE);
				db.execSQL(SGNoticeNewsMetaData.SGNoticeNewsTableMetaData.CREATE_TABLE);
				db.execSQL(GroupMetaData.GroupTableMetaData.CREATE_TABLE);
				db.execSQL(BlackGroupMetaData.BlackGroupTableMetaData.CREATE_TABLE);
				db.execSQL(TempGroupNewsMetaData.TempGroupTableMetaData.CREATE_TABLE);
				db.execSQL(DeleGroupMetaData.GroupTableMetaData.CREATE_TABLE);
				break;
			case 4:
				db.execSQL(TimerMetaData.TimerTableMetaData.CREATE_TABLE);
				db.execSQL(SGGodNewsMetaData.SGNewsTableMetaData.CREATE_TABLE);
				break;
			case 5:
				db.execSQL(TimerMetaData.TimerTableMetaData.ADD_COLUMN_ORDER_TIME);
				db.execSQL(TimerMetaData.TimerTableMetaData.ADD_COLUMN_PAY_TIME);
				break;
		}

	}

}
