package com.zhj.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	@Override
	public void nextPage() {
		Intent intent =new Intent(getApplicationContext(),Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}
	@Override
	public void previousPage() {
		// TODO Auto-generated method stub
		
	}
}
