package com.zhj.mobilesafe;

import java.util.ArrayList;

import com.zhj.mobilesafe.domain.AppInfo;
import com.zhj.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerActivity extends Activity implements OnClickListener {

	private ListView mLv;
	private ArrayList<AppInfo> mList;
	private appAdapter adapter;
	private ArrayList<AppInfo> userappinfo;
	private ArrayList<AppInfo> systemappinfo;
	private ViewHolder2 holder2;
	private PopupWindow mPopup;
	private AppInfo mCurrentappinfo;
	private TextView title2;
	private AnimationSet mSet;
	private View mContentView;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adapter = new appAdapter();
			mLv.setAdapter(adapter);
		};
	};

	@Override
	public void onClick(View v) {
		mPopup.dismiss();
		switch (v.getId()) {
		case R.id.tv_uninstall:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.DELETE");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("package:" + mCurrentappinfo.packagename));
			startActivityForResult(intent, 0);
			break;
		case R.id.tv_launch:
			PackageManager pm = getPackageManager();
			Intent intent2 = pm.getLaunchIntentForPackage(mCurrentappinfo.packagename);
			if (intent2 != null) {
				startActivity(intent2);
			} else {
				Toast.makeText(getApplicationContext(), "ϵͳ���ĳ���,�޷�����", 0).show();
			}
			break;
		case R.id.tv_share:
			Intent intent3 = new Intent();
			intent3.setAction("android.intent.action.SEND");
			intent3.setType("text/plain");
			intent3.putExtra(Intent.EXTRA_TEXT, "����һ����ţx���" + mCurrentappinfo.appname + ",���ص�ַ:www.baidu.com,�Լ�ȥ��");
			startActivity(intent3);
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanager);
		mLv = (ListView) findViewById(R.id.lv);
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				if (mPopup == null) {

					mContentView = View.inflate(getApplicationContext(), R.layout.popup_appinfo, null);

					mPopup = new PopupWindow(mContentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

					// �ؼ���С��������
					ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0.5f);
					scaleAnimation.setDuration(200);

					// ���䶯��
					AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
					alphaAnimation.setDuration(200);

					mSet = new AnimationSet(false);
					mSet.addAnimation(scaleAnimation);
					mSet.addAnimation(alphaAnimation);

					TextView tv_uninstall = (TextView) mContentView.findViewById(R.id.tv_uninstall);
					TextView tv_launch = (TextView) mContentView.findViewById(R.id.tv_launch);
					TextView tv_share = (TextView) mContentView.findViewById(R.id.tv_share);
					tv_uninstall.setOnClickListener(AppManagerActivity.this);
					tv_launch.setOnClickListener(AppManagerActivity.this);
					tv_share.setOnClickListener(AppManagerActivity.this);
				}

				// ִ�ж���
				mContentView.startAnimation(mSet);

				mPopup.setBackgroundDrawable(new ColorDrawable());// ���ñ���Ϊ����ɫ�����ܷ�����ʧ
				mPopup.showAsDropDown(view, 200, -view.getHeight() - 10);// ��ĳ�����ֵ����·���ʾ

				mCurrentappinfo = adapter.getItem(position);
			}
		});
		title2 = (TextView) findViewById(R.id.tv_title_head);
		mLv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (userappinfo != null && systemappinfo != null) {

					if (firstVisibleItem >= userappinfo.size() + 1) {

						title2.setText("ϵͳӦ��(" + systemappinfo.size() + ")");
					} else {
						title2.setText("�û�Ӧ��(" + userappinfo.size() + ")");
					}
				}
			}
		});
		initData();
	}

	private void initData() {
		new Thread() {

			@Override
			public void run() {
				mList = AppInfoProvider.getAppInfos(getApplicationContext());
				userappinfo = new ArrayList<AppInfo>();
				systemappinfo = new ArrayList<AppInfo>();
				for (AppInfo appinfo : mList) {
					if (appinfo.isUser) {
						userappinfo.add(appinfo);
					} else {
						systemappinfo.add(appinfo);
					}
				}

				handler.sendEmptyMessage(0);
			}
		}.start();

	}

	/**
	 * ж���귵�ص���ˢ���б�
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		initData();
	}

	class appAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return userappinfo.size() + systemappinfo.size() + 2;
		}

		@Override
		public AppInfo getItem(int position) {
			if (position < userappinfo.size() + 1) {
				return userappinfo.get(position - 1);
			} else {
				return systemappinfo.get(position - userappinfo.size() - 2);// ��ȡӦ����Ϣ
			}
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		/**
		 * ���ز������͸���
		 */
		@Override
		public int getViewTypeCount() {
			return 2;
		}

		/**
		 * ����item�������ͷ���item
		 */
		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == userappinfo.size() + 1) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// ���ݵ�ǰ�������ͣ�����ʼ����ͬ�Ĳ���
			int type = getItemViewType(position);
			switch (type) {
			case 0:
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(), R.layout.list_appmanager_item_title, null);
					holder2 = new ViewHolder2();
					holder2.title = (TextView) convertView.findViewById(R.id.tv_head);
					convertView.setTag(holder2);
				} else {
					holder2 = (ViewHolder2) convertView.getTag();
				}
				if (position == 0) {
					holder2.title.setText("�û�Ӧ��(" + userappinfo.size() + ")");
				} else {
					holder2.title.setText("ϵͳӦ��(" + systemappinfo.size() + ")");
				}
				break;
			case 1:
				ViewHolder holder;
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(), R.layout.list_item_appmanager, null);
					holder = new ViewHolder();
					holder.appname = (TextView) convertView.findViewById(R.id.tv_appname);
					holder.packagename = (TextView) convertView.findViewById(R.id.tv_packagename);
					holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
					// ��holder��convertView��
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				AppInfo appinfo = getItem(position);
				holder.appname.setText(appinfo.appname);
				holder.packagename.setText(appinfo.packagename);
				holder.icon.setImageDrawable(appinfo.icon);
				break;
			}
			return convertView;
		}
	}

	class ViewHolder {
		ImageView icon;
		TextView appname;
		TextView packagename;
	}

	class ViewHolder2 {

		TextView title;
	}

}
