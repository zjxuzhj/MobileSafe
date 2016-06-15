package com.zhj.mobilesafe;

import java.util.ArrayList;

import com.zhj.mobilesafe.db.dao.CommonNumberDao;
import com.zhj.mobilesafe.db.dao.CommonNumberDao.ChildsInfo;
import com.zhj.mobilesafe.db.dao.CommonNumberDao.GroupInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class CommonNumberActivity extends Activity {

	private ExpandableListView exlistview;
	private Myadapter adapter;
	private ArrayList<GroupInfo> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commonnumber);
		groups = CommonNumberDao.getCommonNumberGroups();
		exlistview = (ExpandableListView) findViewById(R.id.exlitview);
		adapter = new Myadapter();
		exlistview.setAdapter(adapter);
		exlistview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition
					, int childPosition, long id) {

				Intent intent=new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+groups.get(groupPosition).childsInfo.get(childPosition).number));
				System.out.println("tel:"+groups.get(groupPosition).childsInfo.get(childPosition).number);
				startActivity(intent);
				return false;
			}
		});
	}

	private class Myadapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return groups.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groups.get(groupPosition).childsInfo.size();
		}

		@Override
		public GroupInfo getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		@Override
		public ChildsInfo getChild(int groupPosition, int childPosition) {
			return groups.get(groupPosition).childsInfo.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean childPosition, View convertView, ViewGroup parent) {
			TextView textview = new TextView(getApplicationContext());
			textview.setText("        " + groups.get(groupPosition).name);
			textview.setTextColor(Color.RED);
			textview.setTextSize(25);
			return textview;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView,
				ViewGroup parent) {
			TextView textview = new TextView(getApplicationContext());
			textview.setText(groups.get(groupPosition).childsInfo
					.get(childPosition).name + "\n"
					+ groups.get(groupPosition).childsInfo.get(childPosition).number);
			textview.setTextColor(Color.GREEN);
			textview.setTextSize(25);
			return textview;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}

}
