package com.zhj.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListViewActivity extends Activity {

	private ListView mLvlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		mLvlist = (ListView) findViewById(R.id.lv_list);
		final ArrayList<HashMap<String, String>> contact = contact();
			
		mLvlist.setAdapter(new SimpleAdapter(this, contact, R.layout.list_item_contact,
				new String[]{"name","phone"},
				new int[]{R.id.tv_name,R.id.tv_phone}));
		mLvlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String,String>map=contact.get(position);
				String phone=map.get("phone").replace("-", "");
				
				Intent data=new Intent(); 
				data.putExtra("phone",phone);
				
				setResult(1,data);//将数据回传给上一个activity
				finish();
				
			}
		});
	}

	public ArrayList<HashMap<String, String>>  contact() {
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(Uri.parse("content://com.android.contacts/raw_contacts"),
				new String[] { "contact_id" }, null, null, null);
		// 保存所有联系人信息
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		while (cursor.moveToNext()) {
			String cursorId = cursor.getString(0);

			Cursor c = cr.query(Uri.parse("content://com.android.contacts/data"), new String[] { "data1", "mimetype" },
					"raw_contact_id=?", new String[] { cursorId }, null);
			HashMap<String, String> map = new HashMap<String, String>();// 保存联系人详细信息
			while (c.moveToNext()) {
				String data1 = c.getString(0);
				String mimetype = c.getString(1);

				if ("vnd.android.cursor.item/name".equals(mimetype)) {
					map.put("name", data1);
				} else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
					
					map.put("phone", data1);
				}
			}
			c.close();
			list.add(map);
		}
		cursor.close();
		return list;
	}
}
