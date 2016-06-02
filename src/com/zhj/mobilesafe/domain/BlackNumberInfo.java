package com.zhj.mobilesafe.domain;
/**
 * 黑名单对象
 * 
 */
public class BlackNumberInfo {
	public BlackNumberInfo(String blacknum, int mode2) {
     this.number=blacknum;
     this.mode=mode2;
	}
	public String number;
	public int mode;

}
