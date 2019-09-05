package com.dream.mis.core.shiro;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import com.dream.mis.core.common.CacheConstants;
import com.dream.mis.core.web.model.User;
import com.jfinal.plugin.ehcache.CacheKit;
/**
 * 限制认证次数
 * @author by
 *
 */
public class RetryLimitCredentials extends HashedCredentialsMatcher { 
  
    @Override  
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) { 
    	UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
    	String accountName = authcToken.getUsername();
        //获取缓存中用户认证次数
        Integer times=CacheKit.get(CacheConstants.PW_ERROR_TIMES, accountName);
         boolean matches = super.doCredentialsMatch(token, info);   
         if(matches) {  
             //清除缓存 
        	 CacheKit.remove(CacheConstants.PW_ERROR_TIMES,accountName);   
         }else {
        	 if(times==null) {
             	CacheKit.put(CacheConstants.PW_ERROR_TIMES,accountName, 1);
             }else {
            	 times=times+1;
            	 if(times >= 3) {  
                  	User.dao.findFirst("select * from td_user where username=?",accountName).set("is_locked", 1).update();    	
                      throw new ExcessiveAttemptsException("密码错误次数太多，用户已被锁!" ); 
                  }  else {
                  	 CacheKit.put(CacheConstants.PW_ERROR_TIMES,accountName, times);
                }
             }	 
         }
         return matches;   
    }  
}
