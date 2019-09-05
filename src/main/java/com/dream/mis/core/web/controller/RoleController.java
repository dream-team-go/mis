package com.dream.mis.core.web.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.shiro.ShiroDbRealm;
import com.dream.mis.core.web.model.Function;
import com.dream.mis.core.web.model.Role;
import com.dream.mis.core.web.model.RoleFunction;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheKit;

@ControllerBind(controllerKey = "/role", viewPath = "/web/role")
@RequiresAuthentication
@RequiresPermissions("role:index")
public class RoleController extends BaseController {

	public void index() {
        render("index.jsp");
	}
	
	public void roleJson(){
		String search = getPara("search[value]");
		List<Record> list = Role.dao.findAll(search);
		Record json = new Record();
		json.set("recordsTotal", list.size());
		json.set("recordsFiltered",list.size());
		json.set("data", list);
		json.set("draw", getPara("draw"));
		renderJson(json);
	}
	

	/**
	 * 添加表单
	 */
	public void addRole(){
		if(StrKit.notBlank(getPara("role_id"))){
			setAttr("role", Role.dao.findById(getPara("role_id")));
			setAttr("roleFuncs", Function.dao.findRoleFuncs(getPara("role_id")));
		}
		setAttr("roleList", Role.dao.find("select *  from td_role"));
		render("add.jsp");
	}
	

	/**
	 * 保存或者更新
	 */
	public void saveOrUpdateRole(){
		try {
			Role role=getModel(Role.class, "role");
			renderJson(Role.dao.saveRole(role,getPara("function_ids")));
			CacheKit.remove("iss_auth", "menu_"+role.getStr("role_id"));//清除菜单缓存
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(failJson("保存出错！请联系管理员！"));
		}
	}
	
	/**
	 * 删除
	 */
	public void del() {
		Role.dao.deleteById(getPara("role_id"));
		Role.dao.delRFunction(getPara("role_id"));
		renderJson(successJson("删除成功！"));
	}
	
	/**
	 * 批量删除
	 */
	@Before(Tx.class)
	public void batchDelete() {
		String role_ids = getPara("role_ids");
		if(StrKit.notBlank(role_ids)){
			String role_id[] = role_ids.split(",");
			for (String id : role_id) {
				Role.dao.deleteById(id);
				Role.dao.delRFunction(id);
			}
		}
		renderJson(successJson("删除成功！"));
	}
	
	/**
	 * 角色对应的菜单数据
	 */
	public void roleFuncJson(){
		renderJson(RoleFunction.dao.findByRoleId(this.getRequest().getContextPath(), getPara("role_id")));
	}
	
}
