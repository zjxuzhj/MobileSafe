package com.zhj.mobilesafe;

import com.zhj.mobilesafe.service.AppLockService;
import com.zhj.mobilesafe.ui.SettingView;
import com.zhj.mobilesafe.utils.PrefUtils;
import com.zhj.mobilesafe.utils.ServiceUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seeting);

		final SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		final SettingView sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
		final SettingView sv_setting_applock = (SettingView) findViewById(R.id.sv_setting_applock);

		if (PrefUtils.getBoolean(getApplicationContext(), "update", true)) {
			sv_setting_update.setChecked(true);
		} else {
			sv_setting_update.setChecked(false);
		}

		sv_setting_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (PrefUtils.getBoolean(getApplicationContext(), "update", true)) {
					sv_setting_update.setChecked(false);
					PrefUtils.putBoolean(getApplicationContext(), "update", false);

				} else {
					sv_setting_update.setChecked(true);
					PrefUtils.putBoolean(getApplicationContext(), "update", true);
				}
			}
		});
		sv_setting_applock.setChecked(
				ServiceUtils.isServiceRunning(getApplicationContext(), "com.zhj.mobilesafe.service.AppLockService"));
		sv_setting_applock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AppLockService.class);

				if (!sv_setting_applock.isChecked()) {
					sv_setting_applock.setChecked(true);
					startService(intent);
				} else {
					sv_setting_applock.setChecked(false);
					stopService(intent);
				}

			}
		});

	}

}
