package com.dream.mis.core.web.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.web.model.Function;
import com.dream.mis.core.web.model.RoleFunction;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

@ControllerBind(controllerKey = "/func", viewPath = "/web/func")
@RequiresAuthentication
@RequiresPermissions("func:index")
public class FunctionController extends BaseController {

	public void index() {
		render("index.jsp");
	}

	public void funcJson(){
		String search = getPara("search[value]");
		List<Record> list = Function.dao.findParent(search);
		for(Record r:list){
			if(r.getStr("p_id").equals("0")){
				r.set("p_name", "#");
			} else {
				r.set("p_name", Function.dao.findById(r.getStr("p_id")).getStr("name"));
			}
		}
		Record json = new Record();
		json.set("recordsTotal", list.size());
		json.set("recordsFiltered",list.size());
		json.set("data", list);
		json.set("draw", getPara("draw"));
		renderJson(json);
	}
	
	public void func4TreeJson(){
		List<Record> list = Function.dao.find4Tree(this.getRequest().getContextPath());
		if(!list.isEmpty()){
			for (Record record : list) {
				if (record.getInt("is_menu") == 0) {
					record.set("icon", this.getRequest().getContextPath()+"/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/function.png");
				} else {
					record.set("icon", this.getRequest().getContextPath()+"/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/fun_url.png");
				}
			}
		}
		renderJson(list);
	}
	
	/**
	 * 添加功能表单
	 */
	public void addFunc(){
		if(StrKit.notBlank(getPara("function_id"))){
			setAttr("func", Function.dao.findById(getPara("function_id")));
		}
		setAttr("funcList", Function.dao.findParent(""));
		render("add.jsp");
	}
	

	/**
	 * 保存或者更新
	 */
	public void saveOrUpdateFunc(){
		try {
			Record r =  Function.dao.saveFunc(getModel(Function.class, "func"));
			CacheKit.removeAll("iss_auth");
			renderJson(r);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(failJson("保存出错！请联系管理员！"));
		}
	}
	
	/**
	 * 删除
	 */
	public void del(){
		Function.dao.deleteById(getPara("function_id"));
		RoleFunction.dao.delRoleFunction(getPara("function_id"));
		renderJson(successJson("删除成功！"));
	}
	
	
	/**
	 * 批量删除
	 */
	public void batchDelete(){
		String function_ids = getPara("function_ids");
		if(StrKit.notBlank(function_ids)){
			String function_id[] = function_ids.split(",");
			for (String id : function_id) {
				Function.dao.deleteById(id);
				RoleFunction.dao.delRoleFunction(id);
			}
		}
		renderJson(successJson("删除成功！"));
	}
	
	/**
	 * 附带不同图标的数据
	 */
	public void func4TreeWithIconJson(){
		List<Record> list = Function.dao.find4TreeWithIcon(this.getRequest().getContextPath());
		if(!list.isEmpty()){
			for (Record record : list) {
				if (record.getInt("is_menu") == 0) {
					record.set("icon", this.getRequest().getContextPath()+"/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/function.png");
				} else {
					record.set("icon", this.getRequest().getContextPath()+"/statics/plugins/zTree_v3/css/zTreeStyle/img/diy/fun_url.png");
				}
			}
		}
		renderJson(list);
	}
	
	/**
	 * function_id=62fe16ef7a2148e68bc4c28cf3237cef
	 * target_function_id=da94fbb6a32e4799a2cc33ec568c64b6
	 * moveType："inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
	 */
	public void moveTo(){
		Function function = Function.dao.findById(getPara("function_id"));
		Function target = Function.dao.findById(getPara("target_function_id"));
		if(getPara("moveType").equals("next") || getPara("moveType").equals("prev")){
			List<Function> sameLevelList = Function.dao.find("select * from td_function where p_id=? order by function_order asc",target.getStr("p_id"));
			if(sameLevelList.contains(function)){
				sameLevelList.remove(function);
			}
			for (int i = 0; i < sameLevelList.size(); i++) {
				if(sameLevelList.get(i).getStr("function_id").equals(target.getStr("function_id"))){
					if(getPara("moveType").equals("next")){
						sameLevelList.add(i+1, function);
					}
					if(getPara("moveType").equals("prev")){
						sameLevelList.add(i, function);
					}
					break;
				}
			}
			function.set("p_id", target.getStr("p_id"));
			function.update();
			for (int i = 0; i < sameLevelList.size(); i++) {
				Function function2 = sameLevelList.get(i);
				function2.set("function_order", (i+1));
				function2.update();
			}
		}
		if(getPara("moveType").equals("inner")){
			function.set("p_id", target.getStr("function_id"));
			Function orderMaxFunction = Function.dao.findFirst("select * from td_function where p_id=? order by function_order desc",target.getStr("function_id"));
			if(orderMaxFunction==null){
				function.set("function_order", 1);
			} else {
				function.set("function_order", orderMaxFunction.getInt("function_order")+1);
			}
			function.update();
		}
		renderJson(successJson("移动成功！"));
	}
	
}
