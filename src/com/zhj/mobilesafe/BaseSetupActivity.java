package com.zhj.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * ���Ƿ���ҳ���һ������
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
			 * ���ٻ����¼��ص�
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				float startX = e1.getRawX();
				float endX = e2.getRawX();
				float startY = e1.getRawY();
				float endY = e2.getRawY();
				if (endX - startX > 200) {
					// ���һ�������ҳ
					previousPage();
				}

				if (startX - endX > 200) {
					nextPage();
				}
				//�ж��ǲ���б�Ż�
				if(Math.abs(startY-endY)>300){
					Toast.makeText(getApplicationContext(), "�໬�����ᣬ�����»���", 0).show();
					return false;
				}
				return true;
			}
		});
	}

	/**
	 * ��ȡ��ǰactivity�Ĵ����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// ί������ʶ����
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/**
	 * ҳ�沼���е���ת
	 * 
	 * @param view
	 */
	public void next(View view) {
		nextPage();
	}

	/**
	 * ҳ�沼���е���ת
	 * 
	 * @param view
	 */

	public void previous(View view) {
		previousPage();
	}

	// ������һҳ
	public abstract void nextPage();

	// ������һҳ
	public abstract void previousPage();
/**
 * ��������ذ�ť������һ��ҳ��
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
