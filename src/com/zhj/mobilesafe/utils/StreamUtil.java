package com.zhj.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class StreamUtil {

	/*
	 * 将流信息转化为字符串
	 * @return
	 * @throws IOExcxeption
	 */
	public static String parserStreamUtil(InputStream in) throws IOException{
		
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		//写入流
		StringWriter sw=new StringWriter();
		String str=null;
		while((str=br.readLine())!=null){
			sw.write(str);
		}
		sw.close();
		br.close();
		
		
		return sw.toString();
		
		}
}
