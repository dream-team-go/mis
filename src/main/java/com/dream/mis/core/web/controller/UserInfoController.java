package com.dream.mis.core.web.controller;


import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.web.model.Log;
import com.dream.mis.core.web.model.Notice;
import com.dream.mis.core.web.model.User;
import com.dream.mis.core.web.model.Warn;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/userInfo", viewPath = "/web/user")
@RequiresAuthentication
public class UserInfoController extends BaseController {	

	public void index() {
		 User user=(User)getSession().getAttribute("currentUser");
	     String userId=user.getStr("user_id");
	     System.out.println(userId);
		String type=getPara("type");
		if(type==null||"".equals(type)) {
			type="warn";
		}
		String pageString=getPara("page");
		if(pageString==null||"".equals(pageString)) {
			pageString="1";
		}
		String user_id=getCurrentUser().getStr("user_id");
		if("warn".equals(type)) {
			Page<Record> warnPage=Warn.dao.pageByUserId(Integer.parseInt(pageString), 8, user_id);
			List<Record> warns=warnPage.getList();
			int warnCount=warnPage.getTotalPage();
			setAttr("warns", warns);
			setAttr("warnCount", warnCount);
		}else {
			Page<Record> noticePage=Notice.dao.pageByUserId(Integer.parseInt(pageString), 8, user_id);	
			List<Record> notices=noticePage.getList();
			int noticeCount=noticePage.getTotalPage();
			setAttr("notices", notices);
			setAttr("noticeCount", noticeCount);
		}
		long warnNoReadCount=Warn.dao.getNoReadCount(user_id);
		long noticeNoReadCount=Notice.dao.getNoReadCount(user_id);
		setAttr("noticeNoReadCount", noticeNoReadCount);
		setAttr("warnNoReadCount", warnNoReadCount);
		setAttr("page", pageString);
		setAttr("type", type);
		render("personal.jsp");
	}
	
	/**
	 * 更新用户头像
	 */
	public void updateHeadImg(){
		User.dao.updateHeadImg(getCurrentUser().getStr("user_id"), getPara("head_img"));
		setSessionAttr("currentUser", User.dao.findById(getCurrentUser().getStr("user_id")));
		renderJson(successJson("更新成功"));
	}
	
	/**
	 * 访问操作日志面
	 */
	public void myLog(){
		render("myLog.jsp");
	}
	/**
	 * 操作日志列表
	 */
	public void myLogJson(){
		keepPara();
		Page<Record> page = Log.dao.pageMine(getPageNumber(), getParaToInt("length",20),getCurrentUser().getStr("user_id"));
		Record json = new Record();
		json.set("draw", getPara("draw"));
		json.set("recordsTotal", page.getTotalRow());
		json.set("recordsFiltered", page.getTotalRow());
		json.set("data", page.getList());
		renderJson(json);
	}
	
	/**
	 * 查看个人资料
	 */
	public void myInfo() {
		String user_id=getCurrentUser().getStr("user_id");
		Record user=User.dao.getMyInfo(user_id);
		setAttr("user", user);
		render("myInfo.jsp");
	}
	/**
	 * 保存个人资料
	 */
	public void myInfoSave() {
		String user_id=getCurrentUser().getStr("user_id");
		User user=User.dao.findById(user_id);
		user.set("username", getPara("username"));
		user.set("tel_no", getPara("tel_no"));
		user.set("sex", getPara("sex"));
		user.update();
		renderJson(successJson("保存成功！"));
	}
}