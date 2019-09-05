package com.dream.mis.core.web.controller;

import java.util.Enumeration;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.web.model.SystemInfo;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/systeminfo",viewPath="/web/systeminfo")
@RequiresAuthentication
@RequiresPermissions("systeminfo:index")
public class SystemInfoController extends BaseController{
	/**
	 * 系统设置
	 */
	public void index(){
		List<SystemInfo> list = SystemInfo.dao.findAll();
		for(SystemInfo s:list){
			setAttr(s.getStr("code"), s.getStr("val"));
		}
		render("systeminfo.jsp");
	}
	
	/**
	 * 保存
	 */
	public void save(){
		Enumeration<String> names = getParaNames();
		while (names.hasMoreElements()) {
		    String o= names.nextElement();
		    SystemInfo.dao.updateByCodeVal(o,getPara(o));
		}
		renderJson(successJson("保存成功！"));
	}
}
