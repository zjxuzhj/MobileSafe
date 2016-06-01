package com.zhj.mobilesafe.ui;

import com.zhj.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingView extends RelativeLayout {

	private TextView mSetting_message;
	private TextView mSetting_title;
	private CheckBox mCheckbox1;
	private String mTitle;
	private String mDes_on;
	private String mDes_off;

	public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	// 获取属性值
	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTitle = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.zhj.mobilesafe", "title");
		mDes_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.zhj.mobilesafe", "des_on");
		mDes_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.zhj.mobilesafe", "des_off");
		if(!isInEditMode())
		init();
	}

	public SettingView(Context context) {
		super(context);
		 
		init();
	}

	private void init() {

		View.inflate(getContext(), R.layout.setting_view, this);
		mSetting_title = (TextView) this.findViewById(R.id.setting_title);
		mSetting_message = (TextView) this.findViewById(R.id.setting_message);
		mCheckbox1 = (CheckBox) this.findViewById(R.id.checkbox1);
		mSetting_title.setText(mTitle);
		mSetting_message.setText(mDes_off);
		
	}
	public void setChecked(boolean isChecked) {
		mCheckbox1.setChecked(isChecked);
		if(isChecked()){
			mSetting_message.setText(mDes_on);
		}else{
			mSetting_message.setText(mDes_off);
		}
	}

	public boolean isChecked() {
		return mCheckbox1.isChecked();
	}


	

}
