package com.zhj.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CleanCacheActivity extends Activity {
	private static final int SCANNING = 1;
	private static final int SCAN_FINISHED = 2;
	private static final int CACHE_UPDATE = 3;// ���ֻ���
	private LinearLayout ll_cache;
	private ProgressBar pb_cache;
	private TextView tv_cache;
	private PackageManager pm;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANNING:

				String name = (String) msg.obj;
				tv_cache.setText("����ɨ��" + name);
				break;
			case SCAN_FINISHED:

				tv_cache.setText("ɨ�����");
				break;
			case CACHE_UPDATE:
				final CacheInfo info = (CacheInfo) msg.obj;
				View view = View.inflate(getApplicationContext(), R.layout.list_item_cache, null);
				TextView tv_app = (TextView) view.findViewById(R.id.tv_appname);
				TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				ImageView iv_clean = (ImageView) view.findViewById(R.id.iv_clean);
				iv_clean.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent();
						intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
						intent.setData(Uri.parse("package:"+info.packageName));
						startActivity(intent);
					}
				});
				tv_app.setText(info.label);
				tv_size.setText(info.cacheSize / 1024 + "KB");
				iv_icon.setImageDrawable(info.icon);
				
				ll_cache.addView(view, 0);
				break;

			default:
				break;
			}
		};
	};

	public void clearAllCache(View v) {

		try {
			Method method = PackageManager.class.getMethod("freeStorageAndNotify", long.class,
					IPackageDataObserver.class);

			method.invoke(pm, Long.MAX_VALUE, new IPackageDataObserver.Stub() {

				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
					System.out.println("succeeded-->" + succeeded);

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), "����ɹ���", 0).show();
	}


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
						// ���ݰ�����û�����Ϣ
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
		 * �ķ��������߳�����, ����ֱ�Ӹ���������ui
		 */
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			long cacheSize = pStats.cacheSize;

			if (cacheSize > 0) {
				// ˵����Ӧ���л���
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
