package com.zhj.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class AddressService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		TelephonyManager tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(new PhoneStateListener(){
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				
				switch(state){
				case TelephonyManager.CALL_STATE_IDLE://¿ÕÏÐ ×´Ì¬,¹Ò¶Ï×´Ì¬
					System.out.println("µç»°¿ÕÏÐ");
					Toast.makeText(getApplicationContext(), "µç»°¿ÕÏÐ",0).show();
					break;
				case TelephonyManager.CALL_STATE_RINGING:   //ÏìÁåµÄ×´Ì¬
					System.out.println("µç»°Ïì");
					Toast.makeText(getApplicationContext(), "µç»°ÏìÁå",0).show();
					break;
				
				}
				
				
				super.onCallStateChanged(state, incomingNumber);
			}
		},  PhoneStateListener.LISTEN_CALL_STATE);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
