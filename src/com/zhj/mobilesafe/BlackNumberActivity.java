package com.zhj.mobilesafe;

import java.util.ArrayList;

import com.zhj.mobilesafe.db.dao.BlackNumberDao;
import com.zhj.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BlackNumberActivity extends Activity {

	private BlackNumberDao mDao;
	private ListView Lvlist;
	private ArrayList<BlackNumberInfo> mList;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
		
			
			Lvlist.setAdapter(new BaseAdapter() {
				private View mView;

				@Override
				public int getCount() {
					return mList.size();
				}

				@Override
				public BlackNumberInfo getItem(int position) {
					return mList.get(position);
				}

				@Override
				public long getItemId(int position) {
					return position;
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {

					ViewHolder viewholder;
					BlackNumberInfo info = getItem(position);
					if (convertView == null) {
						mView = View.inflate(getApplicationContext(), R.layout.activity_blacknumber_listview_item,
								null);
						viewholder = new ViewHolder();
						viewholder.tv_item_num = (TextView) mView.findViewById(R.id.tv_number);
						viewholder.tv_item_mode = (TextView) mView.findViewById(R.id.tv_mode);
						viewholder.iv_clean = (ImageView) mView.findViewById(R.id.iv_clean);
						// ��������view�������һ��
						mView.setTag(viewholder);
					} else {
						mView = convertView;
						// ��view�����еõ��ؼ�������
						viewholder = (ViewHolder) mView.getTag();
					}

					viewholder.tv_item_num.setText(info.number);
					switch (info.mode) {
					case 0:
						viewholder.tv_item_mode.setText("���ص绰");
						break;
					case 1:
						viewholder.tv_item_mode.setText("���ض���");
						break;
					case 2:
						viewholder.tv_item_mode.setText("ȫ������");
						break;
					}
					return mView;
				}

			});
			mGb.setVisibility(View.GONE);
		};
		
	};
	private ProgressBar mGb;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacknumber);
		mDao = BlackNumberDao.getInstance(this);
		Lvlist = (ListView) findViewById(R.id.lv);
		mGb = (ProgressBar) findViewById(R.id.pb);
		initData();

	}

	/**
	 * ��ʼ���������
	 */
	private void initData() {
		Thread t = new Thread() {
			@Override
			public void run() {

				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mList = mDao.findAll();
				
				Message msg=new Message();
				msg.what=1;
				handler.sendMessage(msg);
				
			}
		};
		t.start();

	}

	/**
	 * ��ſؼ�������
	 * 
	 * @author Administrator
	 *
	 */
	class ViewHolder {
		TextView tv_item_num, tv_item_mode;
		ImageView iv_clean;
	}
}
