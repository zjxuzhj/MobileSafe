package com.zhj.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.zhj.mobilesafe.R;
import com.zhj.mobilesafe.domain.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

public class ProcessInfoProvider {

	// ��ȡ���н�����Ϣ
	public static List<ProcessInfo> getProcessInfo(Context ctx) {
		List<ProcessInfo> list=new ArrayList<ProcessInfo>();
		// ��ȡ���̹�����
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = ctx.getPackageManager();
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

		// �������ϣ��õ�������ͨ�������õ�ͼ�꣬ռ�ÿռ�
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			ProcessInfo info = new ProcessInfo();
			MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[] { runningAppProcessInfo.pid });
			int totalPss = memoryInfo[0].getTotalPss();
			long ramSize = totalPss / 1024;
			info.ramSize = ramSize;

			String packageName = runningAppProcessInfo.processName;
			info.packageName=packageName;
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
				
				Drawable icon = applicationInfo.loadIcon(pm);
				info.icon=icon;
				String label = applicationInfo.loadLabel(pm).toString();

				info.appName=label;
				int flags = applicationInfo.flags;
				boolean isUser;
				// �ж��Ƿ����û�����
				if ((applicationInfo.FLAG_SYSTEM & flags) == applicationInfo.FLAG_SYSTEM) {
					// ϵͳ����
					isUser = false;
				} else {
					// �û�����
					isUser = true;
				}
			} catch (NameNotFoundException e) {
				info.appName=packageName;
				info.icon=ctx.getResources().getDrawable(R.drawable.default_system);
				e.printStackTrace();
			}

			list.add(info);
		}
		return  list;
	}
}
