package com.zhj.mobilesafe;

import java.util.ArrayList;

import com.zhj.mobilesafe.domain.AppInfo;
import com.zhj.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppManagerActivity extends Activity {

	private ListView mLv;
	private ArrayList<AppInfo> mList;
	private appAdapter adapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adapter = new appAdapter();
			mLv.setAdapter(adapter);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanager);
		mLv = (ListView) findViewById(R.id.lv);
		initData();

	}

	private void initData() {
		new Thread() {
			@Override
			public void run() {
				mList = AppInfoProvider.getAppInfos(getApplicationContext());
				handler.sendEmptyMessage(0);
			}
		}.start();

	}

	class appAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public AppInfo getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.list_item_appmanager, null);
				holder = new ViewHolder();
				holder.appname = (TextView) convertView.findViewById(R.id.tv_appname);
				holder.packagename = (TextView) convertView.findViewById(R.id.tv_packagename);
				holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				// ½«holderºÍconvertView°ó¶¨
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AppInfo appinfo = getItem(position);
			holder.appname.setText(appinfo.appname);
			holder.packagename.setText(appinfo.packagename);
			holder.icon.setImageDrawable(appinfo.icon);

			return convertView;
		}

	}

	class ViewHolder {
		ImageView icon;
		TextView appname;
		TextView packagename;
	}
}
