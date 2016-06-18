package com.zhj.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CleanCacheActivity extends Activity {
	private static final int SCANNING = 1;
	private static final int SCAN_FINISHED = 2;
	private static final int CACHE_UPDATE = 3;// 发现缓存
	private LinearLayout ll_cache;
	private ProgressBar pb_cache;
	private TextView tv_cache;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANNING:

				String name = (String) msg.obj;
				tv_cache.setText("正在扫描" + name);
				break;
			case SCAN_FINISHED:

				tv_cache.setText("扫描结束");
				break;
			case CACHE_UPDATE:
				CacheInfo info = (CacheInfo) msg.obj;
				View view = View.inflate(getApplicationContext(), R.layout.list_item_cache, null);
				TextView tv_app = (TextView) view.findViewById(R.id.tv_appname);
				TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_app.setText(info.label);
				tv_size.setText(info.cacheSize /1024+ "KB");
				iv_icon.setImageDrawable(info.icon);
				ll_cache.addView(view, 0);
				break;

			default:
				break;
			}
		};
	};
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_clean_cache);

		pb_cache = (ProgressBar) findViewById(R.id.pb_cache);
		ll_cache = (LinearLayout) findViewById(R.id.ll_cache);
		tv_cache = (TextView) findViewById(R.id.tv_cache);

		pm = getPackageManager();

		new Thread() {
			public void run() {
				List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

				int count = 0;
				pb_cache.setMax(installedPackages.size());
				for (PackageInfo packageInfo : installedPackages) {
					count++;
					String packageName = packageInfo.packageName;
					try {
						// 根据包名获得缓存信息
						Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,
								IPackageStatsObserver.class);

						method.invoke(pm, packageName, new MyObserver());
					} catch (Exception e) {
						e.printStackTrace();
					}

					String label = packageInfo.applicationInfo.loadLabel(pm).toString();

					Message msg = Message.obtain();
					msg.obj = label;
					msg.what = SCANNING;

					pb_cache.setProgress(count);

					handler.sendMessage(msg);

					try {
						Thread.sleep(60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				handler.sendEmptyMessage(SCAN_FINISHED);
			}

		}.start();
	}

	class MyObserver extends IPackageStatsObserver.Stub {

		/**
		 * 改方法在子线程运行, 不能直接更新主界面ui
		 */
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			long cacheSize = pStats.cacheSize;

			if (cacheSize > 0) {
				// 说明该应用有缓存
				CacheInfo info = new CacheInfo();
				info.packageName = pStats.packageName;
				info.cacheSize = cacheSize;

				try {
					ApplicationInfo applicationInfo = pm.getApplicationInfo(info.packageName, 0);
					String label = applicationInfo.loadLabel(pm).toString();
					Drawable icon = applicationInfo.loadIcon(pm);

					info.label = label;
					info.icon = icon;

					Message msg = Message.obtain();
					msg.what = CACHE_UPDATE;
					msg.obj = info;
					handler.sendMessage(msg);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class CacheInfo {
		public String packageName;
		public String label;
		public Drawable icon;
		public long cacheSize;
	}
}
