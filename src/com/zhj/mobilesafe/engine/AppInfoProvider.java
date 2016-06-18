package com.zhj.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.zhj.mobilesafe.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfoProvider {

	public static ArrayList<AppInfo> getAppInfos(Context ctx){
		ArrayList <AppInfo> list=new ArrayList<AppInfo>();
		PackageManager pm=ctx.getPackageManager();
		
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		for(PackageInfo packageinfo: installedPackages){
			AppInfo appinfo=new AppInfo();
			
			appinfo.packagename= packageinfo.packageName;
			ApplicationInfo applicationInfo = packageinfo.applicationInfo;
			int flags = applicationInfo.flags;
			
			if((applicationInfo.FLAG_SYSTEM&flags)==applicationInfo.FLAG_SYSTEM){
				appinfo.isUser=false;
			}else{
				appinfo.isUser=true;
			}
			
			appinfo.icon = applicationInfo.loadIcon(pm);
			appinfo.appname = applicationInfo.loadLabel(pm).toString();
			list.add(appinfo);
		}
		return list;
	}
}
