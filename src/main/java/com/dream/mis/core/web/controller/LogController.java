package com.dream.mis.core.web.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.web.model.Log;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/log", viewPath = "/web/log")
@RequiresAuthentication
@RequiresPermissions("log:index")
public class LogController extends BaseController {

	public void index() {
        render("index.jsp");
	}
	
	public void logJson(){
		keepPara();
		Page<Record> page = Log.dao.page(getPageNumber(), getParaToInt("length",20),getParaMap(),getCurrentUser());
		Record json = new Record();
		json.set("draw", getPara("draw"));
		json.set("recordsTotal", page.getTotalRow());
		json.set("recordsFiltered", page.getTotalRow());
		json.set("data", page.getList());
		renderJson(json);
	}
	
	public void preview(){
		setAttr("log", Log.dao.findVlog(getPara("log_id")));
		render("preview.jsp");
	}
	
	public void findUserLog(){
		keepPara();
		Page<Record> page = Log.dao.pageMine(getPageNumber(), getParaToInt("length",20),getParaMap().get("user_id")[0].toString());
		Record json = new Record();
		json.set("draw", getPara("draw"));
		json.set("recordsTotal", page.getTotalRow());
		json.set("recordsFiltered", page.getTotalRow());
		json.set("data", page.getList());
		renderJson(json);
	}
	
}