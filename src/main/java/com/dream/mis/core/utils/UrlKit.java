package com.dream.mis.core.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;

/**
 * @author liyun
 */
public class UrlKit {
	
	/**
	 * 长链接转换为短网址
	 * @param longurl
	 * @return tinyurl
	 */
	public static String tinyurl(String longurl){
		String s = HttpKit.post("http://dwz.cn/create.php", "url=" + longurl);
		System.out.println(s+"sssss");
		JSONObject json = JSONArray.parseObject(s);
		if(json.getIntValue("status")==0){
			return json.getString("tinyurl");
		} else {
			return json.getString("err_msg");
		}
	}
	
}
