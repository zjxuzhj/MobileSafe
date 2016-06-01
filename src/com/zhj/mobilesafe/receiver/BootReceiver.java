package com.zhj.mobilesafe.receiver;

import com.zhj.mobilesafe.utils.PrefUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {


		String bindsim=PrefUtils.getString(context, "sim", null);
		TelephonyManager tel=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String sim=tel.getSimSerialNumber();
		
		if((!bindsim.equals(null)&&!bindsim.equals(""))&&!sim.equals(null)){
			if(!sim.equals(bindsim)){
				System.out.println("���ĺ���Ͱ󶨺��벻�����Ѿ�������");
				System.out.println(bindsim);
				System.out.println(sim);
			}else{
				System.out.println("�����ֻ��ǰ�ȫ�ģ�");
			}
		}
	}

}
