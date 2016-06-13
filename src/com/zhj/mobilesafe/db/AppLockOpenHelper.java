package com.zhj.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockOpenHelper extends SQLiteOpenHelper {

	public final static String sDB_NAME="info";
	/**
	 * 直接与底层数据库打交道
	 * @param context
	 */
	
	public AppLockOpenHelper(Context context) {
		super(context,"AppLock.db",null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table "+sDB_NAME +
				" (_id integer primary key autoincrement,"
				+ "packagename varchar(50));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
