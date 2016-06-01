package com.zhj.mobilesafe;

import com.zhj.mobilesafe.ui.SettingView;
import com.zhj.mobilesafe.utils.PrefUtils;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		final SettingView sv_bindsim=(SettingView) findViewById(R.id.sv_bindsim);
		if(PrefUtils.getString(getApplicationContext(), "sim", "")==""){
			sv_bindsim.setChecked(false);
		}else{
			sv_bindsim.setChecked(true);
		}
		sv_bindsim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sv_bindsim.isChecked()){
					sv_bindsim.setChecked(false);
					PrefUtils.putString(getApplicationContext(), "sim", "");
				}else{
					TelephonyManager tel=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String sim=tel.getSimSerialNumber();
					PrefUtils.putString(getApplicationContext(), "sim", sim);
					sv_bindsim.setChecked(true);
				}
			}
		});
	}

	public void nextPage() {
		if(PrefUtils.getString(getApplicationContext(), "sim", "")==""){
			Toast.makeText(getApplicationContext(), "±ØÐë°ó¶¨sim¿¨", 0).show();
			return;
		}
		Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
	}
	public void previousPage() {
		Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.translate_previous_in, R.anim.translate_previous_out);
	}
	

}
