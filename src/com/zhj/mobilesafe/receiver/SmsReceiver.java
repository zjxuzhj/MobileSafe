package com.zhj.mobilesafe.receiver;


import com.zhj.mobilesafe.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle=intent.getExtras();
		Object[] objects=(Object[]) bundle.get("pdus");
		for(Object ob:objects){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])ob);
			String messagebody=sms.getMessageBody();
			String sender=sms.getOriginatingAddress();
		
			if("#*alarm*#".equals(messagebody)){
				System.out.println("≤•∑≈±®æØ“Ù¿÷£°");
				/**
				 * ≤•∑≈±®æØ“Ù¿÷
				 */
				AudioManager audiomanager=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,
						audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
				MediaPlayer media=MediaPlayer.create(context,R.raw.alarm);
				media.start();
			}
		}
		
	}
	
		

}
