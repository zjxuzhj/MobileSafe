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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class HomeActivity extends Activity {
	GridView gv_home_gridview;
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		AdManager.getInstance(this).init("c2f0e756bcc42e8b","864fe12ab48fcbed", true);
		// ʵ���������
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);

		// ��ȡҪǶ�������Ĳ���
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);

		// ����������뵽������
		adLayout.addView(adView);
		Intent intent=new Intent(this,AddressService.class);
		startService(intent);
		gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
		gv_home_gridview.setAdapter(new MyAdapter());
		gv_home_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
				case 3:
					Intent intent3=new Intent(HomeActivity.this,ProcessManegerActivity.class);
					startActivity(intent3);
					break;
				case 4:
					Intent intent4=new Intent(HomeActivity.this,TrafficManagerActivity.class);
					startActivity(intent4);
					break;
				case 5:
					Intent intent5=new Intent(HomeActivity.this,AnitVirusActivityy.class);
					startActivity(intent5);
					break;
				case 6:
					Intent intent6=new Intent(HomeActivity.this,CleanCacheActivity.class);
					startActivity(intent6);
					break;
				case 7:
					Intent intent7=new Intent(HomeActivity.this,AToolActivity.class);
					startActivity(intent7);
					break;
				case 8:
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;

				}
			}

		});
	}

	int count = 0;

	protected void showEnterPasswordDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
		final EditText et_setpassword_confrim = (EditText) view.findViewById(R.id.et_confimpassword);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		ImageView iv = (ImageView) view.findViewById(R.id.btn_img);

		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (count % 2 == 0) {
					et_setpassword_confrim.setInputType(0);
				} else {
					et_setpassword_confrim.setInputType(129);
				}
				count++;
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			// ��ȡ���������
			@Override
			public void onClick(View v) {
				String password1 = et_setpassword_confrim.getText().toString().trim();
				String passwordmd5 = passwordMD5(password1);
				if (passwordmd5.equals(PrefUtils.getString(getApplicationContext(), "password", ""))) {
					Intent intent = new Intent(getApplicationContext(), LostAndFindActivity.class);
					mDialog.dismiss();
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "�����������������⣬��˶Ժ��������룡", 0).show();
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
	 * MD5����
	 * 
	 * @return
	 */
	public static String passwordMD5(String password) {
		StringBuilder sb = new StringBuilder();
		try {
			// 1.��ȡ����ժҪ��
			// arg0 : ���ܵķ�ʽ
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			// 2.��һ��byte������м���,���ص���һ�����ܹ���byte����,�����ƵĹ�ϣ����,md5���ܵĵ�һ��
			byte[] digest = messageDigest.digest(password.getBytes());
			// 3.����byte����
			for (int i = 0; i < digest.length; i++) {
				// 4.MD5����
				// byteֵ -128-127
				int result = digest[i] & 0xff;
				// ���õ�int����ת����16�����ַ���
				// String hexString = Integer.toHexString(result)+1;//���������,����
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
			// �Ҳ������ܷ�ʽ���쳣
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
			// ��ȡ���������
			@Override
			public void onClick(View v) {
				String password = et_setpassword_password.getText().toString().trim();
				if (password.equals(null) | password.equals("")) {
					Toast.makeText(getApplicationContext(), "�����������Ϊ��", 0).show();
					return;
				}
				String confrim_password = et_setpassword_confrim.getText().toString().trim();
				if (confrim_password.equals(password)) {
					String passwordmd5 = passwordMD5(password);
					PrefUtils.putString(getApplicationContext(), "password", passwordmd5);
					mDialog.dismiss();
					Toast.makeText(getApplicationContext(), "���뱣��ɹ���", 0).show();
					Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "�����������벻һ��!", 0).show();

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
			String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };
			View view = convertView.inflate(getApplicationContext(), R.layout.activity_home_item, null);
			ImageView home_item_iv_image = (ImageView) view.findViewById(R.id.home_item_iv_image);
			TextView home_item_tv_title = (TextView) view.findViewById(R.id.home_item_tv_title);
			home_item_iv_image.setImageResource(imageId[position]);
			home_item_tv_title.setText(names[position]);
			return view;
		}

	}
}
