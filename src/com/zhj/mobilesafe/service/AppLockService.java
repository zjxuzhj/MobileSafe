package com.zhj.mobilesafe.service;

import java.util.List;

import com.zhj.mobilesafe.AToolActivity;
import com.zhj.mobilesafe.EnterPwdActivity;
import com.zhj.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

public class AppLockService extends Service {
	private AppLockDao dao ;
	
	private boolean isflag = true;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		dao= AppLockDao.getInstance(this);
		final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		new Thread() {
			public void run() {
				while (isflag) {
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
					for(RunningTaskInfo runningTaskInfo: runningTasks){
						ComponentName topActivity=runningTaskInfo.topActivity;
						String packagename=topActivity.getPackageName();
						
						if(dao.find(packagename)){
							Intent intent=new Intent(AppLockService.this,EnterPwdActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra("packagename", packagename);
							startActivity(intent);
						}
					}
				}
			};
		}.start();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	
	}
}
