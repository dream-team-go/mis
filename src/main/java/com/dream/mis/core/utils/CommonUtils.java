package com.dream.mis.core.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Record;

/**
 * Author : ly <br>
 * qq:993046532 <br>
 * 2014-5-23 下午08:33:29 <br>
 * @see 公共的util方法
 * @see isCNOrEN
 * @see isNumeric
 * @see Sleep
 * @see getUUID
 * @see getMostUUID
 * @see getLeastUUID
 * @see getSubstrInStrFrequency
 * @see genRandomNum
 * @see genRandomStr
 * @see downloadPic
 * @see eventRecord
 */
public class CommonUtils {
        
	/**
	 * Author:ly
	 * @return String 
	 * @see 返回是纯中文还是纯英文
	 */
	public static String isCNOrEN(String s) {
		if(StringUtils.isEmpty(s)){
			return "";
		}
		byte[] bytes = s.getBytes();
		int i = bytes.length;// i为字节长度
		int j = s.length();// j为字符长度
		System.out.println(i + " " + j);
		return i == j ? "EN" : "CN";
	}

	/**
	 * Author:ly 
	 * @param str
	 * @return boolean
	 * @see 是否是数字
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	/**
	 * Author:ly 
	 * @param millis
	 * @return void
	 * @see 线程睡会儿
	 */
	public static void Sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return String
	 * @see 原生UUID（去除-）
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * @return String
	 * @see Most UUID
	 */
	public static String getMostUUID(){
		return UUID.randomUUID().getMostSignificantBits()+"";
	}
	
	/**
	 * @return String
	 * @see Least UUID
	 */
	public static String getLeastUUID(){
		return UUID.randomUUID().getLeastSignificantBits()+"";
	}
	
	/**
	 * Author:ly 
	 * @param str
	 * @param regex
	 * @return Map<String,Integer>
	 * @see 获取子字符串在主字符串中出现的次数，返回一个map或者null
	 */
	public static Map<String, Integer> getSubstrInStrFrequency(String str,String regex){
		if(StringUtils.isNotEmpty(str)){
			//"\\|"
			String[] s = str.split(regex);
			Map<String, Integer> map = new HashMap<String, Integer>();
			for (int i = 0; i < s.length; i++) {
				int tmp =0;
				for (int j = 0; j < s.length; j++) {
					if(StringUtils.isNotEmpty(s[i])){
						if(s[i].equals(s[j])){
							tmp += 1;
						}
					}
				}
				System.out.println(s[i] + "=" + tmp);
				if(StringUtils.isNotEmpty(s[i]))
					map.put(s[i], tmp);
			}
			//eg.{6854355913080326291=1, 6854355913080326290=3}
			return map;
		} 
		return null;
		
	}
	
	public static String genRandomNum(int pwd_len){
	    //35是因为数组是从0开始的，26个字母+10个数字
	    final int maxNum = 36;
	    int i; //生成的随机数
	    int count = 0; //生成的密码的长度
	    char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	    StringBuffer pwd = new StringBuffer("");
	    Random r = new Random();
	    while(count < pwd_len){
	     //生成随机数，取绝对值，防止生成负数，
	   
	     i = Math.abs(r.nextInt(maxNum)); //生成的数最大为36-1
	   
	     if (i >= 0 && i < str.length) {
	      pwd.append(str[i]);
	      count ++;
	     }
	    }
	    return pwd.toString();
	 }
	public static String genRandomStr(int pwd_len){
	    //35是因为数组是从0开始的，26个字母+10个数字
	    final int maxNum = 36;
	    int i; //生成的随机数
	    int count = 0; //生成的密码的长度
	    char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	    		,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	    StringBuffer pwd = new StringBuffer("");
	    Random r = new Random();
	    while(count < pwd_len){
	     //生成随机数，取绝对值，防止生成负数，
	   
	     i = Math.abs(r.nextInt(maxNum)); //生成的数最大为36-1
	   
	     if (i >= 0 && i < str.length) {
	      pwd.append(str[i]);
	      count ++;
	     }
	    }
	    return pwd.toString();
	 }
	public static void downloadPic(String remoteUrl, String path, String filename) {
		try {
			URL url = new URL(remoteUrl);
			java.io.BufferedInputStream bis = new BufferedInputStream(url.openStream());
			byte[] bytes = new byte[100];
			OutputStream bos = new FileOutputStream(new File(path + filename));
			int len;
			while ((len = bis.read(bytes)) > 0) {
				bos.write(bytes, 0, len);
			}
			bis.close();
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 针对异步事件处理的返回的封装消息
	 * @param eventKey 事件key，用来标识事件作何处理
	 * @param eventDesc 事件描述，用来描述该事件的处理内容
	 * @param o 额外数据字段
	 * @return record
	 */
	public static Record eventRecord(String eventKey,String eventDesc,Object o){
		return new Record().set("eventKey", eventKey).set("eventDesc", eventDesc).set("extraData", o);
	}
	/***
	 * 针对异步事件处理的返回的封装消息
	 * @param eventKey 事件key，用来标识事件作何处理
	 * @param eventDesc 事件描述，用来描述该事件的处理内容
	 * @param o 额外数据字段
	 * @return record
	 */
	public static Record eventRecord(String eventKey,String eventDesc){
		return new Record().set("eventKey", eventKey).set("eventDesc", eventDesc);
	}
	
	public static void main(String[] args){
		for(int i=1;i<=79;i++){
			System.out.println(CommonUtils.getUUID());
		}
		
	}
}
