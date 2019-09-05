package com.dream.mis.core.web.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.dream.mis.core.baseController.BaseController;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/main",viewPath="/web/main")
@RequiresAuthentication
public class MainController extends BaseController{
	/**
	 * 后台
	 */
	
	public void index(){
		render("index.jsp");
	}
}
