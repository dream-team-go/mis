package com.dream.mis.core.utils;


import java.io.Serializable;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;



public class DataMap implements Map<String,Object>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5569591618378167874L;
	private final String[] formats = { "yyyy-MM-dd HH:MM:ss","yyyy-MM-dd HH:MM", "yyyy-MM-dd", "yyyy年MM月dd日" };
	private Map<String, Object> map;
	
	public DataMap() {
		map = new HashMap<String, Object>();
	}
	/**
	 * 判断是否存在键为Key的值是否存在
	 * */
	public boolean hasVal(Object key) {
		if (map.get(key) != null && StringUtils.isNotEmpty(map.get(key)+"")) {
			return true;
		}
		return false;
	}
	
	public boolean hasNoVla(Object key){
		return !hasVal(key);
	}

	public int getInt(Object key) {
		Object tmp = map.get(key);
		if (tmp == null) {
			throw new RuntimeException("the value for key " + key
					+ " is empty!");
		}
		return toInt(tmp);
	}
	
	public int getInt(Object key,int val){
		Object tmp = map.get(key);
		if (tmp == null) {
			return val;
		}
		return toInt(tmp);
	}

	public long getLong(Object key) {
		Object tmp = map.get(key);
		if (tmp == null) {
			throw new RuntimeException("the value for key " + key+ " is empty!");
		}
		return toLong(tmp);
	}
	public long getLong(Object key,int val) {
		Object tmp = map.get(key);
		if (tmp == null) {
			return val;
		}
		return toLong(tmp);
	}

	public String getString(Object key) {
		Object tmp = map.get(key);
		if (tmp == null) {
			return "";
		}
		return String.valueOf(tmp);
	}
	public String getString(Object key,String val){
		Object tmp = map.get(key);
		if (tmp == null) {
			if (StringUtils.isEmpty(val)) {
				return "";
			}
			return val;
		}
		return String.valueOf(tmp);
	}

	public boolean getBoolen(Object key) {
		Object tmp = map.get(key);
		if (tmp == null) {
			throw new RuntimeException("the value for key " + key
					+ " is empty!");
		}
		return toBoolen(tmp);
	}

	public String[] getArrys(Object key) {
		Object tmp = map.get(key);
		if (tmp == null) {
			throw new RuntimeException("the value for key " + key+ " is empty!");
		}
		return String.valueOf(tmp).split(",");
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	public void clear() {
		map.clear();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Collection<Object> values() {
		return map.values();
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return map.entrySet();
	}
	
	/**
	 * @Desc: 字符串转为Int
	 * @param param
	 * @return int
	 * @Create 2013-11-12 上午11:23:00
	 */
	private int toInt(Object param) {
		try {
			return Integer.parseInt(String.valueOf(param));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * @Desc: 字符串转为Long
	 * @param param
	 * @return int
	 * @Create 2013-11-12 上午11:23:00
	 */
	private long toLong(Object param) {
		try {
			return Long.parseLong(String.valueOf(param));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @Desc: 字符串转为Boolen
	 * @param param
	 * @return int
	 * @Create 2013-11-12 上午11:23:00
	 */
	private boolean toBoolen(Object param) {
		try {
			return Boolean.parseBoolean(String.valueOf(param));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Desc: 字符串转为日期-支持常见格式
	 * @param param
	 * @return int
	 * @Create 2013-11-12 上午11:23:00
	 */
	@SuppressWarnings("unused")
	private  Date toDate(Object param) {
		try {
			return DateUtils.parseDate(String.valueOf(param), formats);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * @Desc: 字符串转为日期-自定议格式
	 * @param param
	 * @return int
	 * @Create 2013-11-12 上午11:23:00
	 */
	@SuppressWarnings("unused")
	private  Date toDate(Object param, String format) {
		try {
			return DateUtils.parseDate(String.valueOf(param),new String[] { format });
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
}
