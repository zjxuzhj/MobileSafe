package com.zhj.mobilesafe;

import java.util.ArrayList;

import com.zhj.mobilesafe.db.dao.AppLockDao;
import com.zhj.mobilesafe.domain.AppInfo;
import com.zhj.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppLockActivity extends Activity {

	private LinearLayout mLl_unlock;
	private LinearLayout mLl_lock;

	private AppLockDao mDao;
	private TextView mTv_unlock;
	private TextView mTv_lock;

	private ListView mLv_lock;
	private ListView mLv_unlock;

	private ArrayList<AppInfo> mUnlockList;
	private ArrayList<AppInfo> mLockList;
	private AppLockAdapter mLockadapter;
	private AppLockAdapter mUnlockadapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			mUnlockadapter = new AppLockAdapter(false);
			mLv_unlock.setAdapter(mUnlockadapter);

			mLockadapter = new AppLockAdapter(true);
			mLv_lock.setAdapter(mLockadapter);

		};
	};
	private TextView mTv_unlocknum;
	private TextView mTv_locknum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applock);
		mTv_unlock = (TextView) findViewById(R.id.tv_unlock);
		mTv_lock = (TextView) findViewById(R.id.tv_lock);

		mLl_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
		mLl_lock = (LinearLayout) findViewById(R.id.ll_lock);

		mTv_unlocknum = (TextView) findViewById(R.id.tv_unlocknum);
		mTv_locknum = (TextView) findViewById(R.id.tv_locknum);

		mDao = AppLockDao.getInstance(this);

		// 点击切换到已加锁软件页面
		mTv_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mLl_unlock.setVisibility(View.GONE);

				mLl_lock.setVisibility(View.VISIBLE);
				mTv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
				mTv_unlock.setBackgroundResource(R.drawable.tab_left_default);

			}
		});

		// 点击切换到未加锁软件页面
		mTv_unlock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mTv_lock.setBackgroundResource(R.drawable.tab_right_default);
				mTv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
				mLl_unlock.setVisibility(View.VISIBLE);
				mLl_lock.setVisibility(View.GONE);

			}
		});

		mLv_lock = (ListView) findViewById(R.id.lv_lock);

		mLv_unlock = (ListView) findViewById(R.id.lv_unlock);
		initData();

	}

	public void initData() {
		new Thread() {

			public void run() {
				ArrayList<AppInfo> list = AppInfoProvider.getAppInfos(getApplicationContext());

				mUnlockList = new ArrayList<AppInfo>();
				mLockList = new ArrayList<AppInfo>();
				for (AppInfo appInfo : list) {
					if (mDao.find(appInfo.packagename)) {
						mLockList.add(appInfo);
					} else {
						mUnlockList.add(appInfo);
					}
				}

				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	private void updateLockNum() {
		mTv_unlocknum.setText("未加锁软件：" + mUnlockList.size() + "个");
		mTv_locknum.setText("已加锁软件：" + mLockList.size() + "个");
	}

	class AppLockAdapter extends BaseAdapter {
		private boolean isLock;

		public AppLockAdapter(boolean isLock) {

			this.isLock = isLock;
		};

		@Override
		public int getCount() {
			updateLockNum();
			if (isLock) {
				return mLockList.size();
			}
			return mUnlockList.size();
		}

		@Override
		public AppInfo getItem(int position) {
			if (isLock) {
				return mLockList.get(position);
			}
			return mUnlockList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.list_item_applock, null);
				holder = new ViewHolder();
				holder.appname = (TextView) convertView.findViewById(R.id.tv_appname);
				holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.iv_lockimg = (ImageView) convertView.findViewById(R.id.iv_lockimg);
				// 将holder和convertView绑定
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final AppInfo appinfo = getItem(position);
			holder.appname.setText(appinfo.appname);
			holder.icon.setImageDrawable(appinfo.icon);
			if (isLock) {
				holder.iv_lockimg.setImageResource(R.drawable.unlock);
			}

			final View view = convertView;
			holder.iv_lockimg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isLock) {

						TranslateAnimation animRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						animRight.setDuration(200);
						view.startAnimation(animRight);
						animRight.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								mDao.delLockApp(appinfo.packagename);
								mLockList.remove(appinfo);
								mUnlockList.add(appinfo);
								mLockadapter.notifyDataSetChanged();
								mUnlockadapter.notifyDataSetChanged();
							}
						});
					} else {
						TranslateAnimation animRight = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						animRight.setDuration(200);
						view.startAnimation(animRight);
						animRight.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								mLockList.add(appinfo);
								mUnlockList.remove(appinfo);
								mDao.addLockApp(appinfo.packagename);
								mLockadapter.notifyDataSetChanged();
								mUnlockadapter.notifyDataSetChanged();
							}
						});
					}

				}
			});

			if (isLock) {

			} else {

			}
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView icon;
		TextView appname;
		ImageView iv_lockimg;
	}

}
