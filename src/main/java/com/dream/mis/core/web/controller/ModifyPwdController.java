package com.dream.mis.core.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.shiro.CaptchaFormAuthenticationInterceptor;
import com.dream.mis.core.web.model.Log;
import com.dream.mis.core.web.model.User;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;


@ControllerBind(controllerKey = "/modifyPwd", viewPath = "/web/modifyPwd")
@RequiresAuthentication
public class ModifyPwdController extends BaseController {

	public void index() {
        render("modifyPwd.jsp");
	}
	
	/**
	 * 检查原始密码是否正确 正确则修改
	 */
	@Before({CaptchaFormAuthenticationInterceptor.class})
	public void doModify(){
		try {
			UsernamePasswordToken token = this.getAttr("shiroToken");
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);
            subject.login(token);
            User.dao.updatePwd(getCurrentUser().getStr("user_id"), getPara("newPassword"));
    		Log.dao.saveLog(getCurrentUser().getStr("user_id"), getCurrentUser().getStr("user_cn_name")+"进行了修改密码",  getRequest(), "/modifyPwd/doModify", JsonKit.toJson(getParaMap()));
    		renderJson(successJson("密码修改成功！"));
        } catch (LockedAccountException e) {
        	e.printStackTrace();
        	renderJson(new Record().set("error", "账号已被锁定，无法修改密码"));
        } catch (ExcessiveAttemptsException e) {
        	e.printStackTrace();
         	renderJson(new Record().set("error","原密码错误次数太多，用户已被锁!"));
        }catch (AuthenticationException e) {
        	e.printStackTrace();
        	renderJson(new Record().set("error", "原密码错误，请更正"));
        } catch (Exception e) {
        	e.printStackTrace();
            renderJson(new Record().set("error", "系统异常，请联系管理员"));
        }
	}
	
}
