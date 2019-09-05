package com.dream.mis.core.utils;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Author : ly <br>
 * qq:993046532 <br>
 * 2014-6-12 上午11:02:59 <br>
 * 
 * @see IP相关工具类
 */
public class IPUtils {

	// 获取IP
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * Author:ly 
	 * @param ip
	 * @return boolean
	 * @see 是否是内网IP
	 */
	public static boolean isInner(String ip) {
	    String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";//正则表达式=。 =、懒得做文字处理了、
	    Pattern p = Pattern.compile(reg);
	    Matcher matcher = p.matcher(ip);
	    return matcher.find();
	}
	
	
	//获取当前IP，判断是否是合法IP
	public static boolean allowIp114(HttpServletRequest request){
		String ip_str = PropertyUtils.getPropertyValue("safeIp114");
		System.out.println(ip_str);
		if(ip_str.indexOf(getIpAddr(request))>-1){
			return true;
		}
		return false;
	}

	/**
	 * Author:ly
	 * 
	 * @return boolean
	 * @see 判断用户的mac是否再合法mac中
	 */
	public static boolean allowMac(HttpServletRequest request) {
		String[] safeMac = PropertyUtils.getPropertyValue("safeMac").split("#");
		boolean b = false;
		for (int i = 0; i < safeMac.length; i++) {
			System.out.println("allmac:" + getMACAddress(getIpAddr(request)));
			System.out.println("safeMac:" + safeMac[i]);
			boolean bool = getMACAddress(getIpAddr(request)).indexOf((safeMac[i])) > -1;
			if (bool) {
				return true;
			} else {
				b = false;
			}
		}
		return b;
	}

	public static String getMACAddress(String ip) {
		System.out.println(ip);
		ip = "192.168.0.220";
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}
}
