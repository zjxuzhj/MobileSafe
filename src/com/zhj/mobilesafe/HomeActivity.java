package com.zhj.mobilesafe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zhj.mobilesafe.service.AddressService;
import com.zhj.mobilesafe.utils.PrefUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	GridView gv_home_gridview;
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Intent intent=new Intent(this,AddressService.class);
		startService(intent);
		gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
		gv_home_gridview.setAdapter(new MyAdapter());
		gv_home_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					if (PrefUtils.getString(getApplicationContext(), "password", "").equals("")) {
						showSetPassWordDialog();
					} else {
						showEnterPasswordDialog();
					}
					break;
				case 1:
					Intent intent1=new Intent(HomeActivity.this,BlackNumberActivity.class);
					startActivity(intent1);
					break;
				case 2:
					Intent intent2=new Intent(HomeActivity.this,AppManagerActivity.class);
					startActivity(intent2);
					break;
				case 8:
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;

				}
			}

		});
	}
	int count=0;
	protected void showEnterPasswordDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
		final EditText et_setpassword_confrim = (EditText) view.findViewById(R.id.et_confimpassword);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		ImageView iv=(ImageView) view.findViewById(R.id.btn_img);
		 
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(count%2==0){
					et_setpassword_confrim.setInputType(0);
				}else{
					et_setpassword_confrim.setInputType(129);
				}
				count++;
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			// 获取输入的密码
			@Override
			public void onClick(View v) {
				String password1 = et_setpassword_confrim.getText().toString().trim();
				String passwordmd5 = passwordMD5(password1);
				if (passwordmd5.equals(PrefUtils.getString(getApplicationContext(), "password", ""))) {
					Intent intent = new Intent(getApplicationContext(), LostAndFindActivity.class);
					mDialog.dismiss();
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "您输入的密码存在问题，请核对后重新输入！", 0).show();
				}
			}
		});
		Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});
		builder.setView(view);
		mDialog = builder.create();
		mDialog.show();
	}

	/**
	 * MD5加密
	 * 
	 * @return
	 */
	public static String passwordMD5(String password) {
		StringBuilder sb = new StringBuilder();
		try {
			// 1.获取数据摘要器
			// arg0 : 加密的方式
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			// 2.将一个byte数组进行加密,返回的是一个加密过的byte数组,二进制的哈希计算,md5加密的第一步
			byte[] digest = messageDigest.digest(password.getBytes());
			// 3.遍历byte数组
			for (int i = 0; i < digest.length; i++) {
				// 4.MD5加密
				// byte值 -128-127
				int result = digest[i] & 0xff;
				// 将得到int类型转化成16进制字符串
				// String hexString = Integer.toHexString(result)+1;//不规则加密,加盐
				String hexString = Integer.toHexString(result);
				if (hexString.length() < 2) {
					// System.out.print("0");
					sb.append("0");
				}
				// System.out.println(hexString);
				// e10adc3949ba59abbe56e057f20f883e
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// 找不到加密方式的异常
			e.printStackTrace();
		}
		return null;
	}

	protected void showSetPassWordDialog() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassword, null);
		final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword);
		final EditText et_setpassword_confrim = (EditText) view.findViewById(R.id.et_confimpassword);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			// 获取输入的密码
			@Override
			public void onClick(View v) {
				String password = et_setpassword_password.getText().toString().trim();
				if (password.equals(null) | password.equals("")) {
					Toast.makeText(getApplicationContext(), "您输入的密码为空", 0).show();
					return;
				}
				String confrim_password = et_setpassword_confrim.getText().toString().trim();
				if (confrim_password.equals(password)) {
					String passwordmd5 = passwordMD5(password);
					PrefUtils.putString(getApplicationContext(), "password", passwordmd5);
					mDialog.dismiss();
					Toast.makeText(getApplicationContext(), "密码保存成功！", 0).show();
					Intent intent=new Intent(getApplicationContext(), Setup1Activity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "两次密码输入不一致!", 0).show();

				}

			}
		});
		Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});
		builder.setView(view);
		mDialog = builder.create();
		mDialog.show();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 9;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app, R.drawable.taskmanager,
					R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
					R.drawable.settings };
			String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
			View view = convertView.inflate(getApplicationContext(), R.layout.activity_home_item, null);
			ImageView home_item_iv_image = (ImageView) view.findViewById(R.id.home_item_iv_image);
			TextView home_item_tv_title = (TextView) view.findViewById(R.id.home_item_tv_title);
			home_item_iv_image.setImageResource(imageId[position]);
			home_item_tv_title.setText(names[position]);
			return view;
		}

	}
}
