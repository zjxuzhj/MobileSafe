package com.zhj.mobilesafe;

import java.util.ArrayList;

import com.zhj.mobilesafe.db.dao.BlackNumberDao;
import com.zhj.mobilesafe.domain.BlackNumberInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BlackNumberActivity extends Activity {

	private BlackNumberDao mDao;
	private ListView Lvlist;
	private ArrayList<BlackNumberInfo> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacknumber);
		mDao = BlackNumberDao.getInstance(this);
		Lvlist = (ListView) findViewById(R.id.lv);
		initData();

	}

	/**
	 * 初始化数据你好
	 */
	private void initData() {
		Thread t = new Thread() {
			@Override
			public void run() {

				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mList = mDao.findAll();
				Lvlist.setAdapter(new BaseAdapter() {
					private View mView;

					@Override
					public int getCount() {
						return mList.size();
					}

					@Override
					public BlackNumberInfo getItem(int position) {
						return mList.get(position);
					}

					@Override
					public long getItemId(int position) {
						return position;
					}

					@Override
					public View getView(int position, View convertView, ViewGroup parent) {

						ViewHolder viewholder;
						BlackNumberInfo info = getItem(position);
						if (convertView == null) {
							mView = View.inflate(getApplicationContext(), R.layout.activity_blacknumber_listview_item,
									null);
							viewholder = new ViewHolder();
							viewholder.tv_item_num = (TextView) mView.findViewById(R.id.tv_number);
							viewholder.tv_item_mode = (TextView) mView.findViewById(R.id.tv_mode);
							viewholder.iv_clean = (ImageView) mView.findViewById(R.id.iv_clean);
							// 将容器和view对象绑定在一起
							mView.setTag(viewholder);
						} else {
							mView = convertView;
							// 从view对象中得到控件的容器
							viewholder = (ViewHolder) mView.getTag();
						}

						viewholder.tv_item_num.setText(info.number);
						switch (info.mode) {
						case 0:
							viewholder.tv_item_mode.setText("拦截电话");
							break;
						case 1:
							viewholder.tv_item_mode.setText("拦截短信");
							break;
						case 2:
							viewholder.tv_item_mode.setText("全部拦截");
							break;
						}
						return mView;
					}

				});
			}
		};
		t.start();

	}

	/**
	 * 存放控件的容器
	 * 
	 * @author Administrator
	 *
	 */
	class ViewHolder {
		TextView tv_item_num, tv_item_mode;
		ImageView iv_clean;
	}
}
