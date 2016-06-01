package com.zhj.mobilesafe.db.dao;

import java.util.ArrayList;

import com.zhj.mobilesafe.db.BlackNumberOpenHelper;
import com.zhj.mobilesafe.domain.BlackNumberInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumberDao {

	private static BlackNumberDao sInstance;
	private BlackNumberOpenHelper mOpenhelper;

	private BlackNumberDao(Context ctx) {
		mOpenhelper = new BlackNumberOpenHelper(ctx);

	}

	/**
	 * 单例模式 只能创建一个dao对象
	 * 
	 * @param ctx
	 * @return
	 */
	public static BlackNumberDao getInstance(Context ctx) {
		if (sInstance == null) {
			synchronized (BlackNumberDao.class) {
				if (sInstance == null) {
					sInstance = new BlackNumberDao(ctx);
				}
			}
		}
		return sInstance;

	}

	// 增加操作
	public void addBlackNum(String blacknum, int mode) {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("blacknum", blacknum);
		values.put("mode", mode);
		db.insert(BlackNumberOpenHelper.sDB_NAME, null, values);
		db.close();
	}

	// 删除操作
	public void delBlackNum(String blacknum) {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();
		db.delete(BlackNumberOpenHelper.sDB_NAME, "blacknum=?", new String[] { blacknum });
		db.close();
	}

	public void updateBlackNum(String blacknum, int mode) {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		db.update(BlackNumberOpenHelper.sDB_NAME, values, "blacknum=?", new String[] { blacknum });
		db.close();
	}

	public int queryBlackNum(String blacknum) {
		int mode = -1;
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();
		Cursor query = db.query(BlackNumberOpenHelper.sDB_NAME, new String[] { "mode" }, "blacknum=?",
				new String[] { blacknum }, null, null, null);
		if (query.moveToNext()) {
			mode = query.getInt(0);
		}
		query.close();
		db.close();
		return mode;
	}

	public ArrayList<BlackNumberInfo>  findAll() {
		SQLiteDatabase db = mOpenhelper.getWritableDatabase();
		Cursor cursor = db.query(BlackNumberOpenHelper.sDB_NAME, new String[] { "blacknum", "mode" }, null,
				null, null,null, null);
		BlackNumberInfo info ;
		ArrayList<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			String number = cursor.getString(0);
			int mode = cursor.getInt(1);
			info = new BlackNumberInfo();
			info.number = number;
			info.mode = mode;
			list.add(info);
			info = null;
			
		}

		
		cursor.close();
		db.close();
		return list;
	}
	
}
