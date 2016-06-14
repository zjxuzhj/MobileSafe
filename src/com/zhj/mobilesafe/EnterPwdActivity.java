package com.zhj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterPwdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enterpwd);
		TextView tv_pwd=(TextView) findViewById(R.id.tv_pwd);
		ImageView im_icon=(ImageView) findViewById(R.id.im_appicon);
		
		Intent intent = getIntent();
		String packagename = intent.getStringExtra("packagename");
		PackageManager pm=getPackageManager();
		try {
			ApplicationInfo info = pm.getApplicationInfo(packagename, 0);
			Drawable icon = info.loadIcon(pm);
			String name = info.loadLabel(pm).toString();
			tv_pwd.setText(name);
			im_icon.setImageDrawable(icon);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
