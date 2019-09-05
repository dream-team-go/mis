package com.dream.mis.core.interceptor;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.dream.mis.core.shiro.SimpleUser;
import com.dream.mis.core.web.model.Function;
import com.dream.mis.core.web.model.UserV;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.activerecord.Record;

/**
 * 菜单等资源的加载
 */
public class MenuInterceptor implements Interceptor {
	
	public void intercept(Invocation ai) {
		
		String actionKey = ai.getActionKey();
		ai.getController().setAttr("subKey", actionKey);
			Subject subject = SecurityUtils.getSubject();
			if(subject.isAuthenticated()){
				UserV user = ((SimpleUser)SecurityUtils.getSubject().getPrincipal()).getUser();
				if(user!=null){
					String role_id = user.getStr("user_role_id");
					Record sub = Function.dao.findByActionKey(actionKey);
					if(sub!=null){
						Function par = Function.dao.findByFID(sub.getStr("p_id"));
						ai.getController().setAttr("subF", sub);
						ai.getController().setAttr("parF", par);
						ai.getController().setAttr("funcName", sub.getStr("name"));
					}
					ai.getController().setAttr("menus", Function.dao.findMenu(role_id));
				}				
				ai.invoke();
			}
	}
}

