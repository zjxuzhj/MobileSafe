package com.zhj.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 这是防盗页面的一个基类
 * 
 * @author admin
 *
 */
public abstract class BaseSetupActivity extends Activity {
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			/**
			 * 快速滑动事件回调
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				float startX = e1.getRawX();
				float endX = e2.getRawX();
				float startY = e1.getRawY();
				float endY = e2.getRawY();
				if (endX - startX > 200) {
					// 向右滑，向上页
					previousPage();
				}

				if (startX - endX > 200) {
					nextPage();
				}
				//判断是不是斜着滑
				if(Math.abs(startY-endY)>300){
					Toast.makeText(getApplicationContext(), "侧滑都不会，别上下滑啊", 0).show();
					return false;
				}
				return true;
			}
		});
	}

	/**
	 * 获取当前activity的触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// 委托手势识别器
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * 页面布局中的跳转
	 * 
	 * @param view
	 */
	public void next(View view) {
		nextPage();
	}

	/**
	 * 页面布局中的跳转
	 * 
	 * @param view
	 */

	public void previous(View view) {
		previousPage();
	}

	// 跳到下一页
	public abstract void nextPage();

	// 跳到上一页
	public abstract void previousPage();
/**
 * 点击物理返回按钮返回上一个页面
 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			previousPage();
		}
		return super.onKeyDown(keyCode, event);
	}
}
