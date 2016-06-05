package com.zhj.mobilesafe;

import java.util.ArrayList;
import com.zhj.mobilesafe.domain.AppInfo;
import com.zhj.mobilesafe.engine.AppInfoProvider;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppManagerActivity extends Activity {

	private ListView mLv;
	private ArrayList<AppInfo> mList;
	private appAdapter adapter;
	private ArrayList<AppInfo> userappinfo;
	private ArrayList<AppInfo> systemappinfo;
	private ViewHolder2 holder2;
	private TextView title2;
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
		title2=(TextView) findViewById(R.id.tv_title_head);
		mLv.setOnScrollListener(new OnScrollListener() {

			

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
				if (userappinfo != null && systemappinfo != null) {

					if (firstVisibleItem >=userappinfo.size() + 1) {

						title2.setText("系统应用(" + systemappinfo.size() + ")");
					} else {
						title2.setText("用户应用(" + userappinfo.size() + ")");
					}
				}
			}
		});
		initData();
	}

	private void initData() {
		new Thread() {

			@Override
			public void run() {
				mList = AppInfoProvider.getAppInfos(getApplicationContext());
				userappinfo = new ArrayList<AppInfo>();
				systemappinfo = new ArrayList<AppInfo>();
				for (AppInfo appinfo : mList) {
					if (appinfo.isUser) {
						userappinfo.add(appinfo);
					} else {
						systemappinfo.add(appinfo);
					}
				}

				handler.sendEmptyMessage(0);
			}
		}.start();

	}

	class appAdapter extends BaseAdapter {

		

		@Override
		public int getCount() {
			return userappinfo.size() + systemappinfo.size() + 2;
		}

		@Override
		public AppInfo getItem(int position) {
			if (position < userappinfo.size() + 1) {
				return userappinfo.get(position - 1);
			} else {
				return systemappinfo.get(position - userappinfo.size() - 2);// 获取应用信息
			}
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		/**
		 * 返回布局类型个数
		 */
		@Override
		public int getViewTypeCount() {
			return 2;
		}

		/**
		 * 根据item布局类型返回item
		 */
		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == userappinfo.size() + 1) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 根据当前布局类型，来初始化不同的布局
			int type = getItemViewType(position);
			switch (type) {
			case 0:
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(), R.layout.list_appmanager_item_title, null);
					holder2 = new ViewHolder2();
					holder2.title = (TextView) convertView.findViewById(R.id.tv_head);
					convertView.setTag(holder2);
				} else {
					holder2 = (ViewHolder2) convertView.getTag();
				}
				if (position == 0) {
					holder2.title.setText("用户应用(" + userappinfo.size() + ")");
				} else {
					holder2.title.setText("系统应用(" + systemappinfo.size() + ")");
				}
				break;
			case 1:
				ViewHolder holder;
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(), R.layout.list_item_appmanager, null);
					holder = new ViewHolder();
					holder.appname = (TextView) convertView.findViewById(R.id.tv_appname);
					holder.packagename = (TextView) convertView.findViewById(R.id.tv_packagename);
					holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
					// 将holder和convertView绑定
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				AppInfo appinfo = getItem(position);
				holder.appname.setText(appinfo.appname);
				holder.packagename.setText(appinfo.packagename);
				holder.icon.setImageDrawable(appinfo.icon);
				break;
			}

			return convertView;
		}
	}

	class ViewHolder {
		ImageView icon;
		TextView appname;
		TextView packagename;
	}

	class ViewHolder2 {

		TextView title;
	}
}
