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
				case TelephonyManager.CALL_STATE_IDLE://���� ״̬,�Ҷ�״̬
					System.out.println("�绰����");
					Toast.makeText(getApplicationContext(), "�绰����",0).show();
					break;
				case TelephonyManager.CALL_STATE_RINGING:   //�����״̬
					System.out.println("�绰��");
					Toast.makeText(getApplicationContext(), "�绰����",0).show();
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
