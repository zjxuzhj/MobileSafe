package com.zhj.mobilesafe;

import com.zhj.mobilesafe.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostAndFindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (PrefUtils.getBoolean(getApplicationContext(), "first", true)) {
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			finish();
		} else {
			setContentView(R.layout.activity_lost_and_find);
		}
		ImageView iv_lock = (ImageView) findViewById(R.id.iv_lock);

		if (PrefUtils.getBoolean(getApplicationContext(), "protect", false)) {
			iv_lock.setImageResource(R.drawable.lock);
			
		} else {
			iv_lock.setImageResource(R.drawable.unlock);
		}
		TextView tv_safenum = (TextView) findViewById(R.id.tv_safenum);
		if (PrefUtils.getString(getApplicationContext(), "safenum", "") != "") {
			tv_safenum.setText(PrefUtils.getString(getApplicationContext(), "safenum", ""));
		}
	}

	public void re(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}
}
