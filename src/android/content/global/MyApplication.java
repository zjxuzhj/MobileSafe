package android.content.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.currentThread().setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
	}
	 private class MyUncaughtExceptionHandler implements UncaughtExceptionHandler{
         //ϵͳ����δ������쳣��ʱ�����
         //Throwable : Error��Exception�ĸ���
         @Override
         public void uncaughtException(Thread thread, Throwable ex) {
             System.out.println("�粶���쳣��......");
             ex.printStackTrace();
             try {
                 //�������쳣,���浽SD����
                 ex.printStackTrace(new PrintStream(new File("/mnt/sdcard/log.txt")));
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
             //myPid() : ��ȡ��ǰӦ�ó���Ľ���id
             //�Լ����Լ�ɱ��
             android.os.Process.killProcess(android.os.Process.myPid());
         }
     }}
