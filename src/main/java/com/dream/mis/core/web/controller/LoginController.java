package com.dream.mis.core.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.shiro.CaptchaFormAuthenticationInterceptor;
import com.dream.mis.core.shiro.CaptchaRender;
import com.dream.mis.core.web.model.SystemInfo;
import com.dream.mis.core.web.model.User;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.ehcache.CacheKit;

/**  
 */
@ControllerBind(controllerKey="/login",viewPath="/web/login")
public class LoginController extends BaseController {
    
    private static final Log LOG = Log.getLog(LoginController.class);
    private static final int DEFAULT_CAPTCHA_LEN = 4;
    private static final String LOGIN_URL = "/login";
    /**
	 * 登陆页面
	 */
    @Clear
	public void index(){
    	Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
        	List<SystemInfo> list = SystemInfo.dao.findAll();
        	Map<String, Object> info=new HashMap<String,Object>();
    		for(SystemInfo s:list){
    			info.put(s.getStr("code"), s.getStr("val"));
    		}
        	setSessionAttr("systemInfos", info);
    		render("login.jsp"); 
        }else{
        	redirect("/userInfo");
        }
	}
    /**
     * @Title: img  
     * @Description: 图形验证码   
     * @since V1.0.0
     */
    @Clear
    public void img(){
        CaptchaRender img = new CaptchaRender(DEFAULT_CAPTCHA_LEN);
        this.setSessionAttr(CaptchaRender.DEFAULT_CAPTCHA_MD5_CODE_KEY, img.getMd5RandonCode());
        render(img);
    }
    
    /**
     * 登录验证
     */
    @Clear
    @Before({CaptchaFormAuthenticationInterceptor.class})
    public void doLogin(){
    	String captcha = getPara("checkCode");
    	if(!CaptchaRender.validate(getSessionAttr(CaptchaRender.DEFAULT_CAPTCHA_MD5_CODE_KEY).toString(), captcha)){
    		renderJson(failJson("验证码错误！"));
    		return;
    	}		
    	try {
    		UsernamePasswordToken token = this.getAttr("shiroToken");
            Subject subject = SecurityUtils.getSubject();
            ThreadContext.bind(subject);//通过key/value键值对为每一个线程提供绑定及解绑对象的方法
            //进行用用户名和密码验证
            subject.login(token);
            User user = User.dao.findByUsernameExceptDel(getPara("username"));
            setSessionAttr("currentUser", user);
            com.dream.mis.core.web.model.Log.dao.saveLog(getCurrentUser().getStr("user_id"), "登录操作", getRequest(),"/user/doLogin", JsonKit.toJson(getParaMap()));
            //登录成功，清除权限相关缓存
            CacheKit.removeAll("iss_auth");
            renderJson(successJson("登陆成功！"));
        } catch (LockedAccountException e) {
        	LOG.error(e.getMessage());
         	renderJson(failJson("账号已被锁定！请联系管理员解锁！"));
        } catch (ExcessiveAttemptsException e) {
        	LOG.error(e.getMessage());
         	renderJson(failJson("密码错误次数太多，用户已被锁!"));
        }catch (AuthenticationException e) {
        	LOG.error(e.getMessage());
        	renderJson(failJson("用户名或密码错误！"));
        } catch (Exception e) {
        	e.printStackTrace();
            LOG.error(e.getMessage());
            renderJson(failJson("系统异常！"));
        }
    }
    
    /**
      * 退出登录
     */
    @Clear
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.logout();
            this.removeSessionAttr("currentUser");
            this.removeSessionAttr("catalogs");
            this.removeSessionAttr("dataMap");
            this.redirect(LOGIN_URL);
        } catch (SessionException ise) {
            LOG.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            LOG.debug("登出发生错误！", e);
        }
    }
    
	/**
	 * @desc 用户登录用
	 * void
	 */
    @Clear
	public void checkUserName() {
		renderJson(User.dao.checkUsernameStatus(getPara("username")));
	}
	/**
	 * @desc 用户注册用
	 * void
	 */
	public void checkUserName2() {
		renderJson(User.dao.checkUsernameStatus2(getPara("user.username")));
	}
	/**
	 * @desc 用户编辑用
	 * void
	 */
	public void checkUserName3() {
		String old = getPara("old");
		renderJson(User.dao.checkUsernameStatus3(getPara("user.username"),old));
	}
}
