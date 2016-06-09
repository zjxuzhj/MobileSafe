package com.zhj.mobilesafe.service;

import com.zhj.mobilesafe.ProcessManegerActivity;
import com.zhj.mobilesafe.engine.ProcessInfoProvider;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoKillService extends Service {

	private ScreenOffReceiver mReceiver;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class ScreenOffReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			System.out.println("ËøÆÁÁË£¡");
			ProcessInfoProvider.killAll(arg0);
		}
	}
	@Override
	public void onCreate() {
		super.onCreate();

		// ×¢²áÆÁÄ»¹Ø±Õ¹ã²¥
		mReceiver = new ScreenOffReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		mReceiver = null;
	}
}
