package com.dream.mis.core.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.web.model.Role;
import com.dream.mis.core.web.model.SystemInfo;
import com.dream.mis.core.web.model.User;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/user", viewPath = "/web/user")
@RequiresAuthentication
@RequiresPermissions("user:index")
public class UserController extends BaseController {

	public void index() {
        render("index.jsp");
	}
	
	/**
	 * 用户列表数据json
	 * @throws IOException 
	 */
	public void usersJson() throws IOException{
		keepPara();
		Page<Record> page = User.dao.page(getPageNumber(), getParaToInt("length",20),getParaMap());
		Record json = new Record();
		json.set("draw", getPara("draw"));
		json.set("recordsTotal", page.getTotalRow());
		json.set("recordsFiltered", page.getTotalRow());
		json.set("data", page.getList());
		renderJson(json);
	}
	
	/**
	 * 添加用户表单
	 */
	public void addUser(){
		if(StrKit.notBlank(getPara("user_id"))){
			setAttr("user", User.dao.findById(getPara("user_id")));
		}
		setAttr("roles", Role.dao.findAll(""));
		//setAttr("defaultPassword", getSystemInfo("defaultPassword"));
		render("add.jsp");
	}
	
	/**
	 * 保存或者更新
	 */
	public void saveOrUpdateUser(){
		try {
			User u = getModel(User.class, "user");
			String psw=SystemInfo.dao.findValByCode("defaultPassword");//defaultPassword 为默认密码code,祥看数据表td_system
			renderJson(User.dao.saveUser(u,psw));
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(failJson("保存出错！请联系管理员！"));
		}
	}
	
	public void changeStatus(){
		String user_id = getPara("user_id");
		int is_receive = getParaToInt("is_receive");
		User user = User.dao.findById(user_id);
		user.set("is_receive", is_receive);
		user.update();
		renderJson(successJson("修改成功！"));
	}
	
	/**
	 * 删除  
	 * 逻辑删除
	 */
	public void del(){
		User user = User.dao.findById(getPara("user_id"));
		user.set("is_del", 1);  //删除状态
		user.update();
		renderJson(successJson("删除成功！"));
	}
	/**
	 * 锁定解锁用户
	 */
	public void lock(){
		User user = new User();
		user.set("user_id", getPara("user_id"));
		String retMsg = "";
		if(getParaToInt("is_locked")==1){
			user.set("is_locked", 0);
			retMsg = "解锁成功！";
		}else{
			user.set("is_locked", 1);
			retMsg = "锁定成功！";
		}
		user.update();
		renderJson(successJson(retMsg));
	}
	/**
	 * 批量删除
	 */
	public void batchDelete(){
		String user_ids = getPara("user_ids");
		if(StrKit.notBlank(user_ids)){
			String user_id[] = user_ids.split(",");
			for (String uid : user_id) {
				User.dao.deleteById(uid);
			}
		}
		renderJson(successJson("删除成功！"));
	}
	/**
	 * @desc 用户名是否存在
	 * void
	 */
	
	public void checkUserExist() {
		String username = getPara("username");
		User u = User.dao.findByTelNo(username);
		if(u!=null&&u.getInt("is_del")==0) {
			renderJson(successJson("用户名已存在！"));
		} else {
			renderJson(failJson("用户名可以使用！"));
		}
	}
	/**
	 * 重置密码
	 */
	public void resetPassword(){
		User user = User.dao.findById(getPara("user_id"));
		String hashAlgorithmName = "MD5";//加密方式  
        Object crdentials =getSystemInfo("defaultPassword");//密码原值  
        ByteSource salt = ByteSource.Util.bytes(user.getStr("username"));//以账号作为盐值  
        int hashIterations = 2;//加密2次  
        SimpleHash hash = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
		user.set("password", hash.toString());
		user.update();
		renderJson(successJson("重置密码成功！"));
	}
}
