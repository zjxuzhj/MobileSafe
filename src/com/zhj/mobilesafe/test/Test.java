package com.zhj.mobilesafe.test;

import java.util.ArrayList;

import com.zhj.mobilesafe.db.BlackNumberOpenHelper;
import com.zhj.mobilesafe.db.dao.BlackNumberDao;
import com.zhj.mobilesafe.domain.BlackNumberInfo;

import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {

	BlackNumberDao dao;
	
	public void createDB(){
		BlackNumberOpenHelper blackNumberOpenHelper=new BlackNumberOpenHelper(getContext());
		blackNumberOpenHelper.getWritableDatabase();
		
	}
	public void add(){
		dao=BlackNumberDao.getInstance(getContext());
		
		for(int i=0;i<99;i++){
			int mode=i%2;
			dao.addBlackNum("134563275"+i, mode);
		}
	}
	public void delete(){
		dao=BlackNumberDao.getInstance(getContext());
		dao.delBlackNum("110");
	}
	public void update(){
		dao=BlackNumberDao.getInstance(getContext());
		dao.updateBlackNum("120",2);
		
	}
	public void find(){
		dao=BlackNumberDao.getInstance(getContext());
		dao.queryBlackNum("120");
	}
	public void findAll(){
		dao=BlackNumberDao.getInstance(getContext());
		 dao.findAll();
	}
}
