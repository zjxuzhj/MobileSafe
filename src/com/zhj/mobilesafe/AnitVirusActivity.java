package com.zhj.mobilesafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zhj.mobilesafe.db.dao.AntiVirusDao;
import com.zhj.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnitVirusActivity extends Activity {

	private static final int SCAN_PASS = 0;
	private static final int SCAN_STOP = 1;
	private static final int SCAN_FIND = 2;
	private TextView mTv_tip;
	private LinearLayout mLl;
	private PackageManager pm;
	private ProgressBar mPb_virus;
	private ImageView mIv_scan;
	private ArrayList<packageInfo1> mList;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCAN_PASS:

				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(16);
				packageInfo1 info1 = (packageInfo1) msg.obj;
				String appname = info1.appName;
				tv.setText(appname);
				mLl.addView(tv, 0);
				mTv_tip.setText("正在扫描：" + appname);
				break;
			case SCAN_STOP:
				if (mList.size() > 0) {
					mTv_tip.setText("扫描结束！发现" + mList.size() + "个病毒，请立即处理！！");
					showSetPassWordDialog();
				} else {
					mTv_tip.setText("扫描结束！没有发现病毒！");
					mTv_tip.setTextColor(Color.GREEN);
				}
				System.out.println(mList.toString());
				System.out.println(mList.size());
				mIv_scan.clearAnimation();

				break;
			case SCAN_FIND:
				packageInfo1 info2 = (packageInfo1) msg.obj;
				String appname2 = info2.appName;
				String package2 = info2.packageName;
				mTv_tip.setText("发现病毒！！！");
				TextView tv1 = new TextView(getApplicationContext());
				tv1.setTextColor(Color.RED);
				tv1.setTextSize(16);
				tv1.setText("发现病毒：" + appname2);
				mLl.addView(tv1, 0);
				mList.add(info2);
			}
		};
	};

	protected void showSetPassWordDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("发现病毒！");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage("扫描结束！发现" + mList.size() + "个病毒，请立即处理！！");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				for(packageInfo1 package1:mList){
					String name = package1.packageName;
					Intent intent = new Intent();
					intent.setAction("android.intent.action.DELETE");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setData(Uri.parse("package:" + name));
					startActivityForResult(intent, 0);
					
				}
				
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create();
		builder.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anit_virus);
		mList = new ArrayList<packageInfo1>();
		mLl = (LinearLayout) findViewById(R.id.ll);
		mPb_virus = (ProgressBar) findViewById(R.id.pb_virus);
		mTv_tip = (TextView) findViewById(R.id.tv_tip);
		mIv_scan = (ImageView) findViewById(R.id.iv_scan);

		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);
		// 设置一个匀速插补器

		LinearInterpolator interpolator = new LinearInterpolator();
		ra.setInterpolator(interpolator);
		mIv_scan.startAnimation(ra);

		pm = getPackageManager();
		new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
				mPb_virus.setMax(packages.size());

				int count = 0;
				for (PackageInfo packageInfo : packages) {
					count++;
					mPb_virus.setProgress(count);
					String appName = packageInfo.applicationInfo.loadLabel(pm).toString();

					String packageName = packageInfo.packageName;

					packageInfo1 packge = new packageInfo1();
					packge.appName = appName;
					packge.packageName = packageName;
					// 获得程序的签名
					Signature[] signatures = packageInfo.signatures;
					String charsString = signatures[0].toCharsString();
					String signaturesMd5 = MD5Utils.encode(charsString);
					// 通过签名的MD5判断程序是否是病毒
					Message msg = Message.obtain();
					msg.obj = packge;
					if (AntiVirusDao.findMd5(signaturesMd5)) {
						msg.what = SCAN_FIND;
					} else {
						msg.what = SCAN_PASS;
					}

					handler.sendMessage(msg);
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

				handler.sendEmptyMessage(SCAN_STOP);

			};
		}.start();

	}

	static class packageInfo1 {
		String packageName;
		String appName;
	}
}
