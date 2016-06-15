package com.zhj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AToolActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_atool);
	}
	public void appLock(View v){
		
		Intent intent=new Intent(getApplicationContext(), AppLockActivity.class);
		startActivity(intent);
	}
	public void numfind(View v){
		Intent intent=new Intent(getApplicationContext(), CommonNumberActivity.class);
		startActivity(intent);
	}
}
