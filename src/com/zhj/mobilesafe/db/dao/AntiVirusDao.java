package com.zhj.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {
	private static final String PATH = "data/data/com.zhj.mobilesafe/files/antivirus.db";

	public static boolean findMd5(String md5) {
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.rawQuery("select desc from datable where md5=?", new String []{md5});
		String desc = null;
		if (cursor.moveToFirst()) {
			desc = cursor.getString(0);// ËµÃ÷ÊÇ²¡¶¾
			return true;
		}
		cursor.close();

		database.close();
		return false;

	}


}
