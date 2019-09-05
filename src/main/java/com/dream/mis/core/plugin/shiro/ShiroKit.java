package com.dream.mis.core.plugin.shiro;

import java.util.concurrent.ConcurrentMap;


/**
 * ShiroKit. (Singleton, ThreadSafe)
 *
 * @author dafei
 */
public class ShiroKit {

	/**
	 * 登录成功时所用的页面。
	 */
	private static String successUrl = "/";

	/**
	 * 登录所用页面。
	 */
	private static String loginUrl = "/login";


	/**
	 * 没有权限所用页面。
	 */
	private static String unauthorizedUrl ="/401.jsp";


	/**
	 * Session中保存的请求的Key值
	 */
	private static String SAVED_REQUEST_KEY = "jfinalShiroSavedRequest";


	/**
	 * 用来记录那个action或者actionpath中是否有shiro认证注解。
	 */
	private static ConcurrentMap<String, AuthzHandler> authzMaps = null;

	/**
	 * 禁止初始化
	 */
	private ShiroKit() {}

	static void init(ConcurrentMap<String, AuthzHandler> maps) {
		authzMaps = maps;
	}

	/**
	 * 获取访问控制处理接口
	 * @param actionKey
	 * @return
	 */
	static AuthzHandler getAuthzHandler(String actionKey){
		/*
		if(authzMaps.containsKey(controllerClassName)){
			return true;
		}*/
		return authzMaps.get(actionKey);
	}

	public static final String getSuccessUrl() {
		return successUrl;
	}

	public static final void setSuccessUrl(String successUrl) {
		ShiroKit.successUrl = successUrl;
	}

	public static final String getLoginUrl() {
		return loginUrl;
	}

	public static final void setLoginUrl(String loginUrl) {
		ShiroKit.loginUrl = loginUrl;
	}

	public static final String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public static final void setUnauthorizedUrl(String unauthorizedUrl) {
		ShiroKit.unauthorizedUrl = unauthorizedUrl;
	}
	/**
	 * Session中保存的请求的Key值
	 * @return
	 */
	public static final String getSavedRequestKey(){
		return SAVED_REQUEST_KEY;
	}
}
