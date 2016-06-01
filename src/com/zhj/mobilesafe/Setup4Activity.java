package com.zhj.mobilesafe;

import com.zhj.mobilesafe.utils.PrefUtils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
	private CheckBox mCheckbox2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		mCheckbox2 = (CheckBox) findViewById(R.id.checkbox2);
		if(PrefUtils.getBoolean(this, "protect", false)){
			mCheckbox2.setText("防盗保护已打开");
			mCheckbox2.setChecked(true);
		}
		mCheckbox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(!mCheckbox2.isChecked()){
					mCheckbox2.setText("防盗保护未开启");
					PrefUtils.putBoolean(getApplicationContext(), "protect", false);
					
				}else{
					mCheckbox2.setText("防盗保护已打开");
					PrefUtils.putBoolean(getApplicationContext(), "protect", true);
				}
			}
		});
	}

	@Override
	public void nextPage() {
		PrefUtils.putBoolean(getApplicationContext(), "first", false);
		Intent intent =new Intent(getApplicationContext(),LostAndFindActivity.class);
		startActivity(intent);
		finish();
	}
	@Override
	public void previousPage() {
		Intent intent =new Intent(getApplicationContext(),Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.translate_previous_in, R.anim.translate_previous_out);
	}
}
