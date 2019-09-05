package com.dream.mis.core.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

public class PropertyUtils {
	
	private static Properties properties;
	
	/**
	 * @param propertyName
	 * @return String
	 * @see 获取自定义的配置
	 */
	public static String getPropertyValue(String propertyName){
		loadPropertyFile("/classes/web_config.txt");
		String value = getProperty(propertyName);
		return value;
	}
	
	/**
	 * Load property file
	 * Example: loadPropertyFile("db_username_pass.txt");
	 * @param file the file in WEB-INF directory
	 */
	public static Properties loadPropertyFile(String file) {
		if (StrKit.isBlank(file))
			throw new IllegalArgumentException("Parameter of file can not be blank");
		if (file.contains(".."))
			throw new IllegalArgumentException("Parameter of file can not contains \"..\"");
		
		InputStream inputStream = null;
		String fullFile;	// String fullFile = PathUtil.getWebRootPath() + file;
		if (file.startsWith(File.separator))
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + file; 
		else
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + file;
		
		try {
			inputStream = new FileInputStream(new File(fullFile));
			Properties p = new Properties();
			p.load(inputStream);
			properties = p;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: " + fullFile);
		} catch (IOException e) {
			throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
		}
		finally {
			try {if (inputStream != null) inputStream.close();} catch (IOException e) {e.printStackTrace();}
		}
		if (properties == null)
			throw new RuntimeException("Properties file loading failed: " + fullFile);
		return properties;
	}
	
	private static String getProperty(String key) {
		checkPropertyLoading();
		return properties.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		checkPropertyLoading();
		return properties.getProperty(key, defaultValue);
	}
	
	private static void checkPropertyLoading() {
		if (properties == null)
			throw new RuntimeException("You must load properties file by invoking loadPropertyFile(String) method in configConstant(Constants) method before.");
	}
}
