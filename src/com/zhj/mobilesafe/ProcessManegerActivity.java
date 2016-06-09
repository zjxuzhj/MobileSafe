package com.zhj.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.zhj.mobilesafe.domain.ProcessInfo;
import com.zhj.mobilesafe.engine.ProcessInfoProvider;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProcessManegerActivity extends Activity {

	private ListView mLv;
	private RelativeLayout rt;
	private ViewHolder2 holder2;
	private List<ProcessInfo> mUserInfo;
	private List<ProcessInfo> mSystemInfo;
	private List<ProcessInfo> mList;
	private ProcessAdapter mAdapter;
	private ProcessInfo info = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			mAdapter = new ProcessAdapter();
			mLv.setAdapter(mAdapter);

			rt.setVisibility(View.GONE);
		};
	};
	private TextView mTitle2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanager);

		mLv = (ListView) findViewById(R.id.lv);
		rt = (RelativeLayout) findViewById(R.id.rl);

		mTitle2 = (TextView) findViewById(R.id.tv_title_head);
		mLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (mUserInfo != null && mSystemInfo != null) {
					if (firstVisibleItem < mUserInfo.size() + 1) {
						mTitle2.setText("用户进程(" + mUserInfo.size() + ")");
					} else {
						mTitle2.setText("系统进程(" + mSystemInfo.size() + ")");
					}
				}
			}
		});
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position <= mUserInfo.size() && position != 0) {
					info = mUserInfo.get(position - 1);

				} else {
					info = mSystemInfo.get(position + 2);
				}
				if (position == 0 || position == mUserInfo.size() + 1) {
					return;
				}
				info.isCheck = !info.isCheck;
				// view.findViewById(R.id.cb_isCheck);
				// 更新页面
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.isCheck.setChecked(info.isCheck);
				System.out.println(info.isCheck);
			}

		});
		initdata();
	}

	public void initdata() {

		rt.setVisibility(View.VISIBLE);
		new Thread() {

			@Override
			public void run() {
				mList = ProcessInfoProvider.getProcessInfo(getApplicationContext());
				mUserInfo = new ArrayList<ProcessInfo>();
				mSystemInfo = new ArrayList<ProcessInfo>();
				for (ProcessInfo processInfo : mList) {
					if (processInfo.isUser) {
						mUserInfo.add(processInfo);
					} else {
						mSystemInfo.add(processInfo);

					}
				}

				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	public void all(View v) {
		for (ProcessInfo processInfo : mUserInfo) {
			processInfo.isCheck = true;
		}
		for (ProcessInfo processInfo : mSystemInfo) {
			processInfo.isCheck = true;
		}
		mAdapter.notifyDataSetChanged();
	}

	public void cancle(View v) {
		for (ProcessInfo processInfo : mUserInfo) {
			processInfo.isCheck = false;
		}
		for (ProcessInfo processInfo : mSystemInfo) {
			processInfo.isCheck = false;
		}
		mAdapter.notifyDataSetChanged();
	}

	
	public  void cleanProcess(Context ctx){
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ProcessInfo> deleteInfo = new ArrayList<ProcessInfo>();
		for (int i = 0; i < mUserInfo.size(); i++) {
			if (mUserInfo.get(i).isCheck) {
				am.killBackgroundProcesses(mUserInfo.get(i).packageName);
				deleteInfo.add(mUserInfo.get(i));
			}
		}
		for (int i = 0; i < mSystemInfo.size(); i++) {
			if (mSystemInfo.get(i).isCheck) {
				am.killBackgroundProcesses(mSystemInfo.get(i).packageName);
				deleteInfo.add(mSystemInfo.get(i));
			}
		}
		for (ProcessInfo processInfo : deleteInfo) {
			if (processInfo.isUser) {
				mUserInfo.remove(processInfo);
			} else {
				mSystemInfo.remove(processInfo);

			}

		}
		Toast.makeText(getApplicationContext(), "恭喜，您的手机内存得到了完美的清理！", 0).show();
		System.out.println("恭喜，您的手机内存得到了完美的清理！");
		deleteInfo.clear();
		deleteInfo = null;
		mAdapter.notifyDataSetChanged();
	}
	public  void clean(View v) {
		cleanProcess(getApplicationContext());
	}

	public void setting(View v) {

		Intent intent=new Intent(this,ProcessSettingActivity.class);
		startActivity(intent);
	}

	class ProcessAdapter extends BaseAdapter {

		private int type;

		@Override
		public int getCount() {
			return mUserInfo.size() + mSystemInfo.size() + 2;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == mUserInfo.size() + 1) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public ProcessInfo getItem(int position) {
			if (position < mUserInfo.size() + 1) {
				return mUserInfo.get(position - 1);
			} else {
				return mSystemInfo.get(position - mUserInfo.size() - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			type = getItemViewType(position);
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
					holder2.title.setText("用户进程(" + mUserInfo.size() + ")");
				} else {
					holder2.title.setText("系统进程(" + mSystemInfo.size() + ")");
				}
				break;

			case 1:
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(), R.layout.list_item_processmanager, null);
					holder = new ViewHolder();
					holder.appName = (TextView) convertView.findViewById(R.id.tv_appname);
					holder.ramSize = (TextView) convertView.findViewById(R.id.tv_ramSize);

					holder.isCheck = (CheckBox) convertView.findViewById(R.id.cb_isCheck);
					holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				ProcessInfo info = getItem(position);
				holder.appName.setText(info.appName);
				holder.ramSize.setText(info.ramSize + "MB");
				holder.icon.setImageDrawable(info.icon);
				// 判断选择框是否被选中
				if (info.isCheck) {
					holder.isCheck.setChecked(true);
				} else {
					holder.isCheck.setChecked(false);
				}

				break;
			}

			return convertView;
		}

	}

	class ViewHolder {
		TextView appName;
		TextView ramSize;
		ImageView icon;
		CheckBox isCheck;
	}

	class ViewHolder2 {
		TextView title;

	}

}
