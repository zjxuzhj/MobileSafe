package com.zhj.mobilesafe;

import com.zhj.mobilesafe.service.AutoKillService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_procresssetting);
		CheckBox cb_clean=(CheckBox) findViewById(R.id.cb_clean);
		cb_clean.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent service=new Intent(getApplicationContext(), AutoKillService.class);
				if(isChecked){
					startService(service);
					
				}else{
					stopService(service);
				}
				
			}
		});
	}
}
