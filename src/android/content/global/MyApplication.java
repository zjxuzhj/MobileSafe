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
         //系统中由未捕获的异常的时候调用
         //Throwable : Error和Exception的父类
         @Override
         public void uncaughtException(Thread thread, Throwable ex) {
             System.out.println("哥捕获异常了......");
             ex.printStackTrace();
             try {
                 //将捕获到异常,保存到SD卡中
                 ex.printStackTrace(new PrintStream(new File("/mnt/sdcard/log.txt")));
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
             //myPid() : 获取当前应用程序的进程id
             //自己把自己杀死
             android.os.Process.killProcess(android.os.Process.myPid());
         }
     }}
