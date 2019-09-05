package com.dream.mis.core.interceptor;

import org.apache.shiro.SecurityUtils;

import com.dream.mis.core.shiro.SimpleUser;
import com.dream.mis.core.web.model.Log;
import com.dream.mis.core.web.model.UserV;
import com.dream.mis.core.web.model.Warn;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

/**
 * 日志全局拦截器
 * @author ly
 */
public class LogInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		Controller controller = inv.getController();		
		String actionKey = inv.getActionKey();//当前请求方法
			String request_params = JsonKit.toJson(controller.getParaMap());//请求参数转json
			UserV user = ((SimpleUser)SecurityUtils.getSubject().getPrincipal()).getUser();//获取登录用户信息
			String user_id = user.getStr("user_id");//当前登录用户id
			
			switch (actionKey) {
			case "/user":
				Log.dao.saveLog(user_id, "访问用户管理页面", controller.getRequest(), actionKey, request_params);
				break;
			case "/user/usersJson"://参数数据太多，不做保存，后续增加到日志文件中保存
				Log.dao.saveLog(user_id, "请求用户列表数据", controller.getRequest(), actionKey, "参数太多，没做保存");
				break;
			case "/user/addUser":
				Log.dao.saveLog(user_id, "点击了添加用户按钮", controller.getRequest(), actionKey, request_params);
				break;
			case "/user/saveOrUpdateUser":
				Log.dao.saveLog(user_id, "执行保存/修改用户操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "执行保存/修改用户操作", actionKey);
				break;
			case "/user/del":
				Log.dao.saveLog(user_id, "删除用户操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "删除用户操作", actionKey);
				break;
			case "/user/lock":
				Log.dao.saveLog(user_id, "锁定/解锁用户操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "锁定/解锁用户操作", actionKey);
				break;
			case "/user/batchDelete":
				Log.dao.saveLog(user_id, "批量删除用户操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "批量删除用户操作", actionKey);
				break;
			case "/user/modifyPwd":
				Log.dao.saveLog(user_id, "访问修改密码页面", controller.getRequest(),actionKey, request_params);
				break;
			case "/user/doModifyPwd":
				Log.dao.saveLog(user_id, "执行密码操作", controller.getRequest(),actionKey, request_params);
				break;
			case "/user/resetPassword":
				Log.dao.saveLog(user_id, "重置密码操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "重置密码操作", actionKey);
				break;
			case "/login/doLogin":
				System.out.println("登录操作，拦截器被clear，在LoginController中添加");
				break;
				
			case "/func":
				Log.dao.saveLog(user_id, "访问权限管理页面", controller.getRequest(),actionKey, request_params);
				break;
			case "/func/addFunc":
				Log.dao.saveLog(user_id, "点击添加权限按钮", controller.getRequest(),actionKey, request_params);
				break;
			case "/func/saveOrUpdateFunc":
				Log.dao.saveLog(user_id, "执行保存/修改权限操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "执行保存/修改权限操作", actionKey);
				break;
			case "/func/del":
				Log.dao.saveLog(user_id, "删除权限操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "删除权限操作", actionKey);
				break;
			case "/func/batchDelete":
				Log.dao.saveLog(user_id, "批量删除权限操作", controller.getRequest(),actionKey, request_params);
				break;
				
			case "/role":
				Log.dao.saveLog(user_id, "访问角色管理页面", controller.getRequest(),actionKey, request_params);
				break;
			case "/role/addRole":
				Log.dao.saveLog(user_id, "点击添加角色按钮", controller.getRequest(),actionKey, request_params);
				break;
			case "/role/saveOrUpdateRole":
				Log.dao.saveLog(user_id, "执行保存/修改角色操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "执行保存/修改角色操作", actionKey);
				break;
			case "/role/del":
				Log.dao.saveLog(user_id, "删除角色操作", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "删除角色操作", actionKey);
				break;
			case "/role/batchDelete":
				Log.dao.saveLog(user_id, "批量删除角色操作", controller.getRequest(),actionKey, request_params);
				break;
				
			case "/systeminfo":
				Log.dao.saveLog(user_id, "访问系统设置页面", controller.getRequest(),actionKey, request_params);
				break;
			case "/systeminfo/save":
				Log.dao.saveLog(user_id, "更新系统设置参数", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "更新系统设置参数操作", actionKey);
				break;
				
			case "/modifyPwd":
				Log.dao.saveLog(user_id, "访问修改密码页面", controller.getRequest(),actionKey, request_params);
				break;
			case "/modifyPwd/doModify":
				Log.dao.saveLog(user_id, "执行修改密码操作", controller.getRequest(),actionKey, request_params);
				break;
				
			case "/userInfo":
				Log.dao.saveLog(user_id, "访问个人中心", controller.getRequest(),actionKey, request_params);
				break;
			case "/userInfo/updateHeadImg":
				Log.dao.saveLog(user_id, "修改个人头像操作", controller.getRequest(),actionKey, request_params);
				break;
			case "/userInfo/myLog":
				Log.dao.saveLog(user_id, "查看个人操作日志", controller.getRequest(),actionKey, request_params);
				break;
			case "/userInfo/myInfo":
				Log.dao.saveLog(user_id, "查看个人资料", controller.getRequest(),actionKey, request_params);
				break;
			case "/userInfo/myInfoSave":
				Log.dao.saveLog(user_id, "修改个人资料", controller.getRequest(),actionKey, request_params);
				Warn.dao.makeLogWarn(user_id, "修改跟人资料", actionKey);
				break;
				
			default:
				break;
			}	
		inv.invoke();
	}

}
