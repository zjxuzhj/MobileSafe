package com.zhj.mobilesafe;

import com.zhj.mobilesafe.utils.PrefUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {

	private EditText mEt_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		mEt_contact = (EditText) findViewById(R.id.et_contact);
		mEt_contact.setText(PrefUtils.getString(this, "safenum", ""));
	}

	@Override
	public void nextPage() {
		
		String safenum = mEt_contact.getText().toString().trim();
		PrefUtils.putString(getApplicationContext(), "safenum", safenum);
		if (PrefUtils.getString(getApplicationContext(), "safenum", "") == "") {
			Toast.makeText(getApplicationContext(), "必须绑定安全号码", 0).show();
			return;
		}
		Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}

	@Override
	public void previousPage() {
		Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.translate_previous_in, R.anim.translate_previous_out);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String phone = null;
		if (data != null) {
			phone = data.getStringExtra("phone");
		}
		mEt_contact.setText(phone);
		PrefUtils.putString(getApplicationContext(), "safenum", phone);
	}

	public void select(View view) {
		Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
		startActivityForResult(intent, 0);

	}
}
