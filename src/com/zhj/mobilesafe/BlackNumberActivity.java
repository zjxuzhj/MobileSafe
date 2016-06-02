package com.zhj.mobilesafe;

import java.util.ArrayList;

import com.zhj.mobilesafe.db.BlackNumberOpenHelper;
import com.zhj.mobilesafe.db.dao.BlackNumberDao;
import com.zhj.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BlackNumberActivity extends Activity {

	private BlackNumberDao mDao;
	private ListView Lvlist;
	private ArrayList<BlackNumberInfo> mList;
	private ProgressBar mGb;
	private MyAdapter myadapter;
	private int startIndex = 0;
	private int maxNum = 20;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			postTask();
		};
	};
	private Dialog dialog;

	/**
	 * 异步刷新listview方法
	 */
	public void postTask() {
		if (myadapter == null) {
			myadapter = new MyAdapter();
			Lvlist.setAdapter(myadapter);
		} else {
			myadapter.notifyDataSetChanged();
		}
		mGb.setVisibility(View.GONE);
	}

	/**
	 * 集合加载方法
	 */
	public void listAdd() {
		if (mList == null) {
			mList = mDao.getPartBlacknum(maxNum, startIndex);
		} else {
			mList.addAll(mDao.getPartBlacknum(maxNum, startIndex));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacknumber);
		mDao = BlackNumberDao.getInstance(this);
		Lvlist = (ListView) findViewById(R.id.lv);
		Button bt_add = (Button) findViewById(R.id.bt_add);
		createDB();
		add();
		bt_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showaddblackDialog();
			}
		});
		Lvlist.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					int position = Lvlist.getLastVisiblePosition();
					int totalCount = mDao.getTotalCount();

					if (position >= mList.size() - 1) {
						startIndex += maxNum;
						mGb.setVisibility(View.VISIBLE);
						initData();
						Log.d("tag2", totalCount + "  " + mList.size());
						if (mList.size() >= totalCount - 1) {
							Toast.makeText(getApplicationContext(), "到底了！", 0).show();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		mGb = (ProgressBar) findViewById(R.id.pb);
		initData();

	}

	public void showaddblackDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		View view = View.inflate(getApplicationContext(), R.layout.addblacknumber, null);
		final EditText et_num = (EditText) view.findViewById(R.id.et_num);
		final RadioGroup tg_button = (RadioGroup) view.findViewById(R.id.rg);
		Button bt_on = (Button) view.findViewById(R.id.bt_on);
		Button bt_off = (Button) view.findViewById(R.id.bt_off);
		bt_on.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String num = et_num.getText().toString().trim();
				if (TextUtils.isEmpty(num)) {
					Toast.makeText(getApplicationContext(), "请输入黑名单号码", 0).show();
					return;
				} else {
					// 获取拦截模式
					int mode = -1;
					int radioButtonId = tg_button.getCheckedRadioButtonId();
					switch (radioButtonId) {
					case R.id.rb_1:
						mode = 0;
						break;
					case R.id.rb_2:
						mode = 1;
						break;
					case R.id.rb_3:
						mode = 2;
						break;

					}
					mDao.addBlackNum(num, mode);
					BlackNumberInfo info = new BlackNumberInfo(num, mode);
					mList.add(0, info);
					// 更新界面
					myadapter.notifyDataSetChanged();
					dialog.dismiss();
				}

			}
		});
		bt_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 初始化数据
	 */
	public void createDB() {
		BlackNumberOpenHelper blackNumberOpenHelper = new BlackNumberOpenHelper(getApplicationContext());
		blackNumberOpenHelper.getWritableDatabase();

	}

	public void add() {
		mDao = BlackNumberDao.getInstance(getApplicationContext());

		for (int i = 0; i < 99; i++) {
			int mode = i % 2;
			mDao.addBlackNum("134563275" + i, mode);
		}
	}

	private void initData() {
		Thread t = new Thread() {
			@Override
			public void run() {
				
				

				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				listAdd();

				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);

			}
		};
		t.start();

	}

	/**
	 * 存放控件的容器
	 * 
	 * @author Administrator
	 *
	 */
	class ViewHolder {
		TextView tv_item_num, tv_item_mode;
		ImageView iv_clean;
	}

	/**
	 * 创建控制器
	 * 
	 * @author admin
	 *
	 */
	class MyAdapter extends BaseAdapter {
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
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder viewholder;
			final BlackNumberInfo info = getItem(position);
			if (convertView == null) {
				mView = View.inflate(getApplicationContext(), R.layout.activity_blacknumber_listview_item, null);
				viewholder = new ViewHolder();
				viewholder.tv_item_num = (TextView) mView.findViewById(R.id.tv_number);
				viewholder.tv_item_mode = (TextView) mView.findViewById(R.id.tv_mode);
				viewholder.iv_clean = (ImageView) mView.findViewById(R.id.iv_clean);
				// 将容器和view对象绑定在一起
				mView.setTag(viewholder);
			} else {
				mView = convertView;
				// 从view对象中得到控件的容器
				viewholder = (ViewHolder) mView.getTag();
			}

			viewholder.tv_item_num.setText(info.number);
			switch (info.mode) {
			case 0:
				viewholder.tv_item_mode.setText("拦截电话");
				break;
			case 1:
				viewholder.tv_item_mode.setText("拦截短信");
				break;
			case 2:
				viewholder.tv_item_mode.setText("全部拦截");
				break;
			}
			viewholder.iv_clean.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new Builder(BlackNumberActivity.this)
							.setMessage("您确定要删除黑名单号码：" + info.number + "?");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mDao.delBlackNum(info.number);
							mList.remove(position);
							myadapter.notifyDataSetChanged();// 更新界面
							dialog.dismiss();
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();

				}
			});
			return mView;
		}

	}
}
