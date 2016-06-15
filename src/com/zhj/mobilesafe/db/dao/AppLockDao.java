package com.zhj.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.zhj.mobilesafe.db.AppLockOpenHelper;
import com.zhj.mobilesafe.db.AppLockOpenHelper;
import com.zhj.mobilesafe.domain.AppInfo;
import com.zhj.mobilesafe.domain.BlackNumberInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AppLockDao {

	private Context mContext;
	private static AppLockDao sInstance;
	private AppLockOpenHelper mOpenhelper;

	private AppLockDao(Context ctx) {
		mOpenhelper = new AppLockOpenHelper(ctx);
		mContext = ctx;

	}

	/**
	 * 单例模式 只能创建一个dao对象
	 * 
	 * @param ctx
	 * @return
	 */
	public static AppLockDao getInstance(Context ctx) {
		if (sInstance == null) {
			synchronized (AppLockDao.class) {
				if (sInstance == null) {
					sInstance = new AppLockDao(ctx);
				}
			}
		}
		return sInstance;

	}

	// 增加操作
	public void addLockApp(String packagename) {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packagename", packagename);
		db.insert(AppLockOpenHelper.sDB_NAME, null, values);
		db.close();
		// 通知数据库更新
		mContext.getContentResolver().notifyChange(
				Uri.parse("content://com.zhj.MobileSafe/change"), null);
	}

	// 删除操作
	public void delLockApp(String packagename) {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();
		db.delete(AppLockOpenHelper.sDB_NAME, "packagename=?", new String[] { packagename });
		db.close();
		mContext.getContentResolver().notifyChange(
				Uri.parse("content://com.zhj.MobileSafe/change"), null);
	}

	public boolean find(String packagename) {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();

		Cursor cursor = db.query(AppLockOpenHelper.sDB_NAME, new String[] { "packagename" }, "packagename=?",
				new String[] { packagename }, null, null, null);
		boolean exist = false;
		if (cursor.moveToFirst()) {
			exist = true;
		}

		cursor.close();
		db.close();
		return exist;
	}

	public ArrayList<String> findAll() {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();
		Cursor cursor = db.query(AppLockOpenHelper.sDB_NAME, new String[] { "packagename" }, null, null, null, null,
				null);
		ArrayList<String> list = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String packagename = cursor.getString(0);
			list.add(packagename);

		}

		cursor.close();
		db.close();
		return list;
	}

}
