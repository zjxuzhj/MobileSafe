package com.zhj.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhj.mobilesafe.utils.PrefUtils;
import com.zhj.mobilesafe.utils.StreamUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	protected static final int MSG_UPDATE_DIALOG = 1;
	protected static final int MSG_NO_DIALOG = 0;
	private TextView tv_splash_versionname;
	private TextView tv_download_onloading;
	private String mDes;
	private String mApkurl;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case MSG_UPDATE_DIALOG:
				showdialog();
				break;
			case MSG_NO_DIALOG:
				enterHome();
				break;
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		copyDb("commonnum.db");// 常用号码查询的数据库拷贝
		tv_splash_versionname=(TextView) findViewById(R.id.tv_splash_versionname);
		tv_splash_versionname.setText("版本号:"+getVersionName());
		tv_download_onloading=(TextView) findViewById(R.id.tv_download_onloading);
		if(PrefUtils.getBoolean(getApplicationContext(), "update", true)){
			update();
		}
		else{
			new Thread(){
				public void run() {
					SystemClock.sleep(2000);
					enterHome();
				};
			}.start();
			
		}
		
	}
	

	/*
	 * 获取当前应用版本号*
	 */
	
	protected void showdialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("新版本："+getVersionName());
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(mDes);
		builder.setCancelable(false);
		builder.setPositiveButton("升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("进行下载操作");
				download();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		builder.show();
	}


	/*
	 * 得到版本号
	 */
	private String getVersionName(){
		PackageManager pm=getPackageManager();
		
		try {
			PackageInfo pi=pm.getPackageInfo(getPackageName(), 0);
			String VersionName=pi.versionName;
			return VersionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	/*
	 * 检查更新
	 */
	private void update(){
		new Thread(){
			

			private String mCode;
			

			public void run() {
				int starttime=(int) System.currentTimeMillis();
				Message message = Message.obtain();
				try {
					URL url=new URL("http://zhouhongjie.top/apk.json");
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(2000);
					conn.setRequestMethod("GET");
					int responseCode=conn.getResponseCode();
					if(200==responseCode){
						System.out.println("连接成功....");
						InputStream in=conn.getInputStream();
						String json=StreamUtil.parserStreamUtil(in);
						JSONObject jsonobject=new JSONObject(json);
						mCode = jsonobject.getString("code");
						mApkurl = jsonobject.getString("apkurl");
						mDes = jsonobject.getString("des");
						System.out.println(mCode+mApkurl+mDes);
						if(mCode.equals(getVersionName())){
							message.what=MSG_NO_DIALOG;
							System.out.println(getVersionName());
							System.out.println("版本号相等，不需要更新");
						}else{
							message.what=MSG_UPDATE_DIALOG;
							System.out.println("需要更新");
						}
						
					}else{
						System.out.println("网络连接失败。。。");
					}
					} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				
					}catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						int endtime=(int) System.currentTimeMillis();
						int time=endtime-starttime;	
						if(time<2000){
							SystemClock.sleep(2000-time);
						}
						handler.sendMessage(message);
					}
				
				
			};
		}.start();
	}
	/*
	 * 跳转主界面函数
	 */
	protected void enterHome(){
		Intent intent=new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
	/*
	 * 下载最新版
	 */
	protected void download(){
		HttpUtils httputils=new HttpUtils();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			httputils.download(mApkurl,"/mnt/sdcard/MobileSafe2.0.apk",
					new RequestCallBack<File>() {
						//下载成功调用
						@Override
						public void onSuccess(ResponseInfo<File> arg0) {

							System.out.println("下载成功");
							installAPK();
							
						}
						//下载失败调用
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							
							Toast.makeText(getApplicationContext(), "您的网络有问题，无法进行下载", 0).show();
						}
						@Override
						public void onLoading(long total, long current, boolean isUploading) {
							// TODO Auto-generated method stub
							super.onLoading(total, current, isUploading);
							tv_download_onloading.setVisibility(View.VISIBLE);
							tv_download_onloading.setText(current/100+"/"+total/100);
						}
						
					});
			
		}
		
	} 
	/*
	 * 安装新版本
	 */
	protected void 	installAPK(){
		 /**
	     *  <intent-filter>
	            <action android:name="android.intent.action.VIEW" />
	            <category android:name="android.intent.category.DEFAULT" />
	            <data android:scheme="content" /> //content : 从内容提供者中获取数据  content://
	            <data android:scheme="file" /> // file : 从文件中获取数据
	            <data android:mimeType="application/vnd.android.package-archive" />
	        </intent-filter>
	     */
		Intent intent=new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/MobileSafe2.0.apk")),
				"application/vnd.android.package-archive");
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}
	/**
	 * 拷贝assets下的数据库至data/data目录
	 */
	private void copyDb(String dbName) {
		File filesDir = getFilesDir();// 获取data/data/files目录的文件夹
		// System.out.println("filesDir:" + filesDir.getAbsolutePath());
		File outFile = new File(filesDir, dbName);

		if (outFile.exists()) {
			System.out.println("数据库" + dbName + "已经存在,无需拷贝!");
			return;
		}

		FileOutputStream out = null;
		InputStream in = null;
		try {
			out = new FileOutputStream(outFile);
			in = getAssets().open(dbName);

			int len = 0;
			byte[] buffer = new byte[1024];

			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("拷贝数据库" + dbName + "成功!");
	}
	
}
