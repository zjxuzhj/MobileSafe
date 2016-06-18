package com.zhj.mobilesafe;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.widget.TextView;

public class TrafficManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);
		TextView tv_traffic = (TextView) findViewById(R.id.tv_traffic_size);
		tv_traffic.setText("总下载流量：" + TrafficStats.getTotalRxBytes() / 1024 + "KB \n" 
		+ "总上传流量："+ TrafficStats.getTotalTxBytes() / 1024 + "KB \n"		
		+ "移动下载流量："+ TrafficStats.getMobileRxBytes() / 1024 + "KB \n"
		+ "移动上传流量："+ TrafficStats.getMobileTxBytes() / 1024 + "KB \n"
				);
	}
}
