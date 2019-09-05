package com.dream.mis.core.web.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.dream.mis.core.baseController.BaseController;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey="/",viewPath="/")
@RequiresAuthentication
public class IndexController extends BaseController{
	@Clear
    public void index() {
		redirect("/login");
    }
	
    /**
     * 左侧菜单伸缩状态
     */
    public void collapseStatus(){
    	setSessionAttr("collapse", getParaToBoolean("collapse"));
    	renderJson(successJson("ok"));
    }
    
    /**
     * 403 无权限
     */
    public void noAuth(){
    	render("web/error/403.jsp");
    }
    
    /**
     * 500 内部服务器错误
     * Internal Server Error
     */
    public void internalServerError(){
    	render("web/error/500.jsp");
    }
    
}	