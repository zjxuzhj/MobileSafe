package com.zhj.mobilesafe;

import java.util.List;

import com.zhj.mobilesafe.domain.ProcessInfo;
import com.zhj.mobilesafe.engine.ProcessInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProcessManegerActivity extends Activity {

	private ListView mLv;
	private RelativeLayout rt;
	private List<ProcessInfo> mList;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			mLv.setAdapter(new ProcessAdapter());

			rt.setVisibility(View.GONE);
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_processmanager);

		mLv = (ListView) findViewById(R.id.lv);
		rt = (RelativeLayout) findViewById(R.id.rl);
		initdata();
	}

	public void initdata() {
		
		rt.setVisibility(View.VISIBLE);
		new Thread(){
			@Override
			public void run() {
				mList = ProcessInfoProvider.getProcessInfo(getApplicationContext());
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	class ProcessAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public ProcessInfo getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.list_item_processmanager, null);
				holder = new ViewHold();
				holder.appName = (TextView) convertView.findViewById(R.id.tv_appname);
				holder.ramSize = (TextView) convertView.findViewById(R.id.tv_ramSize);

				holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHold) convertView.getTag();
			}
			ProcessInfo info = getItem(position);
			holder.appName.setText(info.appName);
			holder.ramSize.setText(info.ramSize + "MB");
			holder.icon.setImageDrawable(info.icon);

			return convertView;
		}

	}

	static class ViewHold {
		TextView appName;
		TextView ramSize;
		ImageView icon;
	}

}
