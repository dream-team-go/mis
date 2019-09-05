package com.dream.mis.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.RenderInfo;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.core.Controller;

/**
 * CacheInterceptor.
 */
public class CacheInterceptor implements Interceptor {
	
	private static final String renderKey = "_renderKey_";
	private static ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<String, ReentrantLock>();
	
	private ReentrantLock getLock(String key) {
		ReentrantLock lock = lockMap.get(key);
		if (lock != null)
			return lock;
		
		lock = new ReentrantLock();
		ReentrantLock previousLock = lockMap.putIfAbsent(key, lock);
		return previousLock == null ? lock : previousLock;
	}
	
	final public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		String cacheName = buildCacheName(inv, controller);
		String cacheKey = buildCacheKey(inv, controller);
		Map<String, Object> cacheData = CacheKit.get(cacheName, cacheKey);
		if (cacheData == null) {
			Lock lock = getLock(cacheName);
			lock.lock();					// prevent cache snowslide
			try {
				cacheData = CacheKit.get(cacheName, cacheKey);
				if (cacheData == null) {
					inv.invoke();
					cacheAction(cacheName, cacheKey, controller);
					return ;
				}
			}
			finally {
				lock.unlock();
			}
		}
		
		useCacheDataAndRender(cacheData, controller);
	}
	
	// TODO 考虑与 EvictInterceptor 一样强制使用  @CacheName
	private String buildCacheName(Invocation inv, Controller controller) {
		CacheName cacheName = inv.getMethod().getAnnotation(CacheName.class);
		if (cacheName != null)
			return cacheName.value();
		cacheName = controller.getClass().getAnnotation(CacheName.class);
		return (cacheName != null) ? cacheName.value() : inv.getActionKey();
	}
	
	private String buildCacheKey(Invocation inv, Controller controller) {
		StringBuilder sb = new StringBuilder(inv.getActionKey());
		String urlPara = controller.getPara();
		if (urlPara != null)
			sb.append("/").append(urlPara);
		
		String queryString = controller.getRequest().getQueryString();
		if (queryString != null)
			sb.append("?").append(queryString);
		
		String hashCode = HashKit.md5(JsonKit.toJson(controller.getParaMap()));
		if (hashCode != null)
			sb.append(hashCode);
		return sb.toString();
	}
	
	private void cacheAction(String cacheName, String cacheKey, Controller controller) {
		HttpServletRequest request = controller.getRequest();
		Map<String, Object> cacheData = new HashMap<String, Object>();
		for (Enumeration<String> names=request.getAttributeNames(); names.hasMoreElements();) {
			String name = names.nextElement();
			cacheData.put(name, request.getAttribute(name));
		}
		
		cacheData.put(renderKey, new RenderInfo(controller.getRender()));		// cache RenderInfo
		CacheKit.put(cacheName, cacheKey, cacheData);
	}
	
	private void useCacheDataAndRender(Map<String, Object> cacheData, Controller controller) {
		HttpServletRequest request = controller.getRequest();
		Set<Entry<String, Object>> set = cacheData.entrySet();
		for (Iterator<Entry<String, Object>> it=set.iterator(); it.hasNext();) {
			Entry<String, Object> entry = it.next();
			request.setAttribute(entry.getKey(), entry.getValue());
		}
		request.removeAttribute(renderKey);
		
		controller.render(((RenderInfo)cacheData.get(renderKey)).createRender());		// set render from cacheData
	}
}



