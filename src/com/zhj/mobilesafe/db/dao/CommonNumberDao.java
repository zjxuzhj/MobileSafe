package com.zhj.mobilesafe.db.dao;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberDao {
	private static final String PATH = "data/data/com.zhj.mobilesafe/files/commonnum.db";

	public static ArrayList<GroupInfo> getCommonNumberGroups() {
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.rawQuery("select name,idx from classlist", null);
		ArrayList<GroupInfo> groupList = new ArrayList<GroupInfo>();
		while (cursor.moveToNext()) {
			GroupInfo groupInfo = new GroupInfo();
			String idx = cursor.getString(1);
			String name = cursor.getString(0);
			
			groupInfo.name = name;
			groupInfo.idx = idx;
			groupInfo.childsInfo=getChilds(database, idx);
			groupList.add(groupInfo);
		}
		cursor.close();

		database.close();
		return groupList;

	}

	public static ArrayList<ChildsInfo> getChilds(SQLiteDatabase database, String idx) {
		Cursor cursor = database.rawQuery("select number,name from table" + idx , null);
		ArrayList<ChildsInfo> childsList = new ArrayList<ChildsInfo>();
		while (cursor.moveToNext()) {
			ChildsInfo childsInfo=new ChildsInfo();
			long number = cursor.getLong(0);
			String name = cursor.getString(1);
			childsInfo.number = number;
			childsInfo.name =name;
			childsList.add(childsInfo);
		}
		cursor.close();

		return childsList;
	}

	public static class GroupInfo {
		public String name;
		public String idx;
		public ArrayList<ChildsInfo> childsInfo;
	}

	public static class ChildsInfo {
		public long number;
		public String name;
	}
}
