package com.zhj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPwdActivity extends Activity {

	private String password;
	private String packagename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		packagename = intent.getStringExtra("packagename");
		setContentView(R.layout.activity_enterpwd);
		TextView tv_pwd = (TextView) findViewById(R.id.tv_pwd);
		final EditText et = (EditText) findViewById(R.id.et_pwd);

		ImageView im_icon = (ImageView) findViewById(R.id.im_appicon);
		Button btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				password = et.getText().toString().trim();
				if ("123".equals(password)) {
					Intent intent = new Intent();
					intent.setAction("com.zhj.MobileSafe.unlock");
					intent.putExtra("packagename", packagename);
					sendBroadcast(intent);
					finish();

				} else {
					Toast.makeText(getApplicationContext(), "ÃÜÂë´íÎó£¡", 0).show();
				}

			}
		});
		
		
		PackageManager pm = getPackageManager();
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

	/**
	 * µã»÷·µ»Ø¼ü
	 */
	@Override
	public void onBackPressed() {
		// Ìø×ªµ½×ÀÃæ
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		finish();
	}
}
