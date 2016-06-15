package com.zhj.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import com.zhj.mobilesafe.EnterPwdActivity;
import com.zhj.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;

public class AppLockService extends Service {
	private AppLockDao dao;

	private boolean isflag = true;
	private String packagename;
	private String unlockpackagename;
	private ArrayList<String> mList;

	private LockReceiver receiver;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		receiver = new LockReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.zhj.MobileSafe.unlock");
		registerReceiver(receiver, filter);

		dao = AppLockDao.getInstance(this);
		mList = dao.findAll();

		final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		new Thread() {

			public void run() {
				while (isflag) {

					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					for (RunningTaskInfo runningTaskInfo : runningTasks) {
						ComponentName topActivity = runningTaskInfo.topActivity;
						packagename = topActivity.getPackageName();

						if (mList.contains(packagename)) {
							if (!packagename.equals(unlockpackagename)) {
								Intent intent = new Intent(AppLockService.this, EnterPwdActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("packagename", packagename);
								startActivity(intent);
							}

						}
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}.start();

		getApplication().getContentResolver().registerContentObserver(Uri.parse("content://com.zhj.MobileSafe/change"),
				true, new ContentObserver(null) {
					@Override
					public void onChange(boolean selfChange) {
						super.onChange(selfChange);
						mList = dao.findAll();
						System.out.println("¸Ä±ä");
					}
				});
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		isflag = false;
		unregisterReceiver(receiver);
		receiver = null;
	}

	class LockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			unlockpackagename = intent.getStringExtra("packagename");
		}
	}

}
