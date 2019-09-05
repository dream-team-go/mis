package com.dream.mis.core.web.controller;


import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.dream.mis.core.baseController.BaseController;
import com.dream.mis.core.enums.WarnType;
import com.dream.mis.core.web.model.Notice;
import com.dream.mis.core.web.model.Warn;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@ControllerBind(controllerKey = "/info", viewPath = "/web/info")
@RequiresAuthentication
public class InfoController extends BaseController {
	
	@RequiresPermissions("notice:list")
	public void noticeList() {
		render("noticeList.jsp");
	}
	
	@RequiresPermissions("notice:list")
	public void noticeJson(){
		keepPara();
		Page<Record> page = Notice.dao.page(getPageNumber(), getParaToInt("length",20),getParaMap());
		Record json = new Record();
		json.set("draw", getPara("draw"));
		json.set("recordsTotal", page.getTotalRow());
		json.set("recordsFiltered", page.getTotalRow());
		json.set("data", page.getList());
		renderJson(json);
	}
	
	/**
	 * 预览
	 */
	@RequiresPermissions("notice:list")
	public void preview(){
		setAttr("notice", Notice.dao.findVNotice(getPara("notice_id")));
		setAttr("user_ids", Notice.dao.findNoticeUserIds(getPara("notice_id")));
		setAttr("read_user_ids", Notice.dao.hasReadUser(getPara("notice_id")));
		setAttr("read_user_names", Notice.dao.findUserByIsRead(getPara("notice_id"),1));
		setAttr("not_read_user_names", Notice.dao.findUserByIsRead(getPara("notice_id"),0));
		render("preview.jsp");
	}
	
	/**
	 * 删除
	 */
	@RequiresPermissions("notice:list")
	public void noticeDel(){
		Notice.dao.deleteById(getPara("notice_id"));
		Notice.dao.delRNoticeUser(getPara("notice_id"));
		renderJson(successJson("删除成功！"));
	}
	
	@RequiresPermissions("info:setWarn")
	public void setWarn() {
		List<Record> types=Warn.dao.getWarnType();
		setAttr("types", types);
		render("warn.jsp");
	}
	
	
	/**
	 * 发布通知
	 */
	@RequiresPermissions("info:noticeAdd")
	public void noticeAdd(){
		render("add.jsp");
	}
	
	/**
	 * 通知 保存或者更新
	 */
	@RequiresPermissions("info:noticeAdd")
	public void noticeSaveOrUpdate(){
		try {
			renderJson(Notice.dao.saveNotice(getModel(Notice.class, "notice"),getCurrentUser().getStr("user_id"),getPara("user_ids")));
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(failJson("发布出错！请联系管理员！"));
		}
	}
	
	/**
	 * 批量删除
	 */
	public void batchDelete(){
		Notice.dao.batchDelete(getPara("notice_ids"));
		renderJson(successJson("删除成功！"));
	}
	
	
	
	/**
	 * 发送给我的站内信
	 */
	public void myNoticeJson(){
		keepPara();
		//Page<Record> page = Notice.dao.pageByUserId(getPageNumber(), getParaToInt("length",20),getCurrentUser().getStr("user_id"),getParaMap());
		Record json = new Record();
		json.set("draw", getPara("draw"));
		json.set("recordsTotal", page.getTotalRow());
		json.set("recordsFiltered", page.getTotalRow());
		json.set("data", page.getList());
		renderJson(json);
	}
	
	/**
	 * 查看通知详情
	 */
	public void myNoticeInfo(){
		setAttr("notice", Notice.dao.findVNotice(getPara("notice_id")));
		Notice.dao.read(getPara("notice_id"), getCurrentUser().getStr("user_id"));
		render("myNoticeInfo.jsp");
	}
	 /**
	 * 查看提醒详情
	 */
	public void myWarnInfo(){
		setAttr("warn", Warn.dao.findVNotice(getPara("warn_id")));
		Warn.dao.read(getPara("warn_id"), getCurrentUser().getStr("user_id"));
		render("myWarnInfo.jsp");
	}
	
	/**
	 * 获取各个角色对应的用户
	 */
	public void roleUsreJson() {
		renderJson(Notice.dao.roleUserJson(getRequest().getContextPath()));
	}
	/**
	 * 根据提醒的类型 获取对应的接收用户
	 */ 
	public void getUsersByType() {
		String type_id=getPara("id");
		List<Record> users=Warn.dao.getUsersByType(type_id);
		renderJson(users);
	}
	 /**
	  * 保存消息提醒设置
	  */
	 public void saveWarnSet() {
		 String type_id=getPara("type_id");
		 String[] user_ids=getParaValues("users[]");
		 String receive_type=getPara("receive_type");
		 try {
			 Warn.dao.saveSet(type_id, user_ids, receive_type);
			 renderJson(new Record().set("status", 0).set("msg", "设置成功"));
		} catch (Exception e) {
			renderJson(new Record().set("status", 1).set("msg", "设置失败"));
			e.printStackTrace();
		}
	 }
	 
	 /**
	  * 提醒已读
	  */
	 public void warnRead() {
		 String id=getPara("id");
		 String user_id=getCurrentUser().getStr("user_id");
		 Record record=Warn.dao.read(id, user_id);
		 renderJson(record);
	 }
	 
	 /**
	  * 通知已读
	  */
	 public void noticeRead() {
		 String id=getPara("id");
		 String user_id=getCurrentUser().getStr("user_id");
		 Record record=Notice.dao.read(id, user_id);
		 renderJson(record);
	 }

}
